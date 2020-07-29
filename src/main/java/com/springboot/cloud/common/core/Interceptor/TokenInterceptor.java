package com.springboot.cloud.common.core.Interceptor;

import com.alibaba.fastjson.JSON;
import com.springboot.cloud.app.timesheet.dao.MemberMapper;
import com.springboot.cloud.app.timesheet.entity.po.Member;
import com.springboot.cloud.common.core.entity.vo.Result;
import com.springboot.cloud.common.core.exception.SystemErrorType;
import com.springboot.cloud.common.core.util.CommonUtil;
import com.springboot.cloud.jwt.JwtUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * 拦截校验token
 *  校验token的合法性（是不是空的什么的），不是验证token的正确性
* @author llc
* @Date 2019/11/18
**/

@Component
public class TokenInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    MemberMapper memberMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String result = null;
        String access_token = request.getHeader("Authorization");
        String channel = request.getHeader("channel");
        response.setContentType("application/json; charset=utf-8");
        response.setCharacterEncoding("UTF-8");

        if(StringUtils.isBlank(access_token)) {
            result = JSON.toJSONString(Result.fail(SystemErrorType.UN_AUTHORIZATION));
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().print(result);
            return false;
        }
        access_token = access_token.replaceAll("Bearer ", "");
        String userId = JwtUtil.getUserId(access_token);
        if(Objects.isNull(userId)) {
            result = JSON.toJSONString(Result.fail(SystemErrorType.TOKEN_UNVALID));
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().print(result);
            return false;
        }
        Member user = memberMapper.getUserByUsername(userId);
        if (user == null) {
            result = JSON.toJSONString(Result.fail(SystemErrorType.TOKEN_UNVALID));
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().print(result);
            return false;
        }

        if(!access_token.equals(user.getAccessToken())) {
            result = JSON.toJSONString(Result.fail(SystemErrorType.TOKEN_UNVALID));
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().print(result);
            return false;
        }

        boolean isValid = JwtUtil.verify(access_token, userId, user.getPassword());
        if(isValid) {
            CommonUtil.setSession("user",user);
            if (CommonUtil.getSession("user") == null){
                result = JSON.toJSONString(Result.fail(SystemErrorType.ACCOUNT_INVALID));
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().print(result);
                return false;
            }
        }else {
            result = JSON.toJSONString(Result.fail(SystemErrorType.TOKEN_UNVALID));
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().print(result);
            return false;
        }
        return true;
    }
}
