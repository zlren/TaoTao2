package lab.zlren.taotao.manage.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * PropertieService 这个service可以利用@value属性注入env.properties的值，controller再注入此service即可
 * 解决了controller无法通过@value注解注入父容器properties文件值得情况
 * Created by zlren on 17/2/15.
 */
@Service
public class PropertieService {

    @Value("${REPOSITORY_PATH}")
    public String REPOSITORY_PATH;

    @Value("${IMAGE_BASE_URL}")
    public String IMAGE_BASE_URL;
}
