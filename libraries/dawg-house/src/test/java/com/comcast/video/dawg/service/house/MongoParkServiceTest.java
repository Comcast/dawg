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
package com.comcast.video.dawg.service.house;

import org.easymock.EasyMock;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.comcast.video.dawg.service.park.MongoParkService;

/**
 * The TestNG Test class for  MongoParkService
 * @author TATA
 *
 */
public class MongoParkServiceTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testFindAll(){
        MongoParkService service = new MongoParkService();
        MongoOperations operations = EasyMock.createMock(MongoOperations.class);
        EasyMock.expect(operations.find((Query) EasyMock.anyObject(), (Class) EasyMock.anyObject(),
                    (String) EasyMock.anyObject())).andReturn(null);
        EasyMock.replay(operations);
        ReflectionTestUtils.setField(service, "template", operations);
        Assert.assertNotNull(service.findAll());

    }
    @SuppressWarnings("unchecked")
    @Test
    public void testFindAllWithPageable(){
        MongoParkService service = new MongoParkService();
        MongoOperations operations = EasyMock.createMock(MongoOperations.class);
        EasyMock.expect(operations.find((Query) EasyMock.anyObject(), (Class) EasyMock.anyObject(),
                    (String) EasyMock.anyObject())).andReturn(null).anyTimes();
        EasyMock.replay(operations);
        ReflectionTestUtils.setField(service, "template", operations);
        Assert.assertNotNull(service.findAll(null));
        Assert.assertNotNull(service.findAll(new PageRequest(0, 1)));

    }
    @SuppressWarnings("unchecked")
    @Test
    public void testFindByKeys(){
        MongoParkService service = new MongoParkService();
        MongoOperations operations = EasyMock.createMock(MongoOperations.class);
        EasyMock.expect(operations.find((Query) EasyMock.anyObject(), (Class) EasyMock.anyObject(),
                    (String) EasyMock.anyObject())).andReturn(null);
        EasyMock.replay(operations);
        ReflectionTestUtils.setField(service, "template", operations);
        String[] keys = new String[1];
        keys[0] = "1";
        Assert.assertNotNull(service.findByKeys(keys));

    }
    @SuppressWarnings("unchecked")
    @Test
    public void testFindByKeysWithPageable(){
        MongoParkService service = new MongoParkService();
        MongoOperations operations = EasyMock.createMock(MongoOperations.class);
        EasyMock.expect(operations.find((Query) EasyMock.anyObject(), (Class) EasyMock.anyObject(),
                    (String) EasyMock.anyObject())).andReturn(null).anyTimes();
        EasyMock.replay(operations);
        ReflectionTestUtils.setField(service, "template", operations);
        String[] keys = new String[1];
        keys[0] = "1";
        Assert.assertNotNull(service.findByKeys(keys,null));
        Assert.assertNotNull(service.findByKeys(keys,new PageRequest(0, 1)));
    }
}
