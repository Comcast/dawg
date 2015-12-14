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

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.springframework.data.mongodb.core.query.Criteria;

/**
 * A condition that groups together two or more conditions with a {@link GroupOperator}.
 * @author Kevin Pearson
 *
 */
public class GroupCondition extends BaseCondition {
    private GroupOperator groupOp;
    private List<Condition> conditions;

    /**
     * Creates a GroupCondition
     * @param groupOp The operator to group the conditions by
     * @param conditions All the conditions that are grouped together
     */
    public GroupCondition(GroupOperator groupOp, Condition...conditions) {
        setGroupOp(groupOp);
        setConditions(Arrays.asList(conditions));
    }

    public GroupOperator getGroupOp() {
        return groupOp;
    }

    public void setGroupOp(GroupOperator groupOp) {
        this.groupOp = groupOp;
    }

    public Collection<Condition> getConditions() {
        return conditions;
    }

    public void setConditions(List<Condition> conditions) {
        if (conditions.size() == 0) {
            throw new RuntimeException("Cannot have a group with no conditions");
        }
        this.conditions = conditions;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Criteria toCriteria(boolean negate) {
        /** If we are negating the whole criteria then we just negate the negation... */
        boolean notToUse = negate ? !not : not;
        Criteria crit = new Criteria();

        /** When you negate a grouping, you flip the operator and then negate all the elements */
        GroupOperator gop = groupOp;
        if (notToUse) {
            /** Flip operator */
            gop = groupOp.equals(GroupOperator.and) ? GroupOperator.or : GroupOperator.and;
        }

        Criteria[] all = new Criteria[conditions.size()];
        for (int i = 0; i < conditions.size(); i++) {
            all[i] = conditions.get(i).toCriteria(notToUse);
        }

        return gop.equals(GroupOperator.and) ? crit.andOperator(all) : crit.orOperator(all);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "(" + StringUtils.join(conditions, " " + groupOp.name() + " ") + ")";
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object object) {
        boolean result = false;
        if (object instanceof GroupCondition) {
            GroupCondition groupCondition = (GroupCondition) object;
            result = new EqualsBuilder().append(this.not, groupCondition.not)
                .append(this.groupOp, groupCondition.groupOp)
                .append(this.conditions, groupCondition.conditions)
                .isEquals();
        }
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(groupOp).append(conditions)
            .toHashCode();
    }
}
