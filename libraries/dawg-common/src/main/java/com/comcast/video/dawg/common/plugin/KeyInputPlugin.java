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
package com.comcast.video.dawg.common.plugin;

import com.comcast.video.stbio.KeyInput;

/**
 * Plugin that provides a {@link KeyInput} that can be used as a remote in dawg-show
 * @author Kevin Pearson
 *
 * @param <C> The class that is used to configure the {@link KeyInput}
 */
public interface KeyInputPlugin<C extends PluginConfiguration> extends DawgPlugin<KeyInput, C> {

    /**
     * Gives the remote type that will uniquely identify what remote the plugin provides KeyInputs for.
     * Stbs can use this value in their metadata as remoteType in order to opt into using this plugin
     * @return
     */
    String getRemoteType();

    /**
     * Gives the id of the remote that is shown visually in dawg-show
     * @return
     */
    String getRemoteUiId();
}
