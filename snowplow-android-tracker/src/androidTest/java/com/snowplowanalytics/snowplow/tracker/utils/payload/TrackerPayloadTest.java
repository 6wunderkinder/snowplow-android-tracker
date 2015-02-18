package com.snowplowanalytics.snowplow.tracker.utils.payload;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import android.test.AndroidTestCase;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.snowplowanalytics.snowplow.tracker.utils.payload.jackson.JacksonTrackerPayload;
import junit.framework.ComparisonFailure;

public class TrackerPayloadTest extends AndroidTestCase {


    private static final HashMap<String, Object> parameterMap = new HashMap<>();

    static {
        parameterMap.put("param1key", "param1value");
        parameterMap.put("param2key", true);
        parameterMap.put("param3key", 42);
    }
    // Helper Methods

    private JacksonTrackerPayload getJacksonPayload() {
        JacksonTrackerPayload payload = new JacksonTrackerPayload();
        payload.add("dummyKey", "dummydata");
        payload.add("parameterkey", parameterMap);
        return payload;
    }

    private TrackerPayload getTrackerPayload() {
        TrackerPayload payload = new TrackerPayload();
        payload.add("dummyKey", "dummydata");
        payload.add("parameterkey", parameterMap);
        return payload;
    }

    // Tests

    public void testToString() {
        JacksonTrackerPayload payload = getJacksonPayload();
        TrackerPayload trackerPayload = getTrackerPayload();
        assertJsonEquals(payload.toString(), trackerPayload.toString());
    }


    public void testAddString() {
        JacksonTrackerPayload payload = getJacksonPayload();
        TrackerPayload trackerPayload = getTrackerPayload();
        assertJsonEquals(payload.toString(), trackerPayload.toString());
        String value = "testAddedString";
        String key = "testAddedKey";
        payload.add(key, value);
        assertFalse(payload.toString().equals(trackerPayload.toString()));
        trackerPayload.add(key, value);
        assertJsonEquals(payload.toString(), trackerPayload.toString());
        assertTrue(payload.toString().contains(key));
        assertTrue(payload.toString().contains(value));
    }


    public void testAddObjectBooelan() {
        JacksonTrackerPayload payload = getJacksonPayload();
        TrackerPayload trackerPayload = getTrackerPayload();
        assertJsonEquals(payload.toString(), trackerPayload.toString());
        Object value = false;
        String key = "testAddedKeyBoolean";
        payload.add(key, value);
        assertFalse(payload.toString().equals(trackerPayload.toString()));
        trackerPayload.add(key, value);
        assertJsonEquals(payload.toString(), trackerPayload.toString());
        assertTrue(payload.toString().contains(key));
        assertTrue(payload.toString().contains(String.valueOf(value)));
    }

    public void testAddObjectInt() {
        JacksonTrackerPayload payload = getJacksonPayload();
        TrackerPayload trackerPayload = getTrackerPayload();
        assertJsonEquals(payload.toString(), trackerPayload.toString());
        Object value = 2277812;
        String key = "testAddedKeyInt";
        payload.add(key, value);
        assertFalse(payload.toString().equals(trackerPayload.toString()));
        trackerPayload.add(key, value);
        assertJsonEquals(payload.toString(), trackerPayload.toString());
        assertTrue(payload.toString().contains(key));
        assertTrue(payload.toString().contains(String.valueOf(value)));
    }


    public void testAddMapSimple() {
        JacksonTrackerPayload payload = getJacksonPayload();
        TrackerPayload trackerPayload = getTrackerPayload();
        assertJsonEquals(payload.toString(), trackerPayload.toString());
        Map<String, Object> value = new HashMap<>();
        value.put("k1string", "hello world");
        value.put("k2boolean", true);
        value.put("k3Int", 9283764);
        payload.addMap(value);
        assertFalse(payload.toString().equals(trackerPayload.toString()));
        trackerPayload.addMap(value);
        assertJsonEquals(payload.toString(), trackerPayload.toString());
        assertTrue(payload.toString().contains("k1string"));
        assertTrue(payload.toString().contains("k2boolean"));
        assertTrue(payload.toString().contains("k3Int"));
        assertTrue(payload.toString().contains("hello world"));
        assertTrue(payload.toString().contains(String.valueOf(true)));
        assertTrue(payload.toString().contains(String.valueOf(9283764)));
    }


    public void testAddMapNoBase64() {
        JacksonTrackerPayload payload = getJacksonPayload();
        TrackerPayload trackerPayload = getTrackerPayload();
        assertJsonEquals(payload.toString(), trackerPayload.toString());
        Map<String, Object> value = new HashMap<>();
        value.put("k1string", "hello world");
        value.put("k2boolean", true);
        value.put("k3Int", 9283764);
        payload.addMap(value, false, "keyb64", "keyNob64");
        assertFalse(payload.toString().equals(trackerPayload.toString()));
        trackerPayload.addMap(value, false, "keyb64", "keyNob64");
        assertJsonEquals(payload.toString(), trackerPayload.toString());
        assertTrue(payload.toString().contains("k1string"));
        assertTrue(payload.toString().contains("k2boolean"));
        assertTrue(payload.toString().contains("k3Int"));
        assertTrue(payload.toString().contains("hello world"));
        assertFalse(payload.toString().contains("keyb64"));
        assertTrue(payload.toString().contains("keyNob64"));
        assertTrue(payload.toString().contains(String.valueOf(true)));
        assertTrue(payload.toString().contains(String.valueOf(9283764)));
    }


    public void testAddMapBase64() {
        JacksonTrackerPayload payload = getJacksonPayload();
        TrackerPayload trackerPayload = getTrackerPayload();
        assertJsonEquals(payload.toString(), trackerPayload.toString());
        Map<String, Object> value = new HashMap<>();
        value.put("k1string", "hello world");
        value.put("k2boolean", true);
        value.put("k3Int", 9283764);
        payload.addMap(value, true, "keyb64", "keyNob64");
        assertFalse(payload.toString().equals(trackerPayload.toString()));
        trackerPayload.addMap(value, true, "keyb64", "keyNob64");
        assertJsonEquals(payload.toString(), trackerPayload.toString());
        assertFalse(payload.toString().contains("k1string"));
        assertFalse(payload.toString().contains("k2boolean"));
        assertFalse(payload.toString().contains("k3Int"));
        assertFalse(payload.toString().contains("hello world"));
        assertTrue(payload.toString().contains("keyb64"));
        assertFalse(payload.toString().contains("keyNob64"));
        assertFalse(payload.toString().contains(String.valueOf(9283764)));
    }

    public void testGetMap() {
        JacksonTrackerPayload payload = getJacksonPayload();
        TrackerPayload trackerPayload = getTrackerPayload();
        assertJsonEquals(payload.toString(), trackerPayload.toString());
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
