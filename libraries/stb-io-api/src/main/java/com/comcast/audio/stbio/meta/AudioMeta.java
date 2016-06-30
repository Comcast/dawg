package com.comcast.audio.stbio.meta;

import com.comcast.video.stbio.meta.VideoMeta;
/**
 * Meta data necessary to access the audio of a set top box
 * @author Devaki Dikshit
 *
 */
public interface AudioMeta {
	/**
     * The url of the audio service
     * @return
     */
    String getAudioUrl();
}
