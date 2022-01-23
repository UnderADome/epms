package com.wisdri.epms.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.wisdri.epms.Dao.ExecuteMapper;
import com.wisdri.epms.Entity.Execute;
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
}
