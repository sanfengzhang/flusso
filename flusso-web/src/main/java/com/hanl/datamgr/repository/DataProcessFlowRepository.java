package com.hanl.datamgr.repository;

import com.hanl.datamgr.entity.DataProcessFlowEntity;
import com.hanl.datamgr.repository.support.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author: Hanl
 * @date :2019/9/30
 * @desc:
 */
@Repository
public interface DataProcessFlowRepository extends BaseRepository<DataProcessFlowEntity, String> {

    public Optional<DataProcessFlowEntity> findByDataProcessFlowName(String flowName);
}
