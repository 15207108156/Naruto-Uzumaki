package com.xiaoyi.ssm.controller.wxapp;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xiaoyi.ssm.dto.ApiMessage;
import com.xiaoyi.ssm.model.Member;
import com.xiaoyi.ssm.service.MemberService;
import com.xiaoyi.ssm.util.Arith;
import com.xiaoyi.ssm.util.Global;
import com.xiaoyi.ssm.util.HttpUtils;
import com.xiaoyi.ssm.util.RedisUtil;
import com.xiaoyi.ssm.util.StringUtil;
import com.xiaoyi.ssm.util.Utils;
import com.xiaoyi.ssm.wxPay.WXConfig;

import net.sf.json.JSONObject;

/**
 * @Description: 微信小程序公共接口
 * @author 宋高俊
 * @date 2018年9月11日 下午4:22:23
 */
@Controller("wxappApiCommonController")
@RequestMapping("wxapp/common")
public class ApiCommonController {
	
    private static Logger logger = Logger.getLogger(ApiCommonController.class.getName());
	@Autowired
	private MemberService memberService;

	/**
	 * @Description: 微信小程序用code换取openid
	 * @author 宋高俊
	 * @param request
	 * @param response
	 * @date 2018年9月11日 下午4:24:23
	 */
	@RequestMapping(value = "/code2accessToken", method = RequestMethod.POST)
	@ResponseBody
	public ApiMessage code2accessToken(HttpServletRequest request, HttpServletResponse response, String code) {
		logger.info("开始获取微信小程序openid");
		JSONObject getCodeResultJson = null;
		// 登录返回数据
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			logger.info("开始根据code获取微信小程序openid");
			String requestUrl = "https://api.weixin.qq.com/sns/jscode2session?appid=APPID&secret=SECRET&js_code=JSCODE&grant_type=authorization_code"
					.replace("APPID", WXConfig.wxAppAppid).replace("SECRET", WXConfig.wxAppApp_secret).replace("JSCODE", code);

			String requestResult = HttpUtils.sendGet(requestUrl, null);// 我们需要自己写或者在网上找一个doGet方法发送doGet请求
			getCodeResultJson = JSONObject.fromObject(requestResult);// 把请求成功后的结果转换成JSON对象

			if (getCodeResultJson == null || getCodeResultJson.get("errcode") != null || getCodeResultJson.getString("openid") == null) {
				logger.error("", new Exception("获取回调异常"));
			}

			logger.info("getCodeResultJson:" + getCodeResultJson.toString());

			Member member = memberService.selectByUnionid(getCodeResultJson.getString("unionid"));

			// 将openid作为key存储登录信息
			RedisUtil.addRedis(Global.REDIS_WXAPP_SESSION, getCodeResultJson.getString("unionid"), getCodeResultJson);
			map.put("token", getCodeResultJson.getString("unionid"));

			if (member != null) {
				// 用户基本信息
				map.put("appavatarurl", member.getAppavatarurl());
				map.put("appnickname", member.getAppnickname());
				logger.info("老用户登入：session_key="+getCodeResultJson.getString("session_key"));
				member.setSessionKey(getCodeResultJson.getString("session_key"));// 将session_key存入缓存

				member.setUnionid(getCodeResultJson.getString("unionid"));
				member.setAppopenid(getCodeResultJson.getString("openid"));
				// 返回小程序的openid
				map.put("openid", getCodeResultJson.getString("openid"));
				memberService.updateByPrimaryKeySelective(member);
				
				RedisUtil.addRedis(Global.redis_member, getCodeResultJson.getString("unionid"), member);
				logger.info("老用户登入");
			} else {
				logger.info("新用户登入");
			}
		} catch (Exception e) {
			logger.error("", e);
		}
		return new ApiMessage(200, "登录成功", map);
	}

	/**
	 * @Description: 创建用户的接口
	 * @author 宋高俊
	 * @return
	 * @date 2018年9月12日 上午11:33:27
	 */
	@RequestMapping(value = "/createMember", method = RequestMethod.POST)
	@ResponseBody
	public ApiMessage createMember(String token, String nickName, String avatarUrl, Integer gender) {
		JSONObject jsonObject = JSONObject.fromObject(RedisUtil.getRedisOne(Global.REDIS_WXAPP_SESSION, token));

		Member member = memberService.selectByUnionid(token);

		if (member != null) {
			member.setModifytime(new Date());
			if (!StringUtil.isBank(avatarUrl)) {
				member.setAppavatarurl(avatarUrl);
			}
			if (!StringUtil.isBank(nickName)) {
				member.setAppnickname(nickName);
			}
			if (gender != null) {
				member.setAppgender(gender);
			}
			memberService.updateByPrimaryKeySelective(member);
			
			member.setSessionKey(jsonObject.getString("session_key"));// 将session_key存入缓存
			RedisUtil.addRedis(Global.redis_member, jsonObject.getString("unionid"), member);
			logger.info("老用户修改用户数据：session_key="+jsonObject.getString("session_key"));
			return new ApiMessage(200, "修改成功");
		} else {
			member = new Member();
			member.setId(Utils.getUUID());
			member.setCreatetime(new Date());
			member.setModifytime(new Date());
			member.setAppavatarurl(avatarUrl);
			member.setAppnickname(nickName);
			member.setAppgender(gender);
			member.setUnionid(jsonObject.getString("unionid"));
			member.setAppopenid(jsonObject.getString("openid"));
			memberService.insertSelective(member);

			member.setSessionKey(jsonObject.getString("session_key"));// 将session_key存入缓存
			RedisUtil.addRedis(Global.redis_member, jsonObject.getString("unionid"), member);
			logger.info("新用户修改用户数据：session_key="+jsonObject.getString("session_key"));
			return new ApiMessage(200, "创建成功");
		}
	}

	/**
	 * @Description: 计算每人应分得的金额
	 * @author 宋高俊
	 * @param amount
	 * @param number
	 * @return
	 * @date 2018年9月27日 下午8:07:28
	 */
	@RequestMapping(value = "/arithAmount", method = RequestMethod.POST)
	@ResponseBody
	public ApiMessage arithAmount(Double amount, String nowAmountStr, double fee, double number) {
		if (StringUtil.isBank(nowAmountStr)) {
			return new ApiMessage(200, "请输入支出金额", 0);
		}

		if (".".equals(nowAmountStr.substring(nowAmountStr.length() - 1))) {
			nowAmountStr = nowAmountStr.substring(0, nowAmountStr.length() - 1);
		}
		double nowAmount = 0.0;
		try {
			nowAmount = Arith.round(Double.valueOf(nowAmountStr), 2);
		} catch (Exception e) {
			return new ApiMessage(200, "请输入支出金额", 0);
		}
		
		// 计算需要AA的金额
		double arityAmount = 0.0;
		// 是否有预算总金额
		if (amount == null) {
			arityAmount = Arith.sub(nowAmount, fee);
		}else {
			if (amount < nowAmount) {
				return new ApiMessage(200, "您最多可以退还" + amount, 0);
			}
			arityAmount = Arith.sub(Arith.sub(amount, fee), nowAmount);
		}

		// 计算每人需要AA的金额
		arityAmount = Arith.round(Arith.div(arityAmount, number), 2);

		if (arityAmount > 0) {
			return new ApiMessage(200, "计算成功", arityAmount);
		} else {
			return new ApiMessage(200, "每人退费金额不足0.01", arityAmount);
		}
	}
	
}
