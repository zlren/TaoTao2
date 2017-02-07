package lab.zlren.taotao.manage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**页面跳转controller
 * Created by zlren on 17/2/5.
 */
@RequestMapping("page")
@Controller
public class PageController {

    @RequestMapping(value = "{pageName}", method = RequestMethod.GET)
    public String toPage(@PathVariable("pageName") String pageName) {
        return pageName;
    }
}
