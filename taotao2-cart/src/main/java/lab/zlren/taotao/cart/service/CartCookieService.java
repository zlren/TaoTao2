package lab.zlren.taotao.cart.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lab.zlren.taotao.cart.pojo.Cart;
import lab.zlren.taotao.cart.pojo.Item;
import lab.zlren.taotao.common.utils.CookieUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zlren on 2017/2/26.
 */
@Service
public class CartCookieService {

    public static final String COOKIE_NAME = "TT_CART";

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static final Integer COOKIE_TIME = 60 * 60 * 24 * 30 * 12;

    @Autowired
    private ItemService itemService;

    public void addItemToCart(Long itemId, HttpServletRequest httpServletRequest, HttpServletResponse
            httpServletResponse) {
        List<Cart> carts = queryCartList(httpServletRequest);

        // 判断该商品在购物车中是否存在
        Cart cart = null;
        for (Cart c : carts) {
            if (c.getItemId().longValue() == itemId.longValue()) {
                cart = c;
                break;
            }
        }

        if (null == cart) {
            // 不存在
            Item item = this.itemService.queryItemById(itemId);
            if (null == item) {
                return;
            }

            cart = new Cart();
            cart.setCreated(new Date());
            cart.setUpdated(cart.getCreated());
            cart.setItemId(itemId);
            cart.setItemImage(item.getImage());
            cart.setItemPrice(item.getPrice());
            cart.setItemTitle(item.getTitle());
            cart.setNum(1);

            carts.add(cart);

        } else {
            // 存在数量相加
            cart.setNum(cart.getNum() + 1);
            cart.setUpdated(new Date());
        }

        try {
            // 将集合写入到cookie中
            CookieUtils.setCookie(httpServletRequest, httpServletResponse, COOKIE_NAME, OBJECT_MAPPER.writeValueAsString
                    (carts), COOKIE_TIME, true);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public List<Cart> queryCartList(HttpServletRequest httpServletRequest) {

        String cookieValue = CookieUtils.getCookieValue(httpServletRequest, COOKIE_NAME, true);
        List<Cart> carts = null;

        if (StringUtils.isEmpty(cookieValue)) {
            carts = new ArrayList<>();
        } else {
            try {
                OBJECT_MAPPER.readValue(cookieValue, OBJECT_MAPPER.getTypeFactory().constructCollectionType(List
                        .class, Cart
                        .class));
            } catch (Exception e) {
                e.printStackTrace();
                carts = new ArrayList<>();
            }
        }

        return carts;
    }

    public void updateNum(Long itemId, Integer num, HttpServletRequest httpServletRequest, HttpServletResponse
            httpServletResponse) {

        List<Cart> carts = queryCartList(httpServletRequest);

        // 判断该商品在购物车中是否存在
        for (Cart c : carts) {
            if (c.getItemId().longValue() == itemId.longValue()) {
                c.setNum(num);
                c.setUpdated(new Date());
                break;
            }
        }

        try {
            // 将集合写入到cookie中
            CookieUtils.setCookie(httpServletRequest, httpServletResponse, COOKIE_NAME, OBJECT_MAPPER.writeValueAsString
                    (carts), COOKIE_TIME, true);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void deleteItem(Long itemId, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        List<Cart> carts = queryCartList(httpServletRequest);

        // 判断该商品在购物车中是否存在
        for (Cart c : carts) {
            if (c.getItemId().longValue() == itemId.longValue()) {
                carts.remove(c);
                break;
            }
        }

        try {
            // 将集合写入到cookie中
            CookieUtils.setCookie(httpServletRequest, httpServletResponse, COOKIE_NAME, OBJECT_MAPPER.writeValueAsString
                    (carts), COOKIE_TIME, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
