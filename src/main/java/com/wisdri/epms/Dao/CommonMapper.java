package com.wisdri.epms.Dao;

import com.wisdri.epms.Entity.Operation;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommonMapper {

    @Insert("insert into oplog values(null, #{personid}, #{ip}, #{action}, #{operateTime}, #{successFlag})")
    public void SaveLog(Operation operation);

}
