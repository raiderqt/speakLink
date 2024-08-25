package com.example.SpeakLink.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class WebConfig implements WebMvcConfigurer
{
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/css/**")
				.addResourceLocations("classpath:/css/");
		registry.addResourceHandler("/js/**")
				.addResourceLocations("classpath:/js/");
		registry.addResourceHandler("/img/**")
				.addResourceLocations("classpath:/img/");
		registry.addResourceHandler("/img/bootstrap-icons/fonts/**")
				.addResourceLocations("classpath:/img/bootstrap-icons/fonts/");
	}
}