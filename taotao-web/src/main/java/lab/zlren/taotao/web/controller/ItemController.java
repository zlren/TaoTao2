package lab.zlren.taotao.web.controller;

import lab.zlren.taotao.manage.pojo.ItemDesc;
import lab.zlren.taotao.web.bean.Item;
import lab.zlren.taotao.web.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * ItemController
 * Created by zlren on 2017/2/21.
 */
@Controller
@RequestMapping("item")
public class ItemController {

    @Autowired
    private ItemService itemService;


    /**
     * 显示商品详情页
     *
     * @param itemId
     * @return
     */
    @RequestMapping(value = "{itemId}", method = RequestMethod.GET)
    public ModelAndView showDetail(@PathVariable("itemId") Long itemId) {
        ModelAndView modelAndView = new ModelAndView("item");

        // 基本数据
        Item item = this.itemService.queryItemById(itemId);
        modelAndView.addObject("item", item);

        // 描述数据
        ItemDesc itemDesc = this.itemService.queryItemDescByItemId(itemId);
        modelAndView.addObject("itemDesc", itemDesc);

        // 规格参数
        String itemParam = this.itemService.queryItemParamByItemId(itemId);
        modelAndView.addObject("itemParam", itemParam);

        return modelAndView;
    }
}
