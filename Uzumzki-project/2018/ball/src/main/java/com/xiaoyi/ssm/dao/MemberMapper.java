package com.xiaoyi.ssm.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xiaoyi.ssm.model.Member;

public interface MemberMapper extends BaseMapper<Member, String>{

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
	int updateByPhone(@Param("phone")String phone, @Param("password")String password);

	/**  
	 * @Description: 根据openid查询用户
	 * @author 宋高俊  
	 * @param openid
	 * @return 
	 * @date 2018年9月10日 下午8:40:45 
	 */ 
	Member selectByOpenid(String openid);
	
	/**  
	 * @Description: 根据Unionid查询用户
	 * @author 宋高俊  
	 * @param unionid
	 * @return 
	 * @date 2018年9月13日 上午11:35:24 
	 */ 
	Member selectByUnionid(String unionid);

	/**  
	 * @Description: 根据AppIosOpenID查询用户
	 * @author 宋高俊  
	 * @param token
	 * @return 
	 * @date 2018年10月23日 上午11:41:32 
	 */ 
	Member selectByAppIosOpenID(String token);

	/**  
	 * @Description: 查询在app登录过的用户
	 * @author 宋高俊  
	 * @return 
	 * @date 2018年10月23日 上午11:57:56 
	 */ 
	List<Member> selectByApp();

	/**  
	 * @Description: 根据用户ID删除openid
	 * @author 宋高俊  
	 * @return 
	 * @date 2018年10月23日 上午11:57:56 
	 */ 
	int updateByMemberOpenID(String id);

	/**
	 * @Description: 根据用户编号查询用户
	 * @author 宋高俊
	 * @param memberno
	 * @return
	 * @date 2018年11月28日上午11:18:43
	 */
	Member selectByMemberno(Integer memberno);

	/**
	 * @Description: 根据场馆ID查询机构回款人信息
	 * @author 宋高俊
	 * @param venueid
	 * @return
	 * @date 2018年11月30日下午5:01:25
	 */
	Member selectByVenueId(String venueId);

	/**
	 * @Description: 根据日期查询回款用户
	 * @author 宋高俊
	 * @param date
	 * @return
	 * @date 2018年12月4日下午5:23:03
	 */
	List<Member> selectByDateOut(String date);
	
}