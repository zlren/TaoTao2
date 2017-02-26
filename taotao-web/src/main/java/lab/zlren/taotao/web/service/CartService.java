package lab.zlren.taotao.web.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lab.zlren.taotao.common.service.httpclient.ApiService;
import lab.zlren.taotao.web.bean.Cart;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * Created by zlren on 2017/2/26.
 */
@Service
public class CartService {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Autowired
    private ApiService apiService;

    public List<Cart> queryCartListByUserId(Long userId) {

        try {
            // 查询购物车系统提供的接口获取购物车列表
            String jsonData = this.apiService.doGet("http://cart.taotao.com/service/cart?userId=" + userId);

            if (StringUtils.isEmpty(jsonData)) {
                return null;
            }

            return OBJECT_MAPPER.readValue(jsonData, OBJECT_MAPPER.getTypeFactory().constructCollectionType(List.class,
                    Cart.class));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }
}
