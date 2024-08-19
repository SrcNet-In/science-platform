package org.opencadc.skaha.image;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.opencadc.skaha.SkahaAction;
import org.opencadc.skaha.utils.RedisCache;

import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.opencadc.skaha.utils.TestUtils.set;
import static org.opencadc.skaha.utils.TestUtils.setEnv;

@RunWith(MockitoJUnitRunner.class)
public class GetActionTest {
    GetAction getAction;

    RedisCache redis;

    private String redisHost = "localhost";
    private int redisPort = 6129;
    private long imageRefreshInterval = 100000;


    @Before
    public void setUp() throws Exception {
        setEnv("skaha.redis-host", redisHost);
        setEnv("skaha.redis-port", Integer.toString(redisPort));
        setEnv("skaha.image-refresh-interval", Long.toString(imageRefreshInterval));
        redis = Mockito.mock(RedisCache.class);
        getAction = new GetAction();
        set(getAction, SkahaAction.class, "redis", redis);
    }

    @After
    public void cleanUp() {
    }

    @Test
    public void testGetImagesFromRedis() throws Exception {
        ImageList expectedImageList = new ImageList(List.of(
                new Image("1", Set.of("notebook"), "hashcode")
        ));

        when(redis.get(eq("lastUpdated")))
                .thenReturn(System.currentTimeMillis() + "");
        when(redis.get(eq("public"), ArgumentMatchers.any()))
                .thenReturn(expectedImageList);

        List<Image> actualImageList = getAction.getImages(null, null);

        assertEquals(expectedImageList.get(), actualImageList);
    }

//    @Test
    public void testGetOldImagesFromRedis() throws Exception {
        long lastUpdatedTime = System.currentTimeMillis() - 2 * imageRefreshInterval;
        ImageList expectedImageList = new ImageList(List.of(
                new Image("1", Set.of("notebook"), "hashcode")
        ));

        when(redis.get(eq("lastUpdated"))).thenReturn(lastUpdatedTime+ "");

        List<Image> actualImageList = getAction.getImages(null, null);

        assertEquals(expectedImageList.get(), actualImageList);
    }
}
