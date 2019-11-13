package com.hanl.datamgr.common;

import org.hibernate.query.internal.QueryImpl;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.sql.Types;
import java.util.List;
import java.util.Map;

/**
 * @author: Hanl
 * @date :2019/10/9
 * @desc:
 */
@Service("commonRepositoryService")
public class CommonRepositoryService {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Map<String, Object>> getResultBySql(String sql, Object... parameters) {
        Query query = entityManager.createNativeQuery(sql);
        int len = parameters.length;
        for (int i = 0; i < len; i++) {
            query.setParameter(i + 1, parameters[i]);
        }
        query.unwrap(QueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String, Object>> resultList = query.getResultList();
        System.out.println(resultList);
        return resultList;
    }

    public boolean existInDB(String tableName, String columnName, Object... parameters) {
        String sql = "select count(0) from " + tableName + " where " + columnName + "=?";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter(1, parameters[0]);
        int count = Integer.valueOf(query.getResultList().get(0).toString());
        return count > 0 ? true : false;
    }

    public boolean existInDB(String sql, Object... parameters) {
        Query query = entityManager.createNativeQuery(sql);
        if (parameters != null && parameters.length > 0) {
            int len = parameters.length;
            for (int i = 0; i < len; i++) {
                query.setParameter(i + 1, parameters[i]);
            }
        }
        int count = Integer.valueOf(query.getResultList().get(0).toString());
        return count > 0 ? true : false;
    }

}
