/*
 * Copyright (c) 2015 Snowplow Analytics Ltd. All rights reserved.
 *
 * This program is licensed to you under the Apache License Version 2.0,
 * and you may not use this file except in compliance with the Apache License Version 2.0.
 * You may obtain a copy of the Apache License Version 2.0 at http://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the Apache License Version 2.0 is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Apache License Version 2.0 for the specific language governing permissions and limitations there under.
 */

package com.snowplowanalytics.snowplow.tracker.utils.payload.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.snowplowanalytics.snowplow.tracker.Payload;
import com.snowplowanalytics.snowplow.tracker.utils.Logger;
import com.snowplowanalytics.snowplow.tracker.utils.Util;

public class TrackerPayload implements Payload {

    private final String TAG = TrackerPayload.class.getSimpleName();
    private HashMap<String, Object> map = new HashMap<>();

    @Override
    public void add(String key, String value) {
        if (value == null || value.isEmpty()) {
            Logger.ifDebug(TAG, "kv-value is empty. Returning out without adding key..");
            return;
        }

        Logger.ifDebug(TAG, "Adding new key-value pair: " + key + "->" + value);
        map.put(key, value);
    }

    @Override
    public void add(String key, Object value) {
        if (value == null) {
            Logger.ifDebug(TAG, "kv-value is empty. Returning out without adding key..");
            return;
        }

        Logger.ifDebug(TAG, "Adding new key-value pair: " + key + "->" + value);
        map.put(key, value);
    }

    @Override
    public void addMap(Map<String, Object> map) {
        // Return if we don't have a map
        if (map == null) {
            Logger.ifDebug(TAG, "Map passed in is null. Returning without adding map..");
            return;
        }

        Set<String> keys = map.keySet();
        for (String key : keys) {
            add(key, map.get(key));
        }
    }

    @Override
    public void addMap(Map map, Boolean base64_encoded, String type_encoded, String type_no_encoded) {
        // Return if we don't have a map
        if (map == null) {
            Logger.ifDebug(TAG, "Map passed in is null. Returning nothing..");
            return;
        }

        String mapString = Util.serializeMap(map);
        if (base64_encoded) { // base64 encoded data
            map.put(type_encoded, Util.base64Encode(mapString));
        } else { // add it as a child node
            add(type_no_encoded, mapString);
        }
    }

    @Override
    public Map getMap() {
        HashMap<String, String> map = new HashMap<String, String>();
        return map;
    }

    @Override
    public String toString() {
        return Util.serializeMap(map);
    }
}
