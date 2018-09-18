package com.xiaoyi.ssm.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 实体
 */
public class VenueTeach implements Serializable {
    /** 场地教学表 */
    private String id;

    /** 创建时间 */
    private Date createtime;

    /** 教学日期 */
    private Date teachdate;

    /** 场馆ID */
    private String venueid;

    /** 场地ID */
    private String fieldid;

    /** 内容 */
    private String content;

    /** 是否设置(0=否1=是) */
    private Integer setting;

    /** 管理员ID */
    private String managerid;

    /** 教学时间段 */
    private String venuetimeframe;

    /**
     * VenueTeach
     */
    private static final long serialVersionUID = 1L;

    /**
     * 场地教学表
     * @return ID 场地教学表
     */
    public String getId() {
        return id;
    }

    /**
     * 场地教学表
     * @param id 场地教学表
     */
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
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
     * 教学日期
     * @return TeachDate 教学日期
     */
    public Date getTeachdate() {
        return teachdate;
    }

    /**
     * 教学日期
     * @param teachdate 教学日期
     */
    public void setTeachdate(Date teachdate) {
        this.teachdate = teachdate;
    }

    /**
     * 场馆ID
     * @return VenueID 场馆ID
     */
    public String getVenueid() {
        return venueid;
    }

    /**
     * 场馆ID
     * @param venueid 场馆ID
     */
    public void setVenueid(String venueid) {
        this.venueid = venueid == null ? null : venueid.trim();
    }

    /**
     * 场地ID
     * @return FieldID 场地ID
     */
    public String getFieldid() {
        return fieldid;
    }

    /**
     * 场地ID
     * @param fieldid 场地ID
     */
    public void setFieldid(String fieldid) {
        this.fieldid = fieldid == null ? null : fieldid.trim();
    }

    /**
     * 内容
     * @return Content 内容
     */
    public String getContent() {
        return content;
    }

    /**
     * 内容
     * @param content 内容
     */
    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    /**
     * 是否设置(0=否1=是)
     * @return Setting 是否设置(0=否1=是)
     */
    public Integer getSetting() {
        return setting;
    }

    /**
     * 是否设置(0=否1=是)
     * @param setting 是否设置(0=否1=是)
     */
    public void setSetting(Integer setting) {
        this.setting = setting;
    }

    /**
     * 管理员ID
     * @return ManagerID 管理员ID
     */
    public String getManagerid() {
        return managerid;
    }

    /**
     * 管理员ID
     * @param managerid 管理员ID
     */
    public void setManagerid(String managerid) {
        this.managerid = managerid == null ? null : managerid.trim();
    }

    /**
     * 教学时间段
     * @return VenueTimeFrame 教学时间段
     */
    public String getVenuetimeframe() {
        return venuetimeframe;
    }

    /**
     * 教学时间段
     * @param venuetimeframe 教学时间段
     */
    public void setVenuetimeframe(String venuetimeframe) {
        this.venuetimeframe = venuetimeframe == null ? null : venuetimeframe.trim();
    }
}