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
package com.comcast.video.dawg.cats.ir;



import org.apache.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.comcast.drivethru.test.MockRestClient;
import com.comcast.video.dawg.common.MetaStb;
import com.comcast.video.stbio.Key;
import com.comcast.video.stbio.exceptions.KeyException;

/**
 * The TestNg test class for IrClient
 * @author TATA
 *
 */
public class IrClientTest {
    public static final String REMOTE_TYPE = "MOTOROLA";

    String catsHost ="10.253.86.157:8080";

    class MockIrClient extends IrClient {

        public MockIrClient(String catsHost, String irHost, String port, String remoteType) {
            this(catsHost, irHost, port, remoteType, HttpStatus.SC_OK);
        }

        public MockIrClient(String catsHost, String irHost, String port, String remoteType, int status) {
            super(catsHost, irHost, port, remoteType);
            assignMockRestClient(status);
        }

        public void assignMockRestClient(int status) {
            MockRestClient mrc = new MockRestClient(null);
            mrc.expect().andReturn(status);
            this.client = mrc;
        }
    }

    @Test(expectedExceptions=IllegalArgumentException.class)
    public void testConstructorWithNullParam() {
        new IrClient(null);
    }

    @Test
    public void testConstructor() {
        MetaStb stb = new MetaStb();
        IrClient client = new IrClient(stb);
        Assert.assertNotNull(client);
    }

    @Test
    public void testPressKeyWithNullCatsHost() throws KeyException {

        MockIrClient client = new MockIrClient(null, null, null, REMOTE_TYPE);

        client.pressKey(Key.VOLUP);
    }

    @Test(expectedExceptions=KeyException.class)
    public void testPressKeyWithInvalidCatsHost() throws KeyException {

        MockIrClient client = new MockIrClient("", null, null, REMOTE_TYPE, HttpStatus.SC_BAD_REQUEST);

        client.pressKey(Key.VOLUP);
    }

    @Test(dataProvider="key")
    public void testPressKeyWithCatsHost(Key key) {

        IrClient client = new MockIrClient(catsHost, null, null, REMOTE_TYPE);

        try {
            client.pressKey(key);
        } catch(Exception e) {
            Assert.fail("PressKey method threw exception" + e);
        }
    }

    @Test
    public void testPressKeys() {

        IrClient client = new MockIrClient(catsHost, null, null, REMOTE_TYPE);
        try {
            Key[]  keys = new Key[] {Key.CHDN,Key.CHDN,Key.GUIDE};
            client.pressKeys(keys );
        } catch(Exception e) {
            Assert.fail("PressKey method threw exception" + e);
        }
    }

    @Test
    public void testPressHoldKey() {

        IrClient client = new MockIrClient(catsHost, null, null, REMOTE_TYPE);
        try {
            long  holdTime = 500;
            client.holdKey(Key.CHDN, holdTime  );

        } catch(Exception e) {
            Assert.fail("PressKey method threw exception" + e);
        }
    }

    @Test
    public void testIRKeyException() {
        String msg = "msg";
        KeyException exp = new KeyException(msg);
        Assert.assertEquals(exp.getMessage(), msg);
        exp = new KeyException(new Throwable());
        Assert.assertNotEquals(exp.getMessage(), msg);
    }

    @DataProvider(name = "key")
    public Object[][] keyProvider() {
        return new Object[][] {
            { Key.A  },
            { Key.APPLIST },
            { Key.ASPECT },
            { Key.AUDIO },
            { Key.B  },
            { Key.BLUE },
            { Key.BYPASS},
            { Key.CHDN},
            { Key.CHUP  },
            { Key.D  },
            { Key.DAYDN},
            { Key.DAYUP},
            { Key.DOWN  },
            { Key.DOT },
            { Key.DVR  },
            { Key.EIGHT },
            { Key.ENTER },
            { Key.FAV },
            { Key.FAV_1 },
            { Key.FAV_2 },
            { Key.FAV_3 },
            { Key.FAV_4 },
            { Key.FF},
            { Key.FIVE},
            { Key.FOUR },
            { Key.FREEZE },
            { Key.FUN_A },
            { Key.GUIDE  },
            { Key.GREEN },
            { Key.HDZOOM },
            { Key.HELP},
            { Key.INFO},
            { Key.LANG  },
            { Key.LAST },
            { Key.LEFT},
            { Key.LIVE},
            { Key.LOCK  },
            { Key.MENU },
            { Key.MUSIC },
            { Key.MUTE},
            { Key.MYDVR},
            { Key.NEXT },
            { Key.NINE  },
            { Key.ONDEMAND },
            { Key.ONE},
            { Key.PAUSE},
            { Key.PICTURE },
            { Key.PGDN},
            { Key.PGUP},
            { Key.PHOTO },
            { Key.PIP },
            { Key.PIPCHDN },
            { Key.PIPCHUP  },
            { Key.PIPMOVE},
            { Key.PIPONOFF},
            { Key.PIPSWAP  },
            { Key.PLAY },
            { Key.POWER},
            { Key.PREV },
            { Key.REC},
            { Key.RED },
            { Key.REPLAY },
            { Key.REVERSE },
            { Key.REW },
            { Key.RIGHT},
            { Key.SELECT},
            { Key.SETTINGS  },
            { Key.SETUP },
            { Key.SEVEN},
            { Key.SIX},
            { Key.SLEEP },
            { Key.SKIPFWD },
            { Key.SOURCE },
            { Key.STOP},
            { Key.SUBTITLE },
            { Key.SWAP},
            { Key.TEXT },
            { Key.THREE},
            { Key.TWO},
            { Key.UP },
            { Key.VIDEO },
            { Key.VOLDN},
            { Key.VOLUP},
            { Key.WATCH_TV },
            { Key.YELLOW },
            { Key.ZERO}
        };
    }
}
