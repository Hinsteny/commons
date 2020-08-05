package com.github.hinsteny.commons.warp.convert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 不同类实例转化的抽象接口.
 *
 * @author hinsteny
 * @date 2020/8/5
 */
public abstract class DataConverter<T, R> {


    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 用于对象子属性递归转化
     */
    @Resource
    protected DataConverterSupport converterSupport;

    /**
     * 对象转化
     */
    public abstract R convert(T origin);

    /**
     * 转化集合
     */
    public List<R> convertCollection(Collection<T> origin) {
        if (CollectionUtils.isEmpty(origin)) {
            return new ArrayList<>();
        }
        return origin.stream().filter(Objects::nonNull).map(this::convert).collect(Collectors.toList());
    }

}
