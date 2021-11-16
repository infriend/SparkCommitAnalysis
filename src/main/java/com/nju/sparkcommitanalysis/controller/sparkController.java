package com.nju.sparkcommitanalysis.controller;

import com.nju.sparkcommitanalysis.service.sparkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Controller
public class sparkController {
    @Autowired
    private sparkService sparkservice;
    private Future<List<Long>> numberListTask;

    //1.主页（开始按钮）

    //2.展示页面（echart）

    //3.开始复制文件至hdfs（异步，每0.5s上传一个新文件）
    @RequestMapping(value = "/startJob")
    public ResponseEntity<String> startTask() {
        numberListTask = sparkservice.generateReport();
        return ResponseEntity.status(200).body("job started");
    }

    @RequestMapping(value = "/stopJob")
    public ResponseEntity<String> cancel() {
        numberListTask.cancel(true);
        return ResponseEntity.status(200).body("Stopped: ".concat(String.valueOf(numberListTask.isCancelled())));
    }

    @RequestMapping(value = "/jobStatus")
    public ResponseEntity<String> jobStatus() {
        String status = null;
        Integer done = -1;
        try {
            if(numberListTask.isCancelled()) {
                done = sparkservice.doneSoFar();
                status = "Cancelled. " + done + " out of " + 250000000000l;
            } else if (numberListTask.isDone()) {
                done = numberListTask.get().size();
                status = "Done 100%. Size: " + done;
            } else {
                done = sparkservice.doneSoFar();
                status = "Processed: " + done + " of 250000000000";
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return ResponseEntity.status(200).body(status);
    }


    //4.返回数据库数据至前端（ajax请求处理，请0.1s请求一次）

}
