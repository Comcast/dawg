/**
 * Copyright 2010 Comcast Cable Communications Management, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.comcast.video.dawg.common;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.comcast.audio.stbio.meta.AudioMeta;
import com.comcast.video.stbio.meta.Capability;
import com.comcast.video.stbio.meta.Family;
import com.comcast.video.stbio.meta.IrMeta;
import com.comcast.video.stbio.meta.Model;
import com.comcast.video.stbio.meta.Program;
import com.comcast.video.stbio.meta.SerialMeta;
import com.comcast.video.stbio.meta.StbProp;
import com.comcast.video.stbio.meta.StbProperties;
import com.comcast.video.stbio.meta.VideoMeta;

/**
 * MetaStb is the base implementation of {@link DawgDevice}. It contains getters
 * and setters for the most important and common meta data associated with a
 * settop box.
 *
 * @author Val Apgar, Prasad Menon
 */
public class MetaStb implements Comparable<MetaStb>, DawgDevice, StbProperties, IrMeta, VideoMeta, SerialMeta, AudioMeta {

    public static final String ID                    = "id";
    public static final String TAGS                  = "tags";
    public static final String NAME                  = "name";
    public static final String MAKE                  = "make";
    public static final String MODEL                 = "model";
    public static final String PLANT                 = "plant";
    public static final String PROGRAM               = "program";
    public static final String CONTENT               = "content";
    public static final String IPADDRESS             = "ipAddress";
    public static final String SLOT_NAME             = "slotName";
    public static final String MACADDRESS            = "macAddress";
    public static final String IRSERVICEURL          = "irServiceUrl";
    public static final String IRBLASTERTYPE         = "irBlasterType";
    public static final String IRSERVICEPORT         = "irServicePort";
    public static final String VIDEOSOURCEURL        = "videoSourceUrl";
    public static final String VIDEOCAMERA           = "videoCamera";
    public static final String HD_VIDEO_URL          = "hdVideoUrl";
    public static final String POWERSERVICEURL       = "powerServiceUrl";
    public static final String POWEROUTLET           = "powerOutlet";
    public static final String POWERTYPE             = "powerType";
    public static final String SERIAL_HOST           = "traceServiceUrl";
    public static final String CATSSERVERHOST        = "catsServerHost";
    public static final String REMOTETYPE            = "remoteType";
    public static final String CAPABILITIES          = "capabilities";
    public static final String FAMILY                = "family";
    public static final String CHANNEL_MAP_ID        = "channelMapId";
    public static final String CONTROLLER_NAME       = "controllerName";
    public static final String CONTROLLER_ID         = "controllerId";
    public static final String CONTROLLER_IP_ADDRESS = "controllerIpAddress";
    public static final String MODIFIED_PROPS        = "modifiedProps";
    public static final String RACK_NAME             = "rackName";
    public static final String ENVIRONMENT_ID        = "environmentId";
    public static final String HARDWARE_REVISION     = "hardwareRevision";
    public static final String AUDIOSOURCEURL        = "audioSourceUrl";
    public static final String CATSKEYSETMAPPING     = "catsKeySetMapping";

    private Map<String, Object> data;

    public MetaStb() {
        this(new HashMap<String, Object>());
    }

    public MetaStb(DawgDevice device) {
        this(device.getData());
    }

