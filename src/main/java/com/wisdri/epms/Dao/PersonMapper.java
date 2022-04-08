package com.wisdri.epms.Dao;

import com.wisdri.epms.Entity.Person;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface PersonMapper {

    @Select("select * from person")
    List<Person> GetPeople();

    @Update("update person set password = #{password} where id = #{id}")
    int UpdatePerson(String id, String password);

    @Insert("insert into person(id, password) values (#{id}, #{password})")
    int AddPerson(String id, String password);

    @Select("select id from person where id = #{id}")
    String SearchPersonId(String id);

    /**
     * 用于检查密码是否正确
     * @param id
     * @return
     */
    @Select("select password from person where id = #{id}")
    public String GetPasswordById(String id);
}
