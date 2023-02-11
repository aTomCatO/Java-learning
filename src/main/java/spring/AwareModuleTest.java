package spring;

import org.junit.Test;
import spring.bean.School;

/**
 * @author XYC
 */
public class AwareModuleTest extends ApplicationTest {
    /**
     * 后处理器属于扩展功能，而 Aware 接口属于内置功能（不加任何扩展，Spring就能识别）
     * Aware 接口用于注入一些与容器相关的信息，例如
     * BeanNameAware 注入 Bean 的名字；
     * BeanFactoryAware 注入 BeanFactory 容器；
     * ApplicationContextAware 注入 ApplicationContext 容器；
     * EmbeddedValueResolveAware 解析 ${};
     */
    @Test
    public void test1() {
        genericApplicationContext.registerBean("school", School.class);
        genericApplicationContext.refresh();

    }
}
