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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Set;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.comcast.pantry.test.TestList;
import com.comcast.video.stbio.Key;

public class RemoteManagerTest {

    @DataProvider(name="testInvalidFormatData")
    public TestList testInvalidFormatData() {
        TestList list = new TestList();

        list.add("MOTOROLA\n");
        list.add("MOTOROLA|BADKEYSET|MOTOROLA\n");

        return list;
    }

    @Test(expectedExceptions={RuntimeException.class}, dataProvider="testInvalidFormatData")
    public void testInvalidFormat(String remotesContents) throws IOException {
        RemoteManager rMan = new RemoteManager();

        rMan.loadRemotes(new ByteArrayInputStream(remotesContents.getBytes()));
    }

    @Test
    public void testLoad() throws IOException {
        RemoteManager rMan = new RemoteManager();
        rMan.load();
        Collection<Remote> remotes = rMan.getRemotes();
        Assert.assertNotNull(remotes);
        Assert.assertFalse(remotes.isEmpty());
        Remote motorolaRemote = rMan.getRemote("MOTOROLA");
        Assert.assertNotNull(motorolaRemote);
        Assert.assertEquals(motorolaRemote.getName(), "MOTOROLA");
        Assert.assertEquals(motorolaRemote.getImageSubpath(), "motorola");
        KeySet ks = motorolaRemote.getKeySet();
        Assert.assertNotNull(ks);
        Assert.assertTrue(ks.getName().contains("MOTOROLA"));
        Set<Key> keys = motorolaRemote.getKeys();
        Assert.assertTrue(keys.contains(Key.GUIDE));
        Remote genericRemote = rMan.getRemote("GENERIC");
        Assert.assertNotNull(genericRemote);
        Assert.assertEquals(genericRemote.getName(), "GENERIC");
        Assert.assertEquals(genericRemote.getImageSubpath(), "generic");
        ks = genericRemote.getKeySet();
        Assert.assertNotNull(ks);
        Assert.assertFalse(ks.getName().contains("GENERIC"));
        keys = genericRemote.getKeys();
        Assert.assertTrue(keys.contains(Key.GUIDE));

    }
}
