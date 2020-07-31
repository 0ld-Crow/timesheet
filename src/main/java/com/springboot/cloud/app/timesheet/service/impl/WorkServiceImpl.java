package com.springboot.cloud.app.timesheet.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.springboot.cloud.app.timesheet.dao.MemberMapper;
import com.springboot.cloud.app.timesheet.dao.ProjectMapper;
import com.springboot.cloud.app.timesheet.dao.UnfilledMapper;
import com.springboot.cloud.app.timesheet.dao.WorkMapper;
import com.springboot.cloud.app.timesheet.entity.po.Member;
import com.springboot.cloud.app.timesheet.entity.po.Project;
import com.springboot.cloud.app.timesheet.entity.po.Work;

import com.springboot.cloud.app.timesheet.entity.vo.SummaryVo;
import com.springboot.cloud.app.timesheet.entity.vo.WorkVo;
import com.springboot.cloud.app.timesheet.service.IWorkService;
import com.springboot.cloud.common.core.entity.vo.Result;
import com.springboot.cloud.common.core.util.CommonUtil;
import com.springboot.cloud.common.core.util.ObjectUtil;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @ClassName WorkServiceImpl
 * @Description 工作记录表 - 信息业务层实现
 * @Author cj
 * @Date: 2019-11-11
 */
@Service("workService")
public class WorkServiceImpl extends ServiceImpl<WorkMapper, Work>  implements IWorkService {
    @Autowired
    WorkMapper workMapper;
    @Autowired
    MemberMapper memberMapper;
    @Autowired
    ProjectMapper projectMapper;
    @Autowired
    UnfilledMapper unfilledMapper;

    @Override
    public int batchDelete(JSONObject param) {
        String id = param.getString("ids");
        return workMapper.batchDelete(id);
    }

    @Override
    public boolean saveWork(JSONObject param) throws Exception {
        Work work = (Work) ObjectUtil.map2ObjAndCast(param, Work.class);
        work.setIsDelete(0);
        work.setCreatedBy(CommonUtil.getCurrentUserId());
        work.setUpdatedBy(CommonUtil.getCurrentUserId());
        work.setCreatedTime(new Date());
        work.setUpdatedTime(new Date());
        return workMapper.insert(work) > 0;
    }

    /**
     * 新增N个工作记录表
     **/
    @Override
    public List<WorkVo> saveWorkList(JSONArray param) throws Exception{
        Boolean res = false;
        List<Work> workList = new ArrayList<Work>();
        for(int i=0;i<param.size();i++){
            //3、把里面的对象转化为JSONObject
            JSONObject job = param.getJSONObject(i);
            Work work = (Work) ObjectUtil.map2ObjAndCast(job,Work.class);
            Long uid = CommonUtil.getLoginUserId();
            work.setUId(uid);
            work.setIsDelete(0);
            work.setCreatedBy(CommonUtil.getCurrentUserId());
            work.setUpdatedBy(CommonUtil.getCurrentUserId());
            work.setCreatedTime(new Date());
            work.setUpdatedTime(new Date());
            res = workMapper.insert(work) > 0;
            if(res){
                workList.add(work);
                unfilledMapper.deleteUnfilled(work.getWorkDate(),uid);
            }
        }
        List<WorkVo> workVosList = workList.stream().map(work -> {
            WorkVo workVo = new WorkVo();
            BeanUtils.copyProperties(work,workVo);
            return workVo;
        }).collect(Collectors.toList());
        return workVosList;
    }

    /**
     * 修改N个工作记录表
     **/
    @Override
    public List<WorkVo> updateWorkList(JSONArray param) throws Exception{
        int res = 0;
        List<Work> workList = new ArrayList<Work>();
        for(int i=0;i<param.size();i++){
            //3、把里面的对象转化为JSONObject
            JSONObject job = param.getJSONObject(i);
            Work work = (Work) ObjectUtil.map2ObjAndCast(job,Work.class);
            work.setUpdatedTime(new Date());
            res =  workMapper.updateById(work);
            if(res > 0){
                workList.add(work);
            }
        }
        List<WorkVo> workVosList = workList.stream().map(work -> {
            WorkVo workVo = new WorkVo();
            BeanUtils.copyProperties(work,workVo);
            return workVo;
        }).collect(Collectors.toList());
        return workVosList;
    }

    /**
     * 查询最近七天工作记录表汇总
     **/
    @Override
    public List<SummaryVo> sevenDay() throws Exception{
        List<SummaryVo> workVoList = new ArrayList<SummaryVo>();
        String srartWorkDate = WorkServiceImpl.getPastDate(7);//距离现在的7天前的日期
        int uid = 1;
        workVoList = workMapper.sevenDay(srartWorkDate);
        System.out.println(workVoList);
        return workVoList;
    }

