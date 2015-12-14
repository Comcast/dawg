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
 * Provides logic for managing the TagCloud
 */
var TagCloud = (function() {

    var tc = {};

    /**
     * Binds this object to the html elements
     * @param cloudUI the div that the cloud tags go in
     * @param deviceTags A mapping of deviceId to the list of tags on that device
     * @param otherTags The tags that should be initialized to 0 because they belong to devices that
     * are not in the DeviceTable
     */
    tc.bind = function(cloudUI, deviceTags, otherTags) {
        tc.cloudUI = cloudUI;
        tc.otherTags = otherTags;
        TagCloud.populate(deviceTags, new Array());
    }

    /**
     * Populates the tag cloud with the tags
     * @param deviceTags A mapping of deviceId to the list of tags on that device
     * @param selectedTags a list of tags that are currently selected by the user
     */
    tc.populate = function(deviceTags, selectedTags) {
        tc.sourceToTags = deviceTags;

        tc.cloudUI.html('');

        tc.tagCounts = new Array();

        /** Initialize all tags to 0 */
        for (var t = 0; t < tc.otherTags.length; t++) {
            tc.tagCounts[tc.otherTags[t]] = 0;
        }
        /** First aggregate all the tags and get the count for each one */
        for (var source in deviceTags){
            var sourceTags = deviceTags[source];
            if (sourceTags != null) {
                for (var t = 0; t < sourceTags.length; t++) {
                    var tag = sourceTags[t];
                    var count = tc.tagCounts[tag];
                    if (count == null) {
                        count = 0;
                    }
                    count++;
                    tc.tagCounts[tag] = count;
                }
            }
        }

        /** Sort the tags based on the number of devices that have the tag */
        var sortedTags = new Array();
        for (var tag in tc.tagCounts) {
            sortedTags.push({name:tag, count:tc.tagCounts[tag]});
        }
        sortedTags.sort(function(a, b) {
            return b.count - a.count;
        });

        /**
         * Add the a UI element (div) for each of the tags to the cloudUI
         */
        for (var t = 0; t < sortedTags.length; t++) {
            var tag = sortedTags[t].name;
            var count = sortedTags[t].count;
            var textDiv = $('<div/>').append(tag + ' (' + count + ')').addClass('tagText');
            var tagDiv = $('<div/>').append(textDiv).addClass('tag').attr('tagval', tag);
            tagDiv.addClass(count == 0 ? 'tagDisabled' : 'tagActive').addClass('tagNoDevices');
            var delWidget = new DeleteTagWidget(tag);
            tagDiv.append(delWidget.element);
            var tagClass = selectedTags.indexOf(tag) == -1 ? 'tagContainerUnselected' : 'tagContainerSelected';
            var tagContainer = $('<div/>').append(tagDiv).addClass('tagContainer').addClass(tagClass);

            /**
             * Click handler for a tag. This will set the tag to selected and then call into
             * DeviceTable to refresh it to only show devices that have that tag
             */
            if (count != 0) {
                tagDiv.click(function() {
                    if ($(':animated').length == 0) {
                        $(this).parent().toggleClass('tagContainerSelected').toggleClass('tagContainerUnselected');
                        DeviceTable.showDevicesOfSelectedTags();
                    }
                });
            }
            tc.cloudUI.append(tagContainer);
        }

        /** Only show the cloud if there are tags in it */
        if (sortedTags.length == 0) {
            tc.cloudUI.hide();
        } else {
            tc.cloudUI.show();
        }
    }

    /**
     * Changes the background color of a tag based on the devices that are selected
     * @param sources the sources that are selected
     */
    tc.selectSources = function(sources) {
        /** Separates the tags into 3 categories: all (tag is on every selected device),
         * some (tag is on some of the selected devices), and none (tag is on none of the selected devices) */
        var all = new Array();
        var some = new Array();
        for (var s = 0; s < sources.length; s++) {
            var tags = tc.sourceToTags[sources[s]];
            if (tags != null) {
                /** Add every tag on source to the some category */
                for (var t = 0; t < tags.length; t++) {
                    if ($.inArray(tags[t], some) == -1) {
                        some.push(tags[t]);
                    }
                }

                if (s == 0) {
                    // set all to the first set of tags in the sources
                    all = tags.slice(0);
                } else {
                    for (var a = 0; a < all.length; ) {
                        if ($.inArray(all[a], tags) == -1) {
                            all.splice(a, 1); // remove tag because it is not in all sources
                        } else {
                            a++;
                        }
                    }
                }
            }
        }
        /**
         * Now that we have categorized every tag, set the background color appropriately.
         * For all the tags that are on some of the selected devices, give a delete icon.
         */
        for (var tag in tc.tagCounts) {
            var opacity = 0;
            var allStates = ['tagSomeDevices', 'tagNoDevices', 'tagAllDevices'];
            var currentState = 'tagNoDevices';
            var tagDiv = $(".tag[tagval='" + tag + "']");
            for (var s = 0; s < allStates.length; s++) {
                if (tagDiv.hasClass(allStates[s])) {
                    currentState = allStates[s];
                    break;
                }
            }
            var nextState = 'tagNoDevices';
            if ($.inArray(tag, all) != -1) {
                nextState = 'tagAllDevices';
                opacity = 1;
            } else if ($.inArray(tag, some) != -1) {
                nextState = 'tagSomeDevices';
                opacity = 1;
            }

            tagDiv.toggleClass(currentState + ' ' + nextState, {duration:400});
            $('.tagDelete', tagDiv).animate({opacity:opacity}, 400);
        }
    }

    /**
     * Adds or removes a set of tags on the given sources and then
     * updates the TagCloud
     * @param tags the list of tags to add or remove
     * @param sources the list of sources to add or remove the tags from
     * @param add true if adding tags, false if removing tags
     */
    tc.addOrRemoveTags = function(tags, sources, add) {
        for (var s = 0; s < sources.length; s++) {
            var curTags = tc.sourceToTags[sources[s]];
            curTags = curTags == null ? [] : curTags;
            if (curTags != null) {
                for (var t = 0; t < tags.length; t++) {
                    var ti = curTags.indexOf(tags[t]);
                    if (add && (ti == -1)) {
                        /** If we are adding a tag and the tag doesn't already exist */
                        curTags.push(tags[t]);
                    } else if (!add && (ti != -1)) {
                        /** If we are removing the tag and the tag exists */
                        curTags.splice(ti, 1);
                    }
                }
            }
            tc.sourceToTags[sources[s]] = curTags;
        }
        /**
         * Repopulate the tag cloud and then refresh the state of the tag based on whether
         * the device is selected or not. Also, maintain what tags were selected by the user before
         * repopulating
         */
        var selectedTags = TagCloud.getSelectedTags();
        TagCloud.populate(tc.sourceToTags, selectedTags);
        TagCloud.selectSources(DeviceTable.getSelectedDevices());
        DeviceTable.showDevicesOfSelectedTags();
    }

    /**
     * Gets the tags that the user has selected
     */
    tc.getSelectedTags = function() {
        var selectedTags = new Array();
        $('.tagContainerSelected', tc.cloudUI).each(function() {
            selectedTags.push($('.tag', $(this)).attr('tagval'));
        });
        return selectedTags;
    }

    /**
     * Gets the mapping of devices to the tags on that device
     */
    tc.getSourceToTags = function() {
        return tc.sourceToTags;
    }

    return tc;

}());

