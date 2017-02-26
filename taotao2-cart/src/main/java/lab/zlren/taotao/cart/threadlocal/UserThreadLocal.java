package lab.zlren.taotao.cart.threadlocal;


import lab.zlren.taotao.cart.bean.User;

/**
 * UserThreadLocal
 * Created by zlren on 2017/2/24.
 */
public class UserThreadLocal {

    private static final ThreadLocal<User> LOCAL = new ThreadLocal<>();

    public static void set(User user) {
        LOCAL.set(user);
    }

    public static User get() {
        return LOCAL.get();
    }
}
