package com.wisdri.epms.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.wisdri.epms.Dao.TrainMapper;
import com.wisdri.epms.Entity.Train;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrainService {
    @Autowired
    private TrainMapper trainMapper;

    /**
     * 存储培训信息
     * @param train
     */
    public void SaveTrain(Train train){
        trainMapper.SaveTrain(train);
    }

    /**
     * 分页查询培训相关信息
     * @param page
     * @param pagesize
     * @return
     */
    public Page<Train> GetTrainByPage(int page, int pagesize){
        PageHelper.startPage(page, pagesize);
        return trainMapper.GetTrainByPage();
    }

    /**
     * 删除培训信息
     * id string->int
     * @param id
     */
    public void DeleteTrain(String id){
        trainMapper.DeleteTrainById(Integer.parseInt(id));
    }

    /**
     * 查询培训信息 by id
     * @param id
     * @return
     */
    public Train GetTrainById(int id){
        return trainMapper.GetTrainById(id);
    }

    /**
     * 更新培训内容 by id
     * @param train
     */
    public void UpdateTrain(Train train){
        trainMapper.UpdateTrain(train);
    }
}
