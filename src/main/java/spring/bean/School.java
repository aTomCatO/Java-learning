package spring.bean;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author XYC
 */
public class School implements BeanNameAware, ApplicationContextAware, InitializingBean {
    public School() {
        System.out.println("construction School");
    }

    @Override
    public void setBeanName(String name) {
        System.out.println("School 实现 BeanNameAware 接口：setBeanName " + name);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        System.out.println("School 实现 ApplicationContextAware 接口");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("School 实现 InitializingBean 接口： 初始化");
    }
}
