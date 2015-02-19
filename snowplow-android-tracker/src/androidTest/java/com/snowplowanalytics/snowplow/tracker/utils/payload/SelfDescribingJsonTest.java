package com.snowplowanalytics.snowplow.tracker.utils.payload;

import java.io.IOException;
import java.util.HashMap;

import android.test.AndroidTestCase;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.snowplowanalytics.snowplow.tracker.utils.payload.jackson.JacksonSelfDescribingJson;
import com.snowplowanalytics.snowplow.tracker.utils.payload.jackson.JacksonTrackerPayload;
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

    private SelfDescribingJson getSelfDescribingJson() {
        return new SelfDescribingJson(SCHEMA, getTrackerPayload());
    }

    private JacksonSelfDescribingJson getJacksonSelfDescribingJson() {
        return new JacksonSelfDescribingJson(SCHEMA, getJacksonPayload());
    }

    // Tests

    public void testConstructorWithTrackingPayload() {
        SelfDescribingJson selfDJson = getSelfDescribingJson();
        JacksonSelfDescribingJson referenceSelfDescrJson = getJacksonSelfDescribingJson();
        assertJsonEquals(referenceSelfDescrJson.toString(), selfDJson.toString());
    }

    public void testConstructorWithMap() {
        SelfDescribingJson selfDJson = new SelfDescribingJson(SCHEMA, parameterMap);
        JacksonSelfDescribingJson referenceSelfDescrJson = new JacksonSelfDescribingJson(SCHEMA, parameterMap);
        assertJsonEquals(referenceSelfDescrJson.toString(), selfDJson.toString());
    }

    public void testConstructorWithSelfDescribing() {
        SelfDescribingJson selfDJson = new SelfDescribingJson(SCHEMA, getSelfDescribingJson());
        JacksonSelfDescribingJson referenceSelfDescrJson = new JacksonSelfDescribingJson(SCHEMA,
                getJacksonSelfDescribingJson());
        System.out.println("xkcd ref: " + referenceSelfDescrJson.toString());
        System.out.println("xkcd cur: " + selfDJson.toString());
        assertJsonEquals(referenceSelfDescrJson.toString(), selfDJson.toString());
    }

    public void testConstructorWithObject() {
        Object parameter = true;
        SelfDescribingJson selfDJson = new SelfDescribingJson(SCHEMA, parameter);
        JacksonSelfDescribingJson referenceSelfDescrJson = new JacksonSelfDescribingJson(SCHEMA, parameter);
        assertJsonEquals(referenceSelfDescrJson.toString(), selfDJson.toString());
        assertTrue(selfDJson.toString().contains(String.valueOf(parameter)));
        parameter = 22;
        selfDJson = new SelfDescribingJson(SCHEMA, parameter);
        referenceSelfDescrJson = new JacksonSelfDescribingJson(SCHEMA, parameter);
        assertJsonEquals(referenceSelfDescrJson.toString(), selfDJson.toString());
        assertTrue(selfDJson.toString().contains(String.valueOf(parameter)));
        parameter = "the big bad wolf";
        selfDJson = new SelfDescribingJson(SCHEMA, parameter);
        referenceSelfDescrJson = new JacksonSelfDescribingJson(SCHEMA, parameter);
        assertJsonEquals(referenceSelfDescrJson.toString(), selfDJson.toString());
        assertTrue(selfDJson.toString().contains(String.valueOf(parameter)));
        parameter = new String[] { "A", "B", "42" };
        selfDJson = new SelfDescribingJson(SCHEMA, parameter);
        referenceSelfDescrJson = new JacksonSelfDescribingJson(SCHEMA, parameter);
        assertJsonEquals(referenceSelfDescrJson.toString(), selfDJson.toString());
        assertTrue(selfDJson.toString().contains("42"));
    }

    public void testConstructorWithObjectToString() {
        Object parameter = true;
        SelfDescribingJson selfDJson = new SelfDescribingJson(SCHEMA, parameter);
        JacksonSelfDescribingJson referenceSelfDescrJson = new JacksonSelfDescribingJson(SCHEMA, parameter);
        assertJsonEquals(referenceSelfDescrJson.toString(), selfDJson.toString());
        assertTrue(selfDJson.toString().contains(String.valueOf(parameter)));
        parameter = 22;
        selfDJson = new SelfDescribingJson(SCHEMA, parameter);
        referenceSelfDescrJson = new JacksonSelfDescribingJson(SCHEMA, parameter);
        assertJsonEquals(referenceSelfDescrJson.toString(), selfDJson.toString());
        assertTrue(selfDJson.toString().contains(String.valueOf(parameter)));
        parameter = "the big bad wolf";
        selfDJson = new SelfDescribingJson(SCHEMA, parameter);
        referenceSelfDescrJson = new JacksonSelfDescribingJson(SCHEMA, parameter);
        assertJsonEquals(referenceSelfDescrJson.toString(), selfDJson.toString());
        assertTrue(selfDJson.toString().contains(String.valueOf(parameter)));
        parameter = new String[] { "A", "B", "42" };
        selfDJson = new SelfDescribingJson(SCHEMA, parameter);
        referenceSelfDescrJson = new JacksonSelfDescribingJson(SCHEMA, parameter);
        assertJsonEquals(referenceSelfDescrJson.toString(), selfDJson.toString());
        assertTrue(selfDJson.toString().contains("42"));
    }


    private void assertJsonEquals(String json1, String json2) throws ComparisonFailure {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node1 = mapper.readTree(json1);
            JsonNode node2 = mapper.readTree(json2);
            assertEquals(node1, node2);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ComparisonFailure(e.getMessage(), "JsonObject", "could not create");
        }
    }

}
