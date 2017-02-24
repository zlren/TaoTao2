package lab.zlren.taotao.search.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by zlren on 2017/2/24.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchResult {
    private Long total;
    private List<?> data;
}
