package lab.zlren.taotao.cart.handlerInterceptor;

import lab.zlren.taotao.cart.bean.User;
import lab.zlren.taotao.cart.service.UserService;
import lab.zlren.taotao.cart.threadlocal.UserThreadLocal;
import lab.zlren.taotao.common.utils.CookieUtils;
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
public class UserHanderInterceptor implements HandlerInterceptor {

    public static final String COOKIE_NAME = "TT_TOKEN";

    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object
            o) throws Exception {

        // 清空当前线程中的ThreadLocal
        UserThreadLocal.set(null);

        String token = CookieUtils.getCookieValue(httpServletRequest, COOKIE_NAME);

        if (StringUtils.isEmpty(token)) {
            // 未登录状态方形
            return true;
        }

        User user = this.userService.queryUserByToken(token);
        if (null == user) {
            return true;
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
