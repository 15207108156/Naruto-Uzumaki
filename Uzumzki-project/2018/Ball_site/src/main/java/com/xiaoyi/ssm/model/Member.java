package com.xiaoyi.ssm.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 会员表实体
 */
public class Member implements Serializable {
	private String sessionKey;
	/** 获取用户信息的access_token凭证 */
	private String access_token_app;
	/** 刷新access_token的凭证 */
	private String refresh_token_app;
    
	public String getSessionKey() {
		return sessionKey;
	}

	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}
	
    public String getAccess_token_app() {
		return access_token_app;
	}

	public void setAccess_token_app(String access_token_app) {
		this.access_token_app = access_token_app;
	}

	public String getRefresh_token_app() {
		return refresh_token_app;
	}

	public void setRefresh_token_app(String refresh_token_app) {
		this.refresh_token_app = refresh_token_app;
	}
    /** 会员 */
    private String id;

    /** 会员编号 */
    private Integer memberno;

    /** 创建时间 */
    private Date createtime;

    /** 修改时间 */
    private Date modifytime;

    /** 姓名 */
    private String name;

    /** 手机号 */
    private String phone;

    /** 微信公众号的openid */
    private String openid;

    /** 小程序的openid */
    private String appopenid;

    /** APP客户端的openid */
    private String appiosopenid;

    /** 公众平台下的唯一标识 */
    private String unionid;

    /** 微信昵称 */
    private String appnickname;

    /** 微信头像URL */
    private String appavatarurl;

    /** 微信性别(0=未知1=女2=男) */
    private Integer appgender;

    /** 微信号 */
    private String wechatid;

    /** 微信转账免手续费额度 */
    private Double wxpayment;

    /** 是否显示手机号 */
    private Integer showphone;

    /** 是否显示历史活动 */
    private Integer showhistory;

    /** 是否显示微信号 */
    private Integer showwechatid;

    /**
     * Member
     */
    private static final long serialVersionUID = 1L;

    /**
     * 会员
     * @return ID 会员
     */
    public String getId() {
        return id;
    }

    /**
     * 会员
     * @param id 会员
     */
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    /**
     * 会员编号
     * @return MemberNo 会员编号
     */
    public Integer getMemberno() {
        return memberno;
    }

    /**
     * 会员编号
     * @param memberno 会员编号
     */
    public void setMemberno(Integer memberno) {
        this.memberno = memberno;
    }

    /**
     * 创建时间
     * @return CreateTime 创建时间
     */
    public Date getCreatetime() {
        return createtime;
    }

    /**
     * 创建时间
     * @param createtime 创建时间
     */
    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    /**
     * 修改时间
     * @return ModifyTime 修改时间
     */
    public Date getModifytime() {
        return modifytime;
    }

    /**
     * 修改时间
     * @param modifytime 修改时间
     */
    public void setModifytime(Date modifytime) {
        this.modifytime = modifytime;
    }

    /**
     * 姓名
     * @return Name 姓名
     */
    public String getName() {
        return name;
    }

    /**
     * 姓名
     * @param name 姓名
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * 手机号
     * @return Phone 手机号
     */
    public String getPhone() {
        return phone;
    }

    /**
     * 手机号
     * @param phone 手机号
     */
    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    /**
     * 微信公众号的openid
     * @return OpenID 微信公众号的openid
     */
    public String getOpenid() {
        return openid;
    }

    /**
     * 微信公众号的openid
     * @param openid 微信公众号的openid
     */
    public void setOpenid(String openid) {
        this.openid = openid == null ? null : openid.trim();
    }

    /**
     * 小程序的openid
     * @return AppOpenID 小程序的openid
     */
    public String getAppopenid() {
        return appopenid;
    }

    /**
     * 小程序的openid
     * @param appopenid 小程序的openid
     */
    public void setAppopenid(String appopenid) {
        this.appopenid = appopenid == null ? null : appopenid.trim();
    }

    /**
     * APP客户端的openid
     * @return AppIosOpenID APP客户端的openid
     */
    public String getAppiosopenid() {
        return appiosopenid;
    }

    /**
     * APP客户端的openid
     * @param appiosopenid APP客户端的openid
     */
    public void setAppiosopenid(String appiosopenid) {
        this.appiosopenid = appiosopenid == null ? null : appiosopenid.trim();
    }

    /**
     * 公众平台下的唯一标识
     * @return UnionID 公众平台下的唯一标识
     */
    public String getUnionid() {
        return unionid;
    }

    /**
     * 公众平台下的唯一标识
     * @param unionid 公众平台下的唯一标识
     */
    public void setUnionid(String unionid) {
        this.unionid = unionid == null ? null : unionid.trim();
    }

    /**
     * 微信昵称
     * @return AppNickName 微信昵称
     */
    public String getAppnickname() {
        return appnickname;
    }

    /**
     * 微信昵称
     * @param appnickname 微信昵称
     */
    public void setAppnickname(String appnickname) {
        this.appnickname = appnickname == null ? null : appnickname.trim();
    }

    /**
     * 微信头像URL
     * @return AppAvatarUrl 微信头像URL
     */
    public String getAppavatarurl() {
        return appavatarurl;
    }

    /**
     * 微信头像URL
     * @param appavatarurl 微信头像URL
     */
    public void setAppavatarurl(String appavatarurl) {
        this.appavatarurl = appavatarurl == null ? null : appavatarurl.trim();
    }

    /**
     * 微信性别(0=未知1=女2=男)
     * @return AppGender 微信性别(0=未知1=女2=男)
     */
    public Integer getAppgender() {
        return appgender;
    }

    /**
     * 微信性别(0=未知1=女2=男)
     * @param appgender 微信性别(0=未知1=女2=男)
     */
    public void setAppgender(Integer appgender) {
        this.appgender = appgender;
    }

    /**
     * 微信号
     * @return WechatID 微信号
     */
    public String getWechatid() {
        return wechatid;
    }

    /**
     * 微信号
     * @param wechatid 微信号
     */
    public void setWechatid(String wechatid) {
        this.wechatid = wechatid == null ? null : wechatid.trim();
    }

    /**
     * 微信转账免手续费额度
     * @return WXPayment 微信转账免手续费额度
     */
    public Double getWxpayment() {
        return wxpayment;
    }

    /**
     * 微信转账免手续费额度
     * @param wxpayment 微信转账免手续费额度
     */
    public void setWxpayment(Double wxpayment) {
        this.wxpayment = wxpayment;
    }

    /**
     * 是否显示手机号
     * @return ShowPhone 是否显示手机号
     */
    public Integer getShowphone() {
        return showphone;
    }

    /**
     * 是否显示手机号
     * @param showphone 是否显示手机号
     */
    public void setShowphone(Integer showphone) {
        this.showphone = showphone;
    }

    /**
     * 是否显示历史活动
     * @return ShowHistory 是否显示历史活动
     */
    public Integer getShowhistory() {
        return showhistory;
    }

    /**
     * 是否显示历史活动
     * @param showhistory 是否显示历史活动
     */
    public void setShowhistory(Integer showhistory) {
        this.showhistory = showhistory;
    }

    /**
     * 是否显示微信号
     * @return ShowWechatID 是否显示微信号
     */
    public Integer getShowwechatid() {
        return showwechatid;
    }

    /**
     * 是否显示微信号
     * @param showwechatid 是否显示微信号
     */
    public void setShowwechatid(Integer showwechatid) {
        this.showwechatid = showwechatid;
    }
}