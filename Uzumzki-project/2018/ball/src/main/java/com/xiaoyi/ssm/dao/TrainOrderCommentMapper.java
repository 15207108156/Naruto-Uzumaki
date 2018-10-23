package com.xiaoyi.ssm.dao;

import org.apache.ibatis.annotations.Param;

import com.xiaoyi.ssm.model.TrainOrderComment;

import java.util.Date;
import java.util.List;

public interface TrainOrderCommentMapper extends BaseMapper<TrainOrderComment, String>{
	/**  
	 * @Description: 根据课时和订单ID查询是否已经发送过评价邀请
	 * @author 宋高俊  
	 * @param hour 当前课时数
	 * @param id 订单ID
	 * @return 
	 * @date 2018年10月9日 下午2:45:02 
	 */ 
	TrainOrderComment selectByHourOrder(@Param("hour")int hour, @Param("id")String id);

	/**
	 * @Description: 根据订单ID获取所有评论
	 * @author 宋高俊
	 * @date 2018/10/10 0010 9:11
	 */
	List<TrainOrderComment> selectByOrder(String id);
	
	/**  
	 * @Description: 查询24小时前创建的评论
	 * @author 宋高俊  
	 * @param preTime
	 * @return 
	 * @date 2018年10月13日 上午9:31:28 
	 */ 
	List<TrainOrderComment> selectByTimeOut(Date preTime);
	
	/**  
	 * @Description: 根据培训机构ID统计60天的评论总数
	 * @author 宋高俊  
	 * @param id
	 * @param date
	 * @return 
	 * @date 2018年10月15日 上午9:58:33 
	 */ 
	Integer countByTeamId(@Param("id")String id, @Param("date")Date date);
}