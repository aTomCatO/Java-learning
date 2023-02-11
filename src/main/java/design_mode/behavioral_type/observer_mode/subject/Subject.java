package design_mode.behavioral_type.observer_mode.subject;

import design_mode.behavioral_type.observer_mode.Observer;
import design_mode.behavioral_type.observer_mode.content.Content;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author XYC
 */
public abstract class Subject {
    public String subjectName;
    public Content content;

    protected List<Observer> observerList = new LinkedList<>();

    /**
     * 添加观察者
     *
     * @param observer 观察者对象
     */
    public void addObserver(Observer observer) {
        observerList.add(observer);
        System.out.println("*******************************\n" +
                this.getClass().getSimpleName() + " 主题添加观察者： " + observer.getName() +
                "\n*******************************");
    }

    /**
     * 移除观察者
     *
     * @param observer 观察者对象
     */
    public void removeObserver(Observer observer) {
        observerList.remove(observer);
        System.out.println("*******************************\n" +
                this.getClass().getSimpleName() + " 主题移除观察者： " + observer.getName() +
                "\n*******************************");
    }

    /**
     * 更新主题内容
     *
     * 方法中的参数值示例：
     * HashMap<String, Object> paramMap = new HashMap<>(5);
     * paramMap.put("weather", "晴");
     * paramMap.put("temperature", "26℃");
     * 该方法废弃原因：
     *    1，在把独立主题内容独立出来前，主题内容是封装在主题对象（Subject）中，
     *    调用 observer.receive(Subject subject) 方法需要传主题对象给观察者，
     *    这样就会把不必要的属性或方法暴露给观察者对象（removeObserver(Observer observer) 方法等）。
     *    2，并且每个主题对象的内容各异，难以对每个主题对象的内容进行更新（下面代码就可以看出来）
     * 解决方案：
     *    把每个主题对象的内容进行独立封装为一个Content对象
     * @return 返回主题对象
     */
    @Deprecated
    public Subject updateSubjectContent(Map<String, Object> paramMap) {
        try {
            //PropertyDescriptor 是属性描述器，描述JavaBean通过一对访问器方法导出的一个属性，可以通过该对象获取属性的get、set方法。
            BeanInfo beanInfo = Introspector.getBeanInfo(this.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                Object paramValue = paramMap.get(propertyDescriptor.getName());
                if (paramValue != null) {
                    Method writeMethod = propertyDescriptor.getWriteMethod();
                    writeMethod.invoke(this, paramValue);
                }
            }
        } catch (IntrospectionException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public Subject updateSubjectContent(Content content) {
        this.content = content;
        return this;
    }

    /**
     * 通知观察者
     */
    public void notifyObserver() {
        System.out.println(this.getClass().getSimpleName() + " 主题通知观察者:");
        for (Observer observer : observerList) {
            observer.receive(content);
        }
    }
}
