package com.github.wz2cool.dynamic.mybatis.mapper.batch;

import org.apache.ibatis.executor.BatchResult;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author Frank
 **/
public class MapperBatchAction<TMapper> {

    private final SqlSessionFactory sqlSessionFactory;
    private final Class<TMapper> mapperClass;
    private int batchSize = 100;

    private List<Consumer<TMapper>> actions = new ArrayList<>();

    public MapperBatchAction(SqlSessionFactory sqlSessionFactory, Class<TMapper> mapperClass) {
        this.sqlSessionFactory = sqlSessionFactory;
        this.mapperClass = mapperClass;
    }

    public MapperBatchAction(SqlSessionFactory sqlSessionFactory, Class<TMapper> mapperClass, int batchSize) {
        this.sqlSessionFactory = sqlSessionFactory;
        this.mapperClass = mapperClass;
        this.batchSize = batchSize;
    }

    public void addAction(Consumer<TMapper> action) {
        actions.add(action);
    }

    public List<BatchResult> batchDoActions() {
        List<BatchResult> result = new ArrayList<>();
        try (SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false)) {
            final TMapper mapper = sqlSession.getMapper(mapperClass);
            for (int i = 0; i < actions.size(); i++) {
                actions.get(i).accept(mapper);

                if (i != 0 && i % batchSize == 0) {
                    final List<BatchResult> batchResults = sqlSession.flushStatements();
                    result.addAll(batchResults);
                }
            }
            final List<BatchResult> batchResults = sqlSession.flushStatements();
            result.addAll(batchResults);
        }
        return result;
    }
}
