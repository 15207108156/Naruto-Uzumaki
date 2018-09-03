package com.xiaoyi.ssm.service;

import com.xiaoyi.ssm.model.Member;

/**  
 * @Description: 用户业务逻辑接口
 * @author 宋高俊  
 * @date 2018年8月16日 下午4:33:14 
 */ 
public interface MemberService extends BaseService<Member, String> {

	/**  
	 * @Description: 微信端用户登录接口
	 * @author 宋高俊  
	 * @date 2018年8月16日 下午4:34:06 
	 */ 
	Member login(Member member);

	/**  
	 * @Description: 根据手机号查询用户
	 * @author 宋高俊  
	 * @date 2018年8月16日 下午5:06:01 
	 */ 
	Member selectByPhone(String phone);

	/**  
	 * @Description: 根据手机号修改密码
	 * @author 宋高俊  
	 * @date 2018年8月16日 下午5:27:29 
	 */ 
	int updateByPhone(String phone, String password);
	
}
