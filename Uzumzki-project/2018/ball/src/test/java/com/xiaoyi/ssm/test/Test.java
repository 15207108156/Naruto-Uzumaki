package com.xiaoyi.ssm.test;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadPoolExecutor;

import org.aspectj.weaver.ast.And;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.xiaoyi.ssm.util.DateUtil;
import com.xiaoyi.ssm.util.Global;
import com.xiaoyi.ssm.util.HttpUtils;
import com.xiaoyi.ssm.util.RedisUtil;
import com.xiaoyi.ssm.util.Utils;
import com.xiaoyi.ssm.wxPay.HttpUtil;

import net.sf.json.JSONObject;

public class Test {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	public static void main(String[] args) {
//		String fileName = "image/png";
//		fileName = fileName.replace("/", ".");
//		
//		fileName = Utils.getUUID() + fileName.substring(fileName.lastIndexOf("."));
//		System.out.println(fileName);
//		List<Person> list = new ArrayList<>();
//		list.add(new Person("jack", 20));
//		list.add(new Person("mike", 25));
//		list.add(new Person("tom", 30));
//		List<String> newlist =  list.stream().map(Person::getName).collect(null);
//		for (int i = 0; i < newlist.size(); i++) {
//			System.out.println(newlist.get(i));
//		}

//		// 创建一个数值格式化对象
//		NumberFormat numberFormat = NumberFormat.getInstance();
//		// 设置精确到小数点后2位
//		numberFormat.setMaximumFractionDigits(0);
////		String result = numberFormat.format((float) 5 / (float) (20) * 100);
//		double result = ((float) 5 / (float) (9) * 100);
//		if (result <= 33) {
//			System.out.println(result);
//			System.out.println("低层");
//		}else if (33 < result && result <= 66) {
//			System.out.println(result);
//			System.out.println("中层");
//		}else if (result > 66) {
//			System.out.println(result);
//			System.out.println("高层");
//		}
//		new Timer("timer - " + 0).schedule(new TimerTask() {
//            @Override
//            public void run() {
//                System.out.println(Thread.currentThread().getName() + " run ");
//            }
//        }, 1000);
//		
//		for (int i = 0; i < 10; ++i) {
//            new Timer("timer - " + i).schedule(new TimerTask() {
//                @Override
//                public void run() {
//                    System.out.println(Thread.currentThread().getName() + " run ");
//                }
//            }, 5000);
//        }
//		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//		executor.setCorePoolSize(10);
//		executor.setMaxPoolSize(15);
//		executor.setKeepAliveSeconds(1);
//		executor.setQueueCapacity(5);
//		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
//		executor.initialize();
//		executor.execute(new Runnable() {
//			@Override
//			public void run() {
//				//执行的代码
//				System.out.println(1);
//			}
//			
//		});
//		executor.execute(new Runnable() {
//			@Override
//			public void run() {
//				//执行的代码
//				new Timer("订单支付超时，已关闭").schedule(new TimerTask() {
//		            @Override
//		            public void run() {
//		                System.out.println(2);
//		            }
//		        }, 5000);
//			}
//		});
//		int sum = (new BigDecimal(Double.toString(10.03)).multiply(new BigDecimal("100"))).intValue();
//		System.out.println(sum);
		
//		String data = HttpUtils.sendGet("https://api.weixin.qq.com/cgi-bin/token", "grant_type=client_credential&appid=wxa76f9c07c99468b2&secret=3caf90ffa0f40292524eba00588f6e0d");
//		JSONObject jsonObject = JSONObject.fromObject(data);
//		System.out.println(jsonObject.get("access_token"));
		@SuppressWarnings("unchecked")
		Map<String, Object> map = (Map<String, Object>) RedisUtil.getRedisOne(Global.REDIS_ACCESS_TOKEN, "wxa76f9c07c99468b2"); 
		System.out.println(map.get("access_token"));
	}
}