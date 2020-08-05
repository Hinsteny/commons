package com.github.hinsteny.commons.warp.convert;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * @author hinsteny
 * @date 2020/8/5
 */
public class DataConverterSupport implements ApplicationListener<ContextRefreshedEvent> {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private Map<Class, Map<Class, DataConverter>> converters = createMap();

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        Collection<DataConverter> converters =
            event.getApplicationContext().getBeansOfType(DataConverter.class).values();
        if (CollectionUtils.isNotEmpty(converters)) {
            converters.forEach((converter) -> solveConverter(converter));
        }
    }

    public <T> Optional<T> convert(Object origin, Class<T> targetClass) {
        Optional<T> result = Optional.empty();
        DataConverter converter = this.getDataConverter(origin, targetClass);
        if (Objects.nonNull(origin)) {
            result = Optional.ofNullable((targetClass.cast(converter.convert(origin))));
        }
        return result;
    }

    public <T> Optional<Collection<T>> convertCollection(Collection<?> origins, Class<T> targetClass) {
        Optional<Collection<T>> result = Optional.empty();
        if (CollectionUtils.isNotEmpty(origins)) {
            Object origin = origins.stream().findFirst().orElse(null);
            DataConverter converter = this.getDataConverter(origin, targetClass);
            if (Objects.nonNull(origin)) {
                result = Optional.ofNullable(converter.convertCollection(origins));
            }
        }
        return result;
    }

    private <T, E> void solveConverter(DataConverter<T, E> converter) {
        Class<? extends DataConverter> clazz = converter.getClass();
        Type type = clazz.getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterType = (ParameterizedType) type;
            Class origin = (Class) parameterType.getActualTypeArguments()[0];
            Class future = (Class) parameterType.getActualTypeArguments()[1];
            Map<Class, DataConverter> originMap = converters.get(origin);
            if (null == originMap) {
                converters.put(origin, createMap());
                originMap = converters.get(origin);
            }
            if (originMap.containsKey(future)) {
                logger.warn("For Origin class[{}], and Future class[{}], have Multiple Converters", origin, future,
                    clazz);
            } else {
                originMap.put(future, converter);
            }
        }
    }

    private <T> DataConverter getDataConverter(Object origin, Class<T> targetClass) {
        DataConverter converter = null;
        if (Objects.nonNull(origin) && Objects.nonNull(targetClass)) {
            Map<Class, DataConverter> originMap = converters.get(origin.getClass());
            if (null != originMap) {
                converter = originMap.get(targetClass);
            }
        }
        return converter;
    }

    private Map createMap() {
        return new HashMap(16);
    }

}
