package lab.zlren.taotao.manage.controller;

import com.github.pagehelper.PageInfo;
import lab.zlren.taotao.common.bean.EasyUIResult;
import lab.zlren.taotao.manage.pojo.ItemParam;
import lab.zlren.taotao.manage.service.ItemParamService;
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
 * ItemParamController
 * Created by zlren on 17/2/16.
 */

@Controller
@RequestMapping("item/param")
public class ItemParamController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ItemParamController.class);

    @Autowired
    private ItemParamService itemParamService;


    /**
     * 根据类目id查找规格参数模板
     *
     * @param itemCatId
     * @return
     */
    @RequestMapping(value = "{ItemCatId}", method = RequestMethod.GET)
    public ResponseEntity<ItemParam> queryByItemCatId(@PathVariable("ItemCatId") Long itemCatId) {
        try {
            ItemParam record = new ItemParam();
            record.setItemCatId(itemCatId);
            ItemParam itemParam = this.itemParamService.queryOne(record);

            if (null == itemParam) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            return ResponseEntity.ok(itemParam);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }


    /**
     * 新增规格参数模板
     *
     * @param itemCatId
     * @param itemParam
     * @return
     */
    @RequestMapping(value = "{ItemCatId}", method = RequestMethod.POST)
    public ResponseEntity<Void> saveItemParam(
            @PathVariable("ItemCatId") Long itemCatId,
            ItemParam itemParam) {
        try {
            itemParam.setItemCatId(itemCatId);
            this.itemParamService.save(itemParam);

            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }


    /**
     * 商品规格参数列表查询
     *
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public ResponseEntity<EasyUIResult> queryItemParamList(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "30") Integer rows) {

        try {
            PageInfo<ItemParam> itemParamPageInfo = this.itemParamService.queryPageList(page, rows);
            EasyUIResult easyUIResult = new EasyUIResult(itemParamPageInfo.getTotal(), itemParamPageInfo.getList());
            return ResponseEntity.ok(easyUIResult);


        } catch (Exception e) {
            e.printStackTrace();
        }

        // 出错 500
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}
