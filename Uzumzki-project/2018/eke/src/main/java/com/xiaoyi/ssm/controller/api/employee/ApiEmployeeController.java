package com.xiaoyi.ssm.controller.api.employee;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xiaoyi.ssm.dto.ApiMessage;
import com.xiaoyi.ssm.util.Global;
import com.xiaoyi.ssm.util.RedisUtil;

/**  
 * @Description: 经济人接口控制器
 * @author 宋高俊  
 * @date 2018年7月18日 下午4:05:38 
 */ 
@Controller
@RequestMapping("api/employee")
public class ApiEmployeeController {
	
	/**  
	 * @Description: 退出登录
	 * @author 宋高俊  
	 * @date 2018年7月18日 下午4:51:02 
	 */ 
	@RequestMapping(value = "/logout" , method = RequestMethod.POST)
	@ResponseBody
	public ApiMessage logout(String token) {
		RedisUtil.delRedis(Global.redis_employee, token);
		return ApiMessage.succeed("退出成功");
	}
}
