package com.shineoxygen.work.config.schedule;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 
 * @author 王辉阳
 * @date 2016年11月16日 下午11:43:50
 * @Description 开启@Schedule注解，定时任务注解，等价xml配置<task:annotation-driven />
 */
@Configuration
@EnableScheduling
public class EnableScheduleConfig {

}