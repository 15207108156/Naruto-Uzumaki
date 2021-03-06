package com.ruiec.web.dto;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import com.ruiec.web.common.LoginUserUnit;
import com.ruiec.web.entity.User;

/**
 * 合成研判任务的参数
 * @author Senghor<br>
 * @date 2018年1月8日 上午11:14:24
 */
public class TaskDTO implements Serializable{

	private static final long serialVersionUID = 2881126805933601170L;
	private User user;//登录用户           
	private LoginUserUnit loginUserUnit;//登录用户管理的单位
	private Integer[] areaUnitIds;//分县局单位ID数组
	private Integer[] areaUnitPoliceIds;//支队单位ID数组
	private Integer taskId;//任务ID
	private Integer taskDetailsId;//任务详情ID
	private String[] fileUrls;//所有附件的地址数组
	private Integer urgency;//任务级别1紧急2不紧急
	private String taskName;//任务名称
	private Integer isCheck;//是否通过审核1通过2不通过
	private String content;//存储各种理由数据
	private Integer type;//任务状态1=待审核2=待签收待反馈
	private String status;//任务状态(0=待审核、1=待签收、2=进行中、3=已结束)

	public void init(HttpServletRequest request) {
		this.user = (User) request.getSession().getAttribute("user");
		this.loginUserUnit = (LoginUserUnit) request.getSession().getAttribute("loginUserUnit");
	}
	
	public User getUser() {
		return user;
	}
	public LoginUserUnit getLoginUserUnit() {
		return loginUserUnit;
	}

	public Integer[] getAreaUnitIds() {
		return areaUnitIds;
	}

	public void setAreaUnitIds(Integer[] areaUnitIds) {
		this.areaUnitIds = areaUnitIds;
	}

	public Integer[] getAreaUnitPoliceIds() {
		return areaUnitPoliceIds;
	}

	public void setAreaUnitPoliceIds(Integer[] areaUnitPoliceIds) {
		this.areaUnitPoliceIds = areaUnitPoliceIds;
	}

	public Integer getTaskId() {
		return taskId;
	}

	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}

	public String[] getFileUrls() {
		return fileUrls;
	}

	public void setFileUrls(String[] fileUrls) {
		this.fileUrls = fileUrls;
	}

	public Integer getUrgency() {
		return urgency;
	}

	public void setUrgency(Integer urgency) {
		this.urgency = urgency;
	}

	public Integer getTaskDetailsId() {
		return taskDetailsId;
	}

	public void setTaskDetailsId(Integer taskDetailsId) {
		this.taskDetailsId = taskDetailsId;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public Integer getIsCheck() {
		return isCheck;
	}

	public void setIsCheck(Integer isCheck) {
		this.isCheck = isCheck;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    /** status */
    public String getStatus() {
        return status;
    }

    /** status */
    public void setStatus(String status) {
        this.status = status;
    }
	
}
