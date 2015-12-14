/*
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
/**
 * Advance search filter handler. It handles the search history.
 */
var FilterHandler = (function() {
    var filterHandler = {
    };

    /**
     * Binds the last executed advance search condition and list of advance search carried out by user.
     *
     * @param latestSearchCondition
     *            Finally executed advanced search condition. If no advanced search executed during loading of page it
     *            can be empty object.
     * @param searchConditions
     *            Array of advanced search condition executed by the user.
     */
    filterHandler.bind = function(latestSearchCondition, searchConditions) {

        if (latestSearchCondition) {
            var advanceSearchCondition = filterHandler.identifyAndProvideTheSearchCondition(latestSearchCondition);
            if (advanceSearchCondition) {
                FilterConstructor.addCondition(advanceSearchCondition);
            }
        }

        var searchConditionArray = [];
        /** Array is iterated from max to min, to ensure the last executed search be first in the list. */
        for ( var conditionIndex = (searchConditions.length - 1); conditionIndex >= 0; conditionIndex--) {
            var searchCondition = filterHandler.identifyAndProvideTheSearchCondition(searchConditions[conditionIndex]);
            searchConditionArray.push(searchCondition);
        }

        /** Loads search history if any element present in the search condition array. */
        if (0 < searchConditionArray.length) {
            FilterConstructor.loadSearchHistory(searchConditionArray);
        }

    };

    /**
     * The method identifies whether the object is a field/group condition and return corresponding js object.
     *
     * @param condition
     *            JSON condition received from controller.
     * @returns JS field condition object if the JSON is of field condition, JS group condition if the JSON is of group
     *          condition, null otherwise.
     */
    filterHandler.identifyAndProvideTheSearchCondition = function(condition) {

        var advanceSearchCondition;
        var searchClass = condition["--class"];
        if (searchClass) {
            if (searchClass.indexOf("FieldCondition") > -1) {
                advanceSearchCondition = filterHandler.getFieldCondition(condition);
            } else if (searchClass.indexOf("GroupCondition") > -1) {
                advanceSearchCondition = filterHandler.getGroupCondition(condition);
            }
        }
        return advanceSearchCondition;
    };

    /**
     * Returns the field condition js object after loading the data from JSON.
     *
     * @param searchCondition
     *            JSON search condition object.
     *
     * @returns JS field condition object.
     */
    filterHandler.getFieldCondition = function(searchCondition) {

        var condition = new Condition(searchCondition.field, searchCondition.fieldDisplay, searchCondition.op,
                searchCondition.value, searchCondition.array);
        condition.not = searchCondition.not;
        return condition;
    };

    /**
     * Returns the group condition object loaded with values received from the passed on JSON search element.
     *
     * @returns JS group condition object loaded with value received from the JSON object.
     */
    filterHandler.getGroupCondition = function(searchCondition) {
        var conditions = [];
        var groupConditionConditions = searchCondition.conditions;
        for ( var conditionCount = 0; conditionCount < groupConditionConditions.length; conditionCount++) {
            var conditionArray = groupConditionConditions[conditionCount];
            conditions.push(filterHandler.identifyAndProvideTheSearchCondition(conditionArray));
        }
        var groupOp = searchCondition.groupOp;
        var groupCondition = new GroupCondition(conditions, groupOp);
        groupCondition.not = searchCondition.not;
        return groupCondition;
    };

    return filterHandler;
}());
