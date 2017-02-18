package lab.zlren.taotao.manage.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import lab.zlren.taotao.common.bean.ItemCatResult;
import lab.zlren.taotao.manage.service.ItemCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * ApiItemCatController
 * Created by zlren on 17/2/17.
 */

@Controller
@RequestMapping("api/item/cat")
public class ApiItemCatController {

    @Autowired
    private ItemCatService itemCatService;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * 对外提供接口，查询商品类目数据
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<ItemCatResult> queryItemCat(@RequestParam("callback") String callback) {
        try {
            ItemCatResult itemCatResult = this.itemCatService.queryAllToTree();
            return ResponseEntity.ok(itemCatResult);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
    // @RequestMapping(method = RequestMethod.GET)
    // public ResponseEntity<String> queryItemCat(@RequestParam("callback") String callback) {
    //     try {
    //         ItemCatResult itemCatResult = this.itemCatService.queryAllToTree();
    //         String json = OBJECT_MAPPER.writeValueAsString(itemCatResult);
    //         if (StringUtils.isEmpty(callback)) {
    //             return ResponseEntity.ok(json);
    //         }
    //         return ResponseEntity.ok(callback + "(" + json + ");");
    //
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     }
    //
    //     return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    // }
}
