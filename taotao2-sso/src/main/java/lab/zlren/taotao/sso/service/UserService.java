package lab.zlren.taotao.sso.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lab.zlren.taotao.common.service.jedis.RedisService;
import lab.zlren.taotao.sso.mapper.UserMapper;
import lab.zlren.taotao.sso.pojo.User;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;

/**
 * UserService
 * Created by zlren on 2017/2/22.
 */
@Service
public class UserService {

    @Autowired
    private RedisService redisService;

    @Autowired
    private UserMapper userMapper;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public Boolean check(String param, Integer type) {
        if (type < 1 || type > 3) {
            return null;
        }

        User record = new User();
        switch (type) {
            case 1:
                record.setUsername(param);
                break;
            case 2:
                record.setPhone(param);
                break;
            case 3:
                record.setEmail(param);
                break;
        }

        return (this.userMapper.selectOne(record) == null);
    }

    /**
     * 注册用户保存
     *
     * @param user
     * @return
     */
    public Boolean saveUser(User user) {

        user.setId(null);
        user.setCreated(new Date());
        user.setUpdated(user.getCreated());

        // 密码加密md5
        user.setPassword(DigestUtils.md5Hex(user.getPassword()));


        return this.userMapper.insert(user) == 1;
    }


    public String doLogin(String username, String password) throws Exception {

        User record = new User();
        record.setUsername(username);

        User user = this.userMapper.selectOne(record);

        if (null == user) {
            return null;
        }

        if (!StringUtils.equals(DigestUtils.md5Hex(password), user.getPassword())) {
            return null;
        }

        // 登陆成功
        // 生成token
        String token = DigestUtils.md5Hex(System.currentTimeMillis() + username);

        // 将用户的数据保存到redis中，这里应该有个时效性
        // select TOKEN_* 便于后序的统计操作
        this.redisService.set("TOKEN_" + token, OBJECT_MAPPER.writeValueAsString(user));

        return token;

    }

    public User queryUserByToken(String token) {

        String key = "TOKEN_" + token;
        String jsonData = this.redisService.get(key);

        try {

            if (StringUtils.isEmpty(jsonData)) {
                return null;
            }

            // 根据key刷新用户的生存时间，保持登录
            // TODO: 2017/2/23

            return OBJECT_MAPPER.readValue(jsonData, User.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}