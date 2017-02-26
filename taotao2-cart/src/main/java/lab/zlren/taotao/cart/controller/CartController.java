package lab.zlren.taotao.cart.controller;

import lab.zlren.taotao.cart.bean.User;
import lab.zlren.taotao.cart.pojo.Cart;
import lab.zlren.taotao.cart.service.CartCookieService;
import lab.zlren.taotao.cart.service.CartService;
import lab.zlren.taotao.cart.threadlocal.UserThreadLocal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by zlren on 2017/2/26.
 */
@Controller
@RequestMapping("cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private CartCookieService cartCookieService;

    /**
     * 加入商品到购物车
     *
     * @param itemId
     * @return
     */
    @RequestMapping(value = "{itemId}", method = RequestMethod.GET)
    public String addItemToCart(@PathVariable("itemId") Long itemId, HttpServletRequest httpServletRequest,
                                HttpServletResponse httpServletResponse) {

        // 判断用户是否登录
        User user = UserThreadLocal.get();
        if (null == user) {
            // 未登录状态
            this.cartCookieService.addItemToCart(itemId, httpServletRequest, httpServletResponse);
        } else {
            this.cartService.addItemToCart(itemId);
        }
        // 重定向到购物车列表页面
        return "redirect:/cart/list.html";
    }


    /**
     * 对外提供接口，根据用户id查询购物车列表
     *
     * @param userId
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, params = "userId")
    public ResponseEntity<List<Cart>> queryCartListByUserId(@RequestParam("userId") Long userId) {

        try {
            List<Cart> carts = this.cartService.queryCartList(userId);
            if (null == carts || carts.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(carts);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }


    /**
     * 显示购物车列表
     *
     * @return
     */
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public ModelAndView showCartList(HttpServletRequest httpServletRequest) {

        ModelAndView modelAndView = new ModelAndView("cart");

        List<Cart> cartList = null;

        // 判断用户是否登录
        User user = UserThreadLocal.get();
        if (null == user) {
            // 未登录状态
            cartList = this.cartCookieService.queryCartList(httpServletRequest);
        } else {
            cartList = this.cartService.queryCartList();
        }

        modelAndView.addObject("cartList", cartList);
        return null;
    }


    /**
     * 修改购买商品的数量
     *
     * @param itemId
     * @param num    最终购买的数量
     * @return
     */
    @RequestMapping(value = "update/num/{itemId}", method = RequestMethod.POST)
    public ResponseEntity<Void> updateNum(@PathVariable("itemId") Long itemId, @PathVariable("num") Integer num,
                                          HttpServletRequest httpServletRequest,
                                          HttpServletResponse httpServletResponse) {
        // 判断用户是否登录
        User user = UserThreadLocal.get();
        if (null == user) {
            // 未登录状态
            this.cartCookieService.updateNum(itemId, num, httpServletRequest, httpServletResponse);
        } else {
            this.cartService.updateNum(itemId, num);
        }

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    /**
     * 删除购物车中的商品
     *
     * @param itemId
     * @return
     */
    @RequestMapping(value = "delete/{itemId}", method = RequestMethod.GET)
    public String deleteItem(@PathVariable("itemId") Long itemId, HttpServletRequest httpServletRequest,
                             HttpServletResponse httpServletResponse) {
        // 判断用户是否登录
        User user = UserThreadLocal.get();
        if (null == user) {
            // 未登录状态
            this.cartCookieService.deleteItem(itemId, httpServletRequest, httpServletResponse);
        } else {
            this.cartService.deleteItem(itemId);
        }

        return "redirect:/cart/list.html";
    }
}
