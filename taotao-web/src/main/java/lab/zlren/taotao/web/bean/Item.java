package lab.zlren.taotao.web.bean;

import org.apache.commons.lang3.StringUtils;

/**
 * Item
 * Created by zlren on 2017/2/22.
 */
public class Item extends lab.zlren.taotao.manage.pojo.Item {

    public String[] getImages() {

        return StringUtils.split(super.getImage(), ","); // 这个分割效率更高，里面也判空了
    }
}
