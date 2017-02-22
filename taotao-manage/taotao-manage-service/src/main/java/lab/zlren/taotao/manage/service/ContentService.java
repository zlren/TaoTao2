package lab.zlren.taotao.manage.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lab.zlren.taotao.common.bean.EasyUIResult;
import lab.zlren.taotao.manage.mapper.ContentMapper;
import lab.zlren.taotao.manage.pojo.Content;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ContentService
 * Created by zlren on 17/2/18.
 */
@Service
public class ContentService extends BaseService<Content> {

    @Autowired
    private ContentMapper contentMapper;


    public EasyUIResult queryList(Long categoryId, Integer page, Integer rows) {

        // 设置分页参数
        PageHelper.startPage(page, rows);

        List<Content> contentList = this.contentMapper.queryList(categoryId);
        PageInfo<Content> pageInfo = new PageInfo<Content>(contentList);

        return new EasyUIResult(pageInfo.getTotal(), pageInfo.getList());
    }
}
