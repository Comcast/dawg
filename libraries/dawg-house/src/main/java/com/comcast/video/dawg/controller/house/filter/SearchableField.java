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
package com.comcast.video.dawg.controller.house.filter;

import com.comcast.video.dawg.common.MetaStb;

/**
 * The enumeration of all the fields that are searchable for a device.
 * @author Kevin Pearson
 *
 */
public enum SearchableField {
    capabilities(MetaStb.CAPABILITIES, "Capabilities", true),
    channel_map_id(MetaStb.CHANNEL_MAP_ID, "Channel Map Id"),
    content(MetaStb.CONTENT, "Content"),
    controller_id(MetaStb.CONTROLLER_ID, "Controller Id"),
    controller_name(MetaStb.CONTROLLER_NAME, "Controller Name"),
    controller_ip_address(MetaStb.CONTROLLER_IP_ADDRESS, "Controller Ip Address"),
    family(MetaStb.FAMILY, "Family"),
    id(MetaStb.ID, "Id"),
    ip_address(MetaStb.IPADDRESS, "IP Address"),
    ir_blaster_type(MetaStb.IRBLASTERTYPE, "IR Blaster Type"),
    mac_address(MetaStb.MACADDRESS, "MAC Address"),
    make(MetaStb.MAKE, "Make"),
    model(MetaStb.MODEL, "Model"),
    name(MetaStb.NAME, "Name"),
    plant(MetaStb.PLANT, "Plant"),
    power_type(MetaStb.POWERTYPE, "Power Type"),
    program(MetaStb.PROGRAM, "Program"),
    remote_type(MetaStb.REMOTETYPE, "Remote Type"),
    tags(MetaStb.TAGS, "Tags", true),
    rack_name(MetaStb.RACK_NAME,"Rack Name"),
    hardware_revision(MetaStb.HARDWARE_REVISION, "Hardware Revision"),
    slot_name(MetaStb.SLOT_NAME, "Slot Name");

    private String field;
    private String display;
    private boolean array;

    /**
     * Creates a SearchableField
     * @param field How the field is provided in the database
     * @param display How the field is displayed to the user
     */
    SearchableField(String field, String display) {
        this(field, display, false);
    }

    /**
     * Creates a SearchableField
     * @param field How the field is provided in the database
     * @param display How the field is displayed to the user
     * @param array If the field in the database has an array of values
     */
    SearchableField(String field, String display, boolean array) {
        this.field = field;
        this.display = display;
        this.array = array;
    }

    public String getField() {
        return field;
    }

    public String getDisplay() {
        return display;
    }

    public boolean isArray() {
        return array;
    }
}
