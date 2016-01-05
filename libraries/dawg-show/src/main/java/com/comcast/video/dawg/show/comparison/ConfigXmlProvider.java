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
package com.comcast.video.dawg.show.comparison;

import java.io.ByteArrayOutputStream;
import java.util.List;

import javax.xml.bind.JAXBException;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.comcast.cats.image.RegionInfo;

/**
 * Generates xml files of different types - toolbox xml and catsvision xml.
 *
 * @author Divya R
 *
 */

@Component
public abstract class ConfigXmlProvider {

    /** Logger object for {@link ConfigXmlProvider} */
    private static final Logger logger = Logger.getLogger(ConfigXmlProvider.class);

    /**
     * Takes list of {@link RegionInfo} as parameter and generates ToolBox xml
     * using the input data
     *
     * @param regionInfoList
     *            list of RegionInfo objects
     * @param imagePath
     *            image file name in jpg format
     * @return {@link ByteArrayOutputStream} toolbox xml contents as byte array
     *         output stream.
     */
    public abstract ByteArrayOutputStream getToolBoxXml(List<RegionInfo> regionInfoList, String imagePath);

    /**
     * Takes list of {@link RegionInfo} as parameter and generates CatsVision
     * xml using the input data
     *
     * @param regionInfoList
     *            list of RegionInfo objects
     * @param imagePath
     *            image file name in jpg format
     * @return {@link ByteArrayOutputStream} catsvision xml contents as byte
     *         array output stream.
     */
    public abstract ByteArrayOutputStream getCatsVisionXml(List<RegionInfo> regionInfoList, String imagePath) throws JAXBException;
}
