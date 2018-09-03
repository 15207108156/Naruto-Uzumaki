/*
 * 版权所有：深圳源中瑞科技有限公司<br>
 * 网 址：www.ruiec.com<br>
 * 电 话：0755-33581131<br><br>
 */
package com.ruiec.web.service;

import java.util.List;
import java.util.Map;

import com.ruiec.framework.server.support.service.BaseService;
import com.ruiec.web.entity.DynamicInfo;

/**
 * 轨迹数据服务接口
 * 
 * @author bingo<br>
 * @date 2017年12月23日 上午9:17:17
 */
public interface DynamicInfoService extends BaseService<DynamicInfo, String> {

	
	/**
	 * 根据重点人id统计每种轨迹数据的数量
	 * @author Senghor<br>
	 * @date 2017年12月30日 下午5:03:01
	 */
	List<Map<String, Object>> dynamicInfoCount(Integer personId);
	
	/**
	 * 根据重点人id和轨迹类型id统计数量
	 * @author Senghor<br>
	 * @date 2018年1月18日 上午9:51:54
	 */
	public Map<String, Object> findTypeCount(Integer personId, Integer typeId);
	
	/**
	 * 根据布控人id统计每种轨迹数据的数量
	 * @author 陈靖原<br>
	 * @date 2018年1月11日 下午8:30:58
	 */
	List<Map<String, Object>> dynamicInfoCountForMonitor(Integer personId);
}