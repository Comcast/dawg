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
/*---------------------------------------------------------------------------
 * Script used to format the date in client time zone to a standard timestamp
 * format for event logging.
 *
 * DEPENDENCIES
 *  - JQuery UI Date Picker
 *---------------------------------------------------------------------------*/

/** Regex pattern to identify hour and minute information from the Date.toLocaleString()
 *  eg locale datetime : Monday, November 18, 2013 10:59:02 PM  */
var date_time_pattern = /[\w,\s\d]+?(\d+)(:\d+:\d+\s*\w+)/;

/** Constant to store value Zero. */
var prefix_zero = "0";

/**
 * Convert the current timestamp information in client timezone
 * to standard format for event logging.
 *
 * @param date date to be converted
 * @returns formatted date in 'yyyy-mm-dd hh:mm:ss' format.
 */
function formatDate(date) {
    var day = $.datepicker.formatDate('yy-mm-dd ', date);
    var formattedDate = date.toLocaleString();

    var time = date_time_pattern.exec(formattedDate);

    if(time.length > 2) {
        var hour = time[1];
        // If hour value is less than 10, append it with 0 to obtain a standard logging format.
        if(hour < 10) {
            hour = prefix_zero + hour;
        }
        formattedDate = day + hour + time[2];
    }
    return formattedDate;
}
