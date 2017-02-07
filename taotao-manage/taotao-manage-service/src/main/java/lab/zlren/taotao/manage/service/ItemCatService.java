package lab.zlren.taotao.manage.service;

import lab.zlren.taotao.manage.mapper.ItemCatMapper;
import lab.zlren.taotao.manage.pojo.ItemCat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.PrimitiveIterator;

/**
 * Created by zlren on 17/2/6.
 */
@Service
public class ItemCatService {

    @Autowired
    private ItemCatMapper itemCatMapper;

    /**
     * 根据父节点id查询商品类目列表
     * @param parentId
     * @return
     */
    public List<ItemCat> queryItemCatListByParentId(Long parentId) {
        ItemCat record = new ItemCat();
        record.setParentId(parentId);

        return itemCatMapper.select(record);
    }
}
