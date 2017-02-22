package lab.zlren.taotao.web.controller;

import lab.zlren.taotao.manage.pojo.Item;
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


    @RequestMapping(value = "{itemId}", method = RequestMethod.GET)
    public ModelAndView showDetail(@PathVariable("itemId") Long itemId) {
        ModelAndView modelAndView = new ModelAndView("item");

        Item item = this.itemService.queryItemById(itemId);
        modelAndView.addObject("item", item)
        return modelAndView;
    }
}
