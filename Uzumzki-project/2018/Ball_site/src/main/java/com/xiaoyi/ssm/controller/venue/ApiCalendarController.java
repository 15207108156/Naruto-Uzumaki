package com.xiaoyi.ssm.controller.venue;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xiaoyi.ssm.dto.ApiMessage;
import com.xiaoyi.ssm.model.Member;
import com.xiaoyi.ssm.model.Venue;
import com.xiaoyi.ssm.model.VenueStatis;
import com.xiaoyi.ssm.model.VenueTemplate;
import com.xiaoyi.ssm.service.FieldTemplateService;
import com.xiaoyi.ssm.service.MemberService;
import com.xiaoyi.ssm.service.VenueService;
import com.xiaoyi.ssm.service.VenueStatisService;
import com.xiaoyi.ssm.service.VenueTemplateService;
import com.xiaoyi.ssm.util.DateUtil;
import com.xiaoyi.ssm.util.Global;
import com.xiaoyi.ssm.util.RedisUtil;

/**
 * @Description: 日历统计数据控制器
 * @author 宋高俊
 * @date 2018年9月6日 下午8:25:56
 */
@Controller
@RequestMapping("venue/manager/calendar")
public class ApiCalendarController {

	@Autowired
	private MemberService memberService;
	@Autowired
	private VenueService venueService;
	@Autowired
	private VenueStatisService venueStatisService;
	@Autowired
	private VenueTemplateService venueTemplateService;
	@Autowired
	private FieldTemplateService fieldTemplateService;

	/**
	 * @Description: 根据月份获取日历的统计数据
	 * @author 宋高俊
	 * @param date  2018-08
	 * @return
	 * @date 2018年9月6日 下午8:28:12
	 */
	@RequestMapping(value = "/calendar")
	@ResponseBody
	public ApiMessage calendar(String date, HttpServletRequest request, String venueid) {
		
		String token = (String) request.getAttribute("token");
		Member member = (Member) RedisUtil.getRedisOne(Global.redis_member, token);
		
		Venue venue = venueService.selectByPrimaryKey(venueid);
		
		//获取当前月份的第一天和最后一天
		Date startDate = DateUtil.dateGetFirst(date);
		Date endDate = DateUtil.dateGetLast(date);
		
		List<VenueStatis> list = venueStatisService.selectByVenue(venue.getId(), startDate, endDate);
		SortedMap<String, Object> sortmap =  new TreeMap<>();
		for (VenueStatis venueStatis : list) {
			
			Map<String, Object> map = new HashMap<>();
			map.put("id", venueStatis.getId());// id
			map.put("template", venueStatis.getTemplate());// 模板名称
			map.put("amount", venueStatis.getAmount());// 营业额
			map.put("score", venueStatis.getScore());// 使用率
			
			sortmap.put(DateUtil.getFormat(venueStatis.getStatisdate(), "yyyy-M-d"), map);
		}
		return new ApiMessage(200, "查询成功", sortmap);
	}
	
	/**  
	 * @Description: 选配模板
	 * @author 宋高俊  
	 * @param date
	 * @return 
	 * @date 2018年9月7日 上午10:55:16 
	 */ 
	@RequestMapping(value = "/setTemplate")
	@ResponseBody
	public ApiMessage setTemplate(String venueid, String date, String templateid, HttpServletRequest request) {
		//根据管理员获取所管理的场馆
		Venue venue = venueService.selectByPrimaryKey(venueid);
		//根据模板ID修改
		VenueTemplate venueTemplate = venueTemplateService.selectByPrimaryKey(templateid);
		return venueStatisService.saveVenueStatis(venue.getId(), venueTemplate, date);
	}
	
	/**  
	 * @Description: 根据日历统计ID修改当天使用模板
	 * @author 宋高俊  
	 * @param date
	 * @param templateid
	 * @param request
	 * @return 
	 * @date 2018年9月12日 下午2:34:51 
	 */ 
	@RequestMapping(value = "/updateTemplate")
	@ResponseBody
	public ApiMessage updateTemplate(String venueid,String id, String templateid, HttpServletRequest request) {
		
		//根据管理员获取所管理的场馆
		Venue venue = venueService.selectByPrimaryKey(venueid);
		
		//新增一条日期模板数据
		VenueStatis vs = venueStatisService.selectByPrimaryKey(id);

		//根据模板ID修改
		VenueTemplate venueTemplate = venueTemplateService.selectByPrimaryKey(templateid);
		
		return fieldTemplateService.saveFieldTemplateStatis(vs, venue,venueTemplate, vs.getStatisdate());
	}

}
