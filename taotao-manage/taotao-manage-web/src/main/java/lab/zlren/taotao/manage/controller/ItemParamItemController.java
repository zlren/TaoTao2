package lab.zlren.taotao.manage.controller;

import lab.zlren.taotao.manage.pojo.ItemParamItem;
import lab.zlren.taotao.manage.service.ItemParamItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * ItemParamItemController
 * Created by zlren on 17/2/16.
 */
@Controller
@RequestMapping("item/param/item")
public class ItemParamItemController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ItemParamItemController.class);

    @Autowired
    private ItemParamItemService itemParamItemService;

    /**
     * 根据商品id查询商品规格参数数据
     * @param itemId
     * @return
     */
    @RequestMapping(value = "{itemId}", method = RequestMethod.GET)
    public ResponseEntity<ItemParamItem> queryByItemId(@PathVariable("itemId") Long itemId) {

        try {
            ItemParamItem record = new ItemParamItem();
            record.setItemId(itemId);

            ItemParamItem itemParamItem = this.itemParamItemService.queryOne(record);

            if (null == itemParamItem) {
                // 资源不存在
                // 404
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            // 200
            return ResponseEntity.ok(itemParamItem);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 出错 500
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}
