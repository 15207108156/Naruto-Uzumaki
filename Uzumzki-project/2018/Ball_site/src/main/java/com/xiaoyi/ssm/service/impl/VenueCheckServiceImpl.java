package com.xiaoyi.ssm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiaoyi.ssm.dao.VenueCheckMapper;
import com.xiaoyi.ssm.model.VenueCheck;
import com.xiaoyi.ssm.service.VenueCheckService;

/**  
 * @Description: 场馆审核业务逻辑实现
 * @author 宋高俊  
 * @date 2018年10月15日 下午5:42:00 
 */ 
@Service
public class VenueCheckServiceImpl extends AbstractService<VenueCheck,String> implements VenueCheckService{

	@Autowired
	private VenueCheckMapper venueCheckMapper;
	
	@Override
	public void setBaseMapper() {
	    super.setBaseMapper(venueCheckMapper);
	}

	/**  
	 * @Description: 查询所有待审核的场馆
	 * @author 宋高俊  
	 * @return 
	 * @date 2018年10月15日 下午5:47:55 
	 */ 
	@Override
	public List<VenueCheck> selectByCheck() {
		return venueCheckMapper.selectByCheck();
	}

	/**  
	 * @Description: 根据培训机构查询需要审核的场馆
	 * @author 宋高俊  
	 * @param trainTeamId
	 * @return 
	 * @date 2018年10月17日 上午10:40:09 
	 */ 
	@Override
	public List<VenueCheck> selectByTrainTeamID(String trainTeamId) {
		return venueCheckMapper.selectByTrainTeamID(trainTeamId);
	}

	/**  
	 * @Description: 统计需要审核的场馆
	 * @author 宋高俊  
	 * @return 
	 * @date 2018年10月18日 下午2:23:40 
	 */
	@Override
	public Integer countCheck() {
		return venueCheckMapper.countCheck();
	}
	
}
