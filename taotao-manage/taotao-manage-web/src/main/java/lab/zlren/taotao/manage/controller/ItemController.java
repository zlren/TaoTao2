package lab.zlren.taotao.manage.controller;

import com.github.pagehelper.PageInfo;
import lab.zlren.taotao.common.bean.EasyUIResult;
import lab.zlren.taotao.manage.pojo.Item;
import lab.zlren.taotao.manage.service.ItemDescService;
import lab.zlren.taotao.manage.service.ItemService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * ItemController 商品基础信息
 * Created by zlren on 17/2/15.
 */
@RequestMapping("/item")
@Controller
public class ItemController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ItemController.class);

    @Autowired
    private ItemService itemService;

    @Autowired
    private ItemDescService itemDescService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> saveItem(Item item, @RequestParam("desc") String desc,
                                         @RequestParam("itemParams") String itemParams) {

        try {

            // 入参处输出日志
            LOGGER.info("新增商品! item = {}, desc = {}", item, desc);

            if (StringUtils.isEmpty(item.getTitle())) {
                // 响应400
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }


            this.itemService.saveItem(item, desc, itemParams); // save之后id就有了

            // 状态发生变化
            LOGGER.info("新增商品成功! itemId = {}", item.getId());

            // 201
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("新增商品失败! title = " + item.getTitle() + ", cid = " + item.getCid(), e);
        }

        // 出错 500
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }


    /**
     * 查询商品列表
     *
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<EasyUIResult> queryItemList(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "30") Integer rows) {

        try {
            PageInfo<Item> itemPageInfo = this.itemService.queryPageList(page, rows);
            EasyUIResult easyUIResult = new EasyUIResult(itemPageInfo.getTotal(), itemPageInfo.getList());
            return ResponseEntity.ok(easyUIResult);


        } catch (Exception e) {
            e.printStackTrace();
        }

        // todo todo的功能很酷啊

        // 出错 500
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    /**
     * 修改商品信息
     * @param item
     * @param desc
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<Void> updateItem(Item item, @RequestParam("desc") String desc,
                                           @RequestParam("itemParams") String itemParams) {

        try {

            // 入参处输出日志
            LOGGER.info("修改商品! item = {}, desc = {}", item, desc);

            if (StringUtils.isEmpty(item.getTitle())) {
                // 响应400
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            // 修改商品
            this.itemService.updateItem(item, desc, itemParams); // save之后id就有了

            // 状态发生变化
            LOGGER.info("修改商品成功! itemId = {}", item.getId());

            // 成功 204
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("修改商品失败! title = " + item.getTitle() + ", cid = " + item.getCid(), e);
        }

        // 出错 500
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }


    /**
     * 根据商品id查询商品数据
     * @param itemId
     * @return
     */
    @RequestMapping(value = "{itemId}", method = RequestMethod.GET)
    public ResponseEntity<Item> queryById(@PathVariable("itemId") Long itemId) {
        try {
            Item item = this.itemService.queryById(itemId);
            if (null == item) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            return ResponseEntity.ok(item);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 出错 500
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}
