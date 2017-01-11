package lab.zlren.taotao.controller;

import lab.zlren.taotao.bean.EasyUIResult;
import lab.zlren.taotao.service.NewUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by zlren on 2017/1/10.
 */
@Controller
@RequestMapping("user")
public class UserController {

    @Autowired
    private NewUserService userService;

    @RequestMapping(value = "list", method = RequestMethod.GET)
    @ResponseBody
    public EasyUIResult queryUserList(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                      @RequestParam(value = "rows", defaultValue = "5") Integer rows) {
        return this.userService.queryUserList(page, rows);
    }
}
