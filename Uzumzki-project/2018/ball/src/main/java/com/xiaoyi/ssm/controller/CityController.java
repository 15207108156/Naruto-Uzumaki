package com.xiaoyi.ssm.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xiaoyi.ssm.dao.CityLogMapper;
import com.xiaoyi.ssm.dto.AdminMessage;
import com.xiaoyi.ssm.dto.AdminPage;
import com.xiaoyi.ssm.model.City;
import com.xiaoyi.ssm.model.CityLog;
import com.xiaoyi.ssm.model.District;
import com.xiaoyi.ssm.service.CityService;
import com.xiaoyi.ssm.service.DistrictService;

/**  
 * @Description: 城市控制器
 * @author 宋高俊  
 * @date 2018年9月21日 下午3:55:30 
 */ 
@Controller(value = "adminCityController")
@RequestMapping(value = "/admin/city")
public class CityController {
	
	private final Logger logger = Logger.getLogger(this.getClass());

	@Autowired
	private CityService cityService;
	@Autowired
	private DistrictService districtService;
	@Autowired
	private CityLogMapper cityLogMapper;

	/**
	 * @Description: 城市页面
	 * @author 宋高俊  
	 * @date 2018年9月21日 下午3:55:30 
	 */
	@RequestMapping(value = "/listview")
	public String listview() {
		return "admin/city/list";
	}

	/**
	 * @Description: 城市数据
	 * @author 宋高俊  
	 * @date 2018年9月21日 下午3:55:30 
	 */
	@RequestMapping(value = "/list")
	@ResponseBody
	public AdminMessage list(AdminPage adminPage) {
		
		PageHelper.startPage(adminPage.getPage(), adminPage.getLimit());
		
		List<City> list = cityService.selectByAll(null);
		
		PageInfo<City> pageInfo = new PageInfo<>(list);
		List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < list.size(); i++) {
			City city = list.get(i);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", city.getId());// ID
			map.put("cityno", city.getCityno());// 编号
			map.put("initial", city.getInitial());// 字母
			map.put("hotflag", city.getHotflag() == 0 ? "否" : "是");// 热门
			map.put("city", city.getCity());// 城市
			
			List<District> list2 = districtService.selectByCityId(city.getId());
			String distinctString = "";
			for (District distinct : list2) {
				distinctString += distinct.getDistrict() + ",";
			}
			if (distinctString.length() > 0) {
				distinctString = distinctString.substring(0, distinctString.length()-1);
			}
			map.put("distinct", distinctString);// 下属区县
			map.put("venuesum", city.getVenuesum());// 场馆数
			
			Integer cityLogSum = cityLogMapper.countByCity(city.getId());
			map.put("cityLogSum", cityLogSum);// 日志
			listMap.add(map);
		}
		return new AdminMessage(pageInfo.getTotal(), listMap);
	}

	/**  
	 * @Description: 城市日志数据
	 * @author 宋高俊  
	 * @param adminPage
	 * @return 
	 * @date 2018年9月21日 下午5:20:02 
	 */ 
	@RequestMapping(value = "/cityLog/list")
	@ResponseBody
	public AdminMessage cityLogList(AdminPage adminPage, String id) {
		
		List<CityLog> list = cityLogMapper.selectByCity(id);
		List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < list.size(); i++) {
			CityLog cityLog = list.get(i);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", cityLog.getId());// ID
			map.put("cityno", cityLog.getCreatetime());// 日志时间
			map.put("initial", cityLog.getStaff().getRname());// 操作人
			map.put("hotflag", cityLog.getContent());// 内容
			listMap.add(map);
		}
		return new AdminMessage(100, 0, listMap);
	}
	
}