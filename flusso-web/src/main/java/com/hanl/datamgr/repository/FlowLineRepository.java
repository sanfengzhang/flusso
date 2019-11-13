package com.hanl.datamgr.repository;

import com.hanl.datamgr.entity.FlowLineEntity;
import com.hanl.datamgr.repository.support.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author: Hanl
 * @date :2019/10/19
 * @desc:
 */
@Repository
public interface FlowLineRepository extends BaseRepository<FlowLineEntity, String> {

    @Query(nativeQuery = true, value = "select * from  flow_cmd_line where flow_id=:flowId and line_status in(1,2) order by line_status asc")
    public List<FlowLineEntity> queryFlowLineByLineStatus(@Param("flowId") String flowId);

    public List<FlowLineEntity> findAllByFlowEntity_Id(String flowId);

    @Modifying
    @Query(nativeQuery = true, value = "delete from flow_cmd_line where flow_id=:flowId")
    public void deleteByDataFlowId(@Param("flowId") String flowId);
}
