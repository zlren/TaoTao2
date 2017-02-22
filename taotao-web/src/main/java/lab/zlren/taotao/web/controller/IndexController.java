package lab.zlren.taotao.web.controller;

import lab.zlren.taotao.web.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * IndexController
 * Created by zlren on 17/2/16.
 */

@Controller
public class IndexController {

    @Autowired
    private IndexService indexService;

    /**
     * 跳转到首页
     *
     * @return
     */
    @RequestMapping(value = "index", method = RequestMethod.GET)
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("index");

        // 大广告位数据
        String indexAD1 = this.indexService.queryIndexAD1();
        modelAndView.addObject("indexAD1", indexAD1);

        // 右上角小广告
        String indexAD2 = this.indexService.queryIndexAD2();
        modelAndView.addObject("indexAD2", indexAD2);

        return modelAndView;
    }
}
