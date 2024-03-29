package com.wisdri.epms.Service;

import com.wisdri.epms.Dao.PersonMapper;
import com.wisdri.epms.Entity.Person;
import com.wisdri.epms.Util.LDAPUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.naming.ldap.LdapContext;

/**
 * @author 李韬 @date 2022/4/8 10:26
 * @description 处理登录的问题
 */
@Service
@Slf4j
public class LoginService {

    @Autowired
    PersonMapper personMapper;

    /**
     * 从数据库检查，密码是否正确、用户是否存在
     * @param id
     * @param password
     * @return
     */
    public boolean CheckPasswordFromDB(String id, String password){
        log.info(id + " 进入DB验证");
        String passwordStored = personMapper.GetPasswordById(id);
        if (passwordStored == null){
            log.info("密码未保存过");
            return false;
        }
        if (passwordStored.equals("") || passwordStored.isEmpty()){
            log.info("密码为空");
            return false;
        }
        if (! passwordStored.equals(password))
            return false;
        return true;
    }

    @Autowired
    LDAPUtil util;
    @Value("${LDAP.baseDN}")
    public String baseDN;


    /**
     * 从LDAP检查，密码是否正确、用户是否存在
     * @param id
     * @param password
     * @return
     */
    public boolean CheckPasswordFromLDAP(String id, String password){
        log.info(id + " 进入LDAP验证");
        //从LDAP检查，密码是否正确、用户是否存在

        log.info("LDAP是否开启 ？ " + util.LDAPEnabled);
        if (Boolean.parseBoolean(util.LDAPEnabled) == false){
            log.info("未开启LDAP认证");
            return false;
        }
        LdapContext ctx = util.Authentication(id, password, util.host, util.domain, util.port);
        if (ctx != null){
            //如果在LDAP中验证成功，则可能是个新用户或者更新过密码
            //新增或更新用户信息
            String idStored = personMapper.SearchPersonId(id);
            //从LDAP中读取用户信息
            String filter = "cn=" + id;
            Person person = util.readLdap(ctx, baseDN, filter);
            if(null!=ctx){
                try {
                    ctx.close();
                    ctx=null;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            person.setPassword(password);
            System.out.println(person);
            if (idStored == null)  //不存在用户的时候，直接插入用户
                personMapper.AddPerson(person);
            else if (!idStored.equals("") && !(idStored == null))  //存在用户的时候更新密码
                personMapper.UpdatePerson(id, password);
            return true;
        }
        else{
            return false;
        }
    }


}
