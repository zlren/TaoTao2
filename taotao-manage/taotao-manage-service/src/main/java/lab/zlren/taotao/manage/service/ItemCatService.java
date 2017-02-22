package lab.zlren.taotao.manage.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lab.zlren.taotao.common.bean.ItemCatData;
import lab.zlren.taotao.common.bean.ItemCatResult;
import lab.zlren.taotao.common.service.jedis.RedisService;
import lab.zlren.taotao.manage.pojo.ItemCat;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ItemCatService
 * Created by zlren on 17/2/6.
 */
@Service
public class ItemCatService extends BaseService<ItemCat> {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Autowired
    private RedisService redisService;

    /**
     * 全部查询，并且生成树状结构
     *
     * @return
     */
    public ItemCatResult queryAllToTree() throws JsonProcessingException {
        ItemCatResult result = new ItemCatResult();

        // 先从缓存中命中，命中返回，没有命中程序继续执行
        String key = "TAOTAO_MANAGE_ITEM_CAT_ALL"; // 最佳实践，项目名_模块名_业务名
        String cacheData = this.redisService.get(key);
        if (StringUtils.isNoneEmpty(cacheData)) {
            // 命中

            try {
                return OBJECT_MAPPER.readValue(cacheData, ItemCatResult.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        // 全部查出，并且在内存中生成树形结构
        List<ItemCat> cats = super.queryAll();

        // 转为map存储，key为父节点ID，value为数据集合
        Map<Long, List<ItemCat>> itemCatMap = new HashMap<Long, List<ItemCat>>();
        for (ItemCat itemCat : cats) {
            if (!itemCatMap.containsKey(itemCat.getParentId())) {
                itemCatMap.put(itemCat.getParentId(), new ArrayList<ItemCat>());
            }
            itemCatMap.get(itemCat.getParentId()).add(itemCat);
        }

        // 封装一级对象
        List<ItemCat> itemCatList1 = itemCatMap.get(0L);
        for (ItemCat itemCat : itemCatList1) {
            ItemCatData itemCatData = new ItemCatData();
            itemCatData.setUrl("/products/" + itemCat.getId() + ".html");
            itemCatData.setName("<a href='" + itemCatData.getUrl() + "'>" + itemCat.getName() + "</a>");
            result.getItemCats().add(itemCatData);
            if (!itemCat.getIsParent()) {
                continue;
            }

            // 封装二级对象
            List<ItemCat> itemCatList2 = itemCatMap.get(itemCat.getId());
            List<ItemCatData> itemCatData2 = new ArrayList<ItemCatData>();
            itemCatData.setItems(itemCatData2);
            for (ItemCat itemCat2 : itemCatList2) {
                ItemCatData id2 = new ItemCatData();
                id2.setName(itemCat2.getName());
                id2.setUrl("/products/" + itemCat2.getId() + ".html");
                itemCatData2.add(id2);
                if (itemCat2.getIsParent()) {
                    // 封装三级对象
                    List<ItemCat> itemCatList3 = itemCatMap.get(itemCat2.getId());
                    List<String> itemCatData3 = new ArrayList<String>();
                    id2.setItems(itemCatData3);
                    for (ItemCat itemCat3 : itemCatList3) {
                        itemCatData3.add("/products/" + itemCat3.getId() + ".html|" + itemCat3.getName());
                    }
                }
            }
            if (result.getItemCats().size() >= 14) {
                break;
            }
        }


        // 添加到缓存，将result序列化成json字符串后存储
        this.redisService.set(key, OBJECT_MAPPER.writeValueAsString(result));


        return result;
    }
}
