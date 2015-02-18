package com.snowplowanalytics.snowplow.tracker.utils.payload;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import android.test.AndroidTestCase;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.snowplowanalytics.snowplow.tracker.utils.payload.jackson.JacksonSelfDescribingJson;
import junit.framework.ComparisonFailure;

public class SelfDescribingJsonTest extends AndroidTestCase {


    private static final HashMap<String, Object> parameterMap = new HashMap<>();
    private static final String SCHEMA = "org.test.json.SCHEME";

    static {
        parameterMap.put("param1key", "param1value");
        parameterMap.put("param2key", true);
        parameterMap.put("param3key", 42);
    }
    // Helper Methods

    private TrackerPayload getTrackerPayload() {
        TrackerPayload payload = new TrackerPayload();
        payload.add("dummyKey", "dummydata");
        payload.add("parameterkey", parameterMap);
        return payload;
    }

    private SelfDescribingJson getSelfDescribingJson(TrackerPayload payload) {
        return new SelfDescribingJson(SCHEMA, payload);
    }

    private JacksonSelfDescribingJson getJacksonSelfDescribingJson(TrackerPayload payload) {
        return new JacksonSelfDescribingJson(SCHEMA, payload);
    }

    // Tests

    public void testToString() {
        TrackerPayload payload = getTrackerPayload();
        SelfDescribingJson selfDJson = getSelfDescribingJson(payload);
        JacksonSelfDescribingJson referenceSelfDescrJson = getJacksonSelfDescribingJson(payload);

    }


    private void assertJsonEquals(String json1, String json2) throws ComparisonFailure {
        try {
            JSONObject ob1 = new JSONObject(json1);
            JSONObject ob2 = new JSONObject(json2);
            assertEquals(ob1.toString(), ob2.toString());
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node1 = mapper.readTree(json1);
            JsonNode node2 = mapper.readTree(json2);
            assertEquals(node1, node2);
        } catch (JSONException | IOException e) {
            e.printStackTrace();
            throw new ComparisonFailure(e.getMessage(), "JsonObject", "could not create");
        }
    }

}
