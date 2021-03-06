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
import com.xiaoyi.ssm.dao.InviteLogMapper;
import com.xiaoyi.ssm.dto.AdminMessage;
import com.xiaoyi.ssm.dto.AdminPage;
import com.xiaoyi.ssm.model.InviteBall;
import com.xiaoyi.ssm.model.InviteJoin;
import com.xiaoyi.ssm.model.InviteLog;
import com.xiaoyi.ssm.model.Permission;
import com.xiaoyi.ssm.model.Staff;
import com.xiaoyi.ssm.service.InviteBallService;
import com.xiaoyi.ssm.service.InviteJoinService;
import com.xiaoyi.ssm.service.MemberService;
import com.xiaoyi.ssm.service.PermissionService;
import com.xiaoyi.ssm.util.DateUtil;

/**
 * @Description: 约球控制器
 * @author 宋高俊
 * @date 2018年9月20日 下午5:03:09
 */
@Controller(value = "adminInviteBallController")
@RequestMapping(value = "/admin/inviteBall")
public class InviteController {

	@Autowired
	private InviteBallService inviteBallService;
	@Autowired
	private InviteJoinService inviteJoinService;
	@Autowired
	private InviteLogMapper inviteLogMapper;
	@Autowired
	private MemberService memberService;
	@Autowired
	private PermissionService permissionService;

	/**
	 * @Description: 约球页面
	 * @author 宋高俊
	 * @return
	 * @date 2018年9月20日 下午5:03:30
	 */
	@RequestMapping(value = "/listview")
	public String listview(HttpServletRequest request, Model model) {
		Staff staff = (Staff) request.getSession().getAttribute("loginStaffInfo");
		List<Permission> list = permissionService.selectByBtu(staff.getPower(), "11");
		for (int i = 0; i < list.size(); i++) {
			model.addAttribute("btn"+list.get(i).getId(), "1");
		}
		return "admin/inviteBall/list";
	}

	/**
	 * @Description: 约球数据
	 * @author 宋高俊
	 * @param adminPage
	 * @return
	 * @date 2018年9月20日 下午5:03:57
	 */
	@RequestMapping(value = "/list")
	@ResponseBody
	public AdminMessage list(AdminPage adminPage) {
		PageHelper.startPage(adminPage.getPage(), adminPage.getLimit());
		List<InviteBall> list = inviteBallService.selectDealInvite();
		PageInfo<InviteBall> pageInfo = new PageInfo<>(list);
		List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < list.size(); i++) {
			InviteBall inviteBall = list.get(i);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", inviteBall.getId());// ID
			map.put("inviteBallNo", inviteBall.getInviteBallNo());// 编号
			map.put("appnickname", inviteBall.getMember().getAppnickname());// 发起人
			map.put("createTime", DateUtil.getFormat(inviteBall.getCreateTime()));// 发起时间
			map.put("countFlag", inviteBall.getCountFlag() == 0 ? "未结算" : "已结算");// 是否已结算收费金额
			map.put("arriveTime", DateUtil.getFormat(inviteBall.getArriveTime()));// 到账时间
			map.put("arriveAmount", inviteBall.getArriveAmount());// 到账金额
			listMap.add(map);
		}
		return new AdminMessage(pageInfo.getTotal(), listMap);
	}

	/**
	 * @Description: 报名详情数据
	 * @author 宋高俊
	 * @param adminPage
	 * @return
	 * @date 2018年9月20日 下午5:03:57
	 */
	@RequestMapping(value = "/inviteJoin/list")
	@ResponseBody
	public AdminMessage inviteJoinList(String id) {
		List<InviteJoin> list = inviteJoinService.selectByJoinDetails(id);
		List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < list.size(); i++) {
			InviteJoin inviteJoin = list.get(i);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", inviteJoin.getId());// ID
			map.put("inviteJoinNo", inviteJoin.getInviteJoinNo());// 加入约球编号
			map.put("createTime", DateUtil.getFormat(inviteJoin.getCreateTime()));// 报名时间
			map.put("appnickname", inviteJoin.getMember().getAppnickname());// 报名人
			map.put("amount", inviteJoin.getAmount());// 支付金额
			map.put("refundFlag", inviteJoin.getRefundFlag() == 0 ? "报名成功" : "退出报名");// 报名状态
			map.put("payTime", DateUtil.getFormat(inviteJoin.getPayTime()));// 金额操作时间
			map.put("refundAmount", inviteJoin.getRefundAmount());// 退费金额
			map.put("refundFeeamount", inviteJoin.getRefundFeeamount());// 收取手续费
			listMap.add(map);
		}
		return new AdminMessage(100, list.size(), listMap);
	}

	/**
	 * @Description: 约球日志数据
	 * @author 宋高俊
	 * @param adminPage
	 * @return
	 * @date 2018年9月20日 下午5:03:57
	 */
	@RequestMapping(value = "/inviteLog/list")
	@ResponseBody
	public AdminMessage inviteLogList(AdminPage adminPage, String id) {
		PageHelper.startPage(adminPage.getPage(), adminPage.getLimit());
		List<InviteLog> list = inviteLogMapper.selectByInviteAdmin(id);
		PageInfo<InviteLog> pageInfo = new PageInfo<>(list);
		List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < list.size(); i++) {
			InviteLog inviteLog = list.get(i);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", inviteLog.getId());// ID
			map.put("createTime", DateUtil.getFormat(inviteLog.getCreateTime()));// 创建时间
			map.put("appnickname", inviteLog.getMember().getAppnickname());// 昵称
			map.put("content", inviteLog.getContent());// 内容
			map.put("type", inviteLog.getType() == 0 ? "留言" : "系统");// 日志类型
			listMap.add(map);
		}
		return new AdminMessage(100, pageInfo.getTotal(), listMap);
	}

}
