package com.github.hinsteny.commons.core.functional.page;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @param <T> the type of elements in this Paginator
 *
 * @author Hinsteny
 * @version Paginator: Paginator 2019-05-08 17:02 All rights reserved.$
 */
public class Paginator<T> implements Serializable {

    private static final long serialVersionUID = 1927750008084483758L;

    /**
     * 总记录数
     */
    private long totalItemsCount;

    /**
     * 分页大小
     */
    private int pageSize;

    /**
     * 当前页码，从1开始
     */
    private int currentPageNum;

    /**
     * 当前分页数据
     */
    private List<T> items;

    /**
     * 构造空数据的分页对象，常用于在已知没有数据的情况下使用。
     *
     * @param pageSize 分页大小
     * @param <T> 泛型类型
     * @return 分页对象
     */
    public static <T> Paginator<T> ofEmpty(int pageSize) {
        return new Paginator(0, new ArrayList<T>(), 1, pageSize);
    }

    /**
     * 从原始的分页对象构建分页结构，其中分页信息使用现有分页对象的信息，数据使用参数{@code items}指定的数据。 通常用于在分页数据结构需要转换时使用。
     *
     * @param old 原始的分页对象
     * @param items 新的数据集合
     */
    public Paginator(Paginator old, List<T> items) {
        this(old.getTotalItemsCount(), items, old.getCurrentPageNum(), old.getPageSize());
    }

    /**
     * 通过分页条件来构造分页对象。
     *
     * @param totalItemsCount 总记录数
     * @param items 记录列表
     * @param pageCondition 分页条件
     */
    public Paginator(long totalItemsCount, List<T> items, PageCondition pageCondition) {
        this.totalItemsCount = totalItemsCount;
        this.items = items;
        this.pageSize = pageCondition.getLimit();
        this.currentPageNum = pageCondition.getPageNum();
    }

    /**
     * 通过页码和页面尺寸来构建分页对象
     *
     * @param totalItemsCount 总记录数
     * @param items 记录列表
     * @param currentPageNum 当前页
     * @param pageSize 页大小
     */
    public Paginator(long totalItemsCount, List<T> items, int currentPageNum, int pageSize) {
        this.totalItemsCount = totalItemsCount;
        this.items = items;
        this.pageSize = pageSize;
        this.currentPageNum = currentPageNum;
    }

    /**
     * 获取数据
     * @return 分页对象
     */
    public List<T> getItems() {
        return items;
    }

    /**
     * 获取总页数
     * @return 总数
     */
    public long getTotalPageCount() {
        return ((totalItemsCount / pageSize) + (totalItemsCount % pageSize == 0 ? 0 : 1));
    }

    /**
     * 获取总条数
     * @return 总数
     */
    public long getTotalItemsCount() {
        return totalItemsCount;
    }

    /**
     * 获取当前页码
     * @return 页码
     */
    public int getCurrentPageNum() {
        if (currentPageNum <= 1) {
            return 1;
        }
        return currentPageNum;
    }

    /**
     * 获取页面条数大小
     * @return 页大小
     */
    public int getPageSize() {
        if (pageSize <= 0) {
            return 1;
        }
        return pageSize;
    }

    /**
     * 获取下一页页码
     * @return 页码
     */
    public int getNextPageNum() {
        if (this.currentPageNum < getTotalPageCount()) {
            return currentPageNum + 1;
        } else {
            return currentPageNum;
        }

    }

    /**
     * 获取前一页页码
     * @return 页码
     */
    public int getPreviousPageNum() {
        if (currentPageNum <= 1) {
            return 1;
        } else {
            return currentPageNum - 1;
        }
    }

}
