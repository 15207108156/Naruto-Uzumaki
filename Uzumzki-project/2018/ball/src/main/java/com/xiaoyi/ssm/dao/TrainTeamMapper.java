package com.xiaoyi.ssm.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xiaoyi.ssm.model.TrainTeam;
import com.xiaoyi.ssm.model.TrainTeamVenue;

public interface TrainTeamMapper extends BaseMapper<TrainTeam, String>{

	/**  
	 * @Description: 查询附近的
	 * @author 宋高俊  
	 * @param lng
	 * @param lat
	 * @return 
	 * @date 2018年9月29日 下午8:20:12 
	 */ 
	List<TrainTeam> selectByNearby(@Param("lng")Double lng, @Param("lat")Double lat);

	/**  
	 * @Description: 根据教练查询所属培训机构
	 * @author 宋高俊  
	 * @param coachid
	 * @return 
	 * @date 2018年9月30日 下午5:16:51 
	 */ 
	TrainTeam selectByCoach(String coachid);

	/**  
	 * @Description: 根据培训机构ID统计使用场地数据
	 * @author 宋高俊  
	 * @param id
	 * @return 
	 * @date 2018年10月10日 下午5:14:49 
	 */ 
	int countByTeamId(String id);
	
	/**  
	 * @Description: 后台查询所有培训机构
	 * @author 宋高俊  
	 * @return 
	 * @date 2018年10月11日 下午8:24:50 
	 */ 
	List<TrainTeam> selectAllAdmin();

	/**  
	 * @Description: 根据场馆查询驻场的培训机构
	 * @author 宋高俊  
	 * @param id
	 * @return 
	 * @date 2018年10月18日 下午4:08:07 
	 */ 
	List<TrainTeam> selectByVenue(String id);

	/**  
	 * @Description: 根据经纬度筛选培训机构
	 * @author 宋高俊  
	 * @param begLng
	 * @param endLng
	 * @param begLat
	 * @param endLat
	 * @return 
	 * @date 2018年10月18日 下午7:37:16 
	 */ 
	List<TrainTeam> selectByNearbyMapTrainTeam(@Param("begLng")double begLng, @Param("endLng")double endLng, @Param("begLat")double begLat, @Param("endLat")double endLat);

	/**  
	 * @Description: 新增培训机构使用场地
	 * @author 宋高俊  
	 * @param trainTeamVenue
	 * @return 
	 * @date 2018年10月19日 上午9:38:56 
	 */ 
	int saveTrainTeamVenue(TrainTeamVenue trainTeamVenue);
}