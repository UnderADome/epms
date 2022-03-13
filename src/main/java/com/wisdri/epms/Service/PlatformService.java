package com.wisdri.epms.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.wisdri.epms.Dao.PlatformMapper;
import com.wisdri.epms.Entity.Platform;
import com.wisdri.epms.ResultEntity.MonthInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlatformService {
    @Autowired
    private PlatformMapper platformMapper;

    /**
     * 存储会议信息
     * @param platform
     */
    public void SavePlatform(Platform platform){
        platformMapper.SavePlatform(platform);
    }

    /**
     * 分页查询会议相关信息
     * @param page
     * @param pagesize
     * @return
     */
    public Page<Platform> GetPlatformByPage(int page, int pagesize){
        PageHelper.startPage(page, pagesize);
        return platformMapper.GetPlatformByPage();
    }

    /**
     * 带条件查询会议相关信息
     * @param page
     * @param pagesize
     * @param platform
     * @return
     */
    public Page<Platform> GetPlatformByPageAndCondition(int page, int pagesize, Platform platform){
        PageHelper.startPage(page, pagesize);
        return platformMapper.GetPlatformByPageAndCondition(platform);
    }

    /**
     * 删除会议信息
     * id string->int
     * @param id
     */
    public void DeletePlatform(String id){
        platformMapper.DeletePlatformById(Integer.parseInt(id));
    }

    /**
     * 查询会议信息 by id
     * @param id
     * @return
     */
    public Platform GetPlatformById(int id){
        return platformMapper.GetPlatformById(id);
    }

    /**
     * 更新会议内容 by id
     * @param platform
     */
    public void UpdatePlatform(Platform platform){
        platformMapper.UpdatePlatform(platform);
    }

    /**
     * 获得年度-月 会议的数量
     * @param year
     * @return
     */
    public List<MonthInfo> GetPlatformCountByYearMonth(String year){
        return platformMapper.GetPlatformCountByYearMonth(year);
    }

    /**
     * 得到总数
     * @return
     */
    public int GetAllCountOfPlatform(){
        return platformMapper.GetAllCountOfPlatform();
    }
}
