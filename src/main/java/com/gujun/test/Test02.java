package com.gujun.test;

import com.gujun.config.SpringConfig;
import com.gujun.entity.Person;
import com.gujun.service.PersonService;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;

public class Test02 {

    @Test
    public void testCacheable(){
        ApplicationContext context=new AnnotationConfigApplicationContext(SpringConfig.class);
        PersonService personService=context.getBean(PersonService.class);
        List<Person> persons=personService.getAll();
        for(Person person:persons){
            System.out.println(person);
        }
    }

    @Test
    public void testCachePut1(){
        ApplicationContext context=new AnnotationConfigApplicationContext(SpringConfig.class);
        PersonService personService=context.getBean(PersonService.class);
        Person person=personService.getById(2);
        System.out.println(person);
    }

    @Test
    public void testCachePut2(){
        ApplicationContext context=new AnnotationConfigApplicationContext(SpringConfig.class);
        PersonService personService=context.getBean(PersonService.class);
        Person person=new Person("gao",25);
        personService.insertPerson(person);
    }

    @Test
    public void testCacheEvict(){
        ApplicationContext context=new AnnotationConfigApplicationContext(SpringConfig.class);
        PersonService personService=context.getBean(PersonService.class);
        personService.deleteById(4);
    }

    /*
        tips:
        不适合缓存的情形：使用缓存前提是高命中率，对于一些模糊条件查询不适合；
        自调用失效：因为缓存注解也是基于Spring AOP实现的；
     */

}
