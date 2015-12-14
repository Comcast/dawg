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

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.springframework.data.mongodb.core.query.Criteria;

import com.comcast.cereal.annotations.Cereal;

/**
 * A condition between a single column and a value with an operation
 * @author Kevin Pearson
 *
 */
public class FieldCondition extends BaseCondition {
    private String field;
    private Operator op;
    private String value;
    @Cereal(defaultValue="false")
    private boolean array;
    private String fieldDisplay;

    /**
     * Creates a FieldCondition
     * @param field The field to compare
     * @param op The operation to perform on the field
     * @param value The value to compare the field to
     */
    public FieldCondition(String field, Operator op, String value) {
        this(field, op, value, false);
    }

    /**
     * Creates a FieldCondition
     * @param field The field to compare
     * @param op The operation to perform on the field
     * @param value The value to compare the field to
     */
    public FieldCondition(SearchableField field, Operator op, String value) {
        this(field.getField(), op, value, field.isArray());
    }

    /**
     *
     * Creates a FieldCondition
     * @param field The field to compare
     * @param op The operation to perform on the field
     * @param value The value to compare the field to
     * @param array true if this field is an array of objects
     */
    public FieldCondition(String field, Operator op, String value, boolean array) {
        setField(field);
        setOp(op);
        setValue(value);
        setArray(array);
    }

    /**
     * Creates a new {@link FieldCondition} object
     * @param field The field to compare
     * @param op The operation to perform on the field
     * @param value The value to compare the field to
     * @param isArray true if this field is an array of objects
     * @param fieldDisplay Display value of the field
     * @param not true if the search condition to be negated, false otherwise.
     */
    public FieldCondition(String field, Operator op, String value,
            boolean isArray, String fieldDisplay, boolean not) {
        setField(field);
        setOp(op);
        setValue(value);
        setArray(isArray);
        setFieldDisplay(fieldDisplay);
        setNot(not);
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public Operator getOp() {
        return op;
    }

    public void setOp(Operator op) {
        this.op = op;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isArray() {
        return array;
    }

    public void setArray(boolean array) {
        this.array = array;
    }
    /**
     * Getting field display value
     * @return Name of the field displayed in UI
     */
    public String getFieldDisplay() {
        return fieldDisplay;
    }

    /**
     * Setting field display value
     * @param fieldDisplay Display value of the field
     */
    public void setFieldDisplay(String fieldDisplay) {
        this.fieldDisplay = fieldDisplay;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Criteria toCriteria(boolean negate) {
        boolean notToUse = negate ? !not : not;
        Criteria crit = Criteria.where("data." + field);
        switch (this.op) {
            case equals:
                return notToUse ? crit.ne(value) : crit.is(value);
            case contains:
                crit = notToUse ? crit.not() : crit;
                if (array) {
                    return crit.in(value);
                } else {
                    return crit.regex(".*" + value + ".*");
                }
            case matches:
                crit = notToUse ? crit.not() : crit;
                return crit.regex(value);
            default:
                return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "(" + field + " " + op.name() + " " + value + ")";
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object object) {
        boolean result = false;
        if (object instanceof FieldCondition) {
            FieldCondition fieldCondition = (FieldCondition) object;
            result = new EqualsBuilder().append(this.not, fieldCondition.not)
                .append(this.field, fieldCondition.field)
                .append(this.op, fieldCondition.op)
                .append(this.value, fieldCondition.value)
                .append(this.array, fieldCondition.array)
                .append(this.fieldDisplay, fieldCondition.fieldDisplay)
                .isEquals();
        }
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(field).append(op).append(value)
            .append(array).append(fieldDisplay).toHashCode();
    }
}
