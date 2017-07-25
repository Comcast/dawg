class dawg::dawg {
    class { 'java':
        package => hiera('java.package', 'java-1.7.0-openjdk'),
    }

    $tomcatSrc = hiera('tomcat.src', 'http://archive.apache.org/dist/tomcat/tomcat-7/v7.0.73/bin/apache-tomcat-7.0.73.tar.gz')
    tomcat::install { '/opt/tomcat':
        source_url => $tomcatSrc,
    }

    exec {'remove-packaged-apps':
        command => "/usr/bin/rm -Rf /opt/tomcat/webapps/*",
        require => Tomcat::Install['/opt/tomcat'],
    }

    file { '/etc/init.d/tomcat':
        content => hiera('tomcat.init'),
        mode => '0755',
        require => Tomcat::Install['/opt/tomcat'],
    }

    exec {"genunit-tomcat":
        path => '/sbin:/bin:/usr/sbin:/usr/bin',
        onlyif => 'test -f /bin/systemctl',
        command => 'systemctl daemon-reload',
        require => File["/etc/init.d/tomcat"],
    }

    # The tomcat puppet module has a resource that does this, but it sucks
    service { 'tomcat':
        ensure => running,
        require => [File['/etc/init.d/tomcat'],Exec['genunit-tomcat']],
    }

    $nexurl = hiera('nexus.url')

	class {'nexus':
	   url => $nexurl
    }

    $version = hiera('dawg.version')
    $repo = hiera('nexus.repo')
    $groupId = "com.comcast.video.dawg"

    $dsArtifactId = "dawg-show"
    nexus::artifact { $dsArtifactId:
            gav => "${groupId}:${dsArtifactId}:${version}",
            repository => $repo,
            output => "/tmp/${dsArtifactId}-${version}.war",
            packaging => 'war',
            ensure => 'present',
   }

    $dhArtifactId = "dawg-house"
    nexus::artifact { $dhArtifactId:
            gav => "${groupId}:${dhArtifactId}:${version}",
            repository => $repo,
            output => "/tmp/${dhArtifactId}-${version}.war",
            packaging => 'war',
            ensure => 'present',
   }

    $dpArtifactId = "dawg-pound"
    nexus::artifact { $dpArtifactId:
            gav => "${groupId}:${dpArtifactId}:${version}",
            repository => $repo,
            output => "/tmp/${dpArtifactId}-${version}.war",
            packaging => 'war',
            ensure => 'present',
   }

   file { '/etc/dawg':
        ensure => 'directory',
        owner => 'tomcat',
   }

   config_file::config_file { 'dawg.ini':
        path => '/etc/dawg/dawg.ini',
        config_key => 'config',
        owner => 'tomcat',
        require => File['/etc/dawg'],
   }

   tomcat::war { "${dsArtifactId}.war":
        catalina_base => '/opt/tomcat',
        war_source    => "/tmp/${dsArtifactId}-${version}.war",
        require => [Nexus::Artifact[$dsArtifactId], Config_file::Config_file['dawg.ini'], Exec['remove-packaged-apps']],
   }

   tomcat::war { "${dhArtifactId}.war":
        catalina_base => '/opt/tomcat',
        war_source    => "/tmp/${dhArtifactId}-${version}.war",
        require => [Nexus::Artifact[$dhArtifactId], Config_file::Config_file['dawg.ini'], Exec['remove-packaged-apps']],
   }

   tomcat::war { "${dpArtifactId}.war":
        catalina_base => '/opt/tomcat',
        war_source    => "/tmp/${dpArtifactId}-${version}.war",
        require => [Nexus::Artifact[$dpArtifactId], Config_file::Config_file['dawg.ini'], Exec['remove-packaged-apps']],
   }

}
