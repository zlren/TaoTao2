package lab.zlren.taotao.manage.service;

import com.github.abel533.entity.Example;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lab.zlren.taotao.manage.mapper.ItemParamMapper;
import lab.zlren.taotao.manage.pojo.Item;
import lab.zlren.taotao.manage.pojo.ItemParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zlren on 17/2/16.
 */
@Service
public class ItemParamService extends BaseService<ItemParam> {

    @Autowired
    private ItemParamMapper itemParamMapper;

    public PageInfo<ItemParam> queryPageList(Integer page, Integer rows) {

        Example example = new Example(ItemParam.class);
        example.setOrderByClause("updated DESC"); // 排序字段

        // 设置分页参数
        PageHelper.startPage(page, rows);

        List<ItemParam> itemParams = this.itemParamMapper.selectByExample(example);

        return new PageInfo<ItemParam>(itemParams);
    }
}
