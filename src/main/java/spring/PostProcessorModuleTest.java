package spring;

import org.junit.Test;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.annotation.InjectionMetadata;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.context.annotation.*;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.MethodMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import spring.bean.Book;
import spring.bean.Config;
import spring.bean.Student;
import spring.postprocessor.MyBeanPostProcessor;
import spring.scan.Teacher;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author XYC
 */
public class PostProcessorModuleTest extends ApplicationTest {
    /**
     * 给ioc容器添加以下扩展:
     * 1，internalConfigurationAnnotationProcessor；
     * 2，internalEventListenerProcessor；
     * 3，internalEventListenerFactory；
     * 4，internalAutowiredAnnotationProcessor；
     * 5，internalCommonAnnotationProcessor；
     */
    @Test
    public void test1() {
        try {
            //注册bean的另一种方式
            abstractBeanDefinition.setBeanClass(Config.class);
            genericApplicationContext.registerBeanDefinition("config", abstractBeanDefinition);

            //初始化容器(添加 ${} 的解析器、)
            genericApplicationContext.refresh();

            //添加扩展
            AnnotationConfigUtils.registerAnnotationConfigProcessors(genericApplicationContext);

            System.out.println("BeanFactory 的后处理器:");
            //BeanFactory 的后处理器，为BeanFactory 提供扩展功能
            Map<String, BeanFactoryPostProcessor> beanFactoryPostProcessorMap
                    = configurableListableBeanFactory.getBeansOfType(BeanFactoryPostProcessor.class);
            beanFactoryPostProcessorMap.forEach((k, v) -> {
                System.out.println("------------------------------------------------------------");
                System.out.println(k + "\n" + v);
                System.out.println("------------------------------------------------------------");

                v.postProcessBeanFactory(configurableListableBeanFactory);
                //ConfigurationClassPostProcessor 进行解析@Bean、@Configuration、@ComponentScan、@Import等
                //EventListenerMethodProcessor
            });

            System.out.println("\nBean 的后处理器:");
            //Bean 的后处理器，针对 bean 的生命周期的各个阶段提供扩展，例如@Autowired、 @Resource
            //如果缺少这些Bean 的后处理器，那么在 Student 类中的 Book 实例将无法通过@Autowired、 @Resource进行依赖注入

            //添加自定义的 Bean 后处理器
            configurableListableBeanFactory.addBeanPostProcessor(new MyBeanPostProcessor());

            Map<String, BeanPostProcessor> beanPostProcessorMap =
                    configurableListableBeanFactory.getBeansOfType(BeanPostProcessor.class);
            beanPostProcessorMap.forEach((k, v) -> {
                System.out.println("------------------------------------------------------------");
                System.out.println(k + "\n" + v);
                System.out.println("------------------------------------------------------------");
                configurableListableBeanFactory.addBeanPostProcessor(v);
                //AutowiredAnnotationBeanPostProcessor 为 Bean 中的@Autowired和@Value注解的注入功能提供支持
                //CommonAnnotationBeanPostProcessor 为 Bean 中的@Resource、@PostConstruct和@PreDestroy注解的注入功能提供支持
            });
            System.out.println("\nbean的实例化：");
            //提前实例化 bean（默认是延迟bean的实例化，也就是在调用getBean方法的时候才去实例化）
            //configurableListableBeanFactory.preInstantiateSingletons();

            System.out.println("==============⬆提前实例化===============bean的实例化分割线=================⬇延迟实例化================");

            System.out.println(configurableListableBeanFactory.getBean("student", Student.class).getBook());
            System.out.println(configurableListableBeanFactory.getBean("student", Student.class).getBook());

            System.out.println();
            System.out.println("现有以下 bean：");
            String[] beanDefinitionNames = configurableListableBeanFactory.getBeanDefinitionNames();
            for (String beanDefinitionName : beanDefinitionNames) {
                System.out.println(beanDefinitionName + " —— " + genericApplicationContext.getBean(beanDefinitionName));
            }
            System.out.println();

            genericApplicationContext.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * AutowiredAnnotationBeanPostProcessor 相关学习
     **/
    @Test
    public void test2() {
        genericApplicationContext.registerBean("teacher", Teacher.class);
        genericApplicationContext.registerBean("book", Book.class);

        Student student = new Student();
        System.out.println("依赖注入前： " + student);

        defaultListableBeanFactory.setAutowireCandidateResolver(new ContextAnnotationAutowireCandidateResolver());
        AutowiredAnnotationBeanPostProcessor autowiredAnnotationBeanPostProcessor
                = new AutowiredAnnotationBeanPostProcessor();
        autowiredAnnotationBeanPostProcessor.setBeanFactory(configurableListableBeanFactory);

        //添加 ${} 的解析器
        configurableListableBeanFactory.addEmbeddedValueResolver(new StandardEnvironment()::resolvePlaceholders);

        //执行依赖注入
        //autowiredAnnotationBeanPostProcessor.postProcessProperties(null, student, "student");
        //System.out.println("依赖注入后： " + student);

        //在上面的postProcessProperties()方法中执行了private修饰的findAutowiringMetadata方法获取InjectionMetadata对象
        //再调用该InjectionMetadata对象的inject方法进行依赖注入
        //以下对postProcessProperties()方法的逻辑进行模拟实现
        try {
            Method findAutowiringMetadata =
                    AutowiredAnnotationBeanPostProcessor.class
                            .getDeclaredMethod("findAutowiringMetadata", String.class, Class.class, PropertyValues.class);
            findAutowiringMetadata.setAccessible(true);
            //找到所有被@Autowired注解的成员信息
            InjectionMetadata injectionMetadata =
                    (InjectionMetadata) findAutowiringMetadata.invoke
                            (autowiredAnnotationBeanPostProcessor, "student", Student.class, null);
            //在这个输出语句上打断点，查看 injectionMetadata -> injectedElements（ArrayList）
            //System.out.println(injectionMetadata);

            injectionMetadata.inject(student, "student", null);
            System.out.println("依赖注入后： " + student);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * test2 续：
     * InjectionMetadata中的inject()方法的逻辑模拟
     */
    @Test
    public void test3() {
        try {
            genericApplicationContext.registerBean("teacher", Teacher.class);
            genericApplicationContext.registerBean("book", Book.class);

            Student student = new Student();
            System.out.println("依赖注入前： " + student);
            System.out.println("=============================================================");

            //根据成员属性找BeanFactory中的bean对象
            Field teacher = Student.class.getDeclaredField("teacher");
            System.out.println("Field teacher：" + teacher);
            //如果是required 为 true的话，找不到将抛出异常
            DependencyDescriptor teacherDependency = new DependencyDescriptor(teacher, false);
            Object dependency = defaultListableBeanFactory
                    .doResolveDependency(teacherDependency, null, null, null);
            System.out.println("dependency：" + dependency);
            //找到后通过set方法注入依赖
            //student.setTeacher((Teacher) dependency);
            System.out.println(student);
            System.out.println("=============================================================");

            //根据成员方法的参数找BeanFactory中的bean对象
            Method setBook = Student.class.getDeclaredMethod("setBook", Book.class);
            System.out.println("Method setBook：" + setBook);
            //PropertyDescriptor 属性描述符，描述JavaBean通过一对访问器方法导出的一个属性，可以通过该对象获取属性的get、set方法。
            //方法对象及该方法的参数索引
            DependencyDescriptor setBookDescriptor =
                    new DependencyDescriptor(new MethodParameter(setBook, 0), false);
            dependency = defaultListableBeanFactory
                    .doResolveDependency(setBookDescriptor, null, null, null);
            System.out.println("dependency：" + dependency);
            student.setBook((Book) dependency);
            System.out.println(student);
            System.out.println("=============================================================");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * BeanFactory 后处理器：
     * ConfigurationClassPostProcessor
     */
    @Test
    public void test4() {
        genericApplicationContext.registerBean("config", Config.class);
        genericApplicationContext.registerBean("configurationClassPostProcessor", ConfigurationClassPostProcessor.class);

        genericApplicationContext.refresh();

        System.out.println("\n现有以下bean：");
        for (String beanDefinitionName : genericApplicationContext.getBeanDefinitionNames()) {
            System.out.println(beanDefinitionName);
        }
        genericApplicationContext.close();
    }

    /**
     * BeanFactory 后处理器模拟实现——@ComponentScan组件扫描@Component注解
     * 及其派生注解@Controller、@Service、@Repository
     */
    @Test
    public void test5() {
        try {
            abstractBeanDefinition.setBeanClass(Config.class);
            //1，先通过注解的工具类 AnnotationUtils 找到@Component注解中的包名
            ComponentScan componentScan =
                    AnnotationUtils.findAnnotation(Config.class, ComponentScan.class);
            if (componentScan != null) {
                for (String basePackage : componentScan.basePackages()) {
                    System.out.println("包名：" + basePackage);
                    //2,通过这个容器的通配符类扫描到该包下的所有类
                    String classpath = "classpath*:" + basePackage.replace(".", "/") + "/**/*.class";
                    Resource[] resources = genericApplicationContext.getResources(classpath);
                    for (Resource resource : resources) {
                        MetadataReader metadataReader = cachingMetadataReaderFactory.getMetadataReader(resource);
                        System.out.println("类名：" + metadataReader.getClassMetadata().getClassName());
                        System.out.println("是否加了@Component注解：" + metadataReader.getAnnotationMetadata()
                                .hasAnnotation(Component.class.getName()));
                        System.out.println("是否加了@Component注解的派生注解：" + metadataReader.getAnnotationMetadata()
                                .hasMetaAnnotation(Component.class.getName()));
                        //判断如果有@Component注解及其派生注解注解的bean
                        if (metadataReader.getAnnotationMetadata().hasAnnotation(Component.class.getName())
                                ||
                                metadataReader.getAnnotationMetadata().hasMetaAnnotation(Component.class.getName())) {
                            String beanName =
                                    annotationBeanNameGenerator.generateBeanName(abstractBeanDefinition, defaultListableBeanFactory);
                            //将有@Component注解及其派生注解注解的bean加入到 configurableListableBeanFactory 中
                            defaultListableBeanFactory.registerBeanDefinition(beanName, abstractBeanDefinition);
                            System.out.println("beanName： " + beanName);
                            System.out.println("=============================================================");
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * BeanFactory 后处理器模拟实现——组件扫描@Bean注解
     */
    @Test
    public void test6() {
        try {
            genericApplicationContext.registerBean("config", Config.class);

            genericApplicationContext.refresh();

            //读取Config类的元信息
            MetadataReader reader =
                    cachingMetadataReaderFactory
                            .getMetadataReader(new ClassPathResource("spring/bean/Config.class"));
            //读取被@Bean修饰的方法信息
            Set<MethodMetadata> methodMetadataSet =
                    reader.getAnnotationMetadata().getAnnotatedMethods(Bean.class.getName());
            System.out.println("@Bean注解的方法为：");
            for (MethodMetadata metadata : methodMetadataSet) {
                String methodName = metadata.getMethodName();
                System.out.println(methodName);
                //将这些@Bean修饰的方法加入到config类中
                beanDefinitionBuilder.setFactoryMethodOnBean(methodName, "config");
                //自动装配
                abstractBeanDefinition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_CONSTRUCTOR);
                defaultListableBeanFactory.registerBeanDefinition(methodName, abstractBeanDefinition);
            }

            System.out.println("===============================================================");
            System.out.println("现有以下 bean：");
            String[] beanDefinitionNames = configurableListableBeanFactory.getBeanDefinitionNames();
            for (String beanDefinitionName : beanDefinitionNames) {
                System.out.println(beanDefinitionName + " —— " + genericApplicationContext.getBean(beanDefinitionName));
            }

//            Field singletonObjects = DefaultSingletonBeanRegistry.class.getDeclaredField("singletonObjects");
//            singletonObjects.setAccessible(true);
//            Map<String, Object> map = (Map<String, Object>) singletonObjects.get(configurableListableBeanFactory);
//            map.forEach((k, v) -> {
//                System.out.println(k + " : " + v);
//            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
