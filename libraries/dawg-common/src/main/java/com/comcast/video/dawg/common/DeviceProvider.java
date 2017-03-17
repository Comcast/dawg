package com.comcast.video.dawg.common;

import java.util.Collection;

/**
 * Interface that provides dawg devices based on a reservation token
 * @author Kevin Pearson
 *
 */
public interface DeviceProvider {
    Collection<MetaStb> getDevices(String token);
}
