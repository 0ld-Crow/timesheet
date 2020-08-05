package com.springboot.cloud.app.timesheet.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.springboot.cloud.app.timesheet.dao.MemberMapper;
import com.springboot.cloud.app.timesheet.entity.form.MemberForm;
import com.springboot.cloud.app.timesheet.entity.po.Member;
import com.springboot.cloud.app.timesheet.entity.vo.MemberVo;
import com.springboot.cloud.app.timesheet.entity.vo.PersonWorkTimeDetailVo;
import com.springboot.cloud.app.timesheet.service.IDepartmentService;
import com.springboot.cloud.app.timesheet.service.IIndexService;
import com.springboot.cloud.app.timesheet.service.IMemberService;
import com.springboot.cloud.app.timesheet.service.IUserService;
import com.springboot.cloud.common.core.entity.po.Role;
import com.springboot.cloud.common.core.util.CommonUtil;
import com.springboot.cloud.common.core.util.ObjectUtil;
import com.springboot.cloud.common.core.util.PoiExcelUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Slf4j
@Service("memberService")
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member>  implements IMemberService {
    private static String DEFAULT_PASSWORD = "123456";

    @Autowired
    MemberMapper memberMapper;
    @Autowired
    IIndexService indexService;
    @Autowired
    public IDepartmentService departmentService;
    @Autowired
    public IUserService userService;

    /**
     * 导出员工工时明细
     **/
    @Override
    public void exportPersonProjectDetail(JSONObject json,HttpServletResponse response) throws Exception{
        String sheetName = "人员工时统计";
        String[] keys = {"workDate","realName","projectName","hourTime","description"};
        String[] columns = {"工作日期","人员名字","项目名称","工作工时","描述"};
        List<PersonWorkTimeDetailVo> personWorkTimeDetailVos = memberMapper.personProjectDetail(json);
        List<JSONObject> jsonObjects = ObjectUtil.obj2JsonList(personWorkTimeDetailVos);
        ByteArrayInputStream bais = PoiExcelUtil.exportExcel(jsonObjects,sheetName,keys,columns);
        PoiExcelUtil.writeToOutput(bais,response,sheetName);
    }

    /**
     * 新增一个员工
     **/
    @Override
    public boolean savePerson(MemberForm memberForm) {
        String createBy = CommonUtil.getCurrentUserId();
//        QueryWrapper<Member> wrapper = new QueryWrapper<>();
//        wrapper.eq("realName",memberForm.getRealName());
//        wrapper.eq("isDelete",0);
//        if (!Objects.isNull(memberMapper.selectOne(wrapper))){
//            throw new RuntimeException("已存在同名的人员！");
//        }

        //如何用户名为空的话，就用真实姓名作为用户名
        if (StringUtils.isBlank(memberForm.getUsername())){
            memberForm.setUsername(memberForm.getRealName());
        }

        memberForm.setSalt("salt");
        Member member = memberForm.toPo(Member.class);
        member.setCreatedBy(createBy);
        member.setUpdatedBy(createBy);
        Date date = new Date();
        member.setCreatedTime(date);
        member.setUpdatedTime(date);
        return save(member);
    }

    /**
     * 查询员工列表
     **/
    @Override
    public Map<String,Object> getMemberList(JSONObject param) {
        QueryWrapper<Member> wrapper = queryMember(param);
        int count = memberMapper.selectCount(wrapper);
        return CommonUtil.wrapPageList(memberToVo(memberMapper.selectList(wrapper)),count);
    }

    /**
     * 分页查询员工列表
     **/
    @Override
    public Map<String,Object> getMemberListByPage(JSONObject param) {

        CommonUtil.defaultParam(param);
        QueryWrapper<Member> wrapper = queryMember(param);
        Page<Member> mPage = new Page<>(param.getLong("pageNum"),param.getLong("pageSize"));
        IPage<Member> page = page(mPage,wrapper);
        int count = memberMapper.selectCount(wrapper);
        return CommonUtil.wrapPageList(memberToVo(page.getRecords()),count);
    }

    private QueryWrapper<Member> queryMember(JSONObject param){
        QueryWrapper<Member> wrapper = new QueryWrapper<>();
        List<Long> ids = new ArrayList<>();
        if (param.containsKey("id")){
            ids.add(param.getLong("id"));
        }
        if (param.containsKey("ids") && StringUtils.isNotBlank(param.getString("ids"))){
            String idstr = param.getString("ids");
            ids.addAll(Stream.of(idstr.split(",")).map(Long::parseLong).collect(Collectors.toList()));
        }
        wrapper.in(!ids.isEmpty(),"id",ids);
        wrapper.like(param.containsKey("realName"),"realName",param.getString("realName"));
        wrapper.like(param.containsKey("username"),"username",param.getString("username"));
        wrapper.like(param.containsKey("job"),"job",param.getString("job"));
        wrapper.eq(param.containsKey("roleId"),"roleId",3);
        wrapper.eq(param.containsKey("isBan"),"isBan",param.getInteger("isBan"));
        wrapper.eq(param.containsKey("isDelete"),"isDelete",param.getInteger("isDelete"));
        return wrapper;

    }

    /**
     * 批量删除员工
     **/
    @Override
    public int batchDelete(JSONObject param) {
        String id = param.getString("ids");
        return memberMapper.batchDelete(id);
    }


    /**
     * 获取员工列表
     **/
    @Override
    public Map<String,Object> getUserList(JSONObject param) {
        QueryWrapper<Member> wrapper = queryProject(param);
        List<Member> members = memberMapper.selectList(wrapper);
        int count = memberMapper.selectCount(wrapper);
        return CommonUtil.wrapPageList(memberToVo(members),count);
    }



    private QueryWrapper<Member> queryProject(JSONObject param){
        List<Long> ids = new ArrayList<>();
        if (param.containsKey("ids") || StringUtils.isNotBlank(param.getString("ids"))){
            String idstr = param.getString("ids");
            ids.addAll(Stream.of(idstr.split(",")).map(Long::parseLong).collect(Collectors.toList()));
        }

        QueryWrapper<Member> wrapper = new QueryWrapper<>();
        if (param.containsKey("id")){
            ids.add(param.getLong("id"));
        }
        wrapper.eq(param.containsKey("roleId"),"roleId",param.getLong("roleId"));
        wrapper.in(!ids.isEmpty(),"id",ids);
        wrapper.eq("isDelete",0);
        return wrapper;

    }

    /**
     * 更新一个员工的信息
     **/
    @Override
    public boolean update(JSONObject param) {
        try {
            Member member = (Member) ObjectUtil.map2Obj(param,Member.class);
            memberMapper.updateById(member);
        }catch (Exception e){
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 导出员工的账号信息
     **/
    @Override
    public void exportMember(JSONObject param,HttpServletResponse response)throws Exception {
        String sheetName = "账号信息";
        String[] keys = {"name","phone","roleName","roleId","status"};
        String[] columns = {"姓名","电话","角色名称","角色id","状态"};
        List<MemberVo> memberVos = (List<MemberVo>)getUserList(param).get("data");
        List<JSONObject> jsonObjects = ObjectUtil.obj2JsonList(memberVos);
        ByteArrayInputStream bais = PoiExcelUtil.exportExcel(jsonObjects,sheetName,keys,columns);
        PoiExcelUtil.writeToOutput(bais,response,sheetName);
    }

    private List<MemberVo> memberToVo(List<Member> members){
        List<Role> roles = indexService.getRoleList();
        List<MemberVo> memberVos = members.stream().map(m -> {
            MemberVo memberVo = new MemberVo();
            memberVo.setName(m.getRealName());
            memberVo.setIsDelete(m.getIsDelete());
            memberVo.setRoleId(m.getRoleId());
            memberVo.setPhone(m.getPhone());
            memberVo.setUsername(m.getUsername());
            memberVo.setUid(m.getId());
            Optional<Role> role = roles.stream().filter(r -> Objects.equals(r.getId() ,m.getRoleId())).findFirst();
            memberVo.setRoleName(role.isPresent() ? role.get().getName() : null);
            memberVo.setStatus(m.getIsBan() == 1 ? "禁用" : "启用");
            memberVo.setJob(m.getJob());
            return memberVo;
        }).collect(Collectors.toList());
        return memberVos;
    }

    //同步企业微信员工列表
    @Override
    public boolean getUserList(){
        try {
            JSONArray jsonArray = departmentService.getDepartmentList();
            for (int i = 0; i < jsonArray.size(); i++){
                String department_id = jsonArray.getJSONObject(i).getString("id");
                JSONArray UserJsonArray = userService.getUserList(department_id);
                for (int j =0; j < UserJsonArray.size(); j++){
                    JSONObject user   = (JSONObject)UserJsonArray.get(j);
                    Member memberOld = memberMapper.getUserByUserId(user.getString("userid"));
                    if(memberOld == null){
                        Member member = new Member();
                        member.setRoleId(1l);
                        member.setRealName(user.getString("name"));
                        member.setJob("员工");
                        member.setUsername(user.getString("name"));
                        member.setPhone(user.getString("mobile"));
                        String password = DEFAULT_PASSWORD;
                        Map<String,Object> base64 = CommonUtil.base64(password);
                        member.setPassword((String)base64.get("hashedStrBase64"));
                        member.setSalt((String)base64.get("salt"));
                        member.setUserId(user.getString("userid"));
                        String createBy = CommonUtil.getCurrentUserId();
                        Date date = new Date();
                        member.setCreatedBy(createBy);
                        member.setUpdatedBy(createBy);
                        member.setCreatedTime(date);
                        member.setUpdatedTime(date);
                        memberMapper.insert(member);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return true;
    }
}