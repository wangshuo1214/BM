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
import com.ws.bm.domain.entity.BmEmployee;
import com.ws.bm.exception.BaseException;
import com.ws.bm.mapper.BmEmployeeMapper;
import com.ws.bm.service.IBmEmployeeService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class BmEmployeeServiceImpl extends ServiceImpl<BmEmployeeMapper, BmEmployee> implements IBmEmployeeService {
    @Override
    public boolean addBmEmployee(BmEmployee bmEmployee) {
        if (checkFiled(bmEmployee)){
            throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.paramsError"));
        }
        //员工名称重复校验
        QueryWrapper<BmEmployee> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(BmEmployee::getEmployeeName,bmEmployee.getEmployeeName());
        wrapper.lambda().eq(BmEmployee::getDeleted, BaseConstant.FALSE);
        if (CollUtil.isNotEmpty(list(wrapper))){
            throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.employee.nameRepeat"));
        }
        //初始化属性
        if (!InitFieldUtil.initField(bmEmployee)){
            throw new BaseException(HttpStatus.ERROR,MessageUtil.getMessage("bm.initFieldError"));
        }
        bmEmployee.setEmployeeId(UUID.randomUUID().toString());
        return save(bmEmployee);
    }

    @Override
    public List<BmEmployee> queryBmEmployee(BmEmployee bmEmployee) {
        if (ObjectUtil.isEmpty(bmEmployee)){
            throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.paramsError"));
        }
        QueryWrapper<BmEmployee> wrapper = new QueryWrapper<>();
        if (StrUtil.isNotEmpty(bmEmployee.getEmployeeName())){
            wrapper.lambda().like(BmEmployee::getEmployeeName,bmEmployee.getEmployeeName());
        }
        if (StrUtil.isNotEmpty(bmEmployee.getPhone())){
            wrapper.lambda().like(BmEmployee::getPhone,bmEmployee.getPhone());
        }
        wrapper.lambda().eq(BmEmployee::getDeleted,BaseConstant.FALSE);
        wrapper.lambda().orderByDesc(BmEmployee::getUpdateDate);
        return list(wrapper);
    }

    @Override
    public boolean deleteBmEmployee(List<String> bmEmployeeIds) {
        if (CollUtil.isEmpty(bmEmployeeIds)){
            throw new BaseException(HttpStatus.BAD_REQUEST,MessageUtil.getMessage("bm.paramsError"));
        }
        List<BmEmployee> bmEmployees = listByIds(bmEmployeeIds);
        if (CollUtil.isEmpty(bmEmployees)){
            throw new BaseException(HttpStatus.BAD_REQUEST,MessageUtil.getMessage("bm.paramsError"));
        }
        bmEmployees.forEach(bmEmployee -> {
            bmEmployee.setDeleted(BaseConstant.TRUE);
            bmEmployee.setUpdateDate(new Date());
        });
        return updateBatchById(bmEmployees);
    }

    @Override
    public boolean updateBmEmployee(BmEmployee bmEmployee) {
        if (ObjectUtil.isEmpty(bmEmployee)){
            throw new BaseException(HttpStatus.BAD_REQUEST,MessageUtil.getMessage("bm.paramsError"));
        }
        //判断员工名称是否重复
        QueryWrapper<BmEmployee> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(BmEmployee::getEmployeeName,bmEmployee.getEmployeeName().trim());
        wrapper.lambda().eq(BmEmployee::getDeleted,BaseConstant.FALSE);
        wrapper.lambda().ne(BmEmployee::getEmployeeId,bmEmployee.getEmployeeId());
        if (CollUtil.isNotEmpty(list(wrapper))){
            throw new BaseException(HttpStatus.BAD_REQUEST,MessageUtil.getMessage("bm.employee.nameRepeat"));
        }
        BmEmployee oldEmployee = getById(bmEmployee.getEmployeeId());
        if (!updateFlag(bmEmployee,oldEmployee)){
            oldEmployee.setEmployeeName(bmEmployee.getEmployeeName());
            oldEmployee.setPhone(bmEmployee.getPhone());
            oldEmployee.setRemark(bmEmployee.getRemark());
        }
        return updateById(oldEmployee);
    }

    @Override
    public BmEmployee getBmEmployee(String bmEmployeeId) {
        if (StrUtil.isEmpty(bmEmployeeId)){
            throw new BaseException(HttpStatus.BAD_REQUEST,MessageUtil.getMessage("bm.paramsError"));
        }
        BmEmployee bmEmployee = getById(bmEmployeeId);
        if (ObjectUtil.isEmpty(bmEmployee) || StrUtil.equals(bmEmployee.getDeleted(),BaseConstant.TRUE)){
            throw new BaseException(HttpStatus.BAD_REQUEST,MessageUtil.getMessage("bm.employee.notexist"));
        }
        return bmEmployee;
    }

    private boolean checkFiled(BmEmployee bmEmployee){
        if(ObjectUtil.isEmpty(bmEmployee) || StrUtil.isEmpty(bmEmployee.getEmployeeName())){
            return true;
        }
        return false;
    }

    private boolean updateFlag(BmEmployee newObj, BmEmployee oldObj){
        StringBuffer sb1 = new StringBuffer("");
        StringBuffer sb2 = new StringBuffer("");
        sb1.append(newObj.getEmployeeName());
        sb2.append(oldObj.getEmployeeName());
        sb1.append(newObj.getPhone());
        sb2.append(oldObj.getPhone());
        sb1.append(newObj.getRemark());
        sb2.append(oldObj.getRemark());
        return sb1.toString().equals(sb2.toString());
    }
}
