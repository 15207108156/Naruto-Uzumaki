package com.ruiec.springboot.util;

import java.io.Serializable;

/**
 * 响应传输对象
 * 
 * @author mrluo735
 *
 */
public class ResponseDTO implements Serializable {
	private static final long serialVersionUID = 3315965630264310008L;

	/**
	 * 
	 */
	private boolean success = true;

	private int code = 200;

	private String message = "ok";

	private Object data;

	public ResponseDTO() {
	}

	/**
	 * 获取是否成功
	 * 
	 * @return
	 */
	public boolean isSuccess() {
		return success;
	}

	/**
	 * 设置是否成功
	 * 
	 * @param success
	 */
	public void setSuccess(boolean success) {
		this.success = success;
	}

	/**
	 * 获取代码
	 * 
	 * @return
	 */
	public int getCode() {
		return this.code;
	}

	/**
	 * 设置代码
	 * 
	 * @param code
	 */
	public void setCode(int code) {
		this.code = code;
	}

	/**
	 * 获取提示信息
	 * 
	 * @return
	 */
	public String getMessage() {
		return this.message;
	}

	/**
	 * 设置提示信息
	 * 
	 * @param message
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * 获取数据
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T getData() {
		return (T) data;
	}

	/**
	 * 设置数据
	 * 
	 * @param data
	 */
	public <T> void setData(T data) {
		this.data = data;
	}
}
