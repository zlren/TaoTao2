package lab.zlren.taotao.cart.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lab.zlren.taotao.cart.bean.User;
import lab.zlren.taotao.common.service.httpclient.ApiService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Created by zlren on 2017/2/24.
 */
@Service
public class UserService {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Autowired
    private ApiService apiService;

    public User queryUserByToken(String token) {
        try {
            String url = "http://sso.taotao.com/service/user/" + token;
            String jsonData = this.apiService.doGet(url);

            if (StringUtils.isNotEmpty(jsonData)) {
                return OBJECT_MAPPER.readValue(jsonData, User.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }
}
