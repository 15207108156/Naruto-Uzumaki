package com.xiaoyi.ssm.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xiaoyi.ssm.dao.StaffLogMapper;
import com.xiaoyi.ssm.dto.AdminMessage;
import com.xiaoyi.ssm.dto.AdminPage;
import com.xiaoyi.ssm.dto.ApiMessage;
import com.xiaoyi.ssm.model.Permission;
import com.xiaoyi.ssm.model.Staff;
import com.xiaoyi.ssm.model.StaffApply;
import com.xiaoyi.ssm.model.StaffLog;
import com.xiaoyi.ssm.service.PermissionService;
import com.xiaoyi.ssm.service.StaffApplyService;
import com.xiaoyi.ssm.service.StaffService;
import com.xiaoyi.ssm.util.DateUtil;
import com.xiaoyi.ssm.util.Global;
import com.xiaoyi.ssm.util.HttpUtils;
import com.xiaoyi.ssm.util.RedisUtil;
import com.xiaoyi.ssm.util.Utils;

/**
 * @Description: 管理员控制器
 * @author 宋高俊
 * @date 2018年7月25日 下午2:43:06
 */
@Controller(value = "adminStaffController")
@RequestMapping(value = "admin/staff")
public class StaffController {
    private static Logger logger = Logger.getLogger(StaffController.class.getName());

	@Autowired
	private StaffService staffService;
	@Autowired
	private StaffLogMapper staffLogMapper;
	@Autowired
	private StaffApplyService staffApplyService;
	@Autowired
	private PermissionService permissionService;

	/**
	 * @Description: 登录方法
	 * @author 宋高俊
	 * @date 2018年7月25日 下午2:37:32
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@ResponseBody
	public ApiMessage login(HttpServletRequest request, Staff staff) {
		Staff loginStaff = staffService.login(staff);
		if (loginStaff != null) {
			if ("正常".equals(loginStaff.getStatusFlag())) {
				Staff updateLogin = new Staff();
				updateLogin.setId(loginStaff.getId());
				updateLogin.setLoginChange(new Date());
				staffService.updateByPrimaryKeySelective(updateLogin);
				request.getSession().setAttribute("loginStaffInfo", loginStaff);
				return new ApiMessage(200, "登录成功");
			} else {
				return new ApiMessage(400, "账号" + loginStaff.getStatusFlag() + ",请联系工作人员!");
			}
		} else {
			return new ApiMessage(400, "账号不存在,请检查账号密码是否正确");
		}
	}
	
	/**  
	 * @Description: 注册用户
	 * @author 宋高俊  
	 * @param staff
	 * @return 
	 * @date 2018年10月19日 下午4:24:05 
	 */ 
	@RequestMapping(value = "/register")
	@ResponseBody
	public ApiMessage register(HttpServletRequest request, StaffApply staffApply, String code, String state) {
		
		String smsCode = RedisUtil.getRedis(Global.web_staff_register_SmsCode_ + staffApply.getTel());
		if (!code.equals(smsCode)) {
			return new ApiMessage(400, "验证码不正确");
		}

		String openid = RedisUtil.getRedis(state+"_openid");
		String unionid = RedisUtil.getRedis(state+"_unionid");
		String access_token = RedisUtil.getRedis(state+"_access_token");
		
		staffApply.setId(Utils.getUUID());
		staffApply.setOpenid(openid != null ? openid : "");
		staffApply.setUnionid(unionid != null ? unionid : "");
		// 获取用户信息
		String userinfo = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID"
				.replace("ACCESS_TOKEN", access_token).replace("OPENID", openid);
		String userinfoReturn = HttpUtils.sendGet(userinfo, null);// 我们需要自己写或者在网上找一个doGet方法发送doGet请求
		
		JSONObject getUserinfoJson = JSONObject.fromObject(userinfoReturn);// 把请求成功后的结果转换成JSON对象
		if (getUserinfoJson == null || getUserinfoJson.get("errcode") != null || getUserinfoJson.getString("openid") == null) {
			logger.error("", new Exception("获取回调异常"));
			return new ApiMessage(400, getUserinfoJson.getString("errmsg"));
		}else {
			staffApply.setHeadImage(getUserinfoJson.get("headimgurl").toString());
			staffApply.setSex(getUserinfoJson.get("sex").toString() == "2" ? "男" : "女");
			staffApply.setNickname(getUserinfoJson.getString("nickname"));
		}

		staffApply.setApplyTime(new Date());
		int flag = staffApplyService.insertSelective(staffApply);		
		if (flag > 0) {
			RedisUtil.delRedis(state+"_openid");
			RedisUtil.delRedis(state+"_unionid");
			RedisUtil.delRedis(state+"_access_token");
			return new ApiMessage(200, "注册申请成功");
		} else {
			return new ApiMessage(400, "注册申请失败");
		}
	}

	/**
	 * @Description: 退出登录
	 * @author 宋高俊
	 * @date 2018年7月25日 下午2:37:32
	 */
	@RequestMapping(value = "/loginout", method = RequestMethod.POST)
	@ResponseBody
	public ApiMessage loginout(HttpServletRequest request, Staff staff) {
		request.getSession().removeAttribute("loginStaffInfo");
		return ApiMessage.succeed();
	}

