package com.ruiec.web.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;
/**
 * 
 * 版权所有：深圳源中瑞科技有限公司<br>
 * 网 址：www.ruiec.com<br>
 * 电 话：0755-33581131<br><br>
 * 
 * 系统参数数字范围验证器
 * @author 肖学良<br>
 * Version: 1.0<br>
 * Date: 2015年3月25日
 */
@Target({ElementType.FIELD, ElementType.METHOD})  
@Retention(RetentionPolicy.RUNTIME)  
@Constraint(validatedBy = SettingNumLimitValidator.class)  
@Documented 
public @interface SettingNumLimit {
	String message() default "";  
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    
    /**
     * 最小值字段名称
     * @author 肖学良<br>
     * Date: 2015年3月25日
     */
    String min() default "";
    /**
     * 最大值字段名称
     * @author 肖学良<br>
     * Date: 2015年3月25日
     */
    String max() default "";
}
