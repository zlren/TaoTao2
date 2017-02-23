package lab.zlren.taotao.sso.controller;

import lab.zlren.taotao.common.utils.CookieUtils;
import lab.zlren.taotao.sso.pojo.User;
import lab.zlren.taotao.sso.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * UserController
 * Created by zlren on 2017/2/22.
 */
@Controller
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    public static final String COOKIE_NAME = "TT_TOKEN";

    @RequestMapping(value = "register", method = RequestMethod.GET)
    public String toRegister() {
        return "register";
    }

    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String toLogin() {
        return "login";
    }


    @RequestMapping(value = "check/{param}/{type}", method = RequestMethod.GET)
    public ResponseEntity<Boolean> check(
            @PathVariable("param") String param,
            @PathVariable("type") Integer type) {

        try {
            Boolean bool = this.userService.check(param, type);

            if (null == bool) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }

            return ResponseEntity.ok(!bool); // 这个逻辑是反的
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);

    }


    /**
     * 注册，SpringMVC的数据校验
     *
     * @param user
     * @return
     */
    @RequestMapping(value = "doRegister", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> doRegister(@Valid User user, BindingResult bindingResult) {

        Map<String, Object> result = new HashMap<String, Object>();

        if (bindingResult.hasErrors()) {

            List<ObjectError> allErrors = bindingResult.getAllErrors();
            List<String> msgs = new ArrayList<>();
            for (ObjectError objectError : allErrors) {
                String defaultMessage = objectError.getDefaultMessage();
                msgs.add(defaultMessage);
            }

            result.put("status", "500");
            result.put("data", StringUtils.join(msgs, "|"));
            return result;
        }


        Boolean bool = this.userService.saveUser(user);
        if (bool) {
            // 注册成功
            result.put("status", "200");
        } else {
            result.put("status", "500");
            result.put("data", "是的!");
        }

        return result;
    }


    @RequestMapping(value = "doLogin", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> doLogin(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            HttpServletRequest request,
            HttpServletResponse response) {


        Map<String, Object> result = new HashMap<String, Object>();

        try {
            String token = this.userService.doLogin(username, password);

            if (null == token) {
                // 失败
                result.put("status", 400); // 非200
            } else {
                // 登陆成功，将token写入到cookie中
                result.put("status", 200);
                CookieUtils.setCookie(request, response, COOKIE_NAME, token);

            }
        } catch (Exception e) {
            e.printStackTrace();
            // 失败
            result.put("status", 400); // 非200
        }
        return result;
    }


    @RequestMapping(value = {"token"}, method = RequestMethod.GET)
    public ResponseEntity<User> queryUserByToken(@PathVariable("token") String token) {
        try {
            User user = this.userService.queryUserByToken(token);
            if (null == user) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}
