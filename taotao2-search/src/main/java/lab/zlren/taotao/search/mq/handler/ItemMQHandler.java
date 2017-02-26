package lab.zlren.taotao.search.mq.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lab.zlren.taotao.search.pojo.Item;
import lab.zlren.taotao.search.service.ItemService;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 搜索处理后台发出的消息队列
 * Created by zlren on 2017/2/25.
 */
public class ItemMQHandler {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Autowired
    private HttpSolrServer httpSolrServer;

    @Autowired
    private ItemService itemService;

    public void execute(String msg) {

        try {
            JsonNode jsonNode = OBJECT_MAPPER.readTree(msg);
            Long itemId = jsonNode.get("itemId").asLong();
            String type = jsonNode.get("type").asText();

            if (StringUtils.equals(type, "insert") || StringUtils.equals(type, "update")) {

                // 从后台查询数据
                Item item = this.itemService.queryItemById(itemId);

                if (item != null) {
                    this.httpSolrServer.addBean(item);
                    this.httpSolrServer.commit();
                }

            } else if (StringUtils.equals(type, "delete")) {
                this.httpSolrServer.deleteById(String.valueOf(itemId));
                this.httpSolrServer.commit();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
