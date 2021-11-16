package com.nju.sparkcommitanalysis;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Repository;

@MapperScan(
        basePackages = "com.nju.sparkcommitanalysis.dao",
        annotationClass = Repository.class
)
@EnableAsync
@SpringBootApplication
public class SparkCommitAnalysisApplication {

    public static void main(String[] args) {
        SpringApplication.run(SparkCommitAnalysisApplication.class, args);
    }

}
