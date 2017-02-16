package lab.zlren.taotao.manage.service;

import com.github.abel533.entity.Example;
import lab.zlren.taotao.manage.mapper.ItemParamItemMapper;
import lab.zlren.taotao.manage.pojo.ItemParamItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by zlren on 17/2/16.
 */
@Service
public class ItemParamItemService extends BaseService<ItemParamItem> {

    @Autowired
    private ItemParamItemMapper itemParamItemMapper;


    public void updateItemParamItem(Long itemId, String itemParams) {

        // 更新的数据
        ItemParamItem record = new ItemParamItem();
        record.setParamData(itemParams);
        record.setUpdated(new Date());

        // example是更新条件
        Example example = new Example(ItemParamItem.class);
        example.createCriteria().andEqualTo("itemId", itemId); // 这里指定的字段是javabean的属性，不是数据库表字段名


        this.itemParamItemMapper.updateByExampleSelective(record, example);
    }
}
