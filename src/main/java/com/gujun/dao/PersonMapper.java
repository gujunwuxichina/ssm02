package com.gujun.dao;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface PersonMapper {

    List<Map> getAll();

}
