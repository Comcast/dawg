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
package com.comcast.video.dawg.show;

/**
 * Holds constants for UI views
 *
 * @author Kevin Pearson
 *
 */
public interface ViewConstants {
    /** View Names */
    public static final String VIEWS = "views/";
    public static final String STB = VIEWS + "stb";
    public static final String SIMPLIFIEDVIEW = VIEWS + "simplifiedview";
    public static final String MULTI = VIEWS + "multi";
    public static final String NOSTB = VIEWS + "nostb";
    public static final String COMPARE_NOT_FOUND = VIEWS + "compareNotFound";
    public static final String META = VIEWS + "meta";
    public static final String BROWSERNOTSUPPORTED = VIEWS + "bns";
    public static final String SNAPPEDIMAGE = VIEWS + "snappedImage";
    public static final String SNAP_DOWNLOAD = VIEWS + "multiSnapDownloadManager";
    public static final String REMOTE_PLUGIN = VIEWS + "remote-plugin";
    public static final String PLUGIN_CONFIG = VIEWS + "plugin-config";

    /** View parameters */
    public static final String DEVICE_ID = "deviceId";
    public static final String STBS = "stbs";
    public static final String MODEL = "model";
    public static final String IPADDRESS = "ipAddress";
    public static final String STB_PARAM = "stb";
    public static final String REMOTE_MANAGER = "remoteManager";
    public static final String REMOTE = "remote";
    public static final String REMOTE_TYPES = "remoteTypes";
    public static final String SELECTED_REMOTE_TYPE = "selectedRemoteType";
    public static final String MOBILE = "mobile";
    public static final String VIDEO_URL = "videoUrl";
    public static final String VIDEO_CAMERA = "videoCamera";
    public static final String VIDEO_AVAILABLE = "videoAvailable";
    public static final String HD_VIDEO_URL = "hdVideoUrl";
    public static final String IR_AVAILABLE = "irAvailable";
    public static final String TRACE_AVAILABLE = "traceAvailable";
    public static final String TRACE_HOST = "traceHost";
    public static final String SUPPORTED = "supported";
    public static final String AUDIO_URL = "audioUrl";
    /** Represent generic remote keys */
    public static final String GENERIC_REMOTE_KEYS = "genericRemoteKeys";
    /** Represent map of image id with corresponding snapshot id */
    public static final String DEVICEID_IMAGEID_MAP = "deviceIdImageIdMap";

    public static final String IMAGE_CACHE = "imageCache";
}
