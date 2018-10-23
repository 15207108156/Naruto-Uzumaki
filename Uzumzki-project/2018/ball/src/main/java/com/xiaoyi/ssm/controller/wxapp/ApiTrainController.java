package com.xiaoyi.ssm.controller.wxapp;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.xiaoyi.ssm.dto.ApiMessage;
import com.xiaoyi.ssm.dto.PageBean;
import com.xiaoyi.ssm.model.City;
import com.xiaoyi.ssm.model.TrainCoach;
import com.xiaoyi.ssm.model.TrainCourse;
import com.xiaoyi.ssm.model.TrainTeam;
import com.xiaoyi.ssm.model.TrainTeamImage;
import com.xiaoyi.ssm.model.Venue;
import com.xiaoyi.ssm.service.CityService;
import com.xiaoyi.ssm.service.TrainCoachService;
import com.xiaoyi.ssm.service.TrainCourseService;
import com.xiaoyi.ssm.service.TrainOrderCommentService;
import com.xiaoyi.ssm.service.TrainTeamImageService;
import com.xiaoyi.ssm.service.TrainTeamService;
import com.xiaoyi.ssm.service.VenueService;
import com.xiaoyi.ssm.util.DateUtil;
import com.xiaoyi.ssm.util.MapUtils;

/**
 * @Description: 培训公共接口控制器
 * @author 宋高俊
 * @date 2018年9月29日 下午7:01:57
 */
@Controller("wxappTrainController")
@RequestMapping("wxapp/train/common")
public class ApiTrainController {

	@Autowired
	private TrainTeamService trainTeamService;
	@Autowired
	private TrainCoachService trainCoachService;
	@Autowired
	private TrainCourseService trainCourseService;
	@Autowired
	private TrainTeamImageService trainTeamImageService;
	@Autowired
	private VenueService venueService;
	@Autowired
	private TrainOrderCommentService trainOrderCommentService;
	@Autowired
	private CityService cityService;

