package cn.suming.config;

import cn.suming.bean.CustomBean;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author suming
 * @since 2025/5/20 16:10
 */
@Configuration(proxyBeanMethods = false)
public class MyConfiguration {

	@Bean
	public CustomBean getCustomBean(){
		return new CustomBean();
	}
}
