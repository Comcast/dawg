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
 * A comparison between a field and a value
 * @param field The field as it is seen in the database
 * @param fieldDisplay The field as it is displayed to the user
 * @param op The operation of the comparison
 * @param value The value to compare the field with
 * @param array true if this field is an array, false if it is a single value
 * @returns {Condition}
 */
function Condition(field, fieldDisplay, op, value, array) {
    this.field = field;
    this.op = op;
    this.value = value;
    this.array = array;
    this.fieldDisplay = fieldDisplay;
    this.not = false;
    /** Tells the server how to decerealize this object */
    this['--class'] = 'com.comcast.video.dawg.controller.house.filter.FieldCondition';

    /**
     * Gets the text that is displayed to the user for this condition
     */
    this.getText = function() {
        return (this.not ? "!" : "") + "(" + this.fieldDisplay + " " + this.op + " " + this.value + ")";
    };
}

/**
 * A logical grouping of conditions
 * @param conditions The conditions to group
 * @param groupOp The operator to group the conditions on (and or or)
 * @returns {GroupCondition}
 */
function GroupCondition(conditions, groupOp) {
    this.conditions = conditions;
    this.groupOp = groupOp;
    this.not = false;
    /** Tells the server how to decerealize this object */
    this['--class'] = 'com.comcast.video.dawg.controller.house.filter.GroupCondition';

    /**
     * Gets the text that is displayed to the user for this condition
     */
    this.getText = function() {
        var text = (this.not ? "!" : "") + "(";
        for (c in conditions) {
            var condition = conditions[c];
            if (c > 0) {
                text += " " + groupOp + " ";
            }
            text += condition.getText();
        }
        text+= ")";
        return text;
    };
}
