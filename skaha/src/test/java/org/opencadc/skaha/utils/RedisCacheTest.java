package org.opencadc.skaha.utils;

import org.junit.Before;
import org.junit.Test;


import static org.junit.Assert.*;

public class RedisCacheTest {
    private RedisCache redisCache;

    @Before
    public void setUp() throws Exception {
        redisCache = new RedisCache("", 1234);
    }

    @Test
    public void testPut() {
        assertNoExceptionIsThrown(() -> redisCache.put("key", "value"));
    }

    private void assertNoExceptionIsThrown(Runnable executable) {
        try {
            executable.run();
        } catch (Exception e) {
            fail(e.getClass().getSimpleName() + " was thrown");
        }
    }
}