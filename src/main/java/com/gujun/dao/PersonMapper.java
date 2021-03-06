package com.gujun.dao;

import com.gujun.entity.Person;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonMapper {

    List<Person> getAll();

    Person getById(Integer id);

    int deleteById(Integer id);

    int saveOne(Person person);

    int update(Person person);

}
