package com.zhisland.finalmybatis.mapper;

import com.zhisland.finalmybatis.query.Query;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

/**
 * 查询映射
 *
 * @author gaoguanglin
 * @date 2019-05-05
 *
 * @param <E>
 * @param <I>
 */
public interface ReadMapper<E,I> extends Mapper<E> {

    /**
     * 内置resultMap名称
     */
    String BASE_RESULT_MAP = "baseResultMap";


    /**
     * 根据主键查询
     *
     * @param id 主键值
     * @return 返回实体对象，没有返回null
     */
    E getById(I id);


    /**
     * 根据条件查找单条记录
     *
     * @param query 查询条件
     * @return 返回实体对象，不存在则返回null
     */
    E getByQuery(@Param("query") Query query);


    /**
     * 根据字段名称查询一条记录
     *
     * @param column 数据库字段名
     * @param value 字段值
     * @return 返回实体对象，不存在则返回null
     */
    E getByColumn(@Param("column") String column, @Param("value") Object value);


    /**
     * 查询总记录数
     *
     * @param query 查询条件
     * @return 返回总记录数
     */
    long getCount(@Param("query") Query query);

    /**
     * 根据字段查询结果集
     *
     * @param column 数据库字段名
     * @param value 字段值
     * @return 返回实体对象集合，没有返回空集合
     */
    List<E> listByColumn(@Param("column") String column, @Param("value") Object value);

    /**
     * 查询结果集
     *
     * @param query 查询条件
     * @return 返回实体对象集合，不存在则返回空集合
     */
    List<E> list(@Param("query") Query query);

    /**
     * 查询指定字段结果集
     * @param columns 返回的字段
     * @param query 查询条件
     * @return 返回结果集,不存在则返回空list
     */
    List<Map<String, Object>> listMap(@Param("columns") List<String> columns, @Param("query") Query query);
}
