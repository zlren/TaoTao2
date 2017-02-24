package lab.zlren.taotao.order.mapper;

import lab.zlren.taotao.order.pojo.Order;
import org.apache.ibatis.annotations.Param;

import java.util.Date;


public interface OrderMapper extends IMapper<Order>{
	
	public void paymentOrderScan(@Param("date") Date date);

}
