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

import com.xiaoyi.ssm.dto.ApiMessage;
import com.xiaoyi.ssm.model.Member;
import com.xiaoyi.ssm.model.TrainCoach;
import com.xiaoyi.ssm.model.TrainEnter;
import com.xiaoyi.ssm.model.TrainTeam;
import com.xiaoyi.ssm.model.TrainTeamFeedback;
import com.xiaoyi.ssm.model.TrainTeamImage;
import com.xiaoyi.ssm.model.TrainTeamPhone;
import com.xiaoyi.ssm.model.Venue;
import com.xiaoyi.ssm.model.VenueCheck;
import com.xiaoyi.ssm.model.VenueEnter;
import com.xiaoyi.ssm.service.TrainCoachService;
import com.xiaoyi.ssm.service.TrainCourseService;
import com.xiaoyi.ssm.service.TrainEnterService;
import com.xiaoyi.ssm.service.TrainOrderCommentService;
import com.xiaoyi.ssm.service.TrainOrderService;
import com.xiaoyi.ssm.service.TrainTeamFeedbackService;
import com.xiaoyi.ssm.service.TrainTeamImageService;
import com.xiaoyi.ssm.service.TrainTeamPhoneService;
import com.xiaoyi.ssm.service.TrainTeamService;
import com.xiaoyi.ssm.service.TrainTrialService;
import com.xiaoyi.ssm.service.VenueCheckService;
import com.xiaoyi.ssm.service.VenueEnterService;
import com.xiaoyi.ssm.service.VenueService;
import com.xiaoyi.ssm.util.DateUtil;
import com.xiaoyi.ssm.util.Global;
import com.xiaoyi.ssm.util.MapUtils;
import com.xiaoyi.ssm.util.RedisUtil;
import com.xiaoyi.ssm.util.Utils;

/**
 * @Description: 培训结构接口控制器
 * @author 宋高俊
 * @date 2018年9月29日 下午7:01:57
 */
@Controller("wxappTrainTeamController")
@RequestMapping("wxapp/train/team")
public class ApiTrainTeamController {

	@Autowired
	private TrainTeamService trainTeamService;
	@Autowired
	private VenueService venueService;
	@Autowired
	private TrainCoachService trainCoachService;
	@Autowired
	private TrainCourseService trainCourseService;
	@Autowired
	private TrainTeamImageService trainTeamImageService;
	@Autowired
	private TrainOrderService trainOrderService;
	@Autowired
	private TrainTrialService trainTrialService;
	@Autowired
	private TrainTeamFeedbackService trainTeamFeedbackService;
	@Autowired
	private TrainTeamPhoneService trainTeamPhoneService;
	@Autowired
	private VenueCheckService venueCheckService;
	@Autowired
	private TrainEnterService trainEnterService;
	@Autowired
	private VenueEnterService venueEnterService;
	@Autowired
	private TrainOrderCommentService trainOrderCommentService;

	/**
	 * @Description: 获取培训机构主页面数据
	 * @author 宋高俊
	 * @param request
	 * @return
	 * @date 2018年10月8日 下午2:48:07
	 */
	@RequestMapping(value = "/manager/getMyTrainTeam/details")
	@ResponseBody
	public ApiMessage getMyCoachDetails(HttpServletRequest request) {
		// 用户数据
		String token = (String) request.getAttribute("token");
		Member member = (Member) RedisUtil.getRedisOne(Global.redis_member, token);
		// 根据用户ID查询教练数据
		TrainCoach trainCoach = trainCoachService.selectByMemberId(member.getId());

		TrainTeam trainTeam = trainTeamService.selectByCoach(trainCoach.getId());

		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("id", trainTeam.getId()); // 机构ID
		map.put("headImage", trainTeam.getHeadImage()); // 机构封面
		map.put("title", trainTeam.getTitle()); // 机构名称
		map.put("name", trainCoach.getName()); // 当前教练姓名
		map.put("type", trainCoach.getType()); // 当前教练身份1=主教2助教
		map.put("manager", trainCoach.getManager()); // 权限级别(1=负责人2=管理员)

		int trainCourseSum = trainCourseService.countByCoach(trainCoach.getId());
		int trainCoachSum = trainCoachService.countByTeam(trainTeam.getId());
		map.put("trainVenueSum", trainTeam.getVenueNumber()); // 教学场地数量
		map.put("trainCoachSum", trainCoachSum); // 教练团队
		map.put("trainCourseSum", trainCourseSum); // 我的课程
		map.put("trainLogSum", 0); // 机构日志
		map.put("refundSum", 0); // 退费申请

		return new ApiMessage(200, "获取成功", map);
	}

