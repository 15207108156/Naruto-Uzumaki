package com.xiaoyi.ssm.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xiaoyi.ssm.model.VenueRefund;

public interface VenueRefundMapper extends BaseMapper<VenueRefund, String>{

	/**  
	 * @Description: 查询是否有申请过
	 * @author 宋高俊
	 * @param orderid
	 * @return
	 * @date 2018年11月16日 下午5:24:59
	 */
	VenueRefund selectByOrder(String orderid);

	/**
	 * @Description: 查询场馆的所有申请
	 * @author 宋高俊
	 * @param venueid
	 * @return
	 * @date 2018年11月16日 下午7:37:42
	 */
	List<VenueRefund> selectByVenue(@Param("venueid")String venueid, @Param("dateStart")Date dateStart, @Param("dateEnd")Date dateEnd);
	
	/**
	 * @Description: 统计申请数量
	 * @author 宋高俊
	 * @param venueid
	 * @return
	 * @date 2018年11月16日 下午8:18:15
	 */
	Integer countByVenue(String venueid);
}