	/**
	 * @Description: 获取培训机构列表数据
	 * @author 宋高俊
	 * @return
	 * @date 2018年9月30日 上午11:13:14
	 */
	@RequestMapping(value = "/getTrainTeamList")
	@ResponseBody
	public ApiMessage getTrainTeamList(PageBean pageBean, HttpServletRequest request, Double lng, Double lat) {
		// 开始分页
		PageHelper.startPage(pageBean.getPageNum(), pageBean.getPageSize());
		List<TrainTeam> list = trainTeamService.selectByNearby(lng, lat);
		List<Map<String, Object>> listmap = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < list.size(); i++) {
			TrainTeam trainTeam = list.get(i);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", trainTeam.getId()); // ID
			map.put("headImage", trainTeam.getHeadImage()); // 封面图片
			map.put("title", trainTeam.getTitle()); // 培训机构名称
			map.put("venueNumber", trainTeam.getVenueNumber()); // 开课球馆数量
			map.put("area", (int) MapUtils.getDistance(lng, lat, trainTeam.getLongitude(), trainTeam.getLatitude())); // 距离
			map.put("freeParking", trainTeam.getFreeParking()); // 是否能免费停车
			map.put("freeClasses", trainTeam.getFreeClasses()); // 是否能免费试课
			map.put("teachClass", trainTeam.getTeachClass()); // 开设科目
			
			map.put("trainOrderCommentSum", trainOrderCommentService.countByTeamId(trainTeam.getId(), DateUtil.getPreTime(new Date(), 4, -2))); // 近期上课人数
			listmap.add(map);
		}
		return new ApiMessage(200, "获取成功", listmap);
	}

	/**
	 * @Description: 获取培训机构详情数据
	 * @author 宋高俊
	 * @return
	 * @date 2018年9月30日 上午11:13:14
	 */
	@RequestMapping(value = "/getTrainTeamDetails")
	@ResponseBody
	public ApiMessage getTrainTeamDetails(HttpServletRequest request, String id, Double lng, Double lat) {
		TrainTeam trainTeam = trainTeamService.selectByPrimaryKey(id);
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("id", trainTeam.getId()); // ID
		map.put("headImage", trainTeam.getHeadImage()); // 封面图片
		map.put("title", trainTeam.getTitle()); // 名称
		map.put("phone", trainTeam.getPhone()); // 电话
		map.put("brandContent", trainTeam.getBrandContent()); // 介绍

		JSONArray venueJsonArray = new JSONArray();
		List<Venue> listVenues = venueService.selectByTrainTeamID(id);
		for (int i = 0; i < listVenues.size(); i++) {
			Venue venue = listVenues.get(i);
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("lng", venue.getLongitude()); // 经度
			jsonObject.put("lat", venue.getLatitude()); // 纬度
			jsonObject.put("title", venue.getName()); // 场馆名
			jsonObject.put("area", (int) MapUtils.getDistance(lng, lat, trainTeam.getLongitude(), trainTeam.getLatitude())); // 距离
			jsonObject.put("address", venue.getAddress()); // 详细地址
			venueJsonArray.add(jsonObject);
		}
		map.put("trainVenues", venueJsonArray); // 地址集合

		JSONArray coachJsonArray = new JSONArray();
		List<TrainCoach> listCoachs = trainCoachService.selectByTrainTeamID(id);
		for (int i = 0; i < listCoachs.size(); i++) {
			TrainCoach trainCoach = listCoachs.get(i);
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("id", trainCoach.getId()); // id
			jsonObject.put("headImage", trainCoach.getHeadImage()); // 头像
			jsonObject.put("name", trainCoach.getName()); // 姓名
			jsonObject.put("workingAge", trainCoach.getWorkingAge()); // 教龄
			coachJsonArray.add(jsonObject);
		}
		map.put("trainCoachs", coachJsonArray); // 教练集合

		JSONArray classJsonArray = new JSONArray();
		List<TrainCourse> listCourses = trainCourseService.selectByTrainTeamID(id);
		for (int i = 0; i < listCourses.size(); i++) {
			TrainCourse trainCourse = listCourses.get(i);
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("id", trainCourse.getId()); // ID
			jsonObject.put("headImage", trainCourse.getHeadImage()); // 封面
			jsonObject.put("title", trainCourse.getTitle()); // 标题
			jsonObject.put("amount", trainCourse.getAmount()); // 价格
			jsonObject.put("minAge", trainCourse.getMinAge() + "~" + trainCourse.getMaxAge() + "岁"); // 适合年龄
			jsonObject.put("classHour", trainCourse.getClassHour() + "课时"); // 价格
			jsonObject.put("toTeachNumber", trainCourse.getToTeachNumber() + "人以下"); // 价格
			classJsonArray.add(jsonObject);
		}
		map.put("trainCourses", classJsonArray); // 课程集合
		return new ApiMessage(200, "获取成功", map);
	}

	/**
	 * @Description: 获取培训机构介绍数据
	 * @author 宋高俊
	 * @param request
	 * @param id
	 * @return
	 * @date 2018年9月30日 上午11:13:14
	 */
	@RequestMapping(value = "/trainTeam/details")
	@ResponseBody
	public ApiMessage trainTeamdetails(HttpServletRequest request, String id) {
		TrainTeam trainTeam = trainTeamService.selectByPrimaryKey(id);
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("id", trainTeam.getId()); // ID
		map.put("headImage", trainTeam.getHeadImage()); // 封面图片
		map.put("title", trainTeam.getTitle()); // 名称
		map.put("brandContent", trainTeam.getBrandContent()); // 品牌故事
		map.put("trait", trainTeam.getTrait().split(";"));// 办学特色

		List<String> list = new ArrayList<String>();
		List<TrainTeamImage> listTeamImages = trainTeamImageService.selectByTrainTeamID(id);
		for (int i = 0; i < listTeamImages.size(); i++) {
			list.add(listTeamImages.get(i).getPath());
		}
		map.put("trainImages", list); // 教学环境集合
		return new ApiMessage(200, "获取成功", map);
	}

	/**
	 * @Description: 获取培训机构全部教练数据
	 * @author 宋高俊
	 * @return
	 * @date 2018年9月30日 上午11:13:14
	 */
	@RequestMapping(value = "/trainTeam/coach/list")
	@ResponseBody
	public ApiMessage trainTeamcoachlist(HttpServletRequest request, String id) {

		List<TrainCoach> listTrainCoachs = trainCoachService.selectByTrainTeamID(id);
		List<Map<String, Object>> listmap = new ArrayList<Map<String, Object>>();

		for (int i = 0; i < listTrainCoachs.size(); i++) {
			Map<String, Object> map = new LinkedHashMap<String, Object>();
			map.put("id", listTrainCoachs.get(i).getId()); // ID
			map.put("headImage", listTrainCoachs.get(i).getHeadImage()); // 头像
			map.put("name", listTrainCoachs.get(i).getName()); // 姓名
			map.put("type", listTrainCoachs.get(i).getType() == 1 ? "主教" : "助教"); // 类型
			map.put("workingAge", listTrainCoachs.get(i).getWorkingAge()); // 教龄
			listmap.add(map);
		}
		return new ApiMessage(200, "获取成功", listmap);
	}

	/**
	 * @Description: 获取培训机构教练详细数据
	 * @author 宋高俊
	 * @return
	 * @date 2018年9月30日 上午11:13:14
	 */
	@RequestMapping(value = "/trainTeam/coach/details")
	@ResponseBody
	public ApiMessage trainTeamcoachdetails(HttpServletRequest request, String id) {

		TrainCoach trainCoach = trainCoachService.selectByPrimaryKey(id);
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("id", trainCoach.getId()); // ID
		map.put("headImage", trainCoach.getHeadImage()); // 头像
		map.put("name", trainCoach.getName()); // 姓名
		map.put("type", trainCoach.getType() == 1 ? "主教" : "助教"); // 类型
		map.put("workingAge", trainCoach.getWorkingAge()); // 教龄
		map.put("teachClass", trainCoach.getTeachClass()); // 所授课程
		map.put("lectureStyle", trainCoach.getLectureStyle()); // 讲课风格
		map.put("vitae", trainCoach.getVitae()); // 个人简介

		map.put("education", trainCoach.getEducation()); // 教育背景
		map.put("honor", trainCoach.getHonor()); // 相关经历

		List<TrainCourse> listTrainCoachs = trainCourseService.selectByTrainCoachID(id);
		List<Map<String, Object>> listmap = new ArrayList<Map<String, Object>>();

		for (int i = 0; i < listTrainCoachs.size(); i++) {
			Map<String, Object> courseMap = new LinkedHashMap<String, Object>();
			courseMap.put("id", listTrainCoachs.get(i).getId()); // ID
			courseMap.put("headImage", listTrainCoachs.get(i).getHeadImage()); // 封面
			courseMap.put("title", listTrainCoachs.get(i).getTitle()); // 标题
			courseMap.put("amount", listTrainCoachs.get(i).getAmount()); // 价格
			courseMap.put("minAge", listTrainCoachs.get(i).getMinAge() + "~" + listTrainCoachs.get(i).getMaxAge() + "岁"); // 适合年龄
			courseMap.put("classHour", listTrainCoachs.get(i).getClassHour() + "课时"); // 价格
			courseMap.put("toTeachNumber", listTrainCoachs.get(i).getToTeachNumber() + "人以下"); // 适合人数
			listmap.add(courseMap);
		}

		map.put("courses", listmap); // 开设课程
		return new ApiMessage(200, "获取成功", map);
	}
	
		
	/**
	 * @Description: 获取培训机构全部课程数据
	 * @author 宋高俊
	 * @return
	 * @date 2018年9月30日 上午11:13:14
	 */
	@RequestMapping(value = "/trainTeam/course/list")
	@ResponseBody
	public ApiMessage trainTeamcourselist(HttpServletRequest request, String id) {

		List<TrainCourse> listTrainCoachs = trainCourseService.selectByTrainTeamID(id);
		List<Map<String, Object>> listmap = new ArrayList<Map<String, Object>>();

		for (int i = 0; i < listTrainCoachs.size(); i++) {
			Map<String, Object> map = new LinkedHashMap<String, Object>();
			map.put("id", listTrainCoachs.get(i).getId()); // ID
			map.put("headImage", listTrainCoachs.get(i).getHeadImage()); // 封面
			map.put("title", listTrainCoachs.get(i).getTitle()); // 标题
			map.put("amount", listTrainCoachs.get(i).getAmount()); // 价格
			map.put("minAge", listTrainCoachs.get(i).getMinAge() + "~" + listTrainCoachs.get(i).getMaxAge() + "岁"); // 适合年龄
			map.put("classHour", listTrainCoachs.get(i).getClassHour() + "课时"); // 价格
			map.put("toTeachNumber", listTrainCoachs.get(i).getToTeachNumber() + "人以下"); // 适合人数
			listmap.add(map);
		}
		return new ApiMessage(200, "获取成功", listmap);
	}

	/**
	 * @Description: 获取培训机构课程详细数据
	 * @author 宋高俊
	 * @return
	 * @date 2018年9月30日 上午11:13:14
	 */
	@RequestMapping(value = "/trainTeam/course/details")
	@ResponseBody
	public ApiMessage trainTeamcoursedetails(HttpServletRequest request, String id) {

		TrainCourse trainCourse = trainCourseService.selectByPrimaryKey(id);
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("id", trainCourse.getId()); // ID
		map.put("headImage", trainCourse.getHeadImage()); // 封面
		map.put("title", trainCourse.getTitle()); // 标题
		map.put("amount", trainCourse.getAmount()); // 价格
		map.put("minAge", trainCourse.getMinAge() + "~" + trainCourse.getMaxAge() + "岁"); // 适合年龄
		map.put("classHour", trainCourse.getClassHour() + "课时"); // 价格
		map.put("toTeachNumber", trainCourse.getToTeachNumber() + "人以下"); // 适合人数
		
		map.put("toTeachTime", trainCourse.getToTeachTime()); // 上课时间
		map.put("classTrait", trainCourse.getClassTrait().split(";")); // 课程亮点
		
		TrainCoach trainCoach = trainCoachService.selectByPrimaryKey(trainCourse.getTrainCoachId());
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", trainCoach.getId()); // ID
		jsonObject.put("headImage", trainCoach.getHeadImage()); // 头像
		jsonObject.put("name", trainCoach.getName()); // 姓名
		jsonObject.put("type", trainCoach.getType() == 1 ? "主教" : "助教"); // 类型
		jsonObject.put("workingAge", trainCoach.getWorkingAge()); // 教龄
		map.put("trainCoach", jsonObject); // 授课教练

		map.put("payMake", trainCourse.getPayMake()); // 预约信息
		map.put("payAdjust", trainCourse.getPayAdjust()); // 调课说明
		map.put("payRefund", trainCourse.getPayRefund()); // 退款说明
		map.put("applyFlag", trainCourse.getApplyFlag()); // 是否允许报名
		
		return new ApiMessage(200, "获取成功", map);
	}

	/**  
	 * @Description: 根据经纬度获取培训机构
	 * @author 宋高俊  
	 * @param request
	 * @param begLng 开始经度
	 * @param endLng 结束经度
	 * @param begLat 开始纬度
	 * @param endLat 结束纬度
	 * @return 
	 * @date 2018年10月18日 下午6:00:40 
	 */ 
	@RequestMapping(value = "/trainTeam/getMapTrainTeam")
	@ResponseBody
	public ApiMessage getMapTrainTeam(HttpServletRequest request, double begLng, double endLng, double begLat, double endLat) {

		List<TrainTeam> list = trainTeamService.selectByNearbyMapTrainTeam(begLng, endLng, begLat, endLat);
		List<Map<String, Object>> listmap = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < list.size(); i++) {
			TrainTeam trainTeam = list.get(i);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", trainTeam.getId()); // ID
			map.put("title", trainTeam.getTitle()); // 培训机构名称
			map.put("lng", trainTeam.getLongitude()); // 经度
			map.put("lat", trainTeam.getLatitude()); // 纬度
			listmap.add(map);
		}
		return new ApiMessage(200, "获取成功", listmap);
	}

	/**  
	 * @Description: 根据城市名称查询获取是否显示地图 
	 * @author 宋高俊  
	 * @param request
	 * @param name
	 * @return 
	 * @date 2018年10月18日 下午7:49:43 
	 */ 
	@RequestMapping(value = "/trainTeam/getCity")
	@ResponseBody
	public ApiMessage trainTeamGetCity(HttpServletRequest request, String name) {
		City city = cityService.selectByName(name);
		return new ApiMessage(200, "获取成功", city.getMapflag());
	}
	
}