	/**
	 * @Description: 获取机构设置
	 * @author 宋高俊
	 * @param request
	 * @return
	 * @date 2018年10月8日 下午2:48:07
	 */
	@RequestMapping(value = "/manager/getMyTrainTeam")
	@ResponseBody
	public ApiMessage getMyCoach(HttpServletRequest request) {
		// 用户数据
		String token = (String) request.getAttribute("token");
		Member member = (Member) RedisUtil.getRedisOne(Global.redis_member, token);
		// 根据用户ID查询教练数据
		TrainCoach trainCoach = trainCoachService.selectByMemberId(member.getId());

		TrainTeam trainTeam = trainTeamService.selectByCoach(trainCoach.getId());

		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("id", trainTeam.getId()); // 机构ID
		map.put("title", trainTeam.getTitle()); // 机构名称
		map.put("headImage", trainTeam.getHeadImage()); // 封面图
		map.put("address", trainTeam.getAddress()); // 地址
		map.put("longitude", trainTeam.getLongitude()); // 经度
		map.put("latitude", trainTeam.getLatitude()); // 纬度
		map.put("phone", trainTeam.getPhone()); // 机构电话
		map.put("freeClasses", trainTeam.getFreeClasses()); // 免费试课
		map.put("freeParking", trainTeam.getFreeParking()); // 免费停车
		map.put("teachClass", trainTeam.getTeachClass()); // 开课科目
		map.put("brandContent", trainTeam.getBrandContent()); // 机构介绍
		map.put("titleFlag", trainTeam.getTitleFlag()); // 是否可以修改名称

		List<String> images = new ArrayList<String>();
		List<TrainTeamImage> trainTeamImages = trainTeamImageService.selectByTrainTeamID(trainTeam.getId());
		for (int i = 0; i < trainTeamImages.size(); i++) {
			images.add(trainTeamImages.get(i).getPath());
		}
		map.put("images", images); // 机构风采
		return new ApiMessage(200, "获取成功", map);
	}

	/**
	 * @Description: 修改机构设置
	 * @author 宋高俊
	 * @param request
	 * @return
	 * @date 2018年10月8日 下午2:48:07
	 */
	@RequestMapping(value = "/manager/updateMyTrainTeam")
	@ResponseBody
	public ApiMessage updateMyCoach(HttpServletRequest request, TrainTeam trainTeam, String images) {
		// 用户数据
		String token = (String) request.getAttribute("token");
		Member member = (Member) RedisUtil.getRedisOne(Global.redis_member, token);
		// 根据用户ID查询教练数据
		TrainCoach trainCoach = trainCoachService.selectByMemberTeam(member.getId(), trainTeam.getId());
		if (trainCoach.getManager() != 1 && trainCoach.getManager() != 2) {
			return new ApiMessage(400, "权限不足");
		}
		trainTeam.setModifyTime(new Date());

		TrainTeam lodTrainTeam = trainTeamService.selectByPrimaryKey(trainTeam.getId());

		if (!lodTrainTeam.getTitle().equals(trainTeam.getTitle())) {
			if (lodTrainTeam.getTitleFlag() == 1) {
				trainTeam.setTitleFlag(0);
			} else {
				return new ApiMessage(400, "每月名称只可修改一次");
			}
		}

		int flag = trainTeamService.updateByPrimaryKeySelective(trainTeam);
		if (flag > 0) {
			trainTeamImageService.deleteByTeamAll(trainTeam.getId());
			String[] urls = images.split(";");
			for (int i = 0; i < urls.length; i++) {
				TrainTeamImage trainTeamImage = new TrainTeamImage();
				trainTeamImage.setId(Utils.getUUID());
				trainTeamImage.setCreateTime(new Date());
				trainTeamImage.setModifyTime(new Date());
				trainTeamImage.setPath(urls[i]);
				trainTeamImage.setTrainTeamId(trainTeam.getId());
				trainTeamImageService.insertSelective(trainTeamImage);
			}

			return new ApiMessage(200, "修改成功");
		} else {
			return new ApiMessage(400, "修改失败");
		}
	}

