package com.gujun.service;

import com.gujun.entity.Person;

import java.util.List;

public interface PersonService {

    List<Person> getAll();

    Person getById(Integer id);

    int deleteById(Integer id);

    int saveOne(Person person);

    Person insertPerson(Person person);

    int update(Person person);

}
