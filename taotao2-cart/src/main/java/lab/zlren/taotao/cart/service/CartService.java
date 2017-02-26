package lab.zlren.taotao.cart.service;

import com.github.abel533.entity.Example;
import lab.zlren.taotao.cart.bean.User;
import lab.zlren.taotao.cart.mapper.CartMapper;
import lab.zlren.taotao.cart.pojo.Cart;
import lab.zlren.taotao.cart.pojo.Item;
import lab.zlren.taotao.cart.threadlocal.UserThreadLocal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by zlren on 2017/2/26.
 */
@Service
public class CartService {

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private ItemService itemService;

    public void addItemToCart(Long itemId) {

        User user = UserThreadLocal.get();
        Cart record = new Cart();
        record.setItemId(itemId);
        record.setUserId(user.getId());
        Cart cart = this.cartMapper.selectOne(record);

        if (null == cart) {
            // 购物车中不存在该商品
            Item item = this.itemService.queryItemById(itemId);
            if (null == item) {
                // TODO: 2017/2/26 给用户提示
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
            cart.setUserId(user.getId());

            this.cartMapper.insert(cart);

        } else {
            // 该商品存在购物车中，数量相加，默认为1
            cart.setNum(cart.getNum() + 1);
            cart.setUpdated(new Date());
            this.cartMapper.updateByPrimaryKeySelective(cart);
        }
    }

    public List<Cart> queryCartList() {

        User user = UserThreadLocal.get();
        return this.queryCartList(user.getId());
    }

    public List<Cart> queryCartList(Long userId) {

        Example example = new Example(Cart.class);
        example.createCriteria().andEqualTo("userId", userId);
        example.setOrderByClause("created DESC"); // 根据创建的时间倒序排序
        return this.cartMapper.selectByExample(example);
    }

    public void updateNum(Long itemId, Integer num) {
        User user = UserThreadLocal.get();

        // 更新的数据
        Cart record = new Cart();
        record.setNum(num);
        record.setUpdated(new Date());

        // 更新的条件
        Example example = new Example(Cart.class);
        example.createCriteria().andEqualTo("itemId", itemId).andEqualTo("userId", user.getId());

        this.cartMapper.updateByExampleSelective(record, example);
    }

    public void deleteItem(Long itemId) {

        Cart record = new Cart();
        record.setUserId(UserThreadLocal.get().getId());
        record.setItemId(itemId);
        this.cartMapper.delete(record);

    }


}
