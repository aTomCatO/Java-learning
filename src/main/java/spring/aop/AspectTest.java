package spring.aop;

import Java.proxy.javabean.People;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

/**
 * @author XYC
 * @EnableAspectJAutoProxy(proxyTargetClass = true)
 */
@Aspect
public class AspectTest {

    /**
     * 定义切点表达式
     */
    @Pointcut("execution(* number(..))")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        System.out.println("环绕通知，在目标方法执行前后实施增强，可以应用于日志、事务管理等功能。");
        System.out.println("环绕通知开始...");

        Object proceed = proceedingJoinPoint.proceed();

        System.out.println("环绕通知结束...");

        return proceed;
    }

    @Before("execution(* number(..))")
    public void before(JoinPoint joinPoint) {
        System.out.println("前置通知，在目标方法执行前实施增强。可以应用于权限管理等功能。");
        System.out.println("目标类是：" + joinPoint.getTarget());
        System.out.println("被植入增强处理的目标方法为：" + joinPoint.getSignature().getName());
    }

    @AfterReturning("execution(* number(..))")
    public void returnAfter(JoinPoint joinPoint) {
        System.out.println("后置通知，在目标方法执行后实施增强，可以应用于关闭流、上传文件、删除临时文件等功能。");
    }

    @AfterThrowing(value = "execution(* number(..))", throwing = "e")
    public void throwingAfter(JoinPoint joinPoint, Throwable e) {
        System.out.println("异常通知，在方法抛出异常后实施增强，可以应用于处理异常记录日志等功能。");
        System.out.println("异常通知：" + e.getMessage());
    }

    @After("execution(* number(..))")
    public void after() {
        System.out.println("最终通知。");
    }
}