	/**
	 * @Description: 获取所有的教学场地
	 * @author 宋高俊
	 * @param request
	 * @return
	 * @date 2018年10月10日 上午11:37:26
	 */
	@RequestMapping(value = "/manager/getMyTrainVenue")
	@ResponseBody
	public ApiMessage getMyTrainVenue(HttpServletRequest request) {
		// 用户数据
		String token = (String) request.getAttribute("token");
		Member member = (Member) RedisUtil.getRedisOne(Global.redis_member, token);
		// 根据用户ID查询教练数据
		TrainCoach trainCoach = trainCoachService.selectByMemberId(member.getId());

		List<Map<String, Object>> listMaps = new ArrayList<Map<String, Object>>();
		// 可用的状态
		List<Venue> listVenue = venueService.selectByTrainTeamID(trainCoach.getTrainTeamId());
		for (int i = 0; i < listVenue.size(); i++) {
			Map<String, Object> map = new LinkedHashMap<String, Object>();
			map.put("id", listVenue.get(i).getId()); // 场地ID
			map.put("headImage", listVenue.get(i).getImage()); // 封面图
			map.put("title", listVenue.get(i).getName()); // 场地名称
			map.put("address", listVenue.get(i).getAddress()); // 场地地址
			map.put("lnt", listVenue.get(i).getLongitude()); // 经度
			map.put("lat", listVenue.get(i).getLatitude()); // 纬度
			map.put("type", "1"); // 审核状态(0=审核中1=审核通过2=审核不通过)
			listMaps.add(map);
		}
		
		// 待审的状态
		List<VenueCheck> listVenueCheck = venueCheckService.selectByTrainTeamID(trainCoach.getTrainTeamId());
		for (int i = 0; i < listVenueCheck.size(); i++) {
			Map<String, Object> map = new LinkedHashMap<String, Object>();
			map.put("id", listVenueCheck.get(i).getId()); // 场地ID
			map.put("headImage", listVenueCheck.get(i).getHeadImage()); // 封面图
			map.put("title", listVenueCheck.get(i).getTitle()); // 场地名称
			map.put("address", listVenueCheck.get(i).getAddress()); // 场地地址
			map.put("lnt", listVenueCheck.get(i).getLng()); // 经度
			map.put("lat", listVenueCheck.get(i).getLat()); // 纬度
			map.put("type", listVenueCheck.get(i).getCheckFlag()); // 审核状态(0=审核中1=审核通过2=审核不通过)
			listMaps.add(map);
		}
		
		return new ApiMessage(200, "获取成功", listMaps);
	}

