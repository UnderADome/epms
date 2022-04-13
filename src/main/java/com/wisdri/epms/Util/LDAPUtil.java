package com.wisdri.epms.Util;

import com.wisdri.epms.Entity.Person;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.directory.*;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * @author 李韬 @date 2022/4/7 9:31
 * @description LDAP认证
 */
@Slf4j
@Service
@PropertySource(value = "classpath:application.properties", ignoreResourceNotFound = true)
public class LDAPUtil {

    /*
        使用范例：
        LDAPUtil util = new LDAPUtil();
		boolean flag = util.Authentication("14530", "Litaozuel123",
				"192.168.200.14", "@wisdri.com", "389");
		System.out.println(flag);
     */
    @Value("${LDAP.host}")
    public String host;
    @Value("${LDAP.domain}")
    public String domain;
    @Value("${LDAP.port}")
    public String port;
    @Value("${LDAP.enabled}")
    public String LDAPEnabled;


    /**
     * @param userName
     * @param password
     * @param host      IP
     * @param domain    @wisdri.com
     * @param port      默认389
     * @return
     */
    public LdapContext Authentication(String userName, String password, String host, String domain, String port){
        String url = new String("ldap://" + host + ":" + port);//固定写法
        String user = userName.indexOf(domain) > 0 ? userName : userName
                + domain;//网上有别的方法，但是在我这儿都不好使，建议这么使用
        Hashtable env = new Hashtable();//实例化一个Env
        LdapContext ctx = null;
        env.put(Context.SECURITY_AUTHENTICATION, "simple");//LDAP访问安全级别(none,simple,strong),一种模式，这么写就行
        env.put(Context.SECURITY_PRINCIPAL, user); //用户名
        env.put(Context.SECURITY_CREDENTIALS, password);//密码
        env.put(Context.INITIAL_CONTEXT_FACTORY,
                "com.sun.jndi.ldap.LdapCtxFactory");// LDAP工厂类
        env.put(Context.PROVIDER_URL, url);//Url
        //env.put(Context.REFERRAL, "follow");//这里有三种模式，默认是ignore
        try {
            ctx = new InitialLdapContext(env, null);
            //ctx1 = new InitialDirContext(env);// 初始化上下文
            log.info("初始化ctx");
            log.info(userName + " " + password + " " + "身份验证成功!");
            //readLdap(ctx, "dc=wisdri,dc=com", "cn=14530");
            return ctx;
        } catch (AuthenticationException e) {
            log.info(LocalDateTime.now() + " " + "身份验证失败!");
            e.printStackTrace();
            return null;
        } catch (javax.naming.CommunicationException e) {
            log.info(LocalDateTime.now() + " " + "AD域连接失败!");
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            log.info(LocalDateTime.now() + " " + "未知的身份信息!");
            e.printStackTrace();
            return null;
        }
        //
        //一个非常狗血的问题：如果写了finally，try里面的内容一定会先走finally，再return
        //
//        finally{
//            if(null!=ctx){
//                try {
//                    ctx.close();
//                    ctx=null;
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
    }

    /**
     * 从ldap中读取用户基本信息
     * @param ctx
     * @param basedn
     * @param filter 过滤条件 格式：filter = "cn=14530";
     */
    public Person readLdap(LdapContext ctx, String basedn, String filter){
        Person person = new Person();
        try{
            if (ctx!=null){
                log.info("进入readLdap方法");
                //过滤条件
                //String filter = "displayName=李韬";//"(&(objectClass=*)(uid=*))"
                String[] attrPersonArray = { "uid", "userPassword", "displayName", "cn", "sn", "mail", "description",
                    "ou", "mobile", "objectClass"};

                //搜索控件
                SearchControls searchControls = new SearchControls();
                searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);//搜索范围
                searchControls.setReturningAttributes(attrPersonArray);
                System.out.println("filter="+filter + " baseDN=" + basedn);
                //可以查询所在组织
                NamingEnumeration<SearchResult> answer_test = ctx.search(basedn, filter.toString(), searchControls);
                //NamingEnumeration<SearchResult> answer_test = ctx.search("dc=wisdri,dc=com", "displayName=李韬", searchControls);
                while(answer_test!=null && answer_test.hasMoreElements()){
                    SearchResult sr = (SearchResult) answer_test.next();
                    log.info("getname=" + sr.getName());
                }

                //https://blog.csdn.net/belialxing/article/details/89157737
                //https://www.cnblogs.com/Nadim/p/4681003.html
                //https://blog.csdn.net/qq_34605063/article/details/108303657
                // 1.要搜索的上下文或对象的名称；
                // 2.过滤条件，可为null，默认搜索所有信息；
                // 3.搜索控件，可为null，使用默认的搜索控件
                NamingEnumeration<SearchResult> answer = ctx.search(basedn, filter.toString(),searchControls);
                log.info("搜索结果是否为空？" + (answer == null) + " hasMore?" + (answer.hasMore()) + "  " + answer.toString());
                while (answer.hasMore()) {
                    log.info("执行搜索ing...");
                    SearchResult result = (SearchResult) answer.next();
                    NamingEnumeration<? extends Attribute> attrs = result.getAttributes().getAll();

                    while (attrs.hasMore()) {
                        Attribute attr = (Attribute) attrs.next();
                        log.info(attr.getID() + " " + attr.get().toString());
                        if("cn".equals(attr.getID())){
                            System.out.println(attr.get().toString());
                            person.setId(attr.get().toString());
                        }else if("displayName".equals(attr.getID())){
                            person.setName(attr.get().toString());
                        }else if("mail".equals(attr.getID())){
                            person.setMail(attr.get().toString());
                        }
                    }
                }
            }
        }catch (Exception e) {
            System.out.println("获取用户信息异常:");
            e.printStackTrace();
        }

        return person;
    }
}