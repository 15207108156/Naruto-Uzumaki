package com.xiaoyi.ssm.service;

import java.util.List;

import com.xiaoyi.ssm.model.VenueCheck;

/**  
 * @Description: 场馆审核业务逻辑层
 * @author 宋高俊  
 * @date 2018年10月15日 下午5:40:45 
 */ 
public interface VenueCheckService extends BaseService<VenueCheck, String> {

	/**  
	 * @Description: 查询所有待审核的场馆
	 * @author 宋高俊  
	 * @return 
	 * @date 2018年10月15日 下午5:47:55 
	 */ 
	List<VenueCheck> selectByCheck();

	/**  
	 * @Description: 根据培训机构查询需要审核的场馆
	 * @author 宋高俊  
	 * @param trainTeamId
	 * @return 
	 * @date 2018年10月17日 上午10:40:09 
	 */ 
	List<VenueCheck> selectByTrainTeamID(String trainTeamId);

	/**  
	 * @Description: 统计需要审核的场馆
	 * @author 宋高俊  
	 * @return 
	 * @date 2018年10月18日 下午2:23:40 
	 */
	Integer countCheck();

}
