package lab.zlren.taotao.web.mq.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lab.zlren.taotao.common.service.jedis.RedisService;
import lab.zlren.taotao.web.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 处理后台发出的消息队列
 * Created by zlren on 2017/2/25.
 */
public class ItemMQHandler {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Autowired
    private RedisService redisService;

    public void execute(String msg) {

        try {
            JsonNode jsonNode = OBJECT_MAPPER.readTree(msg);
            Long itemId = jsonNode.get("itemId").asLong();

            String key = ItemService.REDIS_KEY + itemId;
            this.redisService.del(key);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
