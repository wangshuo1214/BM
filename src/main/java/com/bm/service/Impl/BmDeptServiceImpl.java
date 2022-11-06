package com.bm.service.Impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
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

import java.util.Date;
import java.util.List;
import java.util.UUID;

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

        return list(wrapper);
    }

    @Override
    public boolean deleteDept(String bmDeptId) {

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
        dept.setDeleted(BaseConstant.TRUE);
        dept.setUpdateDate(new Date());
        return updateById(dept);
    }

    public boolean checkFiled(BmDept bmDept){
        if(ObjectUtil.isEmpty(bmDept) ||
                StrUtil.hasEmpty(bmDept.getDeptName(),bmDept.getParentId()) ||
                ObjectUtil.isEmpty(bmDept.getOrderNum())
        ){
            return true;
        }
        return false;
    }
}
