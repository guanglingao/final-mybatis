package com.glinsoft.finalmybatis.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 创建数据库记录映射接口
 *
 * @author gaoguanglin
 * @date 2019-05-05
 *
 * @param <E>
 */
public interface CreateMapper<E> extends Mapper<E>{

    /**
     * 插入数据库记录，并忽略字段值为空的字段
     *
     * @param entity
     * @return 受影响行数
     */
    int insert(E entity);


    /**
     * 批量插入记录
     *
     * @param entities
     * @return 影响行数
     */
    int insertBatch(@Param("entities")List<E> entities);

}