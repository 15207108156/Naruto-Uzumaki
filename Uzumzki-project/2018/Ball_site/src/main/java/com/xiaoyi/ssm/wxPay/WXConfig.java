package com.xiaoyi.ssm.wxPay;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.xiaoyi.ssm.util.Global;
import com.xiaoyi.ssm.util.RedisUtil;

/**  
 * @Description: 微信的配置参数
 * @author 宋高俊  
 * @date 2018年8月30日 下午7:25:22 
 */ 
public class WXConfig {

	/** 公众号id 公司提供wx3d5beb67c8455dd2 */
	public static final String appid = "wxa76f9c07c99468b2";
//	public static final String appid = "wx2efc8721a286a255";
	public static final String appid_web = "wxa6c95fe48935f880";// 网站应用
	public static final String appid_app = "wxa9e29d03b2a14aa4";// 移动应用

	/** 公众号密钥，公司提供aae6b38f32894df280e79da3a53043e3 */
	public static final String appSecret = "3caf90ffa0f40292524eba00588f6e0d";
//	public static final String appSecret = "9a198b8c3497aafa8c71d0397b67d0fb";
	public static final String appSecret_web = "4920697bf2410d51ed708729fad2e5d8";// 网站应用
	public static final String appSecret_app = "f543351fee9f76359f31bbb43eda7cbc";// 移动应用

	/** 商户号 公司提供 */
	public static final String mch_id = "1390967002";

	/** 商户号密钥，公司提供 */
	public static final String paternerKey = "ekeEKE00000000000000000000000000";

	/** 小程序订场支付通知地址 */
	public static final String NOTIFY_URL1 = "https://ball.ekeae.com/WebBackAPI/venue/manager/order/weixinNotify";
	/** 公众号约球支付通知地址 */
	public static final String NOTIFY_URL2 = "https://ball.ekeae.com/WebBackAPI/venue/manager/order/weixinNotify";
	/** 小程序约球支付通知地址 */
	public static final String APPNOTIFY_URL = "https://ball.ekeae.com/WebBackAPI/wxapp/inviteBall/weixinNotify";
	
	/** AA退费补录支付通知地址 */
	public static final String NOTIFY_URL3 = "https://ball.ekeae.com/WebBackAPI/wxapp/inviteBall/weixinAANotify";
	
	/** 培训支付通知地址 */
	public static final String NOTIFY_URL4 = "https://ball.ekeae.com/WebBackAPI/wxapp/train/course/weixinNotify";
	

	/** 统一下单 https://api.mch.weixin.qq.com/pay/unifiedorder */
	public static final String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
	
    /** 微信退款url  微信端提供的退款接口 */
    public static final String gaterefund_url = "https://api.mch.weixin.qq.com/secapi/pay/refund";
    /** 微信企业付款url */
    public static final String payment = "https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers";


	/** 微信小程序的appid wxa9512997005647d0 */
	public static final String wxAppAppid = "wx0f3b253f4d82700e";
	/** 微信小程序的App_secret 362a0ca1db12ed44e0dec2a54f8184a2 */
	public static final String wxAppApp_secret = "0535a52ebf516b2650f92cfecdbcced1";
	/** 微信小程序的商户号 */
	public static final String wxAppMch_id = "1390967002";
    
    
	/** 
	 * 活动报名成功通知模板<br>
	 * 活动名称keyword1<br>
	 * 活动说明keyword2<br>
	 * 活动时间keyword3<br>
	 * 活动地址keyword4<br>
	 * 联系电话keyword5<br>
	 **/
	public static final String templateId1 = "A04m33_InQGNZafKK1wbFDBWSwVJKORS_moY_MjSXdM";
	/** 
	 * 退款通知模板<br>
	 * 退款金额keyword1<br>
	 * 退款时间keyword2<br>
	 * 订单编号keyword3<br>
	 **/
	public static final String templateId2 = "8dfDOqYsBb2vDxEWmp4DZjdm5rgBXmxW3iUqGBRPz1s";
	/** 
	 * 活动取消通知模板<br>
	 * 活动名称keyword1<br>
	 * 活动地点keyword2<br>
	 * 活动时间keyword3<br>
	 **/
	public static final String templateId3 = "nBeb55DKilg5pHld25X4FRRXYpoarFaxcnbft79t63w";
	
	

	/** 
	 * 订单进度提醒<br>
	 * first<br>
	 * 订单编号keyword1<br>
	 * 订单内容keyword2<br>
	 * 订单状态keyword2<br>
	 * remark<br>
	 **/
	public static final String wxTemplateId = "k5a271O9L4aW1JGxeUroqGpLn0bJhz-f6JNciftAkIc";
	
	
	// 获取ip
	public static String getIP(HttpServletRequest request) {
		String ip = request.getRemoteAddr();
		return ip;
	}
	
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		Map<String, Object> map = (Map<String, Object>) RedisUtil.getRedisOne(Global.REDIS_ACCESS_TOKEN, appid);
		System.out.println(map.get("access_token"));
	}
	
}
