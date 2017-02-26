package lab.zlren.taotao.search.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lab.zlren.taotao.common.service.httpclient.ApiService;
import lab.zlren.taotao.search.pojo.Item;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Created by zlren on 2017/2/25.
 */
@Service
public class ItemService {

    @Autowired
    private ApiService apiService;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public Item queryItemById(Long itemId) {
        String url = "http://manage.taotao.com:8001/rest/item/" + itemId;
        try {
            String jsonData = this.apiService.doGet(url);

            if (StringUtils.isEmpty(jsonData)) {
                return null;
            }

            return OBJECT_MAPPER.readValue(jsonData, Item.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
