package com.wisdri.epms.Util;

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
    public boolean Authentication(String userName, String password, String host, String domain, String port){
        String url = new String("ldap://" + host + ":" + port);//固定写法
        String user = userName.indexOf(domain) > 0 ? userName : userName
                + domain;//网上有别的方法，但是在我这儿都不好使，建议这么使用
        Hashtable env = new Hashtable();//实例化一个Env
        LdapContext ctx = null;
        //DirContext ctx = null;
        env.put(Context.SECURITY_AUTHENTICATION, "simple");//LDAP访问安全级别(none,simple,strong),一种模式，这么写就行
        env.put(Context.SECURITY_PRINCIPAL, user); //用户名
        env.put(Context.SECURITY_CREDENTIALS, password);//密码
        env.put(Context.INITIAL_CONTEXT_FACTORY,
                "com.sun.jndi.ldap.LdapCtxFactory");// LDAP工厂类
        env.put(Context.PROVIDER_URL, url);//Url
        try {
            ctx = new InitialLdapContext(env, null);
            //ctx = new InitialDirContext(env);// 初始化上下文
            log.info("初始化ctx");
            readLdap(ctx, "dc=wisdri,dc=com");
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

    public void readLdap(LdapContext ctx, String basedn){
        List<LdapUser> lm=new ArrayList<LdapUser>();
        try{
            if (ctx!=null){
                log.info("进入readLdap方法");
                //过滤条件
                String filter = "cn=14530";//"(&(objectClass=*)(uid=*))"
                String[] attrPersonArray = { "uid", "userPassword", "displayName", "cn", "sn", "mail", "description" };
                //String[] attrPersonArray = { "uid" };
                //搜索控件
                SearchControls searchControls = new SearchControls();
                searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);//搜索范围
                searchControls.setReturningAttributes(attrPersonArray);
                //测试
                NamingEnumeration<SearchResult> answer_test = ctx.search(basedn, filter.toString(), searchControls);
                log.info("查询结果是否为空？" + (answer_test==null));
                log.info(answer_test.hasMoreElements() + "");
                log.info(answer_test.hasMore() + "");
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
                    LdapUser lu=new LdapUser();
                    while (attrs.hasMore()) {
                        Attribute attr = (Attribute) attrs.next();
                        if("userPassword".equals(attr.getID())){
                            Object value = attr.get();
                            lu.setUserPassword(new String((byte [])value));
                        }else if("uid".equals(attr.getID())){
                            lu.setUid(attr.get().toString());
                        }else if("displayName".equals(attr.getID())){
                            lu.setDisplayName(attr.get().toString());
                        }else if("cn".equals(attr.getID())){
                            lu.setCn(attr.get().toString());
                        }else if("sn".equals(attr.getID())){
                            lu.setSn(attr.get().toString());
                        }else if("mail".equals(attr.getID())){
                            lu.setMail(attr.get().toString());
                        }else if("description".equals(attr.getID())){
                            lu.setDescription(attr.get().toString());
                        }
                        System.out.println(lu);
                    }
                    if(lu.getUid()!=null)
                        lm.add(lu);
                }
            }
        }catch (Exception e) {
            System.out.println("获取用户信息异常:");
            e.printStackTrace();
        }

        //return lm;
    }

    @Data
    public class LdapUser {
        public String cn;
        public String sn;
        public String uid;
        public String userPassword;
        public String displayName;
        public String mail;
        public String description;
        public String uidNumber;
        public String gidNumber;
        /**忽略get\set方法**/
    }
}