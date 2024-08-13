
package org.opencadc.skaha.utils;

import com.google.gson.Gson;
import org.opencadc.skaha.image.Image;
import redis.clients.jedis.Jedis;

import java.io.Closeable;
import java.util.*;

public class RedisCache implements Closeable {
    private final Jedis jedis;
    private final Gson gson;

    public RedisCache(String host, int port) {
        jedis = new Jedis(host, port);
        gson = new Gson();
    }

    public void put(String key, String value) {
        if (key == null) throw new RuntimeException("null key");
        try {
            jedis.set(key, value);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public void put(String key, Object value) {
        if (value == null) throw new RuntimeException("null value");
        String valueInString = gson.toJson(value);
        put(key, valueInString);
    }

    public void push(String key, Collection<Object> values) {
        String[] valuesAsArray = values.stream()
                .map(val -> gson.toJson(val))
                .toArray(String[]::new);
        jedis.rpush(key, valuesAsArray);
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
