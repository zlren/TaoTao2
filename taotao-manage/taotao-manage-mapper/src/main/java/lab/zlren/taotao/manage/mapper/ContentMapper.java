package lab.zlren.taotao.manage.mapper;

import com.github.abel533.mapper.Mapper;
import lab.zlren.taotao.manage.pojo.Content;

import java.util.List;

/**
 * ContentMapper
 * Created by zlren on 17/2/18.
 */
public interface ContentMapper extends Mapper<Content> {
    List<Content> queryList(Long categoryId);
}
