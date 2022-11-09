package com.bm.service.Impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.Query;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bm.common.constant.BaseConstant;
import com.bm.common.constant.HttpStatus;
import com.bm.common.utils.InitFieldUtil;
import com.bm.common.utils.MessageUtil;
import com.bm.domain.entity.BmUser;
import com.bm.exception.BaseException;
import com.bm.mapper.BmDeptMapper;
import com.bm.domain.entity.BmDept;
import com.bm.service.IBmDeptService;
import com.bm.service.IBmUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BmDeptServiceImpl extends ServiceImpl<BmDeptMapper, BmDept> implements IBmDeptService {

    @Autowired
    private IBmUserService iBmUserService;

    @Override
    public boolean addBmDept(BmDept bmDept) {

        if (checkFiled(bmDept)){
            throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.paramsError"));
        }
        //父部门停用时，该节点下不能创建部门
        BmDept parenDept = getById(bmDept.getParentId());
        if (ObjectUtil.isEmpty(parenDept) || StrUtil.equals(BaseConstant.EXCEPTION,parenDept.getStatus())){
            throw new BaseException(HttpStatus.BAD_REQUEST,MessageUtil.getMessage("bm.dept.parentStatusError"));
        }
        //判断创建部门名称是否重复
        QueryWrapper<BmDept> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(BmDept::getDeptName,bmDept.getDeptName().trim());
        wrapper.lambda().eq(BmDept::getDeleted,BaseConstant.FALSE);
        List<BmDept> oldDepts = list(wrapper);
        if (CollUtil.isNotEmpty(oldDepts)){
            throw new BaseException(HttpStatus.BAD_REQUEST,MessageUtil.getMessage("bm.dept.nameRepeat"));
        }
        //初始化基本属性
        if (!InitFieldUtil.initField(bmDept)){
            throw new BaseException(HttpStatus.ERROR,MessageUtil.getMessage("bm.initFieldError"));
        }
        bmDept.setDeptId(UUID.randomUUID().toString());
        bmDept.setStatus(BaseConstant.TRUE);
        return save(bmDept);
    }

    @Override
    public List<BmDept> queryBmDept(BmDept bmDept) {

        QueryWrapper<BmDept> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(BmDept::getDeleted,BaseConstant.FALSE);//删除标志
        //查询条件-部门名称
        if (StrUtil.isNotEmpty(bmDept.getDeptName())){
            wrapper.lambda().like(BmDept::getDeptName,bmDept.getDeptName().trim());
        }
        //查询条件-状态
        if(StrUtil.isNotEmpty(bmDept.getStatus())){
            wrapper.lambda().eq(BmDept::getStatus,bmDept.getStatus());
        }
        //排序
        wrapper.lambda().orderByAsc(BmDept::getOrderNum).orderByDesc(BmDept::getUpdateDate);

        return list(wrapper);
    }

    @Override
    public boolean deleteBmDept(String bmDeptId) {

        if (StrUtil.isEmpty(bmDeptId)){
            throw new BaseException(HttpStatus.BAD_REQUEST,MessageUtil.getMessage("bm.paramsError"));
        }

        //如果有子部门，那么不能删除
        QueryWrapper<BmDept> deptWrapper = new QueryWrapper<>();
        deptWrapper.lambda().eq(BmDept::getParentId,bmDeptId);
        deptWrapper.lambda().eq(BmDept::getDeleted,BaseConstant.FALSE);
        List<BmDept> childList = list(deptWrapper);
        if (CollUtil.isNotEmpty(childList)){
            throw new BaseException(HttpStatus.BAD_REQUEST,MessageUtil.getMessage("bm.dept.hasChildren"));
        }

        //如果部门下面有用户，那么不能删除
        QueryWrapper<BmUser> userWrapper = new QueryWrapper<>();
        userWrapper.lambda().eq(BmUser::getDeptId,bmDeptId);
        List<BmUser> userList = iBmUserService.list(userWrapper);
        if(CollUtil.isNotEmpty(userList)){
            throw new BaseException(HttpStatus.BAD_REQUEST,MessageUtil.getMessage("bm.dept.hasUser"));
        }

        //对该部门进行删除
        BmDept dept = getById(bmDeptId);
        if (ObjectUtil.isEmpty(dept)){
            throw new BaseException(HttpStatus.BAD_REQUEST,MessageUtil.getMessage("bm.dept.notexist"));
        }
        dept.setDeleted(BaseConstant.TRUE);
        dept.setUpdateDate(new Date());
        return updateById(dept);
    }

    @Override
    public boolean updateBmDept(BmDept newBmDept) {

        if (ObjectUtil.isEmpty(newBmDept)){
            throw new BaseException(HttpStatus.BAD_REQUEST,MessageUtil.getMessage("bm.paramsError"));
        }

        //父部门停用时，该节点下不能创建部门
        BmDept parenDept = getById(newBmDept.getParentId());
        if (ObjectUtil.isEmpty(parenDept) || StrUtil.equals(BaseConstant.EXCEPTION,parenDept.getStatus())){
            throw new BaseException(HttpStatus.BAD_REQUEST,MessageUtil.getMessage("bm.dept.parentStatusError"));
        }
        //判断修改部门名称是否重复
        QueryWrapper<BmDept> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(BmDept::getDeptName,newBmDept.getDeptName().trim());
        wrapper.lambda().eq(BmDept::getDeleted,BaseConstant.FALSE);
        wrapper.lambda().ne(BmDept::getDeptId,newBmDept.getDeptId());
        List<BmDept> oldDepts = list(wrapper);
        if (CollUtil.isNotEmpty(oldDepts)){
            throw new BaseException(HttpStatus.BAD_REQUEST,MessageUtil.getMessage("bm.dept.nameRepeat"));
        }

        //旧对象
        BmDept oldBmDept = getById(newBmDept.getDeptId());
        if (!updateFlag(newBmDept,oldBmDept)){
            oldBmDept.setDeptName(newBmDept.getDeptName());
            oldBmDept.setParentId(newBmDept.getParentId());
            oldBmDept.setOrderNum(newBmDept.getOrderNum());
            oldBmDept.setLeader(newBmDept.getLeader());
            oldBmDept.setPhone(newBmDept.getPhone());
            oldBmDept.setStatus(newBmDept.getStatus());
        }

        return updateById(oldBmDept);
    }

    @Override
    public BmDept getBmDept(String bmDeptId) {
        if (StrUtil.isEmpty(bmDeptId)){
            throw new BaseException(HttpStatus.BAD_REQUEST,MessageUtil.getMessage("bm.paramsError"));
        }
        BmDept bmDept = getById(bmDeptId);

        if (ObjectUtil.isEmpty(bmDept)){
            throw new BaseException(HttpStatus.BAD_REQUEST,MessageUtil.getMessage("bm.dept.notexist"));
        }
        return bmDept;
    }

    @Override
    public List<BmDept> queryBmDeptExcludeChild(String bmDeptId) {
        if (StrUtil.isEmpty(bmDeptId)){
            throw new BaseException(HttpStatus.BAD_REQUEST,MessageUtil.getMessage("bm.paramsError"));
        }

        //查看该部门下的所有子部门
        List<String> excludeIds = getChildren(Arrays.asList(bmDeptId));

        QueryWrapper<BmDept> wrapper = new QueryWrapper();
        wrapper.lambda().eq(BmDept::getDeleted,BaseConstant.FALSE);
        wrapper.lambda().eq(BmDept::getStatus,BaseConstant.TRUE);
        if (CollUtil.isNotEmpty(excludeIds)){
            wrapper.lambda().notIn(BmDept::getDeptId,excludeIds);
        }
        return list(wrapper);
    }

    //递归查询出该部门的所有子节点
    private List<String> getChildren(List<String> parentIds){
        QueryWrapper<BmDept> wrapper = new QueryWrapper<>();
        wrapper.lambda().select(BmDept::getDeptId);
        wrapper.lambda().eq(BmDept::getDeleted,BaseConstant.FALSE);
        wrapper.lambda().eq(BmDept::getStatus,BaseConstant.TRUE);
        wrapper.lambda().in(BmDept::getParentId,parentIds);
        List<String> ids = list(wrapper).stream().map(BmDept::getDeptId).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(ids)){
            ids.addAll(getChildren(ids));
            return ids;
        }else {
            return new ArrayList<>();
        }
    }

    private boolean updateFlag(BmDept newBmDept, BmDept oldBmDept){
        StringBuffer sb1 = new StringBuffer("");
        StringBuffer sb2 = new StringBuffer("");
        sb1.append(newBmDept.getDeptName());
        sb2.append(oldBmDept.getDeptName());
        sb1.append(newBmDept.getParentId());
        sb2.append(oldBmDept.getParentId());
        sb1.append(newBmDept.getOrderNum());
        sb2.append(oldBmDept.getOrderNum());
        sb1.append(newBmDept.getLeader());
        sb2.append(oldBmDept.getLeader());
        sb1.append(newBmDept.getPhone());
        sb2.append(oldBmDept.getPhone());
        sb1.append(newBmDept.getStatus());
        sb2.append(oldBmDept.getStatus());
        return sb1.toString().equals(sb2.toString());

    }

    private boolean checkFiled(BmDept bmDept){
        if(ObjectUtil.isEmpty(bmDept) ||
                StrUtil.hasEmpty(bmDept.getDeptName(),bmDept.getParentId()) ||
                ObjectUtil.isEmpty(bmDept.getOrderNum())
        ){
            return true;
        }
        return false;
    }
}
