package com.springboot.cloud.common.core.filter;

import com.springboot.cloud.app.timesheet.dao.MemberMapper;
import com.springboot.cloud.app.timesheet.entity.po.Member;
import com.springboot.cloud.common.core.entity.po.User;
import com.springboot.cloud.common.core.util.CommonUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MyShiroRealm extends AuthorizingRealm {

    @Autowired
    private MemberMapper memberMapper;

    //身份认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        UsernamePasswordToken upToken = (UsernamePasswordToken)token;
        String username = upToken.getUsername();
        Member user = memberMapper.getUserByUsername(username);
        if (user == null){
            throw new RuntimeException("账号不存在！");
        }
        SimpleAuthenticationInfo account = new SimpleAuthenticationInfo(username,user.getPassword(),getName());
        if (StringUtils.isNotBlank(user.getSalt())) {
            ByteSource salt = ByteSource.Util.bytes(user.getSalt());
            account.setCredentialsSalt(salt);
        }
        CommonUtil.setSession("user",user);
        return account;
    }

    //权限认证
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return null;
    }
}
