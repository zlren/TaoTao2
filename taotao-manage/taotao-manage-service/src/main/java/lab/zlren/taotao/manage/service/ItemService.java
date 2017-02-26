package lab.zlren.taotao.manage.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.abel533.entity.Example;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lab.zlren.taotao.common.service.httpclient.ApiService;
import lab.zlren.taotao.manage.mapper.ItemMapper;
import lab.zlren.taotao.manage.pojo.Item;
import lab.zlren.taotao.manage.pojo.ItemDesc;
import lab.zlren.taotao.manage.pojo.ItemParamItem;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ItemService
 * Created by zlren on 17/2/15.
 */
@Service
public class ItemService extends BaseService<Item> {

    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private ItemDescService itemDescService;

    @Autowired
    private ItemParamItemService itemParamItemService;

    @Autowired
    private ApiService apiService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * saveItem新增商品，将存储item和itemdesc合成一个事务
     * service中使用了service，嵌套，利用了事务的传播特性，两个都成功才算成功
     * 如果在controller中使用两个并列的service，两个service的操作是独立的，并不是一个整体的事务
     *
     * @param item       item基本数据
     * @param desc       item对应的描述信息
     * @param itemParams
     */
    public void saveItem(Item item, String desc, String itemParams) {

        // 设置初始数据
        item.setStatus(1); // 默认上架状态
        item.setId(null); // 出于安全考虑，防止在页面中添加id属性，在这里强制设置id为null

        super.save(item);

        ItemDesc itemDesc = new ItemDesc();
        itemDesc.setItemId(item.getId());
        itemDesc.setItemDesc(desc);

        this.itemDescService.save(itemDesc); // 保存描述数据

        // 保存规格参数数据
        ItemParamItem itemParamItem = new ItemParamItem();
        itemParamItem.setItemId(item.getId());
        itemParamItem.setParamData(itemParams);

        this.itemParamItemService.save(itemParamItem);

        sendMsg(item.getId(), "insert");
    }

    public PageInfo<Item> queryPageList(Integer page, Integer rows) {

        Example example = new Example(Item.class);
        example.setOrderByClause("updated DESC"); // 排序字段

        // 设置分页参数
        PageHelper.startPage(page, rows);

        List<Item> items = this.itemMapper.selectByExample(example);

        return new PageInfo<Item>(items);

    }

    public void updateItem(Item item, String desc, String itemParams) {

        // 强制设置不能修改的字段为null
        item.setStatus(null);
        item.setCreated(null);

        super.updateSelective(item); // 不为null的字段去修改

        // 修改商品描述数据
        ItemDesc itemDesc = new ItemDesc();
        itemDesc.setItemId(item.getId());
        itemDesc.setItemDesc(desc);

        this.itemDescService.updateSelective(itemDesc);

        // 修改商品规格参数数据
        this.itemParamItemService.updateItemParamItem(item.getId(), itemParams);

        // 通知其他系统该商品已经更新，删除其他系统的缓存
        // 目前只通知前台系统
        // try {
        //     String url = "http://www.taotao.com:8002/item/cache/" + item.getId() + ".html";
        //     this.apiService.doPost(url, null);
        // } catch (IOException e) {
        //     e.printStackTrace();
        // }

        sendMsg(item.getId(), "update");

    }

    private void sendMsg(Long itemId, String type) {
        // 发送MQ消息通知其他系统
        try {
            Map<String, Object> msg = new HashMap<>();
            msg.put("itemId", itemId);
            msg.put("type", "update");
            msg.put("date", System.currentTimeMillis());

            this.rabbitTemplate.convertAndSend("item." + type, OBJECT_MAPPER.writeValueAsString(msg));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
