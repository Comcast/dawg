package com.comcast.video.dawg.show;

import java.util.Map;

import com.comcast.video.dawg.common.MetaStb;

public class DefaultMetaStb extends MetaStb {
    private DawgShowConfiguration config;
    

    public DefaultMetaStb(Map<String, Object> data, DawgShowConfiguration config) {
        super(data);
        this.config = config;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getRackProxyUrl() {
        return getData().containsKey(RACK_PROXY_URL) ? super.getRackProxyUrl() : config.getRackProxyUrlDefault();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean getRackProxyEnabled() {
        return getData().containsKey(RACK_PROXY_ENABLED) ? super.getRackProxyEnabled() : config.getRackProxyEnabledDefault();
    } 
}
