package com.xiaoyi.ssm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiaoyi.ssm.dao.TrainEnterMapper;
import com.xiaoyi.ssm.model.TrainEnter;
import com.xiaoyi.ssm.service.TrainEnterService;

/**  
 * @Description: 培训机构入驻申请业务逻辑实现
 * @author 宋高俊  
 * @date 2018年10月17日 下午3:34:24 
 */ 
@Service
public class TrainEnterServiceImpl extends AbstractService<TrainEnter,String> implements TrainEnterService{

	@Autowired
	private TrainEnterMapper trainEnterMapper;
	
	@Override
	public void setBaseMapper() {
	    super.setBaseMapper(trainEnterMapper);
	}

	/**  
	 * @Description: 根据审核状态查询入驻申请
	 * @author 宋高俊  
	 * @param checkFlag
	 * @return 
	 * @date 2018年10月17日 下午5:33:32 
	 */ 
	@Override
	public List<TrainEnter> selectByEnterAll(Integer checkFlag) {
		return trainEnterMapper.selectByEnterAll(checkFlag);
	}

	/**  
	 * @Description: 统计未处理的培训机构申请数据
	 * @author 宋高俊  
	 * @return 
	 * @date 2018年10月18日 下午2:10:31 
	 */ 
	@Override
	public Integer countEnter() {
		return trainEnterMapper.countEnter();
	}
	
}
