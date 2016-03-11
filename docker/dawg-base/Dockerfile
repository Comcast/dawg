FROM centos:6.7
MAINTAINER trent_schmidt@cable.comcast.com

EXPOSE 8080

RUN mkdir /dawg-common
WORKDIR /dawg-common

ADD resources resources
ADD target/rpms rpms
RUN rpm -Uvh resources/*.rpm rpms/*.rpm
RUN sed -i "s/mirrorlist=https/mirrorlist=http/" /etc/yum.repos.d/epel.repo
RUN yum -y update
RUN yum -y install tomcat tomcat-* java-1.7.0-openjdk
RUN cp resources/tomcat-users.xml /usr/share/tomcat/conf/tomcat-users.xml

CMD service tomcat start && tail -f /var/log/tomcat/catalina.out
