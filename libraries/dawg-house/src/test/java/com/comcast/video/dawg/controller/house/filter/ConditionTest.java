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
package com.comcast.video.dawg.controller.house.filter;


import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.comcast.pantry.test.TestList;

public class ConditionTest {

    @DataProvider(name="testConditionsData")
    public TestList testConditionsData() {
        TestList tl = new TestList();

        FieldCondition equalsCond = new FieldCondition(SearchableField.family.getField(), Operator.equals, "MAC_G");
        FieldCondition notEqualsCond = new FieldCondition(SearchableField.family.getField(), Operator.equals, "MAC_G");
        notEqualsCond.setNot(true);
        FieldCondition strContains = new FieldCondition(SearchableField.family.getField(), Operator.contains, "MAC");
        FieldCondition strMatches = new FieldCondition(SearchableField.family.getField(), Operator.matches, "MAC_(G|S)");
        FieldCondition arrContains = new FieldCondition(SearchableField.capabilities.getField(), Operator.contains, "HD", true);
        FieldCondition notStrContains = new FieldCondition(SearchableField.family.getField(), Operator.contains, "MAC");
        notStrContains.setNot(true);
        FieldCondition notStrMatches = new FieldCondition(SearchableField.family.getField(), Operator.matches, "MAC_(G|S)");
        notStrMatches.setNot(true);
        FieldCondition notArrContains = new FieldCondition(SearchableField.capabilities.getField(), Operator.contains, "HD", true);
        notArrContains.setNot(true);

        GroupCondition and2Group = new GroupCondition(GroupOperator.and, equalsCond, arrContains);
        GroupCondition and3Group = new GroupCondition(GroupOperator.and, equalsCond, arrContains, strMatches);
        GroupCondition orGroup = new GroupCondition(GroupOperator.or, equalsCond, arrContains);
        GroupCondition notAndGroup = new GroupCondition(GroupOperator.and, equalsCond, arrContains);
        notAndGroup.setNot(true);
        GroupCondition notOrGroup = new GroupCondition(GroupOperator.or, equalsCond, arrContains);
        notOrGroup.setNot(true);

        FieldCondition modelEquals = new FieldCondition(SearchableField.model.getField(), Operator.equals, "DCT6416");
        FieldCondition makeEquals = new FieldCondition(SearchableField.make.getField(), Operator.equals, "Motorola");
        GroupCondition nestedGroup = new GroupCondition(GroupOperator.and, and2Group, new GroupCondition(GroupOperator.and, modelEquals, makeEquals));
        GroupCondition notNestedGroup = new GroupCondition(GroupOperator.and, and2Group, new GroupCondition(GroupOperator.and, modelEquals, makeEquals));
        notNestedGroup.setNot(true);

        String famEq = "{ \"data.family\" : \"MAC_G\"}";
        String famNotEq = "{ \"data.family\" : { \"$ne\" : \"MAC_G\"}}";
        String macContainsRegex = "{ \"$regex\" : \".*MAC.*\"}";
        String macPatternRegex = "{ \"$regex\" : \"MAC_(G|S)\"}";
        String famContains = "{ \"data.family\" : " + macContainsRegex + "}";
        String famMatches = "{ \"data.family\" : " + macPatternRegex + "}";
        String containsHd = "{ \"$in\" : [ \"HD\"]}";
        String capContains = "{ \"data.capabilities\" : " + containsHd + "}";
        String notCapContains = "{ \"data.capabilities\" : { \"$not\" : " + containsHd + "}}";
        String modelDCT6416 = "{ \"data.model\" : \"DCT6416\"}";
        String makeMotorola = "{ \"data.make\" : \"Motorola\"}";
        String notModelDCT6416 = "{ \"data.model\" : { \"$ne\" : \"DCT6416\"}}";
        String notMakeMotorola = "{ \"data.make\" : { \"$ne\" : \"Motorola\"}}";

        tl.add(equalsCond, famEq);
        tl.add(notEqualsCond, famNotEq);
        tl.add(strContains, famContains);
        tl.add(strMatches, famMatches);
        tl.add(arrContains, capContains);
        tl.add(orGroup, "{ \"$or\" : [ " + famEq + " , " + capContains + "]}");
        tl.add(notStrContains, "{ \"data.family\" : { \"$not\" : " + macContainsRegex + "}}");
        tl.add(notStrMatches, "{ \"data.family\" : { \"$not\" : " + macPatternRegex + "}}");
        tl.add(notArrContains, notCapContains);
        tl.add(and2Group, "{ \"$and\" : [ " + famEq + " , " + capContains + "]}");
        tl.add(and3Group, "{ \"$and\" : [ " + famEq + " , " + capContains + " , " + famMatches + "]}");
        tl.add(notAndGroup, "{ \"$or\" : [ " + famNotEq + " , " + notCapContains + "]}");
        tl.add(notOrGroup, "{ \"$and\" : [ " + famNotEq + " , " + notCapContains + "]}");

        tl.add(nestedGroup, "{ \"$and\" : [ { \"$and\" : [ " + famEq + " , " + capContains + "]} , { \"$and\" : [ " + modelDCT6416 + " , " + makeMotorola + "]}]}");
        tl.add(notNestedGroup, "{ \"$or\" : [ { \"$or\" : [ " + famNotEq + " , " + notCapContains + "]} , { \"$or\" : [ " + notModelDCT6416 + " , " + notMakeMotorola + "]}]}");

        return tl;
    }

    @Test(dataProvider="testConditionsData")
    public void testConditions(Condition condition, String mongoDbExpression) {
        Assert.assertEquals(condition.toCriteria().getCriteriaObject().toString(), mongoDbExpression);
    }

    @Test
    public void testGettersAndSetters() {
        FieldCondition equalsCond = new FieldCondition(SearchableField.family.getField(), Operator.equals, "MAC_G");
        FieldCondition doesNotHaveCap = new FieldCondition(SearchableField.capabilities, Operator.contains, "HD");
        doesNotHaveCap.setNot(true);
        GroupCondition group = new GroupCondition(GroupOperator.and, equalsCond, doesNotHaveCap);

        Assert.assertEquals(equalsCond.getField(), SearchableField.family.getField());
        Assert.assertEquals(equalsCond.getOp(), Operator.equals);
        Assert.assertEquals(equalsCond.getValue(), "MAC_G");
        Assert.assertEquals(equalsCond.isArray(), false);
        Assert.assertEquals(equalsCond.isNot(), false);

        Assert.assertEquals(doesNotHaveCap.getField(), SearchableField.capabilities.getField());
        Assert.assertEquals(doesNotHaveCap.getOp(), Operator.contains);
        Assert.assertEquals(doesNotHaveCap.getValue(), "HD");
        Assert.assertEquals(doesNotHaveCap.isArray(), true);
        Assert.assertEquals(doesNotHaveCap.isNot(), true);

        Assert.assertEquals(group.getGroupOp(), GroupOperator.and);
        Assert.assertEquals(group.getConditions().size(), 2);
        Assert.assertTrue(group.getConditions().contains(equalsCond));
        Assert.assertTrue(group.getConditions().contains(doesNotHaveCap));

        Assert.assertEquals(SearchableField.family.getDisplay(), "Family");
    }

    @Test(expectedExceptions=RuntimeException.class)
    public void testInvalidGroupCondition() {
        new GroupCondition(GroupOperator.and);
    }
}
