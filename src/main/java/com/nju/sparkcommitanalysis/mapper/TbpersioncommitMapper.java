package com.nju.sparkcommitanalysis.mapper;

import com.nju.sparkcommitanalysis.domain.Tbpersioncommit;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* @author infriend
* @description 针对表【tbPersioncommit】的数据库操作Mapper
* @createDate 2021-11-16 23:49:54
* @Entity com.nju.sparkcommitanalysis.domain.Tbpersioncommit
*/
@Repository
public interface TbpersioncommitMapper {

    List<Tbpersioncommit> countAllCommits();
}
