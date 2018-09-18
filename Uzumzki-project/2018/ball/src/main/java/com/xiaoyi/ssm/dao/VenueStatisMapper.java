package com.xiaoyi.ssm.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xiaoyi.ssm.model.VenueStatis;

public interface VenueStatisMapper extends BaseMapper<VenueStatis, String> {

	/**
	 * @Description: 根据场馆ID获取日统计数据
	 * @author 宋高俊
	 * @param id
	 * @return
	 * @date 2018年9月7日 上午9:44:00
	 */
	List<VenueStatis> selectByVenue(@Param("venueid") String venueid, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

}