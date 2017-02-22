package lab.zlren.taotao.common.service.jedis;

import lab.zlren.taotao.common.service.jedis.impl.JedisClientSingle;
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

    public String get(String key) {
        return jedisClient.get(key);
    }


    public String set(String key, String value) {
        return jedisClient.set(key, value);
    }

    public long expire(String key, int second) {
        return jedisClient.expire(key, second);
    }

    public long del(String key) {
        return jedisClient.del(key);
    }

}
