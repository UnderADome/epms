package com.wisdri.epms.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.wisdri.epms.Dao.ExecuteMapper;
import com.wisdri.epms.Entity.Execute;
import com.wisdri.epms.Entity.Plan;
import com.wisdri.epms.Entity.Receive.SearchExecute;
import com.wisdri.epms.Entity.Receive.SearchPlan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExecuteService {
    @Autowired
    private ExecuteMapper executeMapper;

    /**
     * 存储实施信息
     * @param execute
     */
    public void SaveExecute(Execute execute){
        executeMapper.SaveExecute(execute);
    }

    /**
     * 分页查询实施相关信息
     * @param page
     * @param pagesize
     * @return
     */
    public Page<Execute> GetExecuteByPage(int page, int pagesize){
        PageHelper.startPage(page, pagesize);
        return executeMapper.GetExecuteByPage();
    }

    public Page<Execute> GetExecuteByPageAndCondition(int page, int pagesize, SearchExecute execute){
        PageHelper.startPage(page, pagesize);
        //分三种情况：
        //1、只对项目名称等字符串进行匹配
        if (execute.getExeStartTime() == null && execute.getExeEndTime() == null)
            return executeMapper.GetExecuteByPageAndNormalCondition(execute);
        //2、有一个时间，直接匹配
        if ((execute.getExeStartTime() == null && execute.getExeEndTime() != null) ||
                (execute.getExeStartTime() != null && execute.getExeEndTime() == null))
            //调用的方法同上面的
            return executeMapper.GetExecuteByPageAndNormalCondition(execute);
            //3、有两个时间，范围查询
        else //都不为null，就是时间范围查询
            return executeMapper.GetExecuteByPageAndTimeRange(execute);
    }

    /**
     * 删除实施信息
     * id string->int
     * @param id
     */
    public void DeleteExecute(String id){
        executeMapper.DeleteExecuteById(Integer.parseInt(id));
    }

    /**
     * 查询实施信息 by id
     * @param id
     * @return
     */
    public Execute GetExecuteById(int id){
        return executeMapper.GetExecuteById(id);
    }

    /**
     * 更新实施内容 by id
     * @param execute
     */
    public void UpdateExecute(Execute execute){
        executeMapper.UpdateExecute(execute);
    }

    /**
     * 更新已完成状态
     * @param id
     * @return
     */
    public String FinishExecute(String id){
        try{
            executeMapper.FinishExecute(Integer.parseInt(id));
            return "1";
        }catch (Exception e){
            return "0";
        }

    }
}