    /**
     * 查询最近七天工作记录表汇总的某一天(或几天)情况
     **/
    @Override
    public List<WorkVo> getWorksByIds(@RequestBody String ids) throws Exception{
        List<WorkVo> workVoList = new ArrayList<WorkVo>();
        workVoList = workMapper.getWorksByIds(ids);
        return workVoList;
    }

    /**
     * 获取过去第几天的日期
     *
     * @param past
     * @return
     */
    private static String getPastDate(int past) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - past);
        Date today = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String result = format.format(today);
        return result;
    }

    @Override
    public Map<String,Object> getWorkList(JSONObject param) {
        QueryWrapper<Work> wrapper = queryWork(param);
        int count = workMapper.selectCount(wrapper);
        return CommonUtil.wrapPageList(putMemberName(workMapper.selectList(wrapper)),count);
    }

    @Override
    public Map<String,Object> getWorkListByPage(JSONObject param) {
        CommonUtil.defaultParam(param);
        QueryWrapper<Work> wrapper = queryWork(param);
        Page<Work> mPage = new Page<>(param.getLong("pageNum"),param.getLong("pageSize"));
        IPage<Work> page = page(mPage,wrapper);
        int count = workMapper.selectCount(wrapper);
        return CommonUtil.wrapPageList(putMemberName(page.getRecords()),count);
    }

    @Override
    public Map<String, Object> getWorkListByDateUsernameProjectname(JSONObject param) {

        CommonUtil.defaultParam(param);

        Page<Work> mPage = new Page<>(param.getLong("pageNum"),param.getLong("pageSize"));

        //通过userName模糊查询到所有符合条件的member
        QueryWrapper<Member> wrapper = new QueryWrapper<>();
//        System.out.println("11111111111111111111111111111111111");
//        System.out.println(param.getString("yonghuname"));
        wrapper.like("realName",param.getString("yonghuname"));
        List<Member> memberList = memberMapper.selectList(wrapper);
        List<Long> memberids = new ArrayList<>();
        for(Member menber: memberList){
            memberids.add(menber.getId());
        }
//        System.out.println("33333333333333333333333"+memberids);

        //通过projectName模糊查询到所有符合条件的project
        QueryWrapper<Project> wrapper1 = new QueryWrapper<>();
        wrapper1.like("name",param.getString("projectName"));
        List<Project> projectList = projectMapper.selectList(wrapper1);
        List<Long> projectids = new ArrayList<>();
        for(Project project:projectList){
            projectids.add(project.getId());
        }


        QueryWrapper<Work> wrapper2 = new QueryWrapper<>();
        wrapper2.in("uId",memberids);
//        wrapper2.in
        wrapper2.in("pId",projectids);
        wrapper2.between("workDate",param.getDate("begainTime"),param.getDate("endTime"));



        IPage<Work> page = page(mPage,wrapper2);
        int count = workMapper.selectCount(wrapper2);
        return CommonUtil.wrapPageList(putMemberName(page.getRecords()),count);
    }

    public List<WorkVo> putMemberName(List<Work> works){
        List<WorkVo> workVos = new ArrayList<>();
        for (Work work : works) {
            WorkVo workVo = new WorkVo();
            workVo.setId(work.getId());
            workVo.setPId(work.getPId());
            workVo.setUId(work.getUId());
            workVo.setWorkDate(work.getWorkDate());
            workVo.setHourTime(work.getHourTime());
            workVo.setDescription(work.getDescription());
            Member member = memberMapper.selectById(work.getUId());
            if (member != null){
                workVo.setName(member.getRealName());
            }
            Project project = projectMapper.selectById(work.getPId());
            if (project != null) {
               workVo.setProjectName(project.getName());
            }
            workVos.add(workVo);
        }
        return workVos;
    }


    private QueryWrapper<Work> queryWork(JSONObject param){
        List<Long> ids = new ArrayList<>();
        if (param.containsKey("ids") || StringUtils.isNotBlank(param.getString("ids"))){
            String idstr = param.getString("ids");
            ids.addAll(Stream.of(idstr.split(",")).map(Long::parseLong).collect(Collectors.toList()));
        }

        QueryWrapper<Work> wrapper = new QueryWrapper<>();
        if (param.containsKey("id")){
            ids.add(param.getLong("id"));
        }
        wrapper.in(!ids.isEmpty(),"id",ids);
        wrapper.eq(param.containsKey("uId"),"uId",param.getLong("uId"));
        wrapper.eq(param.containsKey("isBan"),"isBan",param.getInteger("isBan"));
        wrapper.eq(param.containsKey("isDelete"),"isDelete",param.getInteger("isDelete"));
        wrapper.eq(param.containsKey("workDate"),"workDate",param.getString("workDate"));
        return wrapper;

    }
}