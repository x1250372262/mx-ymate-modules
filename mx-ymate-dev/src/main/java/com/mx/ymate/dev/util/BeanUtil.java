package com.mx.ymate.dev.util;


import net.ymate.platform.commons.util.ClassUtils;
import net.ymate.platform.core.persistence.IResultSet;
import net.ymate.platform.core.persistence.impl.DefaultResultSet;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * @Author: mengxiang.
 * @create: 2021-07-02 16:58
 * @Description: bean工具类
 */
public class BeanUtil {

    @FunctionalInterface
    public interface BeanUtilCallBack<S, T> {

        /**
         * 定义默认回调方法
         *
         * @param s
         * @param t
         */
        void callBack(S s, T t);
    }

    /**
     * 复制bean
     *
     * @param source 要复制的
     * @param target 目标对象
     * @return 目标对象
     */
    public static <S, T> T copy(S source, Supplier<T> target) {
        return copy(source, target, null, null);
    }

    /**
     * 复制bean
     *
     * @param source   要复制的
     * @param target   目标对象
     * @param callBack 处理额外数据接口
     * @return 目标对象
     */
    public static <S, T> T copy(S source, Supplier<T> target, BeanUtilCallBack<S, T> callBack) {
        return copy(source, target, null, callBack);
    }

    /**
     * 复制bean
     *
     * @param source 要复制的
     * @param target 目标对象
     * @return 目标对象
     */
    public static <S, T> T copy(S source, Supplier<T> target, ClassUtils.IFieldValueFilter fieldValueFilter) {
        return copy(source, target, fieldValueFilter, null);
    }

    /**
     * 复制bean
     *
     * @param source 要复制的
     * @param target 目标对象
     * @return 目标对象
     */
    public static <S, T> T duplicate(S source, T target) {
        return duplicate(source, target, null, null);
    }

    /**
     * 复制bean
     *
     * @param source 要复制的
     * @param target 目标对象
     * @return 目标对象
     */
    public static <S, T> T duplicate(S source, T target, ClassUtils.IFieldValueFilter fieldValueFilter) {
        return duplicate(source, target, fieldValueFilter, null);
    }

    /**
     * 复制bean
     *
     * @param source   要复制的
     * @param target   目标对象
     * @param callBack 处理额外数据接口
     * @return 目标对象
     */
    public static <S, T> T duplicate(S source, T target, BeanUtilCallBack<S, T> callBack) {
        return duplicate(source, target, null, callBack);
    }

    /**
     * 复制bean
     *
     * @param source 要复制的
     * @param target 目标对象
     * @return 目标对象
     */
    public static <S, T> T duplicate(S source, T target, ClassUtils.IFieldValueFilter fieldValueFilter, BeanUtilCallBack<S, T> callBack) {
        if (source == null || target == null) {
            return null;
        }
        T t = ClassUtils.wrapper(source).duplicate(target, fieldValueFilter);
        if (callBack != null) {
            // 回调
            callBack.callBack(source, t);
        }
        return t;
    }

    /**
     * 复制bean
     *
     * @param source   要复制的
     * @param target   目标对象
     * @param callBack 处理额外数据接口
     * @return 目标对象
     */
    public static <S, T> T copy(S source, Supplier<T> target, ClassUtils.IFieldValueFilter fieldValueFilter, BeanUtilCallBack<S, T> callBack) {
        if (source == null || target == null || target.get() == null) {
            return null;
        }
        T t = ClassUtils.wrapper(source).duplicate(target.get(), fieldValueFilter);
        if (callBack != null) {
            // 回调
            callBack.callBack(source, t);
        }
        return t;
    }


    /**
     * 复制集合
     *
     * @param sources 要复制的
     * @param target  目标对象
     * @return 目标对象
     */
    public static <S, T> List<T> copyList(List<S> sources, Supplier<T> target) {
        return copyList(sources, target, null, null);
    }


    /**
     * 复制集合
     *
     * @param sources  要复制的
     * @param target   目标对象
     * @param callBack 处理额外数据接口
     * @return 目标对象
     */
    public static <S, T> List<T> copyList(List<S> sources, Supplier<T> target, BeanUtilCallBack<S, T> callBack) {
        return copyList(sources, target, null, callBack);
    }

    /**
     * 复制集合
     *
     * @param sources 要复制的
     * @param target  目标对象
     * @return 目标对象
     */
    public static <S, T> List<T> copyList(List<S> sources, Supplier<T> target, ClassUtils.IFieldValueFilter fieldValueFilter) {
        return copyList(sources, target, fieldValueFilter, null);
    }

    /**
     * 复制集合
     *
     * @param sources  要复制的
     * @param target   目标对象
     * @param callBack 处理额外数据接口
     * @return 目标对象
     */
    public static <S, T> List<T> copyList(List<S> sources, Supplier<T> target, ClassUtils.IFieldValueFilter fieldValueFilter, BeanUtilCallBack<S, T> callBack) {
        if (sources == null || target == null || target.get() == null) {
            return new ArrayList<>();
        }
        List<T> list = new ArrayList<>(sources.size());
        for (S source : sources) {
            T t = target.get();
            ClassUtils.wrapper(source).duplicate(t, fieldValueFilter);
            list.add(t);
            if (callBack != null) {
                // 回调
                callBack.callBack(source, t);
            }
        }
        return list;
    }

    /**
     * 复制bean
     *
     * @param sources  要复制的
     * @param target   目标对象
     * @param callBack 处理额外数据接口
     * @return 目标对象
     */
    public static <S, T> IResultSet<T> copyResultSet(IResultSet<S> sources, Supplier<T> target, ClassUtils.IFieldValueFilter fieldValueFilter, BeanUtilCallBack<S, T> callBack) {
        List<T> result = copyList(sources.getResultData(), target, fieldValueFilter, callBack);
        return new DefaultResultSet<>(result, sources.getPageNumber(), sources.getPageSize(), sources.getRecordCount());
    }

    /**
     * 复制bean
     *
     * @param sources 要复制的
     * @param target  目标对象
     * @return 目标对象
     */
    public static <S, T> IResultSet<T> copyResultSet(IResultSet<S> sources, Supplier<T> target) {
        List<T> result = copyList(sources.getResultData(), target, null, null);
        return new DefaultResultSet<>(result, sources.getPageNumber(), sources.getPageSize(), sources.getRecordCount());
    }

}
