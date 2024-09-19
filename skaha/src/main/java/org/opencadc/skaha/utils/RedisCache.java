
package org.opencadc.skaha.utils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.opencadc.skaha.image.Image;
import org.opencadc.skaha.image.ImageList;
import redis.clients.jedis.Jedis;

import java.io.Closeable;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class RedisCache implements Closeable {
    public final Jedis jedis;
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
        try {
            return gson.fromJson(valueInString, className);
        } catch (JsonSyntaxException ex) {
            ex.printStackTrace();
            throw new ClassCastException("Unable to cast value to " + className.getCanonicalName());
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    public static void main(String[] args) {
        RedisCache cache = new RedisCache("localhost", 6379);
        List<String> images = cache.jedis.lrange("public", 0, -1);
        Gson gson = new Gson();
        String type = "notebook";

        List<Image> f = images
                .stream()
                .map(image -> gson.fromJson(image, Image.class))
                .filter(Objects::nonNull)
                .filter(image -> type == null || (image.getTypes().contains(type)))
                .toList();


        cache.close();
    }

    public void close() {
        jedis.close();
    }

}