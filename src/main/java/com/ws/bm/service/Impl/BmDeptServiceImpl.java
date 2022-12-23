package com.ws.bm.service.Impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ws.bm.common.constant.BaseConstant;
import com.ws.bm.common.constant.HttpStatus;
import com.ws.bm.common.utils.InitFieldUtil;
import com.ws.bm.common.utils.MessageUtil;
import com.ws.bm.domain.entity.BmUser;
import com.ws.bm.domain.model.TreeSelect;
import com.ws.bm.exception.BaseException;
import com.ws.bm.mapper.BmDeptMapper;
import com.ws.bm.domain.entity.BmDept;
import com.ws.bm.service.IBmDeptService;
import com.ws.bm.service.IBmUserService;
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
        //没有父部门不能创建部门
        BmDept parenDept = getById(bmDept.getParentId());
        if (!StrUtil.equals(bmDept.getParentId(), BaseConstant.FALSE) && (ObjectUtil.isEmpty(parenDept) || StrUtil.equals(parenDept.getDeleted(),BaseConstant.TRUE))){
            throw new BaseException(HttpStatus.BAD_REQUEST,MessageUtil.getMessage("bm.parentStatusError"));
        }
        //判断创建部门名称是否重复
        QueryWrapper<BmDept> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(BmDept::getDeptName,bmDept.getDeptName().trim());
        wrapper.lambda().eq(BmDept::getDeleted,BaseConstant.FALSE);
        if (CollUtil.isNotEmpty(list(wrapper))){
            throw new BaseException(HttpStatus.BAD_REQUEST,MessageUtil.getMessage("bm.dept.nameRepeat"));
        }
        //初始化基本属性
        if (!InitFieldUtil.initField(bmDept)){
            throw new BaseException(HttpStatus.ERROR,MessageUtil.getMessage("bm.initFieldError"));
        }
        bmDept.setDeptId(UUID.randomUUID().toString());
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

        if (checkFiled(newBmDept)){
            throw new BaseException(HttpStatus.BAD_REQUEST,MessageUtil.getMessage("bm.paramsError"));
        }

        //没有父部门不能创建部门
        BmDept parenDept = getById(newBmDept.getParentId());
        if (ObjectUtil.isEmpty(parenDept)){
            throw new BaseException(HttpStatus.BAD_REQUEST,MessageUtil.getMessage("bm.parentStatusError"));
        }
        //判断修改部门名称是否重复
        QueryWrapper<BmDept> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(BmDept::getDeptName,newBmDept.getDeptName().trim());
        wrapper.lambda().eq(BmDept::getDeleted,BaseConstant.FALSE);
        wrapper.lambda().ne(BmDept::getDeptId,newBmDept.getDeptId());
        List<BmDept> repeatDepts = list(wrapper);
        if (CollUtil.isNotEmpty(repeatDepts)){
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
        }

        return updateById(oldBmDept);
    }

    @Override
    public BmDept getBmDept(String bmDeptId) {
        if (StrUtil.isEmpty(bmDeptId)){
            throw new BaseException(HttpStatus.BAD_REQUEST,MessageUtil.getMessage("bm.paramsError"));
        }
        BmDept bmDept = getById(bmDeptId);

        if (ObjectUtil.isEmpty(bmDept) || StrUtil.equals(bmDept.getDeleted(),BaseConstant.TRUE)){
            throw new BaseException(HttpStatus.BAD_REQUEST,MessageUtil.getMessage("bm.dept.notexist"));
        }
        return bmDept;
    }

    @Override
    public List<BmDept> queryBmDeptExcludeChild(String bmDeptId) {
        QueryWrapper<BmDept> wrapper = new QueryWrapper();
        wrapper.lambda().eq(BmDept::getDeleted,BaseConstant.FALSE);
        //bmMenuId为空认为是点击的添加按钮
        if (StrUtil.isEmpty(bmDeptId)){
            return list(wrapper);
        }else {
            //查看该部门下的所有子部门
            List<String> excludeIds = getAllChildren(Arrays.asList(bmDeptId));
            if (CollUtil.isNotEmpty(excludeIds)){
                wrapper.lambda().notIn(BmDept::getDeptId,excludeIds);
            }
            return list(wrapper);
        }

    }

    @Override
    public TreeSelect getDeptTree() {
        //获取顶级节点
        QueryWrapper<BmDept> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(BmDept::getDeleted,BaseConstant.FALSE);
        wrapper.lambda().eq(BmDept::getParentId,BaseConstant.TOPNODE);
        List<BmDept> toplist =  list(wrapper);
        if (CollUtil.isEmpty(toplist) || toplist.size() > 1){
            throw new BaseException(HttpStatus.BAD_REQUEST,MessageUtil.getMessage("bm.dept.topNodeError"));
        }
        BmDept topDept = toplist.get(0);
        buildDeptTree(topDept);
        return new TreeSelect(topDept);
    }

    //递归，建立子树形结构
    public void buildDeptTree(BmDept pNode){
        QueryWrapper<BmDept> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(BmDept::getDeleted,BaseConstant.FALSE);
        wrapper.lambda().eq(BmDept::getParentId,pNode.getDeptId());
        List<BmDept> chilDepts = list(wrapper);
        if (CollUtil.isNotEmpty(chilDepts)){
            for(BmDept deptNode : chilDepts) {
                buildDeptTree(deptNode);
            }
        }
        pNode.setChildren(chilDepts);
    }

    //递归查询出该部门的所有子节点
    private List<String> getAllChildren(List<String> parentIds){
        QueryWrapper<BmDept> wrapper = new QueryWrapper<>();
        wrapper.lambda().select(BmDept::getDeptId);
        wrapper.lambda().eq(BmDept::getDeleted,BaseConstant.FALSE);
        wrapper.lambda().in(BmDept::getParentId,parentIds);
        List<String> ids = list(wrapper).stream().map(BmDept::getDeptId).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(ids)){
            ids.addAll(getAllChildren(ids));
            return ids;
        }else {
            return new ArrayList<>();
        }
    }

    private boolean updateFlag (BmDept newBmDept, BmDept oldBmDept){
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
