package com.hanl.datamgr.repository;

import com.hanl.datamgr.entity.CanvasCommandInstanceEntity;
import com.hanl.datamgr.repository.support.BaseRepository;

import java.util.Set;
import java.util.List;

/**
 * @author: Hanl
 * @date :2019/10/24
 * @desc:
 */
public interface CanvasCommandInstanceRepository extends BaseRepository<CanvasCommandInstanceEntity,String> {

    public List<CanvasCommandInstanceEntity> findAllByIdIn(Set<String> ids);

    public void deleteAllByIdIn(Set<String> ids);
}
