package lab.zlren.taotao.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by zlren on 2017/1/10.
 */
@Controller
@RequestMapping("user")
public class UserController {

    @RequestMapping(value = "users", method = RequestMethod.GET)
    public String toUsers() {
        return "users";
    }

}
