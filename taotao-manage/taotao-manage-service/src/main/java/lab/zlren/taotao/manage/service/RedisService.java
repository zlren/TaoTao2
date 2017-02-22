package lab.zlren.taotao.manage.service;

import lab.zlren.taotao.manage.service.jedis.impl.JedisClientSingle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * RedisService
 * Created by zlren on 2017/2/21.
 */
@Service
public class RedisService {

    @Autowired
    private JedisClientSingle jedisClient;

    String get(String key) {
        return jedisClient.get(key);
    }


    String set(String key, String value) {
        return jedisClient.set(key, value);
    }

    long expire(String key, int second) {
        return jedisClient.expire(key, second);
    }

    long del(String key) {
        return jedisClient.del(key);
    }

}
