package com.springboot.cloud.config;

import com.springboot.cloud.app.timesheet.dao.MemberMapper;
import com.springboot.cloud.app.timesheet.entity.po.Member;
import com.springboot.cloud.common.core.util.CommonUtil;
import com.springboot.cloud.common.core.util.CookieUtil;
import com.springboot.cloud.common.core.filter.MyShiroRealm;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.OncePerRequestFilter;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @ClassName ShiroConfig
 * @Description Shiro配置
 *
 */
@Configuration
public class ShiroConfig {

    private static String COOKIE_PATH = "/";
    private static int COOKIE_MAX_AGE = 7 * 24 * 60 * 60;

    //权限过滤
    @Bean(name="shiroFilter")
    public ShiroFilterFactoryBean shiroFilter() {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();

        //配置自定义过滤器
        Map<String, Filter> filtersMap = new LinkedHashMap<String, Filter>();
        filtersMap.put("sessionFilter", sessionFilter());
        shiroFilterFactoryBean.setFilters(filtersMap);

        //配置放行和拦截的资源
        //authc: 该过滤器下的页面必须验证后才能访问
        //anon: 所有url都可以匿名访问
        Map<String, String> definitionsMap = new LinkedHashMap<>();
        definitionsMap.put("/work/saveWorkList", "anon"); //新增N个工作记录表
        definitionsMap.put("/timesheet/authorized", "anon"); //企业微信登陆
        definitionsMap.put("/signin", "anon"); //登录
//        definitionsMap.put("/signinMem", "anon"); //会员登录
//        definitionsMap.put("/signout", "anon"); //注销
//        definitionsMap.put("/getKaptchaImage", "anon"); //验证码
//        definitionsMap.put("/sendVcode", "anon"); //验证码
//        definitionsMap.put("/resetPassword", "anon"); //重置密码
//        definitionsMap.put("/verifyEmailVocde", "anon"); //验证邮箱验证码
        definitionsMap.put("/register", "anon"); //注册
        definitionsMap.put("/**", "sessionFilter"); //RememberMe--不加authc/user--否则isRemembered==false


        shiroFilterFactoryBean.setFilterChainDefinitionMap(definitionsMap);

        //配置登录的url
        shiroFilterFactoryBean.setLoginUrl("/");

        //配置核心安全事务管理器
        shiroFilterFactoryBean.setSecurityManager(securityManager());

        return shiroFilterFactoryBean;
    }

    //==============================生成shiroDaoRealm============================================

    @Bean
    public MyShiroRealm shiroDaoRealm() {
        MyShiroRealm shiroDaoRealm = new MyShiroRealm();
        //设置密码凭证匹配器
        shiroDaoRealm.setCredentialsMatcher(hashedCredentialsMatcher());

        return shiroDaoRealm;
    }

    //密码匹配凭证管理器
    @SuppressWarnings("deprecation")
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setStoredCredentialsHexEncoded(false);
        hashedCredentialsMatcher.setHashIterations(1024);
        hashedCredentialsMatcher.setHashAlgorithmName(Sha256Hash.ALGORITHM_NAME);
        hashedCredentialsMatcher.setHashSalted(true);

