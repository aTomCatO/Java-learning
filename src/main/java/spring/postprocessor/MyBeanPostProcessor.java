package spring.postprocessor;

import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.DestructionAwareBeanPostProcessor;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;

import java.beans.PropertyDescriptor;

/**
 * @author XYC
 */

public class MyBeanPostProcessor implements BeanPostProcessor, InstantiationAwareBeanPostProcessor, DestructionAwareBeanPostProcessor {
    private final String beanName = "student";

    @Override
    public void postProcessBeforeDestruction(Object bean, String beanName) throws BeansException {
        if (this.beanName.equals(beanName)) {
            System.out.println("MyBeanPostProcessor : " + beanName + " 销毁之前执行");
        }
    }

    @Override
    public boolean requiresDestruction(Object bean) {
        return DestructionAwareBeanPostProcessor.super.requiresDestruction(bean);
    }

    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
        if (this.beanName.equals(beanName)) {
            System.out.println("MyBeanPostProcessor :" + beanName + " 实例化之前执行");
        }
        //这里返回的对象会替换掉原本的bean
        return InstantiationAwareBeanPostProcessor.super.postProcessBeforeInstantiation(beanClass, beanName);
    }

    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
        if (this.beanName.equals(beanName)) {
            System.out.println("MyBeanPostProcessor :" + beanName + " 实例化之后执行");
        }

        //这里如果返回 false 会跳过依赖注入（@Autowired）阶段
        return InstantiationAwareBeanPostProcessor.super.postProcessAfterInstantiation(bean, beanName);
    }

    @Override
    public PropertyValues postProcessProperties(PropertyValues pvs, Object bean, String beanName) throws BeansException {
        if (this.beanName.equals(beanName)) {
            System.out.println("MyBeanPostProcessor :" + beanName + " 依赖注入阶段");
        }
        return InstantiationAwareBeanPostProcessor.super.postProcessProperties(pvs, bean, beanName);
    }

    @Override
    public PropertyValues postProcessPropertyValues(PropertyValues pvs, PropertyDescriptor[] pds, Object bean, String beanName) throws BeansException {
        return InstantiationAwareBeanPostProcessor.super.postProcessPropertyValues(pvs, pds, bean, beanName);
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (this.beanName.equals(beanName)) {
            System.out.println("MyBeanPostProcessor :" + beanName + " 初始化之前执行");

        }
        //这里返回的对象会替换掉原本的bean
        return InstantiationAwareBeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (this.beanName.equals(beanName)) {
            System.out.println("MyBeanPostProcessor :" + beanName + " 初始化之后执行");
        }

        //这里返回的对象会替换掉原本的bean
        return InstantiationAwareBeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }
}
