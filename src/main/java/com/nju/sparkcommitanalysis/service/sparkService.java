package com.nju.sparkcommitanalysis.service;

import com.nju.sparkcommitanalysis.domain.Tbpersioncommit;

import java.util.List;
import java.util.concurrent.Future;

public interface sparkService {
    //async example
    public Future<List<Long>> generateReport();
    public Integer doneSoFar();

    List<Tbpersioncommit> getData();
}
