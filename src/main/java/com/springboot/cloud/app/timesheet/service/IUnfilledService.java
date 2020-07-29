package com.springboot.cloud.app.timesheet.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.springboot.cloud.app.timesheet.entity.po.Unfilled;
import com.springboot.cloud.app.timesheet.entity.vo.UnfilledVo;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @ClassName IUnfilledService
 * @Description 工作未填写表 - 抽象信息业务层接口
 */
public interface IUnfilledService extends IService<Unfilled>{

    /**
     * 人员未汇报清单
     **/
    public Map<String,Object> getPersonUnreportList(JSONObject json);

    /**
     * 导出人员未汇报清单列表
     **/
    public void exportPersonUnreportList(JSONObject json, HttpServletResponse response) throws Exception;

    /**
     * 批量删除未汇报清单
     **/
    public void batchDeleteUnreport(JSONObject json);

    /**
     * 检查今天没有提交报告的人
    **/
    public void updateTodayNoReport();
}