	/**
	 * @Description: 根据场地名和经纬度获取教学场地
	 * @author 宋高俊
	 * @param request
	 * @return
	 * @date 2018年10月10日 上午11:37:26
	 */
	@RequestMapping(value = "/manager/searchMyTrainVenue")
	@ResponseBody
	public ApiMessage searchMyTrainVenue(HttpServletRequest request, String name, Double longitude, Double latitude) {
		// 用户数据
//		String token = (String) request.getAttribute("token");
//		Member member = (Member) RedisUtil.getRedisOne(Global.redis_member, token);
//		// 根据用户ID查询教练数据
//		TrainCoach trainCoach = trainCoachService.selectByMemberId(member.getId());

		List<Map<String, Object>> listMaps = new ArrayList<Map<String, Object>>();
		List<Venue> list = venueService.selectTrainVenue(name, longitude, latitude);
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> map = new LinkedHashMap<String, Object>();
			map.put("id", list.get(i).getId()); // 场地ID
			map.put("title", list.get(i).getName()); // 场地名称
			map.put("address", list.get(i).getAddress()); // 场地地址
			map.put("lnt", list.get(i).getLongitude()); // 经度
			map.put("lat", list.get(i).getLatitude()); // 纬度
			listMaps.add(map);
		}
		return new ApiMessage(200, "获取成功", listMaps);
	}
	
	/**
	 * @Description: 根据ID删除教学场地
	 * @author 宋高俊
	 * @param request
	 * @return
	 * @date 2018年10月10日 上午11:37:26
	 */
	@RequestMapping(value = "/manager/deleteMyTrainVenue")
	@ResponseBody
	public ApiMessage deleteMyTrainVenue(HttpServletRequest request, String id, int type) {
		// 用户数据
		String token = (String) request.getAttribute("token");
		Member member = (Member) RedisUtil.getRedisOne(Global.redis_member, token);
		if (type == 1) {
			// 根据用户ID查询教练数据
			TrainCoach trainCoach = trainCoachService.selectByMemberId(member.getId());
			// 根据培训机构ID删除场地
			int flag = venueService.deleteByTeamVenue(trainCoach.getTrainTeamId(), id);
			if (flag > 0) {
				TrainTeam trainTeam = trainTeamService.selectByPrimaryKey(trainCoach.getTrainTeamId());
				trainTeam.setVenueNumber(trainTeam.getVenueNumber() - 1);
				trainTeamService.updateByPrimaryKeySelective(trainTeam);
				return new ApiMessage(200, "删除成功");
			}
		}else {
			int flag = venueCheckService.deleteByPrimaryKey(id);
			if (flag > 0) {
				return new ApiMessage(200, "删除成功");
			}
		}
		return new ApiMessage(400, "删除失败");
		
	}

	/**
	 * @Description: 保存教学场地
	 * @author 宋高俊
	 * @param request
	 * @param trainVenue
	 * @return
	 * @date 2018年10月10日 下午2:43:48
	 */
	@RequestMapping(value = "/manager/saveMyTrainVenue")
	@ResponseBody
	public ApiMessage saveMyTrainVenue(HttpServletRequest request, String title, String address, double longitude, 
			double latitude, String headImage, String phone, String content, Integer type) {
		// 用户数据
		String token = (String) request.getAttribute("token");
		Member member = (Member) RedisUtil.getRedisOne(Global.redis_member, token);

		// 根据用户ID查询教练数据
		TrainCoach trainCoach = trainCoachService.selectByMemberId(member.getId());

		VenueCheck vc = new VenueCheck();
		vc.setId(Utils.getUUID());
		vc.setCreateTime(new Date());
		vc.setModifyTime(new Date());
		vc.setTitle(title);
		vc.setBallType(type);
		vc.setPhone(phone);
		vc.setContent(content);
		vc.setLng(longitude);
		vc.setLat(latitude);
		vc.setAddress(address);
		vc.setTrainTeamId(trainCoach.getTrainTeamId());
		vc.setCheckFlag(0);
		
		int flag = venueCheckService.insertSelective(vc);
		if (flag > 0) {
//			// 增加培训机构使用场地
//			TrainTeamVenue trainTeamVenue = new TrainTeamVenue();
//			trainTeamVenue.setId(Utils.getUUID());
//			trainTeamVenue.setTrainTeamId(trainCoach.getTrainTeamId());
//			trainTeamVenue.setTrainVenueId(vc.getId());
//			venueService.saveTeamVenue(trainTeamVenue);

//			// 修改培训机构使用场地数量
//			TrainTeam trainTeam = trainTeamService.selectByPrimaryKey(trainCoach.getTrainTeamId());
//			trainTeam.setVenueNumber(trainTeam.getVenueNumber() + 1);
//			trainTeamService.updateByPrimaryKeySelective(trainTeam);
			return new ApiMessage(200, "添加成功");
		} else {
			return new ApiMessage(400, "添加失败");
		}
	}

	/**
	 * @Description: 反馈线下报名
	 * @author 宋高俊
	 * @param request
	 * @param content
	 * @return
	 * @date 2018年10月12日 上午10:47:38
	 */
	@RequestMapping(value = "/manager/saveTrainTeamFeedback")
	@ResponseBody
	public ApiMessage saveTrainTeamFeedback(HttpServletRequest request, String content, String id) {
		// 用户数据
		String token = (String) request.getAttribute("token");
		Member member = (Member) RedisUtil.getRedisOne(Global.redis_member, token);

		TrainTeamFeedback trainTeamFeedback = new TrainTeamFeedback();
		trainTeamFeedback.setId(Utils.getUUID());
		trainTeamFeedback.setContent(content);
		trainTeamFeedback.setCreateTime(new Date());
		trainTeamFeedback.setMemberId(member.getId());
		trainTeamFeedback.setTrainTeamId(id);
		
		int flag = trainTeamFeedbackService.insertSelective(trainTeamFeedback);
		if (flag > 0) {
			return new ApiMessage(200, "添加成功");
		} else {
			return new ApiMessage(400, "添加失败");
		}
	}
	
	/**  
	 * @Description: 统计拨打电话次数
	 * @author 宋高俊  
	 * @param request
	 * @param id
	 * @return 
	 * @date 2018年10月15日 上午11:35:27 
	 */ 
	@RequestMapping(value = "/manager/countTrainTeamPhone")
	@ResponseBody
	public ApiMessage countTrainTeamPhone(HttpServletRequest request, String id) {
		// 用户数据
		String token = (String) request.getAttribute("token");
		Member member = (Member) RedisUtil.getRedisOne(Global.redis_member, token);

		TrainTeamPhone trainTeamPhone = new TrainTeamPhone();
		trainTeamPhone.setId(Utils.getUUID());
		trainTeamPhone.setCreateTime(new Date());
		trainTeamPhone.setMemberId(member.getId());
		trainTeamPhone.setTrainTeamId(id);

		int flag = trainTeamPhoneService.insertSelective(trainTeamPhone);
		if (flag > 0) {
			return new ApiMessage(200, "添加成功");
		} else {
			return new ApiMessage(400, "添加失败");
		}
	}
	
	/**  
	 * @Description: 培训机构入驻申请接口
	 * @author 宋高俊  
	 * @param request
	 * @param trainEnter
	 * @return 
	 * @date 2018年10月17日 下午3:36:38 
	 */ 
	@RequestMapping(value = "/addTrainEnter")
	@ResponseBody
	public ApiMessage addTrainEnter(HttpServletRequest request, TrainEnter trainEnter) {
		// 用户数据
		String token = (String) request.getAttribute("token");
		Member member = (Member) RedisUtil.getRedisOne(Global.redis_member, token);

		trainEnter.setId(Utils.getUUID());
		trainEnter.setCreateTime(new Date());
		trainEnter.setMemberId(member.getId());
		trainEnter.setCheckFlag(0);

		int flag = trainEnterService.insertSelective(trainEnter);
		if (flag > 0) {
			return new ApiMessage(200, "添加成功");
		} else {
			return new ApiMessage(400, "添加失败");
		}
	}
	
	/**  
	 * @Description: 场馆入驻申请接口
	 * @author 宋高俊  
	 * @param request
	 * @param trainEnter
	 * @return 
	 * @date 2018年10月17日 下午3:36:38 
	 */ 
	@RequestMapping(value = "/addVenueEnter")
	@ResponseBody
	public ApiMessage addVenueEnter(HttpServletRequest request, VenueEnter venueEnter) {
		// 用户数据
		String token = (String) request.getAttribute("token");
		Member member = (Member) RedisUtil.getRedisOne(Global.redis_member, token);

		venueEnter.setId(Utils.getUUID());
		venueEnter.setCreateTime(new Date());
		venueEnter.setMemberId(member.getId());
		venueEnter.setCheckFlag(0);

		int flag = venueEnterService.insertSelective(venueEnter);
		if (flag > 0) {
			return new ApiMessage(200, "添加成功");
		} else {
			return new ApiMessage(400, "添加失败");
		}
	}
	
	/**  
	 * @Description: 根据名称模糊查询场馆
	 * @author 宋高俊  
	 * @param name
	 * @return 
	 * @date 2018年10月18日 下午2:58:34 
	 */ 
	@RequestMapping(value = "/likeVenueName")
	@ResponseBody
	public ApiMessage likeVenueName(String name) {
		List<Map<String, Object>> listMaps = new ArrayList<Map<String, Object>>();
		List<Venue> list = venueService.selectByName(name);
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> map = new LinkedHashMap<String, Object>();
			map.put("id", list.get(i).getId()); // 场地ID
			map.put("name", list.get(i).getName()); // 场地名称
			listMaps.add(map);
		}
		return new ApiMessage(200, "添加成功", listMaps);
	}
	
	/**  
	 * @Description: 根据场馆查询驻场的培训机构
	 * @author 宋高俊  
	 * @param id
	 * @return 
	 * @date 2018年10月18日 下午3:49:46 
	 */ 
	@RequestMapping(value = "/enterTrainTeam")
	@ResponseBody
	public ApiMessage enterTrainTeam(String id, Double lng, Double lat) {
		List<Map<String, Object>> listmap = new ArrayList<Map<String, Object>>();
		List<TrainTeam> list =trainTeamService.selectByVenue(id);
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
		return new ApiMessage(200, "添加成功", listmap);
	}
}
