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
var FilterConstructor = (function() {

    var fc = {
        conditions : new Array(),
        conditionElements : new Object(),
        historyConditions : [],
        HEIGHT_SEARCH_CONDITION_TEXT : 15,
        WRAP_LENGTH_SEARCH_CONDITION_TEXT : 114
    };

    /**
     * Binds this object to the html elements
     * @param fcUI the overlay UI
     * @param launchButton The button that launches the overlay
     */
    fc.bind = function(fcUI, launchButton, token) {
        fc.fcUI = fcUI;

        /**
         * Sets the overlay as a jqueryUI diaglog box
         */
        fc.fcUI.dialog({
            autoOpen : false,
            minWidth : 800,
            width : 800,
            minHeight : 250,
            height : 250
        });

        /**
         * Click handler for launching the overlay. This will open the dialog
         */
        launchButton.click(function() {
            fc.fcUI.show();
            fc.fcUI.dialog("open");
        });

        /**
         * Called when enter is pressed when the add condition field is focused. This will
         * just add the field
         */
        $('.fieldValueInput', fcUI).keydown(function(event) {
            if (event.which == 13) {
                $('.btnAddCondition', $(this).parent()).click();
            }
        });

        /**
         * Click handler for adding a new condition
         */
        $('.btnAddCondition', fcUI).click(function() {
            var field = $('.fieldSelect', fc.fcUI).val();
            var op = $('.opSelect', fc.fcUI).val();

            var fieldDisplay = $('.fieldSelect option:selected', fc.fcUI).text();
            var array = $('.fieldSelect option:selected', fc.fcUI).attr('data-array');
            var value = $('.fieldValueInput', fc.fcUI).val();

            var condition = new Condition(field, fieldDisplay, op, value, array);
            FilterConstructor.addCondition(condition);
            $('.fieldValueInput', fc.fcUI).val('');
        });

        /** Click handler for the AND button */
        $('.btnAnd', fcUI).click(function() {
            FilterConstructor.combineConditions('and');
        });

        /** Click handler for the OR button */
        $('.btnOr', fcUI).click(function() {
            FilterConstructor.combineConditions('or');
        });

        /** Click handler for the NOT button */
        $('.btnNot', fcUI).click(function() {
            FilterConstructor.notConditions();
        });

        /** Click handler for the DEL button */
        $('.btnDel', fcUI).click(function() {
            FilterConstructor.deleteConditions();
        });

        /** Click handler for the BREAK button */
        $('.btnBreak', fcUI).click(function() {
            FilterConstructor.breakConditions();
        });

        /** Click handler for the Search button
         * This will send the conditions to the server and then reload the main page*/
        $('.btnSearch', fcUI).click(function() {

            var sep = FilterConstructor.separateChecked();
            var condition = sep.checked.length != 0 ? sep.checked[0] : sep.checkedGroups[0];
            $.ajax({
                  url: CXT_PATH + '/filter/store',
                  type:"POST",
                  data:JSON.stringify(condition),
                  contentType:"application/json; charset=utf-8",
                  success: function() {
                      window.location.href = CXT_PATH + '/' + token + '/';
                  }
            });
        });
    }

    /**
     * Adds a condition to the list of conditions then refreshes the list
     */
    fc.addCondition = function(condition) {
        if (fc.conditionElements[condition.getText()] == null) {
            fc.conditions.push(condition);
            FilterConstructor.refreshConditionsTable();
        }
    }

    /**
     * Refreshes the list of conditions.
     */
    fc.refreshConditionsTable = function() {
        /** Restore checks */
        var sep = FilterConstructor.separateChecked();
        var checked = sep.checked.concat(sep.checkedGroups);

        fc.conditionElements = new Array();
        $('.conditionList', fc.fcUI).html('');
        for (var c = 0; c < fc.conditions.length; c++) {
            var cond = fc.conditions[c];
            var cDiv = $('<div>').append($('<span>').addClass('conditionText').append(cond.getText()));

            var cbCondition = $('<input>').attr('type', 'checkbox').addClass('cbCondition');
            if ($.inArray(cond, checked) != -1) {
                cbCondition.prop('checked', true);
            }
            cbCondition.click(function() {
                FilterConstructor.checkChecks();
            });
            fc.conditionElements[fc.conditions[c].getText()] = cDiv;
            $('.conditionList', fc.fcUI).append(cDiv.append(cbCondition));
        }
        FilterConstructor.checkChecks();
    }

    /**
     * Helper method to separate conditions into 4 categories: singleChecked, groupChecked, singleUnchecked, groupUnchecked.
     * This is meant to be reusable code on all the condition control actions (AND, OR, DEL, BREAK)
     */
    fc.separateChecked = function() {
        var rv = {
                checked : new Array(),
                checkedGroups : new Array(),
                unchecked : new Array(),
                uncheckedGroups : new Array(),
        }
        for (var c = 0; c < fc.conditions.length; c++) {
            var cond = fc.conditions[c];
            var ele = fc.conditionElements[cond.getText()];
            var checked;
            if (ele == null) {
                // this is a newly added condition, so lets say it is checked
                checked = true;
            } else {
                var cb = $('.cbCondition', ele);
                checked = cb.prop('checked');
            }
            if (checked) {
                if (cond instanceof GroupCondition) {
                    rv.checkedGroups.push(cond);
                } else {
                    rv.checked.push(cond);
                }
            } else {
                if (cond instanceof GroupCondition) {
                    rv.uncheckedGroups.push(cond);
                } else {
                    rv.unchecked.push(cond);
                }
            }
        }
        return rv;
    }

    /**
     * Looks at all the conditions that are currently checked and then determines if
     * a control button should be enabled.
     * AND/OR 2 or more must be checked
     * DEL 1 or more must be checked
     * NOT 1 or more must be checked
     * BREAK one or more groups must be checked
     * Search exactly one condition must be checked
     */
    fc.checkChecks = function() {
        var sep = FilterConstructor.separateChecked();
        var totalChecked = sep.checked.length + sep.checkedGroups.length;
        disable($('.btnAnd', fc.fcUI), totalChecked  < 2);
        disable($('.btnOr', fc.fcUI), totalChecked < 2);
        disable($('.btnDel', fc.fcUI), totalChecked < 1);
        disable($('.btnNot', fc.fcUI), totalChecked < 1);
        disable($('.btnBreak', fc.fcUI), sep.checkedGroups.length < 1);
        disable($('.btnSearch', fc.fcUI), totalChecked != 1);
    }

    /**
     * Combines all checked conditions into a group
     * @param op AND or OR
     */
    fc.combineConditions= function(op) {
        var sep = FilterConstructor.separateChecked();
        var toCombine = sep.checked.concat(sep.checkedGroups);
        var newConditions = sep.unchecked.concat(sep.uncheckedGroups);

        var group = new GroupCondition(toCombine, op);
        newConditions.push(group);

        FilterConstructor.setConditions(newConditions);
    }

    /**
     * Negates all checked conditions
     */
    fc.notConditions= function() {
        var sep = FilterConstructor.separateChecked();
        var toNot = sep.checked.concat(sep.checkedGroups);

        for (var c in toNot) {
            var cond = toNot[c];
            cond.not = !cond.not;
        }

        FilterConstructor.refreshConditionsTable();
    }

    /**
     * Deletes all checked conditions
     */
    fc.deleteConditions = function() {
        var sep = FilterConstructor.separateChecked();
        var newConditions = sep.unchecked.concat(sep.uncheckedGroups);
        var deleted = sep.checked.concat(sep.checkedGroups);
        for (var c in deleted) {
            var cond = deleted[c];
            delete fc.conditionElements[cond.getText()];
        }
        FilterConstructor.setConditions(newConditions);
    }

    /**
     * Breaks all group conditions into their original components
     */
    fc.breakConditions = function() {
        var sep = FilterConstructor.separateChecked();
        var newConditions = sep.unchecked.concat(sep.uncheckedGroups);
        for (var ctb in sep.checkedGroups) {
            var group = sep.checkedGroups[ctb];
            for (var nc in group.conditions) {
                newConditions.push(group.conditions[nc]);
            }
        }

        FilterConstructor.setConditions(newConditions);
    }

    /**
     * Sets a new list of conditions and then refreshes the table
     */
    fc.setConditions = function(newConditions) {
        delete fc.conditions;
        fc.conditions = newConditions;
        FilterConstructor.refreshConditionsTable();
    }

    /**
     * Loads the history of searches user carried out previously.
     *
     * @param historySearchConditions
     *            Search conditions previously carried out by user.
     */
    fc.loadSearchHistory = function(historySearchConditions) {
        fc.historyConditions = historySearchConditions;
        $('.searchHistory', fc.fcUI).html('');
        var orderedHistoryList = $('<ol id="searchHistorySelectable">');
        var searchCondition;
        for ( var historyCount = 0; historyCount < historySearchConditions.length; historyCount++) {
            searchCondition = historySearchConditions[historyCount];
            var searchConditionText = searchCondition.getText();
            var historyConditionSpan = fc.getHistoryConditionSpan(searchConditionText, historyCount);
            var historyDelButton = fc.getHistoryDelButton(historyCount);
            var searchItem = $('<li class="searchHistoryItem ui-widget-content">').append(historyConditionSpan).append(
                    historyDelButton);
            // If the search condition text go beyond to the limit of display length it need to be wrapped by increasing
            // the height of li.
            var quotient = Math.ceil(searchConditionText.length / fc.WRAP_LENGTH_SEARCH_CONDITION_TEXT);
            var height = quotient * fc.HEIGHT_SEARCH_CONDITION_TEXT;
            searchItem.css({
                "height" : height + "px"
            });
            orderedHistoryList.append(searchItem);
        }
        $('.searchHistory', fc.fcUI).append(orderedHistoryList);
    };

    /**
     * Creates and provides a new history condition span with clickable  event.
     * @param searchConditionText search condition text.
     * @param historyCount index position of search condition in history list.
     */
    fc.getHistoryConditionSpan = function(searchConditionText, historyCount) {
        var historyConditionSpan = $('<span class="searchHistoryConditionText">').append(searchConditionText);
        historyConditionSpan.attr('historyCount', historyCount);
        historyConditionSpan.click(function() {
            $(this).parent().addClass("searchSelected").siblings().removeClass("searchSelected");
            /** Clearing the previous selected conditions. */
            fc.conditions = [];
            fc.conditionElements = {};
            /** Deep cloning to avoid any modification happen to the search history array elements. */
            var selectedSearch = $.extend(true, new Condition(), fc.historyConditions[$(this).attr('historyCount')]);
            fc.addCondition(selectedSearch);
        });
        return historyConditionSpan;
    };

    /**
     * Creates and provide a new delete button for history element.
     *
     * @param historyCount
     *            index position of search condition in history list for which a delete button need to be created.
     */
    fc.getHistoryDelButton = function(historyCount) {
        var historyDelButton = $('<input type="button" value="-" class="searchHistoryDelButton">');
        historyDelButton.attr('historyCount', historyCount);
        historyDelButton.click(function() {
            var historyCount = $(this).attr('historyCount');
            /** Delete the condition from history. */
            var deleteSearchCondition = fc.historyConditions[historyCount];
            $.ajax({
                url : CXT_PATH + '/filter/remove',
                type : "POST",
                data : JSON.stringify(deleteSearchCondition),
                contentType : "application/json; charset=utf-8"
            });
            fc.historyConditions.splice(historyCount, 1);
            fc.loadSearchHistory(fc.historyConditions);
        });
        return historyDelButton;
    };

    return fc;
}());

function disable(ele, disable) {
    if (disable) {
        ele.attr('disabled','disabled');
    } else {
        ele.removeAttr('disabled');
    }
}
