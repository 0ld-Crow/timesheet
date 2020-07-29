package com.springboot.cloud.app.timesheet.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.springboot.cloud.app.timesheet.entity.po.AccessToken;
import com.springboot.cloud.app.timesheet.entity.vo.AccessTokenVo;
import com.springboot.cloud.app.timesheet.entity.vo.SummaryVo;

import java.util.List;

/**
 * @ClassName IAccessTokenService
 * @Description accesstoken - 业务层接口
 */
public interface IAccessTokenService extends IService<AccessToken>{

    public String getToken(Byte type) throws Exception;


}
