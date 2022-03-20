package com.wisdri.epms.Dao;

import com.wisdri.epms.Entity.Person;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PersonMapper {

    @Select("select * from person")
    List<Person> GetPeople();
}
