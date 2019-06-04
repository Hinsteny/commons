package com.github.hinsteny.commons.core.functional.page;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 分页转换器
 *
 * @author Hinsteny
 * @version PageConverter: PageConverter 2019-05-08 17:18 All rights reserved.$
 */
public class PageConverter {

    /**
     * 分页转换
     *
     * @param paginator 原始分页对象
     * @param function 转换分页列表数据的方法
     */
    public static <T, E> Paginator<T> convert(Paginator<E> paginator, Function<List<E>, List<T>> function) {
        return new Paginator<T>(paginator.getTotalItemsCount(), function.apply(paginator.getItems()),
            paginator.getCurrentPageNum(), paginator.getPageSize());
    }

    /**
     * 分页转换（带回调）
     *
     * @param paginator 原始分页对象
     * @param function 转换分页列表数据的方法
     * @param consumer 转换后需要处理的逻辑
     */
    public static <T, E> Paginator<T> convert(Paginator<E> paginator, Function<List<E>, List<T>> function, Consumer<T> consumer) {
        List<T> target = function.apply(paginator.getItems());
        if (target != null && consumer != null) {
            target.forEach(consumer);
        }
        return new Paginator<T>(paginator.getTotalItemsCount(), target, paginator.getCurrentPageNum(), paginator.getPageSize());
    }

}
