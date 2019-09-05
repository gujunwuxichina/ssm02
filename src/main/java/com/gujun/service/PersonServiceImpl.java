package com.gujun.service;

import com.gujun.dao.PersonMapper;
import com.gujun.entity.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonServiceImpl implements PersonService {

    @Autowired
    private PersonMapper personMapper;

    @Override
    /*
        @Cacheable，表示在进入方法前，Spring会先去缓存服务器里查找对应的key;若有不会调用方法，而是将缓存的数据直接返回；
        若没有，spring执行方法，再将结构缓存到key中；
        属性：
        value,string[],使用缓存的名称；
        key,string,key的值，支持spring表达式；
     */
    @Cacheable(value = "cacheNoLimit",key = "'persons'")
    public List<Person> getAll() {
        return personMapper.getAll();
    }

    @Override
    /*
        @CachePut,spring不会先去缓存中找，而是直接执行方法，再缓存，即方法始终会被调用；
     */
    @CachePut(value = "cacheLimit",key="'person-'+#id")
    public Person getById(Integer id) {
        return personMapper.getById(id);
    }

    @Override
    /*
        @CacheEvict是为了移除缓存对应的键值对，主要对于删除操作；
        属性：
        allEntries,若为true,表示删除所有键值对(此时指定的key无效)，默认为false;
        beforeInvocation,为true则在方法前删除，若false则是在方法后删除；
     */
    @CacheEvict(value = "cacheLimit",key = "'person-'+#id",beforeInvocation = false)
    public int deleteById(Integer id) {
        return personMapper.deleteById(id);
    }

    @Override
    public int saveOne(Person person) {
        return personMapper.saveOne(person);
    }

    @Override
    //spring表达式中，#result表示方法返回值；
    @CachePut(value = "cacheLimit",key = "'person-'+#result.id")
    public Person insertPerson(Person person) {
        personMapper.saveOne(person);
        return person;
    }

    @Override
    public int update(Person person) {
        return personMapper.update(person);
    }
}
