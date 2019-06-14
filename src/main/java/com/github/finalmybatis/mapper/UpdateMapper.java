package com.github.finalmybatis.mapper;

import com.github.finalmybatis.query.Query;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

public interface UpdateMapper<E> extends Mapper<E> {

    /**
     * 更新，更新所有字段
     *
     * @param entity 实体类
     * @return 影响行数
     */
    int update(E entity);


    /**
     * 更新，忽略null字段
     *
     * @param entity 实体类
     * @return 影响行数
     */
    int updateIgnoreNull(E entity);


    /**
     * 根据条件批量更新
     *
     * @param entity 待更新的数据
     * @param query 更新条件
     * @return 影响行数
     */
    int updateNotNull(@Param("entity") E entity, @Param("query") Query query);


    /**
     * 根据条件更新
     *
     * @param map 待更新的数据，key为数据库字段名，value为待设定的值
     * @param query 更新条件
     * @return 影响行数
     */
    int updateByMap(@Param("entity") Map<String, Object> map, @Param("query") Query query);

}
