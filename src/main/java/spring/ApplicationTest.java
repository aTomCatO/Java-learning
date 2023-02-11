package spring;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.*;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;

/**
 * @author XYC
 */
@SpringBootApplication
public class ApplicationTest {

    final GenericApplicationContext genericApplicationContext = new GenericApplicationContext();

    final ConfigurableListableBeanFactory configurableListableBeanFactory = genericApplicationContext.getBeanFactory();

    final DefaultListableBeanFactory defaultListableBeanFactory = genericApplicationContext.getDefaultListableBeanFactory();

    /**
     * 缓存元数据读取器工厂
     */
    final CachingMetadataReaderFactory cachingMetadataReaderFactory = new CachingMetadataReaderFactory();

    /**
     * Bean名字的生成者
     */
    final AnnotationBeanNameGenerator annotationBeanNameGenerator = new AnnotationBeanNameGenerator();

    final BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition();

    final AbstractBeanDefinition abstractBeanDefinition = beanDefinitionBuilder.getBeanDefinition();

}