	/**
	 * @Description: 修改密码
	 * @author 宋高俊
	 * @date 2018年8月2日 上午9:45:46
	 */
	@RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
	@ResponseBody
	public ApiMessage updatePassword(HttpServletRequest request, String oldPassword, String newPassword) {
		Staff staff = (Staff) request.getSession().getAttribute("loginStaffInfo");
		if (staff.getPassword().equals(oldPassword)) {
			Staff updateStaff = new Staff();
			updateStaff.setId(staff.getId());
			updateStaff.setPassword(newPassword);
			staffService.updateByPrimaryKeySelective(updateStaff);
			// 修改完成后跳转到登录页面
			request.getSession().removeAttribute("loginStaffInfo");
			return ApiMessage.succeed();
		} else {
			return new ApiMessage(400, "旧密码错误");
		}
	}

	/**
	 * @Description: 伙伴列表页面
	 * @author 宋高俊
	 * @date 2018年7月27日 下午6:55:37
	 */
	@RequestMapping(value = "/listview")
	public String listview(HttpServletRequest request, Model model) {
		Staff staff = (Staff) request.getSession().getAttribute("loginStaffInfo");
		List<Permission> list = permissionService.selectByBtu(staff.getPower(), "41");
		for (int i = 0; i < list.size(); i++) {
			model.addAttribute("btn"+list.get(i).getId(), "1");
		}
		return "admin/staff/list";
	}

	/**
	 * @Description: 伙伴列表页面
	 * @author 宋高俊
	 * @date 2018年7月27日 下午6:55:37
	 */
	@RequestMapping(value = "/list")
	@ResponseBody
	public AdminMessage list(AdminPage adminPage) {
		PageHelper.startPage(adminPage.getPage(), adminPage.getLimit());
		List<Staff> list = staffService.selectByAll(null);
		PageInfo<Staff> pageInfo = new PageInfo<>(list);
		List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < list.size(); i++) {
			Staff staff = list.get(i);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", staff.getId());// ID
			map.put("staffno", staff.getStaffNo());// 编号
			map.put("nickname", staff.getNickname());// 昵称
			map.put("name", staff.getName());// 姓名
			map.put("position", staff.getPosition());// 岗位
			map.put("power", staff.getPower() == 1 ? "一级"
					: staff.getPower() == 2 ? "二级"
							: staff.getPower() == 3 ? "三级"
									: staff.getPower() == 4 ? "四级"
											: staff.getPower() == 5 ? "五级" 
													: staff.getPower() == 6 ? "六级" 
															: staff.getPower() == 7 ? "七级" 
																	: staff.getPower() == 8 ? "八级" : "九级");// 权级
			map.put("statusFlag", staff.getStatusFlag());// 状态
			map.put("loginchange", DateUtil.getFormat(staff.getLoginChange()));// 最近登录
			map.put("remark", staff.getRemark());// 备注
			listMap.add(map);
		}
		return new AdminMessage(pageInfo.getTotal(), listMap);
	}

	/**
	 * @Description: 修改页面
	 * @author 宋高俊
	 * @date 2018年7月28日 上午9:34:28
	 */
	@RequestMapping(value = "/editStaff")
	public String editStaff(Model model, String id) {
		Staff staff = staffService.selectByPrimaryKey(id);
		model.addAttribute("staff", staff);
		return "admin/staff/update";
	}

	/**
	 * @Description: 修改页面
	 * @author 宋高俊
	 * @date 2018年7月28日 上午9:34:28
	 */
	@RequestMapping(value = "/updateStaff")
	@ResponseBody
	public ApiMessage updateStaff(HttpServletRequest request, Staff staff) {
		Staff loginstaff = (Staff) request.getSession().getAttribute("loginStaffInfo");

		Staff oldstaff = staffService.selectByPrimaryKey(staff.getId());
		String logdata = "";
		if (!oldstaff.getPosition().equals(staff.getPosition())) {
			logdata += "岗位从" + oldstaff.getPosition() + "修改为" + staff.getPosition() + ";";
		}
		if (!oldstaff.getPower().equals(staff.getPower())) {
			logdata += "级别从" + oldstaff.getPower() + "级修改为" + staff.getPower() + "级;";
		}
		if (!oldstaff.getStatusFlag().equals(staff.getStatusFlag())) {
			logdata += "状态从" + oldstaff.getStatusFlag() + "修改为" + staff.getStatusFlag() + ";";
		}
		//管理员修改日志
		StaffLog staffLog = new StaffLog();
		staffLog.setId(Utils.getUUID());
		staffLog.setStaffid(staff.getId());
		staffLog.setEditstaffid(loginstaff.getId());
		staffLog.setContent(logdata);
		staffLog.setTime(new Date());

		staffLogMapper.insertSelective(staffLog);

		int flag = staffService.updateByPrimaryKeySelective(staff);
		if (flag > 0) {
			return ApiMessage.succeed();
		} else {
			return ApiMessage.error();
		}
	}

	/**
	 * @Description: 新增页面
	 * @author 宋高俊
	 * @date 2018年7月28日 上午9:34:28
	 */
	@RequestMapping(value = "/addStaff")
	public String addStaff() {
		return "admin/staff/add";
	}

