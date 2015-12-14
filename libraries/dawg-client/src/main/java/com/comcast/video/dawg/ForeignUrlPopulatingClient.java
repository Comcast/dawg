package com.comcast.video.dawg;

import java.util.Map;
import java.util.List;
import java.util.LinkedList;
import java.util.IllegalFormatException;
import java.util.Iterator;

import java.io.IOException;

import java.net.URL;
import java.net.MalformedURLException;

import com.comcast.video.dawg.common.Config;
import com.comcast.video.dawg.common.MetaStb;

import com.google.gson.JsonParser;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import com.google.gson.JsonArray;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;

import org.springframework.beans.factory.annotation.Autowired;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ForeignUrlPopulatingClient implements PopulatingClient
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ForeignUrlPopulatingClient.class);

    private String foreignUrlFmt;

    @Autowired
    public ForeignUrlPopulatingClient()
    {
        this.foreignUrlFmt = Config.get("ForeignURLPopulatingClient", "url-fmt");
    }

    public List<MetaStb> populate(String arg) throws Exception
    {
        List<MetaStb> ret = new LinkedList<MetaStb>();

        if(this.foreignUrlFmt == null) {
            LOGGER.error("The foreign url format string is null, so no URL can be queried.");
            return ret;
        }

        String fmtUrl = "";

        try {
            fmtUrl = String.format(this.foreignUrlFmt, arg);
        }
        catch(IllegalFormatException ife) {
            LOGGER.error("Unable to format the url string: {}", this.foreignUrlFmt, ife);
            return ret;
        }

        URL url = null;

        try {
            url = new URL(fmtUrl);
        }
        catch(MalformedURLException mue) {
            LOGGER.error("The entered url[{}] is malformed, and cannot be querried.", fmtUrl, mue);
            return ret;
        }

        String data = null;

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            IOUtils.copy(url.openStream(), baos);
            data = baos.toString();
        }
        catch(IOException ioe) {
            LOGGER.error("Error retrieving url[{}]: ", fmtUrl, ioe);
            return ret;
        }

        if(StringUtils.isBlank(data)) {
            LOGGER.warn("The returned data from url[{}] was blank.", fmtUrl);
            return ret;
        }

        JsonParser parser = new JsonParser();
        JsonElement elem = null;

        try {
            elem = parser.parse(data);
        }
        catch(JsonSyntaxException jse) {
            LOGGER.error("Failed to parse the returned data as json.", jse);
            return ret;
        }

        if(elem.isJsonArray()) { //there is a returned array of MetaStb
            JsonArray jarr = (JsonArray)elem;
            JsonElement jel = null;

            Iterator<JsonElement> jels = jarr.iterator();

            while(jels.hasNext()) {
                jel = jels.next();

                if(jel.isJsonObject())
                    ret.add(ForeignUrlPopulatingClient.fillMetaStb((JsonObject)jel));
                else
                    LOGGER.error("Unable to handle input.  Expected only objects under root json array.");
            }
        }
        else if(elem.isJsonObject()) { //returned a single stb
            ret.add(ForeignUrlPopulatingClient.fillMetaStb((JsonObject)elem));
        }
        else {
            LOGGER.error("Unable to handle json input.  Top level object should be either an array of objects representing MetaStb, or a single object representing a single Metastb");
            return ret;
        }

        return ret;
    }

    private static MetaStb fillMetaStb(JsonObject jobj) {
        MetaStb stb = new MetaStb();

        String key = null;
        JsonElement value = null;

        for(Map.Entry<String, JsonElement> entry : jobj.entrySet()) {
            key = entry.getKey();
            value = entry.getValue();

            if(value.isJsonPrimitive()) { //the value is primitive, do direct translation
                JsonPrimitive prim = (JsonPrimitive)value;
                if(prim.isBoolean())
                    stb.setPropertyValue(key, new Boolean(prim.getAsBoolean()));
                else if(prim.isString())
                    stb.setPropertyValue(key, prim.getAsString());
                else { //it has to be a number
                    stb.setPropertyValue(key, prim.getAsNumber());
                }
            }
            else { //set the JsonElement because the inner value is non-primitive
                stb.setPropertyValue(key, value);
            }
        }

        return stb;
    }
}
