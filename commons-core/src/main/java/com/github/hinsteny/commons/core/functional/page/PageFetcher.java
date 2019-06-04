package com.github.hinsteny.commons.core.functional.page;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 分页数据提取器
 *
 * @author Hinsteny
 * @version PageFetcher: PageFetcher 2019-05-08 17:17 All rights reserved.$
 */
public class PageFetcher {

    /**
     * 分页数据提取
     * @param pageCondition 分页条件
     * @param counter count计算方法
     * @param listFetcher 列表数据获取
     * @param dataConverter 数据转换，通常是DO列表转换为Model列表
     * @param <T>
     * @param <R>
     * @return
     */
    public static <T, R> Paginator<R> fetchPageData(PageCondition pageCondition, Supplier<Long> counter,
        Supplier<List<T>> listFetcher,
        Function<List<T>, List<R>> dataConverter) {
        if (dataConverter == null) {
            throw new IllegalArgumentException("分页获取中的dataConverter不能为空");
        }

        //预处理分页对象
        PageCondition realPageCondition = pageCondition;
        if (pageCondition == null) {
            realPageCondition = new PageCondition.Builder().build();
        }
        //查询count
        Long count = counter.get();
        if (count == null || count == 0) {
            return Paginator.ofEmpty(realPageCondition.getLimit());
        }
        //查询列表数据
        List<T> originData = listFetcher.get();

        //组装分页数据
        return new Paginator<>(count, dataConverter.apply(originData), realPageCondition);
    }

}
