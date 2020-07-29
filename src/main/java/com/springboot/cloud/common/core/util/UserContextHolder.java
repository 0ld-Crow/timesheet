package com.springboot.cloud.common.core.util;

import com.google.common.collect.Maps;

import java.util.Map;
import java.util.Optional;

/**
 * @ClassName UserContextHolder
 * @Description 用户上下文
 * @Author cj
 * @Date 2019/05/30
 */
public class UserContextHolder {

    private ThreadLocal<Map<String, String>> threadLocal;

    private UserContextHolder() {
        // 改成子线程也兼容,支持async和异步线程 by cxl
        // ps：后续模块还是不要直接访问项目项目端口，应该是要gateway转发
        this.threadLocal = new InheritableThreadLocal<>();
    }

    /**
     * 创建实例
     *
     * @return
     */
    public static UserContextHolder getInstance() {
        return SingletonHolder.S_INSTANCE;
    }

    /**
     * 静态内部类单例模式
     * 单例初使化
     */
    private static class SingletonHolder {
        private static final UserContextHolder S_INSTANCE = new UserContextHolder();
    }

    /**
     * 用户上下文中放入信息
     *
     * @param map
     */
    public void setContext(Map<String, String> map) {
        threadLocal.set(map);
    }

    /**
     * 获取上下文中的信息
     *
     * @return
     */
    public Map<String, String> getContext() {
        return threadLocal.get();
    }


    public String getExtid() {
        return Optional.ofNullable(threadLocal.get()).orElse(Maps.newHashMap()).get("extid");
    }
    /**
     * 获取上下文中的用户名
     *
     * @return
     */
    public String getUsername() {
        return Optional.ofNullable(threadLocal.get()).orElse(Maps.newHashMap()).get("user_name");
    }
    /**
     * 获取上下文中的当前年级
     *
     * @return
     */
    public String getGradeId() {
        return Optional.ofNullable(threadLocal.get()).orElse(Maps.newHashMap()).get("grade_id");
    }

    /**
     * 清空上下文
     */
    public void clear() {
        threadLocal.remove();
    }

}
