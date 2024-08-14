package org.opencadc.skaha.utils;

import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.opencadc.skaha.utils.TestUtils.set;

@RunWith(MockitoJUnitRunner.class)
public class RedisCacheTest {

    @InjectMocks
    private RedisCache redisCache;

    Jedis jedis;


    private static final String OK = "OK";

    @Before
    public void setUp() throws Exception {
        jedis = Mockito.mock(Jedis.class);
        set(redisCache, "jedis", jedis);
    }

    @Test
    public void testPut() {
        String keyName = "key-name", value = "{}";

        when(jedis.set(ArgumentMatchers.eq(keyName), ArgumentMatchers.eq(value)))
                .thenReturn(OK);

        assertEquals(OK, redisCache.put(keyName, value));
    }

    @Test
    public void testObjectPut() {
        String keyName = "key-name";
        Map<String, String> payload = new HashMap<>();
        payload.put("key", "value");
        String payloadInString = new Gson().toJson(payload);

        when(jedis.set(ArgumentMatchers.eq(keyName), ArgumentMatchers.eq(payloadInString)))
                .thenReturn(OK);

        assertEquals(OK, redisCache.put(keyName, payload));
    }

    @Test
    public void testGet() {
        String keyName = "key-name", value = "{}";

        when(jedis.get(ArgumentMatchers.eq(keyName)))
                .thenReturn(value);

        assertEquals(value, redisCache.get(keyName));
    }

    @Test
    public void testObjectGet() {
        String keyName = "key-name";
        Map<String, String> payload = new HashMap<>();
        payload.put("key", "value");
        String payloadInString = new Gson().toJson(payload);

        when(jedis.get(ArgumentMatchers.eq(keyName)))
                .thenReturn(payloadInString);

        assertEquals(payload, redisCache.get(keyName, Map.class));
    }

}