        return hashedCredentialsMatcher;
    }

    //==================================rememberMe功能========================================

    //cookie对象
    @Bean
    public SimpleCookie rememberMeCookie() {
        SimpleCookie simpleCookie = new SimpleCookie();
        //这个参数是cookie的名称,对应前端的checkbox的name=rememberMe
        simpleCookie.setName("rememberMe");
        //setcookie的httponly属性如果设为true的话,会增加对xss防护的安全系数.它有以下特点:
        //setcookie()的第七个参数
        //设为true后,只能通过http访问,javascript无法访问
        //防止xss读取cookie
        simpleCookie.setHttpOnly(false);
        simpleCookie.setPath(COOKIE_PATH);
        //记住我cookie生效时间7天,单位秒
        simpleCookie.setMaxAge(COOKIE_MAX_AGE);

        return simpleCookie;
    }

    //cookie管理对象;记住我功能
    @Bean
    public CookieRememberMeManager rememberMeManager() {
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(rememberMeCookie());
        //rememberme cookie加密的密钥建议每个项目都不一样--默认AES算法 密钥长度(128 256 512 位),通过以下代码可以获取
        //KeyGenerator keygen = KeyGenerator.getInstance("AES");
        //SecretKey deskey = keygen.generateKey();
        //System.out.println(Base64.encodeToString(deskey.getEncoded()));
        cookieRememberMeManager.setCipherKey(Base64.decode("8jrQAoMK9QplyfscbQnOcg=="));

        return cookieRememberMeManager;
    }

    //==============================生成sessionManager============================================

    //指定会话ID
    @Bean
    public SimpleCookie sessionIdCookie() {
        SimpleCookie simpleCookie = new SimpleCookie();
        simpleCookie.setName("sId");
//        simpleCookie.setHttpOnly(false);
        simpleCookie.setPath(COOKIE_PATH);
        simpleCookie.setMaxAge(180000);
        return simpleCookie;
    }

    //会话ID重组
    @Bean
    public SessionIdGenerator sessionIdGenerator() {
        SessionIdGenerator idGenerator = new SessionIdGenerator() {
            @Override
            public Serializable generateId(Session session) {
                Serializable uuid = new JavaUuidSessionIdGenerator().generateId(session);
                return uuid;
            }
        };

        return idGenerator;
    }

    /**
     * 会话SessionDAO的作用是为Session提供CRUD并进行持久化的一个shiro组件
     * MemorySessionDAO直接在内存中进行会话维护
     * EnterpriseCacheSessionDAO 提供了缓存功能的会话维护,默认情况下使用MapCache实现,内部使用ConcurrentHashMap保存缓存的会话
     */
    @Bean
    public SessionDAO sessionDAO() {
        EnterpriseCacheSessionDAO enterpriseCacheSessionDAO=new EnterpriseCacheSessionDAO();
        //设置session缓存的名字--默认为shiro-activeSessionCache
        enterpriseCacheSessionDAO.setActiveSessionsCacheName("shiro-activeSessionCache");
        //sessionId生成器
        enterpriseCacheSessionDAO.setSessionIdGenerator(sessionIdGenerator());

        return enterpriseCacheSessionDAO;
    }

    //session管理器
    @Bean(name="sessionManager")
    public DefaultWebSessionManager sessionManager() {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        //全局会话超时时间(单位毫秒),默认30分钟--此处一天
        sessionManager.setGlobalSessionTimeout(86400000);
        //是否开启删除无效的session对象,默认为true
        sessionManager.setDeleteInvalidSessions(true);
        //是否开启定时调度器进行检测过期session,默认为true
        sessionManager.setSessionValidationSchedulerEnabled(true);
        //设置session失效的扫描时间,清理用户直接关闭浏览器造成的孤立会话 默认为 1个小时
        //设置该属性 就不需要设置 ExecutorServiceSessionValidationScheduler底层也是默认自动调用ExecutorServiceSessionValidationScheduler
        sessionManager.setSessionValidationInterval(3600000);
        //取消url后面的JSESSIONID
        sessionManager.setSessionIdUrlRewritingEnabled(false);
        //sessionDao
        sessionManager.setSessionDAO(sessionDAO());

        //是否启用/禁用Session Id Cookie
        sessionManager.setSessionIdCookieEnabled(true);
        //指定sessionid
        sessionManager.setSessionIdCookie(sessionIdCookie());

        return sessionManager;
    }

    //session过滤器
    @Bean
    public SessionFilter sessionFilter(){
        return new SessionFilter();
    }

    //==========================================================================
    //由于shiroDaoRealm，sessionManager，rememberMeManager来生成securityManager

    //配置核心安全事务管理器
    @Bean(name = "securityManager")
    public DefaultWebSecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        //设置自定义realm
        securityManager.setRealm(shiroDaoRealm());
        //配置自定义session管理
        securityManager.setSessionManager(sessionManager());
        //配置记住我
        securityManager.setRememberMeManager(rememberMeManager());

        return securityManager;
    }

    //==============================生成shiroDaoRealm============================================

    //开启shiro注解--注解权限
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);

        return authorizationAttributeSourceAdvisor;
    }

    //Shiro生命周期处理器--可以自定的来调用配置在 Spring IOC 容器中 shiro bean 的生命周期方法
    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    //开启shiro注解--启用 IOC 容器中使用 shiro 的注解.但必须在配置了 LifecycleBeanPostProcessor 之后才可以使用
    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator daap = new DefaultAdvisorAutoProxyCreator();
        daap.setProxyTargetClass(true);

        return daap;
    }

    //==========================================================================

    //使用RemenberMe Cookie时反射重建刷新session
    //@link https://blog.csdn.net/Q_AN1314/article/details/53485778#java
    @Component
    class SessionFilter extends OncePerRequestFilter {

        @Autowired
        private MemberMapper userDao;

        @Override
        protected void doFilterInternal(ServletRequest request, ServletResponse response, FilterChain chain)
                throws ServletException, IOException {
            // TODO Auto-generated method stub
            HttpServletRequest req = (HttpServletRequest) request;
            Subject subject = SecurityUtils.getSubject();

            String aa = req.getServletPath();
            if (!"/".equals(req.getServletPath()) && subject.isRemembered()) {
                //如果是RemenberMe则进行session失效判断,如果失效则重建刷新session
                //重建刷新session
                //true: 若存在会话则返回该会话，否则新建一个会话;
                //false: 若存在会话则返回该会话,否则返回NULL
                Session session = subject.getSession(false);
                if (session.getAttribute("user") == null) { //用户session失效
                    String username = (String) subject.getPrincipal();
                    Member user = userDao.getUserByUsername(username);
                    if (user == null) {
                        subject.logout();
                        CookieUtil.deleteAllCookie();

                        response.setContentType("application/json; charset=utf-8");
                        response.setCharacterEncoding("UTF-8");
//                        String resutl = CommonUtil.response(StateUtil.NOT_EXISTS, StateUtil.NOT_EXISTIS_USERNAME_MESSAGES, null, null);
                        response.getWriter().write("error");
                        return;
                    }

                    //设置session
                    session.setAttribute("user", user);
                    //设置cookies
                    CookieUtil.setCookie("user", CommonUtil.toJSONString(user));
                }
            }
            chain.doFilter(request, response);
        }
    }
}