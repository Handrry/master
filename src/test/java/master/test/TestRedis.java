package master.test;

import com.master.cache.CacheService;
import com.master.lock.RedisLock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * Created by xiaoxia on 2017/9/2 9:59.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:*.xml"})
public class TestRedis {

    @Autowired
    private CacheService cacheService;

    @Test
    public void setV() {
        cacheService.set("yes", "测试redis");
        String s = (String) cacheService.get("yes");
        System.out.println(s);
    }

    @Test
    public void testLock() {
        Thread thread1 = new Thread() {
            @Override
            public void run() {

                for (int i = 0; i < 100; i++) {

                        System.out.println("大家好");

                }

            }
        };
        Thread thread2 = new Thread() {
            @Override
            public void run() {

                for (int i = 0; i < 100; i++) {
                        System.out.println("nimen好");


                }
            }
        };
//        thread1.start();
//        thread2.start();
        RedisLock redisLock = null;
        try {
            redisLock = cacheService.getLock("t1");
            thread1.start();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("12222333333333");
        } finally {
            if (redisLock != null) {
                redisLock.unlock();
            }
        }
        RedisLock redisLock1 = null;
        try {
            redisLock1 = cacheService.getLock("t1");
            thread2.start();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("dadada2222222");
        } finally {
            if (redisLock1 != null) {
                redisLock1.unlock();
            }
        }

        while (true){

        }
    }



}
