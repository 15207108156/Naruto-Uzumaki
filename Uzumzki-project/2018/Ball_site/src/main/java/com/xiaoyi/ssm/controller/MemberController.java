package com.xiaoyi.ssm.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xiaoyi.ssm.dto.AdminMessage;
import com.xiaoyi.ssm.dto.AdminPage;
import com.xiaoyi.ssm.model.Member;
import com.xiaoyi.ssm.model.Permission;
import com.xiaoyi.ssm.model.Staff;
import com.xiaoyi.ssm.service.MemberService;
import com.xiaoyi.ssm.service.PermissionService;
import com.xiaoyi.ssm.util.DateUtil;

/**
 * @Description: 用户控制器
 * @author 宋高俊
 * @date 2018年9月20日 下午3:32:38
 */
@Controller(value = "adminMemberController")
@RequestMapping(value = "/admin/member")
public class MemberController {

	@Autowired
	private MemberService memberService;
	@Autowired
	private PermissionService permissionService;

	/**
	 * @Description: 用户页面
	 * @author 宋高俊
	 * @return
	 * @date 2018年9月20日 下午3:33:36
	 */
	@RequestMapping(value = "/listview")
	public String listview(HttpServletRequest request, Model model) {
		Staff staff = (Staff) request.getSession().getAttribute("loginStaffInfo");
		List<Permission> list = permissionService.selectByBtu(staff.getPower(), "21");
		for (int i = 0; i < list.size(); i++) {
			model.addAttribute("btn"+list.get(i).getId(), "1");
		}
		return "admin/member/list";
	}

	/**
	 * @Description: 会员数据
	 * @author 宋高俊
	 * @param adminPage
	 * @return
	 * @date 2018年9月20日 下午3:33:48
	 */
	@RequestMapping(value = "/list")
	@ResponseBody
	public AdminMessage list(AdminPage adminPage) {
		PageHelper.startPage(adminPage.getPage(), adminPage.getLimit());
		List<Member> list = memberService.selectByAll(null);
		PageInfo<Member> pageInfo = new PageInfo<>(list);
		List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < list.size(); i++) {
			Member member = list.get(i);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", member.getId());// ID
			map.put("createtime", DateUtil.getFormat(member.getCreatetime(), "yyyy-MM-dd"));// 注册时间
			map.put("appnickname", member.getAppnickname());// 微信昵称
			map.put("name", member.getName());// 真实姓名
			map.put("phone", member.getPhone());// 电话
			map.put("appavatarurl", member.getAppavatarurl());// 头像
			map.put("memberno", member.getMemberno());// 用户编号
			listMap.add(map);
		}
		return new AdminMessage(pageInfo.getTotal(), listMap);
	}

}
