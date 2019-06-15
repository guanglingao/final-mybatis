package com.glinsoft.finalmybatis.mapper;

/**
 * 负责编辑的mapper
 * 
 * @param <E> 实体类
 */
public interface EditMapper<E,I> extends CreateMapper<E>, UpdateMapper<E>, DeleteMapper<E,I> {

}