/*	*//**
	 * @Description: 保存伙伴页面
	 * @author 宋高俊
	 * @date 2018年7月28日 上午9:34:28
	 *//*
	@RequestMapping(value = "/saveStaff")
	@ResponseBody
	public ApiMessage saveStaff(Staff staff) {
		staff.setId(Utils.getUUID());
		if (StringUtils.isBlank(staff.getPassword())) {
			staff.setPassword("123456");
		}
		Staff oldstaff = staffService.selectByEName(staff.getName());
		if (oldstaff != null) {
			return new ApiMessage(400, "英文名重复，请修改后提交");
		} else {
			int flag = staffService.insertSelective(staff);
			if (flag > 0) {
				return ApiMessage.succeed();
			} else {
				return ApiMessage.error();
			}
		}
	}*/

	/**
	 * @Description: 删除伙伴
	 * @author 宋高俊
	 * @date 2018年7月28日 上午9:34:28
	 */
	@RequestMapping(value = "/delStaff")
	@ResponseBody
	public ApiMessage delStaff(String staffId) {
		int flag = staffService.deleteByPrimaryKey(staffId);
		if (flag > 0) {
			return ApiMessage.succeed();
		}
		return ApiMessage.error();
	}

	/**  
	 * @Description: 个人资料
	 * @author 宋高俊  
	 * @date 2018年8月11日 上午10:57:13 
	 */ 
	@RequestMapping(value = "/staffDetails")
	public String staffDetails() {
		return "admin/staff/staffDetails";
	}
	
	/**
	 * @Description: 伙伴申请列表数据
	 * @author 宋高俊
	 * @date 2018年7月27日 下午6:55:37
	 */
	@RequestMapping(value = "/staffApply/list")
	@ResponseBody
	public AdminMessage staffApplyList(AdminPage adminPage, Integer checkFlag) {
		PageHelper.startPage(adminPage.getPage(), adminPage.getLimit());
		List<StaffApply> list = staffApplyService.selectByApply(checkFlag);
		PageInfo<StaffApply> pageInfo = new PageInfo<>(list);
		List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < list.size(); i++) {
			StaffApply staffApply = list.get(i);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", staffApply.getId());// ID
			map.put("applyTime", DateUtil.getFormat(staffApply.getApplyTime()));// 申请时间
			map.put("name", staffApply.getName());// 姓名
			map.put("tel", staffApply.getTel());// 手机号
			map.put("nickname", staffApply.getNickname());// 昵称
			map.put("checkFlag", staffApply.getCheckFlag() == 0 ? "待核" : staffApply.getCheckFlag() == 1 ? "通过" : "无效");// 审核状态(0=待审1=通过2=无效)
			map.put("checkName", staffApply.getStaff().getName());// 审核人
			map.put("checkTime", DateUtil.getFormat(staffApply.getCheckTime()));// 审核时间
			map.put("checkContent", staffApply.getCheckContent());// 审核意见
			listMap.add(map);
		}
		return new AdminMessage(pageInfo.getTotal(), listMap);
	}
	
	/**  
	 * @Description: 审核加入申请
	 * @author 宋高俊  
	 * @param request
	 * @param id
	 * @param content
	 * @param checkFlag
	 * @return 
	 * @date 2018年10月22日 下午3:16:18 
	 */ 
	@RequestMapping(value = "/checkStaffApply")
	@ResponseBody
	public ApiMessage checkStaffApply(HttpServletRequest request, String id,
			String content, Integer checkFlag, String selectStatusFlag, String selectPosition, Integer selectPower) {
		Staff loginstaff = (Staff) request.getSession().getAttribute("loginStaffInfo");
		StaffApply staffApply = staffApplyService.selectByPrimaryKey(id);
		staffApply.setCheckFlag(checkFlag);
		staffApply.setCheckContent(content);
		staffApply.setCheckStaff(loginstaff.getId());
		staffApply.setCheckTime(new Date());
		staffApplyService.updateByPrimaryKeySelective(staffApply);

		// 通过审核才成为正式用户
		if (checkFlag == 1) {
			Staff staff = new Staff();
			staff.setId(Utils.getUUID());
			staff.setName(staffApply.getName());
			staff.setTel(staffApply.getTel());
			staff.setSex(staffApply.getSex());
			staff.setReferee(staffApply.getReferee());
			staff.setNickname(staffApply.getNickname());
			staff.setHeadImage(staffApply.getHeadImage());
			staff.setOpenid(staffApply.getOpenid());
			staff.setUnionid(staffApply.getUnionid());
			
			staff.setStatusFlag(selectStatusFlag);
			staff.setPosition(selectPosition);
			staff.setPower(selectPower);
			staffService.insertSelective(staff);
		}
		return ApiMessage.succeed();
	}
}
