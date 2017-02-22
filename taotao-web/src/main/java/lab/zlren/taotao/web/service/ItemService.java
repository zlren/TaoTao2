package lab.zlren.taotao.web.service;

import lab.zlren.taotao.manage.pojo.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by zlren on 2017/2/21.
 */
@Service
public class ItemService {

    @Autowired
    private ApiService apiService;

    public Item queryItemById(Long itemId) {
    }
}
