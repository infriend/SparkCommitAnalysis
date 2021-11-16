package com.nju.sparkcommitanalysis.service;

import java.util.List;
import java.util.concurrent.Future;

public interface sparkService {
    //async example
    public Future<List<Long>> generateReport();
    public Integer doneSoFar();
}
