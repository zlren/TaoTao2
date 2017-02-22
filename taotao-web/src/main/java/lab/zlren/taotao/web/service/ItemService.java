package lab.zlren.taotao.web.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lab.zlren.taotao.common.service.httpclient.ApiService;
import lab.zlren.taotao.common.service.jedis.RedisService;
import lab.zlren.taotao.manage.pojo.ItemDesc;
import lab.zlren.taotao.web.bean.Item;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * ItemService
 * Created by zlren on 2017/2/21.
 */
@Service
public class ItemService {

    @Autowired
    private ApiService apiService;

    @Value("${TAOTAO_MANAGE_URL}")
    private String TAOTAO_MANAGE_URL;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Autowired
    private RedisService redisService;

    public static final String REDIS_KEY = "TAOTAO_WEB_ITEM_DETAIL_";

    public Item queryItemById(Long itemId) {

        String key = REDIS_KEY + itemId;

        // 查询缓存
        try {
            String cacheData = this.redisService.get(key);

            if (StringUtils.isNotEmpty(cacheData)) {
                // 命中
                return OBJECT_MAPPER.readValue(cacheData, Item.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            String url = TAOTAO_MANAGE_URL + "/rest/item/" + itemId;
            String jsonData = this.apiService.doGet(url);
            if (StringUtils.isEmpty(jsonData)) {
                return null;
            }

            try {
                // 将结果写入到缓存
                this.redisService.set(key, jsonData);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return OBJECT_MAPPER.readValue(jsonData, Item.class);
        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }

    public ItemDesc queryItemDescByItemId(Long itemId) {
        try {
            String url = TAOTAO_MANAGE_URL + "/rest/item/desc/" + itemId;
            String jsonData = this.apiService.doGet(url);
            if (StringUtils.isEmpty(jsonData)) {
                return null;
            }
            return OBJECT_MAPPER.readValue(jsonData, ItemDesc.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String queryItemParamByItemId(Long itemId) {
        try {
            String url = TAOTAO_MANAGE_URL + "/rest/item/param/item/" + itemId;
            String jsonData = this.apiService.doGet(url);
            JsonNode jsonNode = OBJECT_MAPPER.readTree(jsonData);
            String paramData = jsonNode.get("paramData").asText();
            ArrayNode arrayNode = (ArrayNode) OBJECT_MAPPER.readTree(paramData);

            // 拼接html
            StringBuilder sb = new StringBuilder();
            sb.append("<table cellpadding=\"0\" cellspacing=\"1\" width=\"100%\" border=\"0\" class=\"Ptable\"><tbody>");


            for (JsonNode node : arrayNode) {
                sb.append("<tr><th class=\"tdTitle\" colspan=\"2\">" + node.get("group").asText()
                        + "</th></tr>");
                ArrayNode params = (ArrayNode) node.get("params");
                for (JsonNode kv : params) {
                    sb.append("<tr><td class=\"tdTitle\">" + kv.get("k").asText() + "</td><td>"
                            + kv.get("v").asText() + "</td></tr>");
                }
            }
            sb.append("</tbody></table>");
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
