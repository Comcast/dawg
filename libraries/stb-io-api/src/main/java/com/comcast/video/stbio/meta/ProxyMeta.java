package com.comcast.video.stbio.meta;

/**
 * Created by djerus200 on 12/21/16.
 */
public interface ProxyMeta {
    /**
     * Get url of ack proxy
     * @return url of rack proxy
     */
    String getRackProxyUrl();

    /**
     * Get whether or not rack proxy is enabled
     * @return True if enabled; False otherwise
     */
    Boolean getRackProxyEnabled();

    /**
     * Gets Device ID
     * @return
     */
    String getId();
}
