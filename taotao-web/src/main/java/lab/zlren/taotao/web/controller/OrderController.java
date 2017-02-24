package lab.zlren.taotao.web.controller;

import lab.zlren.taotao.web.bean.Item;
import lab.zlren.taotao.web.bean.Order;
import lab.zlren.taotao.web.service.ItemService;
import lab.zlren.taotao.web.service.OrderService;
import lab.zlren.taotao.web.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

/**
 * OrderController
 * Created by zlren on 2017/2/24.
 */
@Controller
@RequestMapping("order")
public class OrderController {

    @Autowired
    private ItemService itemService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    /**
     * 去订单确认页
     *
     * @param itemId
     * @return
     */
    @RequestMapping(value = "{itemId}", method = RequestMethod.GET)
    public ModelAndView toOrder(@PathVariable("itemId") Long itemId) {

        ModelAndView modelAndView = new ModelAndView("order");
        Item item = this.itemService.queryItemById(itemId);
        modelAndView.addObject("item", item);

        return modelAndView;
    }


    @RequestMapping(value = "submit", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> submitOrder(Order order) {

        Map<String, Object> result = new HashMap<>();


        String orderId = this.orderService.submitOrder(order);

        if (StringUtils.isEmpty(orderId)) {
            // 提交订单失败
            result.put("status", 300);
        } else {
            result.put("status", 200);
            result.put("data", orderId);
        }
        return result;
    }

    @RequestMapping(value = "success", method = RequestMethod.GET)
    public ModelAndView success(@RequestParam("id") String orderId) {
        ModelAndView modelAndView = new ModelAndView("success");

        Order order = this.orderService.queryOrderById(orderId);
        modelAndView.addObject("order", order);
        // 当前时间向后推两天，格式化：01月20号
        modelAndView.addObject("date", new DateTime().plusDays(2).toString("MM月dd日"));

        return modelAndView;
    }
}
