package com.comcast.video.dawg.common.security;

import org.springframework.ldap.core.support.BaseLdapPathContextSource;
import org.springframework.ldap.pool.factory.PoolingContextSource;
import org.springframework.security.config.annotation.authentication.configurers.ldap.LdapAuthenticationProviderConfigurer;

/**
 * Not sure why ldap library doesn't provide this, but this allows creating a PoolingContextSource and pass it into the 
 * {@link LdapAuthenticationProviderConfigurer#contextSource(BaseLdapPathContextSource)}
 * @author Kevin Pearson
 *
 */
public class LdapPoolingContextSourceAdapter extends PoolingContextSource implements BaseLdapPathContextSource {

}