/**
 * Manages functionality around the widget that deletes a tag.
 * @returns {DeleteTagWidget}
 */
function DeleteTagWidget(tag) {
    this.element = $('<div/>').addClass('tagDelete').attr('tagval', tag).css('opacity', 0);

    /**
     * Handler for when the widget is clicked
     */
    this.element.click(function(event) {
        if ($(this).css('opacity') == 1) {
            var tagToDel = $(this).attr('tagval');
            if (confirm("Are you sure you want to remove the tag '" + tagToDel + "' from all selected devices?")) {
                var sources = DeviceTable.getSelectedDevices();
                var target = CXT_PATH + '/devices/update/tags/remove';
                var payload = {"id":sources,"tag":[tagToDel]};
                $.ajax({
                      url:target,
                      type:"POST",
                      data:JSON.stringify(payload),
                      contentType:"application/json; charset=utf-8",
                      success: function() {
                          /**
                           * Once the tag has been removed from the device in the database
                           * then remove it from the cloud and the device metadata
                           */
                          TagCloud.addOrRemoveTags([tagToDel], sources, false);
                          DeviceTable.addOrRemoveTags([tagToDel], sources, false);
                      }
                });
            }
            /** Stop propagation so that the tag isn't clicked after it is deleted */
            event.stopPropagation();
        }
    });
}
