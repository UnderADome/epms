package com.wisdri.epms.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.wisdri.epms.Dao.ExecuteMapper;
import com.wisdri.epms.Entity.Execute;
import com.wisdri.epms.Entity.Plan;
import com.wisdri.epms.Entity.Receive.SearchExecute;
import com.wisdri.epms.Entity.Receive.SearchPlan;
import com.wisdri.epms.ResultEntity.MonthInfo;
import com.wisdri.epms.ResultEntity.PersonalInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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
        Page<Execute> exes = executeMapper.GetExecuteByPage();
        exes = CalIfOverdue(exes);
        return exes;
    }

    public Page<Execute> GetExecuteByPageAndCondition(int page, int pagesize, SearchExecute execute){
        PageHelper.startPage(page, pagesize);
        //分三种情况：
        //1、只对项目名称等字符串进行匹配
        Page<Execute> exes = new Page<Execute>();
        if (execute.getExeStartTime() == null && execute.getExeEndTime() == null){
            exes = executeMapper.GetExecuteByPageAndNormalCondition(execute);
            exes = CalIfOverdue(exes);
            return exes;
        }
        //2、有一个时间，直接匹配
        if ((execute.getExeStartTime() == null && execute.getExeEndTime() != null) ||
                (execute.getExeStartTime() != null && execute.getExeEndTime() == null)){
            //调用的方法同上面的
            exes = executeMapper.GetExecuteByPageAndNormalCondition(execute);
            exes = CalIfOverdue(exes);
            return exes;
        }

            //3、有两个时间，范围查询
        else { //都不为null，就是时间范围查询
            exes =  executeMapper.GetExecuteByPageAndTimeRange(execute);
            exes = CalIfOverdue(exes);
            return exes;
        }
    }

    public Page<Execute> CalIfOverdue(Page<Execute> exes){
        for (Execute execute: exes) {
            if (execute.getExeEndTime()!=null && execute.getExeRealEndTime()!=null)
            if (execute.getExeRealEndTime().isAfter(execute.getExeEndTime())){
                System.out.println(execute.getId());
                execute.setOverdue(1);
            }
            if (execute.getExeEndTime()!=null)
            if (LocalDate.now().isAfter(execute.getExeEndTime()) && execute.getFinished() == 0){
                execute.setOverdue(1);
            }
        }
        //调用dao来对overdue进行回写
        executeMapper.SetOverdue(exes);
        return exes;
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
        if (execute.getExeRealEndTime() != null){
            //如果实际结束时间不为空，则表示已完成
            executeMapper.UpdateExecuteWithDone(execute);
        }else{
            //如果实际结束时间为空，则表示未完成
            executeMapper.UpdateExecuteWithNotDone(execute);
        }
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

    /**
     * 更新为未完成的状态
     * @param id
     * @return
     */
    public String NotdoneExecute(String id){
        try{
            executeMapper.NotdoneExecute(Integer.parseInt(id));
            return "1";
        }catch (Exception e){
            return "0";
        }
    }

    /**
     * 根据plan id查询所属的实施
     * @param planId
     * @return
     */
    public List<Execute> GetExecutesByPlanId(String planId){
        return executeMapper.GetExecutesByPlanId(Integer.parseInt(planId));
    }

    /**
     * 通过年份查询该年有多少个实施
     * @param year
     * @return
     */
    public int GetExecuteCountByYear(String year){
        return executeMapper.GetExecuteCountByYear(year);
    }

    public List<MonthInfo> GetExecuteCountByYearMonth(String year){
        return executeMapper.GetExecuteCountByYearMonth(year);
    }
    /**
     * 计算年度实施最多的一个月
     * @param year
     * @return
     */
    public String GetExeMostMonthByYear(String year){
        return executeMapper.GetExeMostMonthByYear(year);
    }

    /**
     * 得到总数
     * @return
     */
    public int GetAllCountOfExecute(){
        return executeMapper.GetAllCountOfExecute();
    }
    /**
     * 按照年月、人员id查询实施情况
     */
    public List<PersonalInfo> GetPersonalInfoByYearMonthAndPersonId(String year, String personId){
        return executeMapper.GetPersonalInfoByYearMonthAndPersonId(year, personId);
    }
}
