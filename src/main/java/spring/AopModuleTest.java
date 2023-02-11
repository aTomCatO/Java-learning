package spring;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.aspectj.lang.annotation.Before;
import org.junit.Test;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectInstanceFactory;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.aspectj.AspectJMethodBeforeAdvice;
import org.springframework.aop.aspectj.SingletonAspectInstanceFactory;
import org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.CommonAnnotationBeanPostProcessor;
import org.springframework.context.annotation.ConfigurationClassPostProcessor;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.stereotype.Component;
import spring.aop.AspectTest;
import spring.bean.People;
import spring.bean.Student;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Map;

/**
 * @author XYC
 */
public class AopModuleTest extends ApplicationTest {
    /**
     * AOP
     * AspectJAroundAdvice
     * AspectJMethodBeforeAdvice
     * AspectJAfterReturningAdvice
     * AspectJAfterThrowingAdvice
     * AspectJAfterAdvice
     */
    @Test
    public void test1() {
        //切点
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("execution(* number(..))");

        //通知
        MethodInterceptor methodInterceptor = new MethodInterceptor() {
            @Override
            public Object invoke(MethodInvocation invocation) throws Throwable {
                System.out.println("前置增强...");
                invocation.getArguments()[0] = 18;
                String proceed = (String) invocation.proceed();
                System.out.println("后置增强...");
                return "今年：" + proceed + " 岁";
            }
        };
        //切面
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(pointcut, methodInterceptor);

        /**
         * 创建代理
         * ProxyTargetClass=false 目标实现了接口，用JDK动态代理
         * ProxyTargetClass=false 目标没有实现接口，用Cglib动态代理
         * ProxyTargetClass=true  用Cglib动态代理
         * */
        Student student = new Student();
        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.setTarget(student);
        proxyFactory.setInterfaces(student.getClass().getInterfaces());
        proxyFactory.addAdvisor(advisor);
        People proxy = (People) proxyFactory.getProxy();
        System.out.println(proxy.number(16));
        System.out.println("代理类对象是：" + proxy.getClass());
    }

    /**
     * 常见切点匹配的实现
     * 1，AspectJExpressionPointcut类中的 matches方法
     * 2、StaticMethodMatcherPointcut类中的 matches方法
     */
    @Test
    public void test2() throws NoSuchMethodException {
        AspectJExpressionPointcut aspectJExpressionPointcut = new AspectJExpressionPointcut();

        //根据方法信息进行匹配的切点表达式
        aspectJExpressionPointcut.setExpression("execution(* number(..))");

        System.out.println("切点是否匹配init方法：" + aspectJExpressionPointcut.matches(Student.class.getMethod("init"), Student.class));
        System.out.println("切点是否匹配number方法：" + aspectJExpressionPointcut.matches(Student.class.getMethod("number", Number.class), Student.class));

        System.out.println("========================================================================");

        //根据方法上的注解进行匹配切点表达式
        aspectJExpressionPointcut = new AspectJExpressionPointcut();
        aspectJExpressionPointcut.setExpression("@annotation(javax.annotation.PostConstruct)");
        System.out.println("切点是否匹配init方法：" + aspectJExpressionPointcut.matches(Student.class.getMethod("init"), Student.class));
        System.out.println("切点是否匹配number方法：" + aspectJExpressionPointcut.matches(Student.class.getMethod("number", Number.class), Student.class));
        System.out.println("========================================================================");

        StaticMethodMatcherPointcut staticMethodMatcherPointcut = new StaticMethodMatcherPointcut() {
            @Override
            public boolean matches(Method method, Class<?> targetClass) {
                //检查方法上是否加了@PostConstruct注解
                MergedAnnotations annotations = MergedAnnotations.from(method);
                if (annotations.isPresent(PostConstruct.class)) {
                    System.out.println("该方法上加了@PostConstruct注解");
                    return true;
                }
                //检查类上是否加了@Component注解
                annotations = MergedAnnotations.from(targetClass);
                if (annotations.isPresent(Component.class)) {
                    System.out.println("该类上加了@Component注解");
                    return true;
                }
                return false;
            }
        };
        System.out.println(staticMethodMatcherPointcut.matches(Student.class.getMethod("init"), Student.class));
        System.out.println(staticMethodMatcherPointcut.matches(Student.class.getMethod("number", Number.class), Student.class));

    }

    /**
     * AnnotationAwareAspectJAutoProxyCreator
     * Bean 的后处理器 —— 处理切面类，根据切面创建代理对象
     */
    @Test
    public void test3() {
        genericApplicationContext.refresh();

        genericApplicationContext.registerBean(ConfigurationClassPostProcessor.class);
        genericApplicationContext.registerBean(CommonAnnotationBeanPostProcessor.class);

        genericApplicationContext.registerBean("proxyCreator", AnnotationAwareAspectJAutoProxyCreator.class);

        //添加 bean 的后处理器
        Map<String, BeanPostProcessor> beanPostProcessorMap =
                configurableListableBeanFactory.getBeansOfType(BeanPostProcessor.class);
        beanPostProcessorMap.forEach((k, v) -> {
            configurableListableBeanFactory.addBeanPostProcessor(v);
        });

        AnnotationAwareAspectJAutoProxyCreator proxyCreator =
                genericApplicationContext.getBean("proxyCreator", AnnotationAwareAspectJAutoProxyCreator.class);
        //强制使用 Cglib 实现AOP
        proxyCreator.setProxyTargetClass(true);

        genericApplicationContext.registerBean(AspectTest.class);
        genericApplicationContext.registerBean("student", Student.class);

        People student = genericApplicationContext.getBean("student", Student.class);
        System.out.println(student.number(66));

        genericApplicationContext.close();
    }

    /**
     * 高级切面转为低级切面
     */
    @Test
    public void test4() {
        ArrayList<Advisor> list = new ArrayList<>();
        AspectInstanceFactory aspectInstanceFactory = new SingletonAspectInstanceFactory(new AspectTest());
        for (Method declaredMethod : AspectTest.class.getDeclaredMethods()) {
            if (declaredMethod.isAnnotationPresent(Before.class)) {
                String execution = declaredMethod.getAnnotation(Before.class).value();
                AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
                pointcut.setExpression(execution);

                //通知类
                AspectJMethodBeforeAdvice advice =
                        new AspectJMethodBeforeAdvice(declaredMethod, pointcut, aspectInstanceFactory);


                DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(pointcut, advice);
                list.add(advisor);
            }
        }
        for (Advisor advisor : list) {
            System.out.println(advisor);
        }
    }

}
