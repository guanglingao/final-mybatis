package com.zhisland.finalmybatis.mapper;

import com.zhisland.finalmybatis.query.Query;
import org.apache.ibatis.annotations.Param;

public interface DeleteMapper<E,I> extends Mapper<E> {


    /**
     * 根据id删除
     *
     * @param id 主键id值
     */
    void deleteById(I id);

    /**
     * 根据条件删除
     *
     * @param query 查询对象
     */
    void deleteByQuery(@Param("query") Query query);


}