    public MetaStb(Map<String, Object> data) {
        this.data = data;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public String getId() {
        return (String) data.get(MetaStb.ID);
    }

    public void setId(String id) {
        data.put(MetaStb.ID, id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return (String) data.get(MetaStb.NAME);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setName(String name) {
        data.put(MetaStb.NAME, name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getMake() {
        return (String) data.get(MetaStb.MAKE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setMake(String make) {
        data.put(MetaStb.MAKE, make);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Model getModel() {
        return Model.valueOf(((String) data.get(MetaStb.MODEL)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setModel(Model model) {
        data.put(MetaStb.MODEL, stbPropName(model));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Family getFamily() {
        return Family.valueOf(((String) data.get(MetaStb.FAMILY)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setFamily(Family family) {
        data.put(MetaStb.FAMILY, stbPropName(family));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public Collection<Capability> getCapabilities() {
        Collection<Capability> rv = new ArrayList<Capability>();
        List<String> capStrs = (List<String>) data.get(MetaStb.CAPABILITIES);
        if (capStrs != null) {
            for (String cap : capStrs) {
                rv.add(Capability.valueOf(cap));
            }
        }
        return rv;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCapabilities(Collection<Capability> capabilities) {
        Collection<String> caps = new ArrayList<String>();
        for (Capability cap : capabilities) {
            if (cap != null) caps.add(cap.name());
        }
        data.put(MetaStb.CAPABILITIES, caps);
    }

    public String getPlant() {
        return (String) data.get(MetaStb.PLANT);
    }

    public void setPlant(String plant) {
        data.put(MetaStb.PLANT, plant);
    }

    public String getContent() {
        return (String) data.get(MetaStb.CONTENT);
    }

    public void setContent(String content) {
        data.put(MetaStb.CONTENT, content);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InetAddress getIpAddress() {
        try {
            return InetAddress.getByName((String) data.get(MetaStb.IPADDRESS));
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setIpAddress(InetAddress ipAddress) {
        data.put(MetaStb.IPADDRESS, ipAddress.getHostName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getMacAddress() {
        return (String) data.get(MetaStb.MACADDRESS);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setMacAddress(String macAddress) {
        data.put(MetaStb.MACADDRESS, macAddress);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSlotName() {
        return (String) data.get(MetaStb.SLOT_NAME);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSlotName(String slotName) {
        data.put(MetaStb.SLOT_NAME, slotName);
    }

    public Set<String> getTags() {
        return getSet(MetaStb.TAGS, String.class);
    }

    public void setTags(Set<String> tags) {
        data.put(MetaStb.TAGS, tags);
    }

    public String getIrServiceUrl() {
        return (String) data.get(MetaStb.IRSERVICEURL);
    }

    public void setIrServiceUrl(String irServiceUrl) {
        data.put(MetaStb.IRSERVICEURL, irServiceUrl);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getVideoSourceUrl() {
        return (String) data.get(VIDEOSOURCEURL);
    }

    public void setVideoSourceUrl(String videoSourceUrl) {
        data.put(MetaStb.VIDEOSOURCEURL, videoSourceUrl);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getAudioUrl(){
    	return (String) data.get(AUDIOSOURCEURL);

    }

    public void setAudioUrl(String audioSourceUrl){
    	data.put(MetaStb.AUDIOSOURCEURL, audioSourceUrl);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getVideoCamera() {
        return (String) data.get(VIDEOCAMERA);
    }

    public void setVideoCamera(String videoCamera) {
        data.put(MetaStb.VIDEOCAMERA, videoCamera);
    }

    public String getHdVideoUrl() {
        return (String) data.get(HD_VIDEO_URL);
    }

    public void setHdVideoUrl(String hdVideoUrl) {
        data.put(MetaStb.HD_VIDEO_URL, hdVideoUrl);
    }

    public String getPowerServiceUrl() {
        return (String) data.get(MetaStb.POWERSERVICEURL);
    }

    public void setPowerServiceUrl(String powerServiceUrl) {
        data.put(MetaStb.POWERSERVICEURL, powerServiceUrl);
    }

    public String getSerialHost() {
        return (String) data.get(MetaStb.SERIAL_HOST);
    }

    public void setSerialHost(String serialHost) {
        data.put(MetaStb.SERIAL_HOST, serialHost);
    }

    public String getCatsServerHost() {
        return (String) data.get(MetaStb.CATSSERVERHOST);
    }

    public void setCatsServerHost(String catsServerHost) {
        data.put(MetaStb.CATSSERVERHOST, catsServerHost);
    }

    public String getIrServicePort() {
        return (String) data.get(MetaStb.IRSERVICEPORT);
    }

    public void setIrServicePort(String irServicePort) {
        data.put(MetaStb.IRSERVICEPORT, irServicePort);
    }

    public String getRemoteType() {
        return (String) data.get(MetaStb.REMOTETYPE);
    }

    public void setRemoteType(String remoteType) {
        data.put(MetaStb.REMOTETYPE, remoteType);
    }

    public void setCatsKeySetMapping(String catsKeySetMapping) {
        data.put(MetaStb.CATSKEYSETMAPPING, catsKeySetMapping);
    }

    public String getCatsKeySetMapping() {
        return (String) data.get(MetaStb.CATSKEYSETMAPPING);
    }
    @Override
    public String getBlasterType() {
        return (String) data.get(MetaStb.IRBLASTERTYPE);
    }

    public void setBlasterType(String blasterType) {
        data.put(MetaStb.IRBLASTERTYPE, blasterType);
    }

    public String getPowerOutlet() {
        return (String) data.get(MetaStb.POWEROUTLET);
    }

    public void setPowerOutlet(String powerOutlet) {
        data.put(MetaStb.POWEROUTLET, powerOutlet);
    }

    public String getPowerType() {
        return (String) data.get(MetaStb.POWERTYPE);
    }

    public void setPowerType(String powerType) {
        data.put(MetaStb.POWERTYPE, powerType);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Program getProgram() {
        return Program.valueOf((String) data.get(MetaStb.PROGRAM));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setProgram(Program program) {
        data.put(MetaStb.PROGRAM, stbPropName(program));
    }

    public String getChannelMapId() {
        return (String) data.get(MetaStb.CHANNEL_MAP_ID);
    }

    public void setChannelMapId(String channelMapId) {
        data.put(MetaStb.CHANNEL_MAP_ID, channelMapId);
    }

    public Set<String> getModifiedProps() {
        return getSet(MetaStb.MODIFIED_PROPS, String.class);
    }

    /**
     * Sets the modified properties.
     *
     * @param modifiedProps
     *            the Set<String> value to be set as the modified properties.
     */
    public void setModifiedProps(Set<String> modifiedProps) {
        data.put(MetaStb.MODIFIED_PROPS, modifiedProps);
    }

    @SuppressWarnings({ "unchecked" })
    private <T> Set<T> getSet(String key, Class<T> itemClass) {
        /** Performing a type checked cast.
         * This might be a little more robust if we convert the MetaStb object to have actual fields
         * instead of a map, but we will save that for a later date.
         * kpears201
         */
        Collection<Object> coll = (Collection<Object>) data.get(key);
        if (coll == null) {
            return null;
        }
        if (coll instanceof Set) {
            return (Set<T>) coll;
        }
        Set<T> set = new HashSet<T>();
        for (Object obj : coll) {
            set.add((T) obj);
        }
        data.put(key, set);
        return set;
    }

    @Override
    public String getControllerId(){
        return (String) data.get(MetaStb.CONTROLLER_ID);
    }

    @Override
    public void setControllerId(String controllerId) {
        data.put(MetaStb.CONTROLLER_ID, controllerId);
    }

    @Override
    public String getControllerName() {
        return (String) data.get(MetaStb.CONTROLLER_NAME);
    }

    @Override
    public void setControllerName(String controllerName) {
        data.put(MetaStb.CONTROLLER_NAME, controllerName);
    }

    @Override
    public String getControllerIpAddress() {
        return (String) data.get(MetaStb.CONTROLLER_IP_ADDRESS);
    }

    @Override
    public void setControllerIpAddress(String controllerIpAddress) {
        data.put(MetaStb.CONTROLLER_IP_ADDRESS, controllerIpAddress);
    }

    @Override
    public String getEnvironmentId() {
        return (String) data.get(MetaStb.ENVIRONMENT_ID);
    }

    @Override
    public void setEnvironmentId(String environmentId) {
        data.put(MetaStb.ENVIRONMENT_ID, environmentId);
    }

    /**
     * Get the current Hardware revision number
     * @return current Hardware revision number
     */
    @Override
    public String getHardwareRevision() {
        return (String) data.get(MetaStb.HARDWARE_REVISION);
    }

    /**
     * Sets the hardware revision number
     * @param hardwareRevision hardware revision number to be set.
     */
    @Override
    public void setHardwareRevision(String hardwareRevision) {
        data.put(MetaStb.HARDWARE_REVISION, hardwareRevision);
    }

    /**
     * Get the name of the rack
     * @return rack name
     */
    public String getRackName(){
        return (String) data.get(MetaStb.RACK_NAME);
    }

    /**
     * Sets the rack name
     * @param rackName name of the rack to be set.
     */
    public void setRackName(String rackName){
        data.put(MetaStb.RACK_NAME, rackName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasCapability(Capability cap) {
        for (Capability capability : getCapabilities()) {
            if (capability.equals(cap)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int compareTo(MetaStb o) {
        if (null == o) {
            return -1;
        }
        if (null == this.getId() && null == o.getId()) {
            return 0;
        }
        if (null == this.getId()) {
            return -1;
        }
        if (null == o.getId()) {
            return 1;
        }
        return this.getId().compareTo(o.getId());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((data == null) ? 0 : data.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        MetaStb other = (MetaStb) obj;
        if (data == null) {
            if (other.data != null)
                return false;
        } else if (!data.equals(other.data))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return getData().toString();
    }

    /**
     * {@inheritDocs}
     */
    @Override
    public Object getPropertyValue(String propKey) {
        return data.get(propKey);
    }

    /**
     * {@inheritDocs}
     */
    @Override
    public void setPropertyValue(String propKey, Object val) {
        data.put(propKey, val);
    }

    private String stbPropName(StbProp prop) {
        return prop == null ? null : prop.name();
    }
}
