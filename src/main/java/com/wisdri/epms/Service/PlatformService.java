package com.wisdri.epms.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.wisdri.epms.Dao.PlatformMapper;
import com.wisdri.epms.Entity.Platform;
import com.wisdri.epms.Entity.Platform;
import com.wisdri.epms.ResultEntity.MonthInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
        Page<Platform> platforms = platformMapper.GetPlatformByPage();
        platforms = CalIfOverdue(platforms);
        return platforms;
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

        //分三种情况：
        //1、只对项目名称等字符串进行匹配
        Page<Platform> platforms = new Page<Platform>();
        if (platform.getPlatStartTime() == null && platform.getPlatEndTime() == null){
            platforms = platformMapper.GetPlatformByPageAndCondition(platform);
            platforms = CalIfOverdue(platforms);
            return platforms;
        }
        //2、有一个时间，直接匹配
        if ((platform.getPlatStartTime() == null && platform.getPlatEndTime() != null) ||
                (platform.getPlatStartTime() != null && platform.getPlatEndTime() == null)){
            //调用的方法同上面的
            platforms = platformMapper.GetPlatformByPageAndCondition(platform);
            platforms = CalIfOverdue(platforms);
            return platforms;
        }

        //3、有两个时间，范围查询
        else { //都不为null，就是时间范围查询
            platforms =  platformMapper.GetPlatformByPageAndTimeRange(platform);
            platforms = CalIfOverdue(platforms);
            return platforms;
        }
    }

    /**
     * 判断是否延期了
     * @param platforms
     * @return
     */
    public Page<Platform> CalIfOverdue(Page<Platform> platforms){
        for (Platform platform: platforms) {
            if (platform.getPlatEndTime()!=null && platform.getPlatRealEndTime()!=null)
                if (platform.getPlatRealEndTime().isAfter(platform.getPlatEndTime())){
                    System.out.println(platform.getId());
                    platform.setOverdue(1);
                }
            if (platform.getPlatEndTime()!=null)
                if (LocalDate.now().isAfter(platform.getPlatEndTime()) && platform.getFinished() == 0){
                    platform.setOverdue(1);
                }
        }
        //调用dao来对overdue进行回写
        platformMapper.SetOverdue(platforms);
        return platforms;
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
     * 更新内容 by id
     * //更新之后需要对修改了预期完成时间的情况进行判别
     * @param platform
     */
    public void UpdatePlatform(Platform platform){
        if (platform.getPlatRealEndTime() != null)
            platformMapper.UpdatePlatformWithDone(platform);
        else
            platformMapper.UpdatePlatformWithNotDone(platform);
        //如果修改之后的预计完成时间，在当前时间之后，并且任务并没有完成的时候
        if (platform.getPlatEndTime().isAfter(LocalDate.now()) &&
            platform.getPlatRealEndTime() == null){
            NotdonePlatform(platform.getId());
            platformMapper.SetOverdueOfOnePlatform(platform);
        }
    }

    /**
     * 更新为未完成的状态
     * @param id
     * @return
     */
    public String NotdonePlatform(String id){
        try{
            platformMapper.NotdonePlatform(Integer.parseInt(id));
            return "1";
        }catch (Exception e){
            return "0";
        }
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
