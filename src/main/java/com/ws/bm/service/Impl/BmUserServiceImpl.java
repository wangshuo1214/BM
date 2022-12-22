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
import com.ws.bm.common.utils.PasswordUtil;
import com.ws.bm.exception.BaseException;
import com.ws.bm.mapper.BmUserMapper;
import com.ws.bm.domain.entity.BmUser;
import com.ws.bm.mapper.BmUserRoleMapper;
import com.ws.bm.service.IBmUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class BmUserServiceImpl extends ServiceImpl<BmUserMapper, BmUser> implements IBmUserService {

    @Autowired
    private BmUserRoleMapper bmUserRoleMapper;

    @Override
    public boolean addBmUser(BmUser bmUser) {
        if (checkFiled(bmUser)){
            throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.paramsError"));
        }
        QueryWrapper<BmUser> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(BmUser::getUserName,bmUser.getUserName());
        wrapper.lambda().eq(BmUser::getDeleted, BaseConstant.FALSE);
        if (CollUtil.isNotEmpty(list(wrapper))){
            throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.user.nameRepeat"));
        }
        if (!InitFieldUtil.initField(bmUser)){
            throw new BaseException(HttpStatus.ERROR,MessageUtil.getMessage("bm.initFieldError"));
        }
        bmUser.setUserId(UUID.randomUUID().toString());
        bmUser.setPassword(PasswordUtil.pwdEncrypt(bmUser.getPassword()));
        return save(bmUser);
    }

    @Override
    public List<BmUser> queryBmUser(BmUser bmUser) {
        QueryWrapper<BmUser> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(BmUser::getDeleted,BaseConstant.FALSE);
        if (StrUtil.isNotEmpty(bmUser.getDeptId())){
            wrapper.lambda().eq(BmUser::getDeptId,bmUser.getDeptId());
        }
        if (StrUtil.isNotEmpty(bmUser.getUserName())){
            wrapper.lambda().like(BmUser::getUserName,bmUser.getUserName());
        }
        if (StrUtil.isNotEmpty(bmUser.getRealName())){
            wrapper.lambda().like(BmUser::getRealName,bmUser.getRealName());
        }
        if (StrUtil.isNotEmpty(bmUser.getStatus())){
            wrapper.lambda().eq(BmUser::getStatus,bmUser.getStatus());
        }
        return list(wrapper);
    }

    @Override
    public boolean deleteBmUser(List<String> bmUserIds) {
        if (CollUtil.isEmpty(bmUserIds)){
            throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.paramsError"));
        }
        //用户关联角色，不可删除
        if (CollUtil.isNotEmpty(bmUserRoleMapper.queryUserRolesByUserIds(bmUserIds))){
            throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.user.userRelateRole"));
        }
        List<BmUser> delList = listByIds(bmUserIds);
        if (CollUtil.isEmpty(delList)){
            throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.paramsError"));
        }
        delList.forEach(bmUser -> {
            bmUser.setDeleted(BaseConstant.TRUE);
            bmUser.setUpdateDate(new Date());
        });
        return updateBatchById(delList);
    }

    @Override
    public boolean updateBmUser(BmUser newBmUser) {
        if (checkFiled(newBmUser)){
            throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.paramsError"));
        }
        QueryWrapper<BmUser> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(BmUser::getUserName,newBmUser.getUserName());
        wrapper.lambda().eq(BmUser::getDeleted,BaseConstant.FALSE);
        if (CollUtil.isNotEmpty(list(wrapper))){
            throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.user.nameRepeat"));
        }
        BmUser oldBmUser = getById(newBmUser.getUserId());
        if (updateFlag(newBmUser,oldBmUser)){
            oldBmUser.setRealName(newBmUser.getRealName());
            oldBmUser.setDeleted(newBmUser.getDeptId());
            oldBmUser.setStatus(newBmUser.getStatus());
            oldBmUser.setRemark(newBmUser.getRemark());
            oldBmUser.setUpdateDate(new Date());
        }
        return updateById(oldBmUser);
    }

    @Override
    public BmUser getBmUser(String bmUserId) {
        if (StrUtil.isEmpty(bmUserId)){
            throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.paramsError"));
        }
        BmUser bmUser = getById(bmUserId);
        if (ObjectUtil.isEmpty(bmUser) || StrUtil.equals(bmUser.getDeleted(),BaseConstant.TRUE)){
            throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.paramsError"));
        }
        return bmUser;
    }

    @Override
    public boolean resetBmUserPassword(List<String> bmUserIds) {
        if (CollUtil.isEmpty(bmUserIds)){
            throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.paramsError"));
        }
        List<BmUser> bmUsers = listByIds(bmUserIds);
        if(CollUtil.isEmpty(bmUsers)){
            throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.paramsError"));
        }

        bmUsers.forEach(bmUser -> {
            bmUser.setPassword(PasswordUtil.pwdEncrypt(BaseConstant.BASEPASSWORD));
            bmUser.setUpdateDate(new Date());
        });
        return updateBatchById(bmUsers);
    }

    private boolean updateFlag(BmUser newObj, BmUser oldObj){
        StringBuffer sb1 = new StringBuffer("");
        StringBuffer sb2 = new StringBuffer("");
        sb1.append(newObj.getRealName());
        sb2.append(oldObj.getRealName());
        sb1.append(newObj.getDeptId());
        sb2.append(oldObj.getDeptId());
        sb1.append(newObj.getStatus());
        sb2.append(oldObj.getStatus());
        sb1.append(newObj.getRemark());
        sb2.append(oldObj.getRemark());
        return sb1.toString().equals(sb2.toString());
    }

    private boolean checkFiled(BmUser bmUser){
        if (StrUtil.hasEmpty(bmUser.getDeptId(),bmUser.getUserName(),
                bmUser.getPassword(), bmUser.getRealName(),bmUser.getStatus()
        )){
            return true;
        }
        return false;
    }
}
