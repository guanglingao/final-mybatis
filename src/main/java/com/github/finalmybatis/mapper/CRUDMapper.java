package com.github.finalmybatis.mapper;

/**
 * 具备CRUD功能的数据库映射
 *
 * @author gaoguanglin
 * @date 2019-05-05
 *
 * @param <E> 实体类
 * @param <I> 主键数据类型
 */
public interface CRUDMapper<E,I> extends EditMapper<E,I>,ReadMapper<E,I>{


}
