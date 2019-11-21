package com.hanl.datamgr.repository;

import com.hanl.datamgr.entity.CommandInstanceEntity;
import com.hanl.datamgr.repository.support.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * @author: Hanl
 * @date :2019/9/30
 * @desc:
 */
@Repository
public interface CommandInstanceRepository extends BaseRepository<CommandInstanceEntity, String> {

    public List<CommandInstanceEntity> findAllByIdIn(Set<String> ids);


}
