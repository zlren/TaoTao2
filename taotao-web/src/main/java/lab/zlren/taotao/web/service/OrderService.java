package lab.zlren.taotao.web.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lab.zlren.taotao.common.service.httpclient.ApiService;
import lab.zlren.taotao.common.service.httpclient.HttpResult;
import lab.zlren.taotao.web.bean.Order;
import lab.zlren.taotao.web.bean.User;
import lab.zlren.taotao.web.threadlocal.UserThreadLocal;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * OrderService
 * Created by zlren on 2017/2/24.
 */
@Service
public class OrderService {

    @Autowired
    private ApiService apiService;

    @Value("${TAOTAO_ORDER_URL}")
    private String TAOTAO_ORDER_URL;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public String submitOrder(Order order) {

        User user = UserThreadLocal.get(); // 从本地线程中获取user对象

        order.setUserId(user.getId());
        order.setBuyerNick(user.getUsername());

        try {
            String url = TAOTAO_ORDER_URL + "/order/create";

            HttpResult httpResult = this.apiService.doPostJson(url, OBJECT_MAPPER.writeValueAsString(order));
            if (httpResult.getCode() == 200) {
                String jsonData = httpResult.getData();
                JsonNode jsonNode = OBJECT_MAPPER.readTree(jsonData);
                if (jsonNode.get("status").intValue() == 200) {
                    // 订单提交成功
                    return jsonNode.get("data").asText();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }

    public Order queryOrderById(String orderId) {

        String url = TAOTAO_ORDER_URL + "/order/query/" + orderId;

        try {
            String jsonData = this.apiService.doGet(url);

            if (StringUtils.isNoneEmpty(jsonData)) {
                return OBJECT_MAPPER.readValue(jsonData, Order.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
