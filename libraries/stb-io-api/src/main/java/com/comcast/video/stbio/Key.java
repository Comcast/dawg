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
package com.comcast.video.stbio;

/**
 * Enumeration for all the keys
 * @author Kevin Pearson
 *
 */
public enum Key {

    ZERO,
    ONE,
    TWO,
    THREE,
    FOUR,
    FIVE,
    SIX,
    SEVEN,
    EIGHT,
    NINE,
    A,
    APPLIST,
    ASPECT,
    AUDIO,
    B,
    BLUE,
    BYPASS,
    C,
    CHDN,
    CHUP,
    D,
    DAYDN,
    DAYUP,
    DVR,
    DOT,
    DOWN,
    ENTER,
    EXIT,
    FAV,
    FAV_1,
    FAV_2,
    FAV_3,
    FAV_4,
    FF,
    FREEZE,
    FUN_A,
    GREEN,
    GUIDE,
    HDZOOM,
    HELP,
    INFO,
    LANG,
    LAST,
    LEFT,
    LIVE,
    LOCK,
    MENU,
    MUSIC,
    MUTE,
    MYDVR,
    NEXT,
    ONDEMAND,
    PAUSE,
    PGDN,
    PGUP,
    PHOTO,
    PICTURE,
    PIP,
    PIPONOFF,
    PIPMOVE,
    PIPSWAP,
    PIPCHUP,
    PIPCHDN,
    PLAY,
    POWER,
    PREV,
    REC,
    RED,
    REPLAY,
    REVERSE,
    REW,
    RIGHT,
    SELECT,
    SETTINGS,
    SETUP,
    SKIPFWD,
    SLEEP,
    SOURCE,
    STOP,
    SUBTITLE,
    SWAP("PIPSWAP"),
    TEXT,
    UP,
    VIDEO,
    VOLDN,
    VOLUP,
    WATCH_TV,
    YELLOW;

    private Key() {
        this(null);
    }

    private Key(String key) {
        this.key = key;
    }

    private String key;

    private static final Key[] DIGITS = new Key[] {ZERO, ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE};

    public static Key[] keysForChannel(String channel) {
        Key[] list = new Key[channel.length()];
        for (int i = 0; i < channel.length(); i++) {
            char c = channel.charAt(i);
            int digit = Character.digit(c, 10);
            if ((digit < 0) || (digit > 9)) {
                String err = "Unknown digit '" + c + "' in channel configuration.";
                throw new IllegalArgumentException(err);
            }
            list[i] = DIGITS[digit];

        }
        return list;
    }

    public String getKey() {
        return key == null ? name() : key;
    }
}
