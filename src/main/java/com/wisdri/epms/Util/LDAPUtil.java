package com.wisdri.epms.Util;

import lombok.extern.slf4j.Slf4j;

import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Hashtable;

/**
 * @author 李韬 @date 2022/4/7 9:31
 * @description LDAP认证
 */
@Slf4j
public class LDAPUtil {

    /*
        使用范例：
        LDAPUtil util = new LDAPUtil();
		boolean flag = util.Authentication("14530", "Litaozuel123",
				"192.168.200.14", "@wisdri.com", "389");
		System.out.println(flag);
     */

    /**
     * @param userName
     * @param password
     * @param host      IP
     * @param domain    @wisdri.com
     * @param port      默认389
     * @return
     */
    public boolean Authentication(String userName, String password, String host, String domain, String port){
        String url = new String("ldap://" + host + ":" + port);//固定写法
        String user = userName.indexOf(domain) > 0 ? userName : userName
                + domain;//网上有别的方法，但是在我这儿都不好使，建议这么使用
        Hashtable env = new Hashtable();//实例化一个Env
        DirContext ctx = null;
        env.put(Context.SECURITY_AUTHENTICATION, "simple");//LDAP访问安全级别(none,simple,strong),一种模式，这么写就行
        env.put(Context.SECURITY_PRINCIPAL, user); //用户名
        env.put(Context.SECURITY_CREDENTIALS, password);//密码
        env.put(Context.INITIAL_CONTEXT_FACTORY,
                "com.sun.jndi.ldap.LdapCtxFactory");// LDAP工厂类
        env.put(Context.PROVIDER_URL, url);//Url
        try {
            ctx = new InitialDirContext(env);// 初始化上下文
            log.info(LocalDateTime.now() + " " + userName + " " + password + " " + "身份验证成功!");
            return true;
        } catch (AuthenticationException e) {
            log.info(LocalDateTime.now() + " " + "身份验证失败!");
            e.printStackTrace();
            return false;
        } catch (javax.naming.CommunicationException e) {
            log.info(LocalDateTime.now() + " " + "AD域连接失败!");
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            log.info(LocalDateTime.now() + " " + "未知的身份信息!");
            e.printStackTrace();
            return false;
        } finally{
            if(null!=ctx){
                try {
                    ctx.close();
                    ctx=null;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
