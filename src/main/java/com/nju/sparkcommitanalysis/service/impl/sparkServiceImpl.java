package com.nju.sparkcommitanalysis.service.impl;

import com.nju.sparkcommitanalysis.service.sparkService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class sparkServiceImpl implements sparkService {
    private List<Long> tasksList;
    private AtomicBoolean inProgress = new AtomicBoolean(false);
    private static String[] fileList;

    static {
        File f = new File("dataset");
        fileList = f.list();
    }

    @Override
    @Async
    public Future<List<Long>> generateReport() {
        if (inProgress.compareAndSet(false, true)) {
            tasksList = Collections.synchronizedList(new ArrayList<Long>());

            for (long i=0 ; i<250000000000l ; i++) {
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("Cancelled");
                    inProgress.set(false);
                    return new AsyncResult<List<Long>>(tasksList);
                }
                tasksList.add(Long.valueOf(i));

                try {
                    TimeUnit.MILLISECONDS.sleep(1);
                } catch (InterruptedException e) {
                    System.out.println("Cancelled in sleep");
                    // thread might get interrupted during sleep not only during work
                    return new AsyncResult<List<Long>>(tasksList);
                }
            }
        }
        return new AsyncResult<List<Long>>(tasksList);
    }

    public Integer doneSoFar() {
        return tasksList.size() ;
    }
}
