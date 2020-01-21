package com.example.university.distributeddatabase.config;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SparkConfig {

	@Value("${spark.app.name}")
	private String appName;
	@Value("${spark.master}")
	private String masterUri;
	//@Value("${spark.home}")
	//private String sparkHome;

	@Bean
	public SparkConf conf() {
		return new SparkConf().setAppName(appName).setMaster(masterUri);
	}

	@Bean
	public JavaSparkContext sc() {
		System.setProperty("hadoop.home.dir", "C:\\2\\");
		return new JavaSparkContext(conf());
	}

}
