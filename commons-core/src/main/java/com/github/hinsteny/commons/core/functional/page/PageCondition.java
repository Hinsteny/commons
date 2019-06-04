package com.github.hinsteny.commons.core.functional.page;

import java.util.ArrayList;
import java.util.List;
import com.github.hinsteny.commons.core.base.Pair;

/**
 * 分页条件（带排序）
 *
 * @author Hinsteny
 * @version PageCondition: PageCondition 2019-05-08 17:05 All rights reserved.$
 */
public class PageCondition {

    /** 当前页开始位置，注意是索引，从0开始 */
    private int                 offset  = 0;

    /** 页码索引，从1开始 */
    private int                 pageNum = 1;

    /** 每页大小 */
    private int                 limit   = 15;

    /** 排序字符串 */
    private String              sort;

    /** 升序 */
    private static final String ASC     = "ASC";

    /** 降序 */
    private static final String DESC    = "DESC";

    private PageCondition() {
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public int getLimit() {
        return limit;
    }

    public void updateLimit(int limit) {
        if (limit <= 0) {
            throw new IllegalArgumentException("limit can't less than zero");
        }
        this.limit = limit;
    }

    public int getOffset() {
        return offset;
    }

    public void updateOffset(int offset) {
        this.offset = offset;
        this.pageNum = (offset + 1) / limit + ((offset + 1) % limit == 0 ? 0 : 1);
    }

    public int getPageNum() {
        return pageNum;
    }

    public void updatePageNum(int pageNum) {
        this.pageNum = pageNum;
        this.offset = (pageNum - 1) * limit;
    }

    public static Builder builder() {
        return new Builder();
    }

    /**
     * 分页对象构造器
     */
    public static class Builder {

        /**
         * 排序条件集合，key为字段名，value为排序方式
         */
        private List<Pair<String, String>> sorts = new ArrayList<>();

        /**
         * 分页对象
         */
        private PageCondition pageCondition = new PageCondition();

        public Builder offset(int offset) {
            if (offset > 0) {
                pageCondition.offset = offset;
            }
            return this;
        }

        public Builder pageNum(int pageNum) {
            if (pageNum >= 1) {
                pageCondition.pageNum = pageNum;
            }
            return this;
        }

        public Builder limit(int limit) {
            if (limit > 0) {
                pageCondition.limit = limit;
            }
            return this;
        }

        public Builder asc(String sortColumn) {
            if (sortColumn != null) {
                sorts.add(new Pair<>(sortColumn, ASC));
            }
            return this;
        }

        public Builder desc(String sortColumn) {
            if (sortColumn != null) {
                sorts.add(new Pair<>(sortColumn, DESC));
            }
            return this;
        }

        /**
         * 构建分页对象
         *
         * @return 分页对象
         */
        public PageCondition build() {
            StringBuilder sortBuilder = new StringBuilder();
            for (Pair<String, String> pair : sorts) {
                sortBuilder.append(",").append(pair.getFirst()).append(" ").append(pair.getSecond());
            }
            if (sortBuilder.length() > 0) {
                pageCondition.sort = sortBuilder.substring(1);
            }

            //默认order by 1
            if (pageCondition.sort == null) {
                pageCondition.sort = " 1 ";
            }
            //通过offset计算pageNum
            if (pageCondition.offset > 0) {
                pageCondition.pageNum = (pageCondition.offset + 1) / pageCondition.limit
                    + ((pageCondition.offset + 1) % pageCondition.limit > 0 ? 1 : 0);
                if (pageCondition.pageNum < 0) {
                    pageCondition.pageNum = 1;
                }
            } else if (pageCondition.offset == 0 && pageCondition.pageNum >= 0) {
                //通过pageNum计算offset
                pageCondition.offset = (pageCondition.pageNum - 1) * pageCondition.limit;
            }
            return pageCondition;
        }
    }

}
