package com.wisdri.epms.Dao;

import com.github.pagehelper.Page;
import com.wisdri.epms.Entity.Train;
import org.apache.ibatis.annotations.*;

@Mapper
public interface TrainMapper {

    @Insert("Insert into epms.Train(id, content, question, partner, trainTime, infoCreateTime, " +
            "organize, type)" +
            " values (null, #{content}, #{question}, #{partner}, #{trainTime}, now(), " +
            "#{organize}, #{type})")
    void SaveTrain(Train train);

    @Select("select * from train")
    @ResultType(Train.class)
    Page<Train> GetTrainByPage();

    @Delete("delete from train where id = #{id}")
    void DeleteTrainById(int id);

    @Select("select * from train where id = #{id}")
    Train GetTrainById(int id);

    @Update("update train set content = #{content}, question = #{question}, partner = #{partner}, " +
            "trainTime = #{trainTime}, infoCreateTime = #{infoCreateTime}, organize = #{organize}, type = #{type} " +
            "where id = #{id}")
    void UpdateTrain(Train train);
}
