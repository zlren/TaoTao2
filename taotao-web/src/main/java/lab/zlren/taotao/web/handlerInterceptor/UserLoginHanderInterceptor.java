package lab.zlren.taotao.web.handlerInterceptor;

import lab.zlren.taotao.common.utils.CookieUtils;
import lab.zlren.taotao.web.bean.User;
import lab.zlren.taotao.web.service.UserService;
import lab.zlren.taotao.web.threadlocal.UserThreadLocal;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * UserLoginHanderInterceptor
 * Created by zlren on 2017/2/24.
 */
public class UserLoginHanderInterceptor implements HandlerInterceptor {

    public static final String COOKIE_NAME = "TT_TOKEN";
    public static final String LOGIN_URL = "http://sso.taotao.com/user/login.html";

    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object
            o) throws Exception {

        // 清空当前线程中的ThreadLocal
        UserThreadLocal.set(null);

        String token = CookieUtils.getCookieValue(httpServletRequest, COOKIE_NAME);
        if (StringUtils.isEmpty(token)) {
            // 未登陆状态
            httpServletResponse.sendRedirect(LOGIN_URL);
            return false;
        }

        User user = this.userService.queryUserByToken(token);
        if (null == user) {
            httpServletResponse.sendRedirect(LOGIN_URL);
            return false;
        }

        // user对象放入当前线程中的ThreadLocal
        UserThreadLocal.set(user);

        // 处于登陆状态
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o,
                           ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                Object o, Exception e) throws Exception {

    }
}
