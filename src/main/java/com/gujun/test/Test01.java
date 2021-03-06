package com.gujun.test;

import com.gujun.dao.PersonMapper;
import com.gujun.entity.Person;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;
import java.util.Map;

/**
 * @ClassName gu
 * @Description TODO
 * @Author GuJun
 * @Date 2019/8/7 22:05
 * @Version 1.0
 **/
public class Test01 {

    @Test
    public void test01(){
        ApplicationContext context=new ClassPathXmlApplicationContext("spring-config.xml");
        PersonMapper personMapper=context.getBean(PersonMapper.class);
        List<Person> persons=personMapper.getAll();
        System.out.println(persons.size());
        for(Person person:persons){
            System.out.println(person.toString());
        }
    }

}
