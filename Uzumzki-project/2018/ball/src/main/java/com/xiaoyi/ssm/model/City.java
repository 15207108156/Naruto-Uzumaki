package com.xiaoyi.ssm.model;

import java.io.Serializable;

/**
 * 城市表实体
 */
public class City implements Serializable {
    /** 系统->城市 */
    private String id;

    /** 城市编号 */
    private Integer cityno;

    /** 城市名 */
    private String city;

    /**
     * City
     */
    private static final long serialVersionUID = 1L;

    /**
     * 系统->城市
     * @return ID 系统->城市
     */
    public String getId() {
        return id;
    }

    /**
     * 系统->城市
     * @param id 系统->城市
     */
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    /**
     * 城市编号
     * @return CityNo 城市编号
     */
    public Integer getCityno() {
        return cityno;
    }

    /**
     * 城市编号
     * @param cityno 城市编号
     */
    public void setCityno(Integer cityno) {
        this.cityno = cityno;
    }

    /**
     * 城市名
     * @return City 城市名
     */
    public String getCity() {
        return city;
    }

    /**
     * 城市名
     * @param city 城市名
     */
    public void setCity(String city) {
        this.city = city == null ? null : city.trim();
    }
}