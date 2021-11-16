package com.nju.sparkcommitanalysis.controller;

import com.nju.sparkcommitanalysis.domain.Tbpersioncommit;
import com.nju.sparkcommitanalysis.service.sparkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Controller
public class sparkController {
    @Autowired
    private sparkService sparkservice;
    private Future<List<Long>> numberListTask;

    //1.主页（开始按钮）


    //2.展示页面（echart）开始复制文件至hdfs（异步，每0.5s上传一个新文件）同时返回展示视图

    @RequestMapping(value = "/startJob")
    public ResponseEntity<String> startTask() {
        numberListTask = sparkservice.generateReport();
        return ResponseEntity.status(200).body("job started");
    }

    //3.返回数据库数据至前端（ajax请求处理，请0.1s请求一次）
    @RequestMapping(value = "/getdata")
    @ResponseBody
    public List<Tbpersioncommit> getData(){
        return sparkservice.getData();
    }


}
