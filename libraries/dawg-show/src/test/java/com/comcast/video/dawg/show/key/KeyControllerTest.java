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
package com.comcast.video.dawg.show.key;


import javax.servlet.http.HttpServletRequest;

import org.easymock.EasyMock;
import org.springframework.util.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.comcast.pantry.test.TestList;
import com.comcast.pantry.test.Wiring;
import com.comcast.video.dawg.cats.ir.IrClient;
import com.comcast.video.dawg.common.MetaStb;
import com.comcast.video.dawg.common.security.jwt.JwtDeviceAccessValidator;
import com.comcast.video.dawg.show.plugins.RemotePluginManager;
import com.comcast.video.dawg.show.util.MockMetaStbCache;
import com.comcast.video.dawg.show.util.TestUtils;
import com.comcast.video.stbio.Key;
import com.comcast.video.stbio.exceptions.KeyException;

public class KeyControllerTest {

    @DataProvider(name = "testSendKeyData")
    public TestList testChangeModelNameData() {
        TestList testList = new TestList();
        testList.add(new String[] { TestUtils.ID }, "GUIDE", "SA");
        testList.add(new String[] { TestUtils.ID }, "GUIDE", null);
        return testList;
    }

    @Test(dataProvider = "testSendKeyData")
    public void testSendKey(String[] deviceIds, String key, String remoteType) throws KeyException, InterruptedException {
        final MockMetaStbCache cache = new MockMetaStbCache();
        MetaStb stb = TestUtils.createGenericMetaStb();
        cache.cache.put(TestUtils.ID, stb);

        final IrClient client = EasyMock.createMock(IrClient.class);
        client.pressKeys(Key.GUIDE);
        EasyMock.expectLastCall();
        
        HttpServletRequest req = createReq();

        EasyMock.replay(client, req);

        KeyController controller = new KeyController();
        Assert.notNull(controller.createSendKeyThread(null, null, null, null, null, null)); // really just for code coverage
        RemotePluginManager rpm = new RemotePluginManager();

        /**
         * Injecting the IrClient into the SendKeyThread, then injecting the
         * SendKeyThread into the KeyController
         */
        controller = getKeyController(cache, client, rpm);

        Wiring.autowire(controller, cache);
        Wiring.autowire(controller, rpm);
        controller.sendKey(req, null, deviceIds, key, remoteType);

        EasyMock.verify(client, req);
    }
    
    private HttpServletRequest createReq() {
        HttpServletRequest req = EasyMock.createMock(HttpServletRequest.class);
        EasyMock.expect(req.getSession()).andReturn(null);
        EasyMock.expect(req.getCookies()).andReturn(null);
        EasyMock.expect(req.getHeader("Authorization")).andReturn("Bearer X");
        return req;
    }

    private KeyController getKeyController(final MockMetaStbCache cache, final IrClient client, final RemotePluginManager rpm) {
        KeyController controller = new KeyController() {
            protected SendKeyThread createSendKeyThread(String deviceId, Key[] key, String holdTime, RemotePluginManager rpm, String remoteType, String jwt) {
                SendKeyThread thread = new SendKeyThread(deviceId, key,
                        holdTime, cache, rpm, remoteType) {
                    @Override
                    protected IrClient getIrClient(MetaStb stb, String remoteType) {
                        return client;
                    }
                };
                return thread;
            }
        };
        JwtDeviceAccessValidator validator = new JwtDeviceAccessValidator(null, null, false);
        Wiring.autowire(controller, validator);
        return controller;
    }

    @DataProvider(name = "testDirectTuneData")
    public TestList testDirectTuneData() {
        TestList testList = new TestList();
        testList.add(new String[] { TestUtils.ID }, "1234", "SA");
        testList.add(new String[] { TestUtils.ID }, "1234", null);
        testList.add(new String[] { TestUtils.ID }, "4", "SA");
        testList.add(new String[] { TestUtils.ID }, "4", null);

        return testList;
    }

    @Test(dataProvider = "testDirectTuneData")
    public void testDirectTune(String[] deviceIds, String channelNum,
            String remoteType) throws KeyException, InterruptedException {
        final MockMetaStbCache cache = new MockMetaStbCache();
        MetaStb stb = TestUtils.createGenericMetaStb();
        cache.cache.put(TestUtils.ID, stb);

        final IrClient client = EasyMock.createMock(IrClient.class);
        Key[] keys = Key.keysForChannel(channelNum);

        RemotePluginManager rpm = new RemotePluginManager();
        HttpServletRequest req = createReq();

        client.pressKeys(keys);
        EasyMock.expectLastCall();
        EasyMock.replay(client, req);

        KeyController controller = getKeyController(cache, client, rpm);
        Wiring.autowire(controller, cache);

        controller.directTune(req, null, deviceIds, channelNum, remoteType);

        EasyMock.verify(client, req);
    }
}
