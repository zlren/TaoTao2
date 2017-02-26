package lab.zlren.taotao.search.controller;

import lab.zlren.taotao.search.bean.SearchResult;
import lab.zlren.taotao.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by zlren on 2017/2/24.
 */
@Controller
public class SearchController {

    @Autowired
    private SearchService searchService;


    @RequestMapping(value = "search", method = RequestMethod.GET)
    public ModelAndView search(@RequestParam("q") String keyWords, @RequestParam(value = "page", defaultValue = "1")
            Integer page) {
        ModelAndView modelAndView = new ModelAndView("search");


        try {

            // 解决get请求中文乱码问题
            keyWords = new String(keyWords.getBytes("ISO-8859-1"), "UTF-8");

            // 搜索商品
            SearchResult searchResult = this.searchService.search(keyWords, page);

            modelAndView.addObject("query", keyWords); // 搜索关键字
            modelAndView.addObject("itemList", searchResult.getData()); // 商品列表
            modelAndView.addObject("page", page); // 页数
            int total = searchResult.getTotal().intValue();
            int pages = total % SearchService.ROWS == 0 ? total % SearchService.ROWS : total % SearchService.ROWS + 1;
            modelAndView.addObject("pages", pages); // 页数
        } catch (Exception e) {
            e.printStackTrace();
        }
        return modelAndView;
    }
}
