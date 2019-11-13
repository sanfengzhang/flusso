package com.hanl.datamgr.repository;

import com.hanl.datamgr.entity.JobEntity;
import com.hanl.datamgr.repository.support.BaseRepository;
import org.springframework.stereotype.Repository;

/**
 * @author: Hanl
 * @date :2019/9/30
 * @desc:
 */
@Repository
public interface JobRepository extends BaseRepository<JobEntity, String> {

}
