package com.github.wz2cool.dynamic.mybatis.mapper.batch;

import com.github.wz2cool.dynamic.mybatis.mapper.DynamicQueryMapper;
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
public class MapperBatchAction<M extends DynamicQueryMapper<?>> {

    private final SqlSessionFactory sqlSessionFactory;
    private final Class<M> mapperClass;
    private int batchSize = 100;

    private List<Consumer<M>> actions = new ArrayList<>();

    public MapperBatchAction(Class<M> mapperClass, SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
        this.mapperClass = mapperClass;
    }

    public MapperBatchAction(Class<M> mapperClass, SqlSessionFactory sqlSessionFactory, int batchSize) {
        this.sqlSessionFactory = sqlSessionFactory;
        this.mapperClass = mapperClass;
        this.batchSize = batchSize;
    }

    public static <M extends DynamicQueryMapper<?>> MapperBatchAction<M> create(
            Class<M> mapperClass, SqlSessionFactory sqlSessionFactory) {
        return new MapperBatchAction<>(mapperClass, sqlSessionFactory);
    }

    public static <M extends DynamicQueryMapper<?>> MapperBatchAction<M> create(
            Class<M> mapperClass, SqlSessionFactory sqlSessionFactory, int batchSize) {
        return new MapperBatchAction<>(mapperClass, sqlSessionFactory, batchSize);
    }

    public MapperBatchAction<M> addAction(Consumer<M> action) {
        actions.add(action);
        return this;
    }

    public List<BatchResult> doBatchActions() {
        List<BatchResult> result = new ArrayList<>();
        try (SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false)) {
            final M mapper = sqlSession.getMapper(mapperClass);
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
