package lab.zlren.taotao.web.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lab.zlren.taotao.common.bean.EasyUIResult;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lab.zlren.taotao.manage.pojo.Content;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

/**
 * IndexService
 * Created by zlren on 17/2/18.
 */

@Service
public class IndexService {

    @Autowired
    private ApiService apiService;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Value("${TAOTAO_MANAGE_URL}")
    private String TAOTAO_MANAGE_URL;

    @Value("${INDEX_AD1_URL}")
    private String INDEX_AD1_URL;

    @Value("${INDEX_AD2_URL}")
    private String INDEX_AD2_URL;


    public String queryIndexAD1() {
        try {
            String url = TAOTAO_MANAGE_URL + INDEX_AD1_URL;
            String jsonData = this.apiService.doGet(url);

            if (StringUtils.isEmpty(jsonData)) {
                return null;
            }

            // 解析json，生成首页所需要的json数据
            JsonNode jsonNode = OBJECT_MAPPER.readTree(jsonData);

            List<Map<String, Object>> result = new ArrayList<>();

            ArrayNode rows = (ArrayNode) jsonNode.get("rows");
            for (JsonNode row : rows) {
                Map<String, Object> map = new LinkedHashMap<>();

                map.put("srcB", row.get("pic").asText());
                map.put("height", 240);
                map.put("alt", row.get("title").asText());
                map.put("width", 670);
                map.put("src", row.get("pic").asText());
                map.put("widthB", 550);
                map.put("href", row.get("url").asText());
                map.put("heightB", 240);

                result.add(map);
            }
            return OBJECT_MAPPER.writeValueAsString(result);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    /**
     * 右上角小广告
     *
     * @return
     */
    public String queryIndexAD2() {
        try {
            String url = TAOTAO_MANAGE_URL + INDEX_AD2_URL;
            String jsonData = this.apiService.doGet(url);

            if (StringUtils.isEmpty(jsonData)) {
                return null;
            }

            // 解析json，生成首页所需要的json数据
            EasyUIResult easyuiResult = EasyUIResult.formatToList(jsonData, Content.class);
            List<Map<String, Object>> result = new ArrayList<>();

            for (Content content : (List<Content>) easyuiResult.getRows()) {
                Map<String, Object> map = new LinkedHashMap<>();

                map.put("width", 310);
                map.put("height", 70);
                map.put("src", content.getPic());
                map.put("href", content.getUrl());
                map.put("alt", content.getTitle());
                map.put("widthB", 210);
                map.put("heightB", 70);
                map.put("srcB", content.getPic());

                result.add(map);
            }
            return OBJECT_MAPPER.writeValueAsString(result);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
