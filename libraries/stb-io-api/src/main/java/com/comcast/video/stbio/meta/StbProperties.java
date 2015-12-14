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
package com.comcast.video.stbio.meta;

import java.net.InetAddress;
import java.util.Collection;

/**
 * The base interface of an object that contains essential stb properties
 * @author Kevin Pearson, Prasad Menon
 *
 */
public interface StbProperties {

    /**
     * Gets the model of the stb
     * @return
     */
    public Model getModel();

    /**
     * Sets the model of the stb
     * @param model
     */
    public void setModel(Model model);

    /**
     * Gets the software running on the stb
     * @return
     */
    public Program getProgram() ;

    /**
     * Sets the software running on the stb
     * @param program
     */
    public void setProgram(Program program);

    /**
     * Gets the capabilities of an stb
     * @return
     */
    public Collection<Capability> getCapabilities();

    /**
     * Sets the capabilities of an stb
     * @param capabilities
     */
    public void setCapabilities(Collection<Capability> capabilities);

    /**
     * Gets the name given to the stb
     * @return
     */
    public String getName();

    /**
     * Sets the name given to the stb
     * @param name
     */
    public void setName(String name);

    /**
     * Gets the stb mac address
     * @return
     */
    public String getMacAddress();

    /**
     * Sets the stb mac address
     * @param macAddress
     */
    public void setMacAddress(String macAddress);

    /**
     * Gets the slot position information of the settop in the rack
     * @return The slot position information of the settop in the rack
     */
    public String getSlotName();

    /**
     * Sets the slot position information of the settop in the rack
     * @param slotName  The slot name information
     */
    public void setSlotName(String slotName);

    /**
     * Gets the up address of the stb
     * @return
     */
    public InetAddress getIpAddress();

    /**
     * Sets the up address of the stb
     * @param ipAddress
     */
    public void setIpAddress(InetAddress ipAddress);

    /**
     * Gets some arbitrary name use to categorize stbs
     * @return
     */
    public Family getFamily();

    /**
     * Sets some arbitrary name use to categorize stbs
     * @param family
     */
    public void setFamily(Family family);

    /**
     * Gets the maker of the stb
     * @return
     */
    public String getMake();

    /**
     * Sets the maker of the stb
     * @param make
     */
    public void setMake(String make);

    /**
     * Checks if this stb has the given capability
     * @param cap The capability to check if the stb has
     * @return
     */
    public boolean hasCapability(Capability cap);

    /**
     * Get the name of the controller.
     * @return controller name
     */
    public String getControllerName();

    /**
     * Sets the controller name
     * @param controllerName name of the controller to be set.
     */
    public void setControllerName(String controllerName);

    /**
     * Gets the controller id.
     * @return controller id
     */
    public String getControllerId();

    /**
     * Sets the controller id.
     * @param controllerId id box controller id.
     */
    public void setControllerId(String controllerId);

    /**
     * Gets the controller ip Address.
     * @return controller ip Address
     */
    public String getControllerIpAddress();

    /**
     * Sets the controller ip Address.
     * @param controller ip Address.
     */
    public void setControllerIpAddress(String controllerIpAddress);

    /**
     * Gets the Content type of the settop
     * @return content
     */
    public String getContent();
    /**
     * Sets the content type of the settop
     * @param content
     */
    public void setContent(String content);

    /**
     * Gets the CATS server Host
     * @return  CATS server Host
     */
    public String getCatsServerHost();

    /**
     * Sets the CATS server Host
     * @param catsServerHost The CATS server Host
     */
    public void setCatsServerHost(String catsServerHost);

    /**
     * Gets the SCAT host which is connected
     *
     * @return The SCAT host
     */
    public String getSerialHost();

    /**
     * Sets the SCAT host
     *
     * @param serialHost The SCAT host
     */
    public void setSerialHost(String serialHost);

    /**
     * Gets the environment Id for getting the CATS server information associated with a Settop
     *
     * @return environment Id The environment Id
     */
    public String getEnvironmentId();

    /**
     * Sets the environment Id associated with a Settop
     *
     * @param environmentId The environment Id
     */
    public void setEnvironmentId(String environmentId);

    /**
     * Gets a property value for the given key
     * @param propKey The key of the property to get
     * @return
     */
    public Object getPropertyValue(String propKey);

    /**
     * Sets a property value based on a key
     * @param propKey The key that the property is stored under
     * @param val The value to set it to
     */
    public void setPropertyValue(String propKey, Object val);

    /**
     * Gets the Hardware revision number
     * @return Hardware revision number
     */
    public String getHardwareRevision ();

    /**
     * Sets the Hardware revision number associated with the STB
     * @param hardwareRevision Hardware revision number
     */
    public void setHardwareRevision (String hardwareRevision);
}
