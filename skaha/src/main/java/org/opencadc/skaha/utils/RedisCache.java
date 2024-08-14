
package org.opencadc.skaha.utils;

import com.google.gson.Gson;
import redis.clients.jedis.Jedis;

import java.io.Closeable;

public class RedisCache implements Closeable {
    private final Jedis jedis;
    private final Gson gson = new Gson();

    public RedisCache() {
        jedis = null;
    }

    public RedisCache(String host, int port) {
        jedis = new Jedis(host, port);
    }

    public String put(String key, String value) {
        if (key == null) throw new RuntimeException("null key");
        try {
            return jedis.set(key, value);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public String put(String key, Object value) {
        if (value == null) throw new RuntimeException("null value");
        String valueInString = gson.toJson(value);
        return put(key, valueInString);
    }

    public String get(String key) {
        if (key == null) throw new RuntimeException("null key");
        try {
            return jedis.get(key);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public <T> T get(String key, Class<T> className) {
        String valueInString = get(key);
        if (valueInString == null) return null;
        return gson.fromJson(valueInString, className);
    }

    public void close() {
        jedis.close();
    }
}
