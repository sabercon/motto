package cn.sabercon.motto.common.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.beans.FeatureDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.stream.Stream;

/**
 * 实体操作的工具类
 *
 * @author ywk
 * @date 2019-11-05
 */
@UtilityClass
@Slf4j
public class EntityUtils {

    /**
     * 复制属性到目标实体，来源中为null或空字符串的属性会被忽略
     *
     * @param source 复制来源
     * @param target 复制目标
     */
    public void copyPropertiesIgnoreEmpty(Object source, Object target) {
        BeanUtils.copyProperties(source, target, getEmptyPropertyNames(source));
    }

    /**
     * 得到实体中为null或空字符串的属性名称数组
     */
    private String[] getEmptyPropertyNames(Object bean) {
        BeanWrapper beanWrapper = new BeanWrapperImpl(bean);
        PropertyDescriptor[] descriptors = beanWrapper.getPropertyDescriptors();
        return Stream.of(descriptors).map(FeatureDescriptor::getName)
                .filter(name -> StringUtils.isEmpty(beanWrapper.getPropertyValue(name)))
                .distinct().toArray(String[]::new);
    }

    /**
     * 更新数据库实体，更新内容为空的字段自动忽略
     *
     * @param repository    实体的repository
     * @param updateContent 包含更新内容的bean，必须有id
     */
    @Deprecated
    public <E, D> void updateByDto(JpaRepository<E, Long> repository, D updateContent) {
        Long id = (Long) invokeGetMethod(updateContent, "id");
        Assert.notNull(id, "the update content must have id");
        E entity = repository.getOne(id);
        copyPropertiesIgnoreEmpty(updateContent, entity);
    }

    /**
     * 根据属性名对bean执行get方法
     * 获取错误时返回null
     *
     * @param bean      bean对象
     * @param fieldName 属性名
     * @return 属性名对应get方法的返回值
     */
    public Object invokeGetMethod(Object bean, String fieldName) {
        String methodName = "get" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
        try {
            return bean.getClass().getMethod(methodName).invoke(bean);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            log.error("get method cannot be invoked correctly, get method name: {}", methodName);
            return null;
        }
    }

    /**
     * 根据属性名对bean执行set方法
     *
     * @param bean      bean对象
     * @param fieldName 属性名
     * @param value     要设置的值
     */
    public void invokeSetMethod(Object bean, String fieldName, Object value) {
        String methodName = "set" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
        try {
            bean.getClass().getMethod(methodName, value.getClass()).invoke(bean, value);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            log.error("set method cannot be invoked correctly, set method name: {}", methodName);
        }
    }
}
