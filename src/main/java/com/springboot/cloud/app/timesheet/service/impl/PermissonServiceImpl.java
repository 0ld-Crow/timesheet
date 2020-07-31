package com.springboot.cloud.app.timesheet.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.springboot.cloud.app.timesheet.dao.PermissonMapper;
import com.springboot.cloud.app.timesheet.entity.po.Permisson;
import com.springboot.cloud.app.timesheet.service.IPermissonService;
import org.springframework.stereotype.Service;

/**
 * @ClassName PermissionServiceImpl
 * @Description 权限信息表
 */
@Service("permissonService")
public class PermissonServiceImpl extends ServiceImpl<PermissonMapper, Permisson> implements IPermissonService {



}