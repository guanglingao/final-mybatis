package com.glinsoft.finalmybatis.query.expression;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import com.glinsoft.finalmybatis.SQLConsts;

/**
 * list或数组查询雕件
 * 
 */
public class ListExpression implements ExpressionListable {

    private String column = "";
    private String equal = SQLConsts.IN;
    private Collection<?> value = Collections.emptyList();
    private String joint = SQLConsts.AND;

    public ListExpression(String joint, String column, String equal, Collection<?> value) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("ListExpression构造方法参数value不能为空");
        }
        this.joint = joint;
        this.equal = equal;
        this.column = column;
        this.value = value;
    }

    public <T> ListExpression(String joint, String column, String equal, Collection<T> value,
            ValueConvert<T> valueConvert) {
        this(joint, column, equal, buildValueConvert(value, valueConvert));
    }

    public <T> ListExpression(String joint, String column, String equal, T[] value, ValueConvert<T> valueConvert) {
        this(joint, column, equal, Arrays.asList(value), valueConvert);
    }

    public ListExpression(String joint, String column, String equal, Object[] value) {
        this(joint, column, equal, Arrays.asList(value));
    }

    private static <T> Collection<?> buildValueConvert(Collection<T> value, ValueConvert<T> valueConvert) {
        if (value == null) {
            return Collections.emptyList();
        }
        if (valueConvert == null) {
            throw new NullPointerException("参数ValueConvert不能为null");
        }
        Collection<Object> newSet = new HashSet<Object>(value.size());
        for (T obj : value) {
            newSet.add(valueConvert.convert(obj));
        }
        return newSet;
    }

    @Override
    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    @Override
    public String getEqual() {
        return equal;
    }

    public void setEqual(String equal) {
        this.equal = equal;
    }

    @Override
    public Collection<?> getValue() {
        return value;
    }

    public void setValue(Collection<?> value) {
        this.value = value;
    }

    @Override
    public String getJoint() {
        return joint;
    }

    public void setJoint(String joint) {
        this.joint = joint;
    }

}
