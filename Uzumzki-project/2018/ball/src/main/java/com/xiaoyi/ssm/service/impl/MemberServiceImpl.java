package com.xiaoyi.ssm.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiaoyi.ssm.dao.MemberMapper;
import com.xiaoyi.ssm.model.Member;
import com.xiaoyi.ssm.service.MemberService;

/**  
 * @Description: 用户业务逻辑实现
 * @author 宋高俊  
 * @date 2018年8月16日 下午4:35:09 
 */ 
@Service
public class MemberServiceImpl extends AbstractService<Member,String> implements MemberService{

	@Autowired
	private MemberMapper memberMapper;
	
	@Override
	public void setBaseMapper() {
	    super.setBaseMapper(memberMapper);
	}

	/**  
	 * @Description: 微信端用户登录接口
	 * @author 宋高俊  
	 * @date 2018年8月16日 下午4:34:06 
	 */ 
	@Override
	public Member login(Member member) {
		return memberMapper.login(member);
	}

	/**  
	 * @Description: 根据手机号查询用户
	 * @author 宋高俊  
	 * @date 2018年8月16日 下午5:06:01 
	 */ 
	@Override
	public Member selectByPhone(String phone) {
		return memberMapper.selectByPhone(phone);
	}
	
	/**  
	 * @Description: 根据手机号修改密码
	 * @author 宋高俊  
	 * @date 2018年8月16日 下午5:27:29 
	 */ 
	@Override
	public int updateByPhone(String phone, String password) {
		return memberMapper.updateByPhone(phone, password);
	}

}
