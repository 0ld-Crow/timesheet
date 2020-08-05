package com.springboot.cloud.app.timesheet.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.springboot.cloud.app.timesheet.dao.MemberMapper;
import com.springboot.cloud.app.timesheet.dao.UnfilledMapper;
import com.springboot.cloud.app.timesheet.dao.WorkMapper;
import com.springboot.cloud.app.timesheet.entity.po.Unfilled;
import com.springboot.cloud.app.timesheet.entity.vo.PersonWorkTimeDetailVo;
import com.springboot.cloud.app.timesheet.entity.vo.TodayNoReportPersonVo;
import com.springboot.cloud.app.timesheet.entity.vo.UnfilledVo;
import com.springboot.cloud.app.timesheet.service.IUnfilledService;
import com.springboot.cloud.common.core.util.CommonUtil;
import com.springboot.cloud.common.core.util.ObjectUtil;
import com.springboot.cloud.common.core.util.PoiExcelUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName UnfilledServiceImpl
 * @Description 工作未填写表 - 信息业务层实现
 */
@Slf4j
@Service("unfilledService")
public class UnfilledServiceImpl extends ServiceImpl<UnfilledMapper, Unfilled>  implements IUnfilledService {
    @Autowired
    UnfilledMapper unfilledMapper;

    /**
     * 人员未汇报清单
     **/
    @Override
    public Map<String,Object> getPersonUnreportList(JSONObject json) {
        CommonUtil.defaultParam(json);
        int count = unfilledMapper.getUnfillListCount(json);
        return CommonUtil.wrapPageList(unfilledMapper.getUnfillList(json),count);
    }

    /**
     * 导出人员未汇报清单列表
     **/
    @Override
    public void exportPersonUnreportList(JSONObject json, HttpServletResponse response) throws Exception{
        String sheetName = "未汇报清单";
        String[] keys = {"workDate","userName"};
        String[] columns = {"工作日期","人员名字"};
        List<UnfilledVo> unfilledVos = unfilledMapper.getUnfillList(json);
        List<JSONObject> jsonObjects = ObjectUtil.obj2JsonList(unfilledVos);
        ByteArrayInputStream bais = PoiExcelUtil.exportExcel(jsonObjects,sheetName,keys,columns);
        PoiExcelUtil.writeToOutput(bais,response,sheetName);
    }

    /**
     * 批量删除未汇报清单
     **/
    @Override
    public void batchDeleteUnreport(JSONObject json) {
        String id = json.getString("ids");
        String[] ids = id.split(",");
        List<String> idList = Arrays.asList(ids).stream().distinct().collect(Collectors.toList());
        for (String s : idList) {
            Unfilled unfilled = new Unfilled();
            unfilled.setId(Long.parseLong(s));
            unfilled.setIsDelete(1);
            unfilledMapper.updateById(unfilled);
        }
    }

    /**
     * 检查今天没有提交报告的人
     **/
    @Override
    public void updateTodayNoReport() {
        List<TodayNoReportPersonVo> todayNoReportPersonVos = unfilledMapper.getNoReportPerson();
        for (TodayNoReportPersonVo noReportPersonVo : todayNoReportPersonVos) {
            Unfilled unfilled = new Unfilled();
            unfilled.setUId(noReportPersonVo.getMemberId());
            unfilled.setWorkDate(new Date());
            unfilled.setIsDelete(0);
            unfilled.setCreatedBy(CommonUtil.getCurrentUserId());
            unfilled.setUpdatedBy(CommonUtil.getCurrentUserId());
            unfilled.setCreatedTime(new Date());
            unfilled.setUpdatedTime(new Date());
            save(unfilled);
        }
    }
}