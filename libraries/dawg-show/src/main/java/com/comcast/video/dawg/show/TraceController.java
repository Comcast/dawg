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

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.comcast.video.dawg.common.security.jwt.JwtDeviceAccessValidator;

/**
 * Controller class to export the trace logs from dawg show.
 *
 * @author Kiran Kumar S
 *
 */
@Controller
public class TraceController {

    /** Date formatter to format date which is used in log file naming. */
    private static final SimpleDateFormat FILE_NAME_DATE_FORMATTER = new SimpleDateFormat("MM-dd-yyyy_hh-mm-ss");

    private static final Logger LOGGER = Logger.getLogger(TraceController.class);

    @Autowired
    private JwtDeviceAccessValidator accessValidator;

    @RequestMapping(value = "trace/export", method = { RequestMethod.POST }, produces = "text/plain")
    @ResponseBody
    public ModelAndView exportTrace(@RequestParam() String trace, @RequestParam() String device, 
            HttpServletRequest req, HttpServletResponse response)  throws IOException {
        generateLogFile(trace, device, req, response);
        return null;
    }

    /**
     * Method to generate a trace log file
     * @param trace trace log obtained from trace div
     * @param device device Id of the settop
     * @param response http response to get the trace file
     * @throws IOException Exception occurring while writing log.
     */
    public void generateLogFile(String trace, String device, HttpServletRequest req, HttpServletResponse response) throws IOException {
        accessValidator.validateUserHasAccessToDevices(req, response, false, device);
        String timestamp = FILE_NAME_DATE_FORMATTER.format(new Date());
        response.setHeader("Content-Disposition", "attachment; filename=Settop_"+ device + "_" + timestamp + ".log");

        PrintWriter writer = null;
        String log = StringEscapeUtils.unescapeHtml(trace.trim());
        try {
            writer = response.getWriter();
            writer.write(log);
        } catch (IOException ioe) {
            LOGGER.error("Exception while generating trace logs ", ioe);
            throw ioe;
        } finally {

            if (writer != null) {
                writer.flush();
                writer.close();
            }

            response.flushBuffer();
        }
    }
}
