package com.wisdri.epms.Dao;

import com.github.pagehelper.Page;
import com.wisdri.epms.Entity.Meeting;
import com.wisdri.epms.Entity.Train;
import com.wisdri.epms.ResultEntity.MonthInfo;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface TrainMapper {

    @Insert("Insert into epms.Train(id, content, question, partner, trainTime, infoCreateTime, " +
            "organize, type)" +
            " values (null, #{content}, #{question}, #{partner}, #{trainTime}, now(), " +
            "#{organize}, #{type})")
    void SaveTrain(Train train);

    @Select("select * from train order by trainTime desc")
    @ResultType(Train.class)
    Page<Train> GetTrainByPage();

    @Select("<script>" +
            "select * from train where 1=1 " +
            "<if test='organize!=null and organize!=\"\"'> and organize like concat('%',#{organize},'%') </if> " +
            "<if test='type!=null and type!=\"\"'> and type like concat('%',#{type},'%') </if> " +
            "<if test='trainTime!=null'> and trainTime like concat('%',#{trainTime},'%') </if> " +
            "order by train.infoCreateTime desc" +
            "</script>")
    Page<Train> GetTrainByPageAndCondition(Train train);

    @Delete("delete from train where id = #{id}")
    void DeleteTrainById(int id);

    @Select("select * from train where id = #{id}")
    Train GetTrainById(int id);

    @Update("update train set content = #{content}, question = #{question}, partner = #{partner}, " +
            "trainTime = #{trainTime}, infoCreateTime = #{infoCreateTime}, organize = #{organize}, type = #{type} " +
            "where id = #{id}")
    void UpdateTrain(Train train);

    @Select("select date_format(trainTime, '%m') as time, count(id) as trainCount from train " +
            "where year(trainTime) = #{year} group by month(trainTime)")
    List<MonthInfo> GetTrainCountByYearMonth(String year);

    @Select("select count(id) from train")
    int GetAllCountOfTrain();
}
