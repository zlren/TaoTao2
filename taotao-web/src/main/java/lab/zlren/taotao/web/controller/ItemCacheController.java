package lab.zlren.taotao.web.controller;

import lab.zlren.taotao.common.service.jedis.RedisService;
import lab.zlren.taotao.web.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * ItemCacheController
 * Created by zlren on 2017/2/22.
 */
@Controller
@RequestMapping("item/cache")
public class ItemCacheController {

    @Autowired
    private RedisService redisService;


    /**
     * 前端开放的清除缓存的接口
     * @param itemId
     * @return
     */
    @RequestMapping(value = "{itemId}", method = RequestMethod.POST)
    public ResponseEntity<Void> deleteCache(@PathVariable("itemId") Long itemId) {

        try {
            String key = ItemService.REDIS_KEY + itemId;

            this.redisService.del(key);

            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

    }
}
