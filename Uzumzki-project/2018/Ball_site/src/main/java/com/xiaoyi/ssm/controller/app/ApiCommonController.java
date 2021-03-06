package com.xiaoyi.ssm.controller.app;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xiaoyi.ssm.dto.ApiMessage;
import com.xiaoyi.ssm.model.Member;
import com.xiaoyi.ssm.service.MemberService;
import com.xiaoyi.ssm.util.Global;
import com.xiaoyi.ssm.util.HttpUtils;
import com.xiaoyi.ssm.util.RedisUtil;
import com.xiaoyi.ssm.util.StringUtil;
import com.xiaoyi.ssm.util.Utils;
import com.xiaoyi.ssm.wxPay.WXConfig;

/**  
 * @Description: 手机客户端公告接口
 * @author 宋高俊  
 * @date 2018年10月19日 下午8:22:24 
 */ 
@Controller("appApiCommonController")
@RequestMapping("app/common")
public class ApiCommonController {
	
    private static Logger logger = Logger.getLogger(ApiCommonController.class.getName());
	@Autowired
	private MemberService memberService;

	/**  
	 * @Description: 客户端使用code换取openid
	 * @author 宋高俊  
	 * @param request
	 * @param code
	 * @return 
	 * @date 2018年10月19日 下午8:28:04 
	 */ 
	@RequestMapping(value = "/codeAndAccessToken", method = RequestMethod.POST)
	@ResponseBody
	public ApiMessage codeAndAccessToken(HttpServletRequest request, String code) {
		if (StringUtil.isBank(code)) {
			return new ApiMessage(400, "code不能为空");
		}
		
		logger.info("客户端code"+code);
		logger.info("开始获取客户端openid");
		JSONObject getCodeResultJson = null;
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			logger.info("开始根据code获取客户端openid");
			String requestUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code"
					.replace("APPID", WXConfig.appid_app).replace("SECRET", WXConfig.appSecret_app).replace("CODE", code);

			String requestResult = HttpUtils.sendGet(requestUrl, null);// 我们需要自己写或者在网上找一个doGet方法发送doGet请求
			getCodeResultJson = JSONObject.fromObject(requestResult);// 把请求成功后的结果转换成JSON对象
			
			logger.info("APP登录的返回信息：getCodeResultJson:" + getCodeResultJson.toString());
			if (getCodeResultJson == null || getCodeResultJson.get("errcode") != null || getCodeResultJson.getString("openid") == null) {
				logger.error("", new Exception("获取回调异常"));
				return new ApiMessage(400, "登录失败", getCodeResultJson);
			}
			String access_token = getCodeResultJson.getString("access_token");
			String refresh_token = getCodeResultJson.getString("refresh_token");
			String openid = getCodeResultJson.getString("openid");
			String unionid = getCodeResultJson.getString("unionid");
			// 微信授权域snsapi_userinfo

			Member member = memberService.selectByUnionid(unionid);
			
			// 获取用户信息
			String userinfo = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID"
					.replace("ACCESS_TOKEN", access_token).replace("OPENID", openid);
			String userinfoReturn = HttpUtils.sendGet(userinfo, null);// 我们需要自己写或者在网上找一个doGet方法发送doGet请求
			
			JSONObject getUserinfoJson = JSONObject.fromObject(userinfoReturn);// 把请求成功后的结果转换成JSON对象
			if (getUserinfoJson == null || getUserinfoJson.get("errcode") != null || getUserinfoJson.getString("openid") == null) {
				logger.error("", new Exception("获取用户信息回调异常"));
				return new ApiMessage(400, "获取失败:"+getUserinfoJson.get("errmsg"));
			}else {
				logger.info("返回用户信息：getUserinfoJson:" + getUserinfoJson.toString());
				
				map.put("token", openid);
				map.put("appavatarurl", getUserinfoJson.getString("headimgurl"));
				map.put("appnickname", getUserinfoJson.getString("nickname"));
				if (member != null) {
					member.setModifytime(new Date());
					member.setUnionid(unionid);
					member.setAppiosopenid(openid);
					member.setAppavatarurl(getUserinfoJson.getString("headimgurl"));
					member.setAppnickname(getUserinfoJson.getString("nickname"));
					member.setAppgender(getUserinfoJson.getInt("sex"));
					memberService.updateByPrimaryKeySelective(member);
				}else {
					member = new Member();
					member.setId(Utils.getUUID());
					member.setCreatetime(new Date());
					member.setModifytime(new Date());
					member.setUnionid(unionid);
					member.setAppiosopenid(openid);
					member.setAppavatarurl(getUserinfoJson.getString("headimgurl"));
					member.setAppnickname(getUserinfoJson.getString("nickname"));
					member.setAppgender(getUserinfoJson.getInt("sex"));
					memberService.insertSelective(member);
				}

				// 保存刷新凭证
				member.setAccess_token_app(access_token);
				member.setRefresh_token_app(refresh_token);
				// 保存用户登录信息
				RedisUtil.addRedis(Global.redis_member_app, openid, member);
			}
		} catch (Exception e) {
			logger.error("", e);
		}
		return new ApiMessage(200, "登录成功", map);
	}
	
	/**  
	 * @Description: 客户端使用code换取openid
	 * @author 宋高俊  
	 * @param request
	 * @param code
	 * @return 
	 * @date 2018年10月19日 下午8:28:04 
	 */ 
	@RequestMapping(value = "/unionidAndUserinfo", method = RequestMethod.POST)
	@ResponseBody
	public ApiMessage unionidAndUserinfo(HttpServletRequest request, String token) {
		if (StringUtil.isBank(token)) {
			return new ApiMessage(400, "token不能为空");
		}
		logger.info("token:" + token);
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			// 第一步获取查询所需的access_token
			Member member = (Member) RedisUtil.getRedisOne(Global.redis_member_app, token);
			if (member == null) {
				return new ApiMessage(400, "用户未登录");
			}
			
			// 获取用户信息
			String userinfo = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID"
					.replace("ACCESS_TOKEN", member.getAccess_token_app()).replace("OPENID", token);
			String userinfoReturn = HttpUtils.sendGet(userinfo, null);
			JSONObject getUserinfoJson = JSONObject.fromObject(userinfoReturn);
			if (getUserinfoJson == null || getUserinfoJson.get("errcode") != null || getUserinfoJson.getString("openid") == null) {
				logger.error("", new Exception("获取用户信息回调异常"));
				return new ApiMessage(400, "获取失败:"+getUserinfoJson.get("errmsg"));
			}else {
				logger.info("返回用户信息：getUserinfoJson:" + getUserinfoJson.toString());
				map.put("appavatarurl", getUserinfoJson.getString("headimgurl"));
				map.put("appnickname", getUserinfoJson.getString("nickname"));
			}
		} catch (Exception e) {
			logger.error("", e);
		}
		return new ApiMessage(200, "登录成功", map);
	}

}
