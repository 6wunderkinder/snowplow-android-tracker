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

package com.snowplowanalytics.snowplow.tracker.utils.payload.jackson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.snowplowanalytics.snowplow.tracker.Payload;
import com.snowplowanalytics.snowplow.tracker.constants.Parameters;
import com.snowplowanalytics.snowplow.tracker.utils.Logger;
import com.snowplowanalytics.snowplow.tracker.utils.Preconditions;


public class JacksonSelfDescribingJson implements Payload {

    private final String TAG = JacksonSelfDescribingJson.class.getSimpleName();
    private final ObjectMapper objectMapper = Util.getObjectMapper();
    private ObjectNode objectNode = objectMapper.createObjectNode();

    public JacksonSelfDescribingJson(String schema) {
        this(schema, new HashMap<>());
    }

    public JacksonSelfDescribingJson(String schema, JacksonTrackerPayload data) {
        setSchema(schema);
        setData(data);
    }

    public JacksonSelfDescribingJson(String schema, JacksonSelfDescribingJson data) {
        setSchema(schema);
        setData(data);
    }

    public JacksonSelfDescribingJson(String schema, Object data) {
        setSchema(schema);
        setData(data);
    }

    /**
     * Sets the Schema for the SelfDescribingJson
     *
     * @param schema a valid schema string
     */
    public JacksonSelfDescribingJson setSchema(String schema) {
        Preconditions.checkNotNull(schema, "schema cannot be null");
        Preconditions.checkArgument(!schema.isEmpty(), "schema cannot be empty.");
        objectNode.put(Parameters.SCHEMA, schema);
        return this;
    }

    /**
     * Adds data to the SelfDescribingJson
     * - Accepts a TrackerPayload object
     *
     * @param data the data to be added to the SelfDescribingJson
     */
    public JacksonSelfDescribingJson setData(JacksonTrackerPayload data) {
        if (data == null) {
            return this;
        }
        try {
            objectNode.putPOJO(Parameters.DATA, objectMapper.writeValueAsString(data.getMap()));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * Adds data to the SelfDescribingJson
     *
     * @param data the data to be added to the SelfDescribingJson
     */
    public JacksonSelfDescribingJson setData(Object data) {
        if (data == null) {
            return this;
        }
        try {
            objectNode.putPOJO(Parameters.DATA, objectMapper.writeValueAsString(data));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * Allows us to add data from one SelfDescribingJson into another
     * without copying over the Schema.
     *
     * @param payload the payload to add to the SelfDescribingJson
     */
    private void setData(JacksonSelfDescribingJson payload) {
        if (payload == null) {
            return;
        }
        ObjectNode data = objectMapper.valueToTree(payload.getMap());
        objectNode.set(Parameters.DATA, data);
    }

    @Deprecated
    @Override
    public void add(String key, String value) {
        /*
         * We intentionally do nothing because we do not want our SchemaPayload
         * to do anything except accept a 'data' and 'schema'
         */
        Logger.ifDebug(TAG, "add(String, String) method called: Doing nothing.");
    }

    @Deprecated
    @Override
    public void add(String key, Object value) {
        /*
         * We intentionally do nothing because we do not want our SchemaPayload
         * to do anything except accept a 'data' and 'schema'
         */
        Logger.ifDebug(TAG, "add(String, Object) method called: Doing nothing.");
    }

    @Deprecated
    @Override
    public void addMap(Map<String, Object> map) {
        /*
         * We intentionally do nothing because we do not want our SchemaPayload
         * to do anything except accept a 'data' and 'schema'
         */
        Logger.ifDebug(TAG, "addMap(Map<String, Object>) method called: Doing nothing.");
    }

    @Deprecated
    @Override
    public void addMap(Map map, Boolean base64_encoded, String type_encoded,
                       String type_no_encoded) {
        /*
         * We intentionally do nothing because we do not want our SchemaPayload
         * to do anything except accept a 'data' and 'schema'
         */
        Logger.ifDebug(TAG, "addMap(Map, Boolean, String, String) method called: Doing nothing.");
    }

    public Map<String, Object> getMap() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        try {
            map = objectMapper.readValue(objectNode.toString(),
                    new TypeReference<HashMap>() {
                    });
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    public JsonNode getNode() {
        return objectNode;
    }

    public String toString() {
        return objectNode.toString();
    }
}
