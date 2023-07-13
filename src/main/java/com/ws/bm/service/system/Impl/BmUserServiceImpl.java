package com.ws.bm.service.system.Impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ws.bm.common.constant.BaseConstant;
import com.ws.bm.common.constant.HttpStatus;
import com.ws.bm.common.utils.*;
import com.ws.bm.config.BmConfig;
import com.ws.bm.domain.entity.system.BmDept;
import com.ws.bm.domain.entity.system.BmRole;
import com.ws.bm.exception.BaseException;
import com.ws.bm.mapper.system.BmDeptMapper;
import com.ws.bm.mapper.system.BmRoleMapper;
import com.ws.bm.mapper.system.BmUserMapper;
import com.ws.bm.domain.entity.system.BmUser;
import com.ws.bm.mapper.system.BmUserRoleMapper;
import com.ws.bm.service.system.IBmUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BmUserServiceImpl extends ServiceImpl<BmUserMapper, BmUser> implements IBmUserService {

    @Autowired
    private BmUserRoleMapper bmUserRoleMapper;

    @Autowired
    private BmDeptMapper bmDeptMapper;

    @Autowired
    private BmRoleMapper bmRoleMapper;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    RedisUtil redisUtil;

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
            //查找出该部门的所有子部门
            List<String> deptAllChildren = getDeptAllChildren(Arrays.asList(bmUser.getDeptId()));
            deptAllChildren.add(bmUser.getDeptId());
            wrapper.lambda().in(BmUser::getDeptId,deptAllChildren);
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
        List<BmUser> result = list(wrapper);
        if (CollUtil.isNotEmpty(result)){
            result.stream().forEach(user ->{
                BmDept bmDept = bmDeptMapper.selectById(user.getDeptId());
                user.setDeptName(bmDept.getDeptName());
            });
        }
        return result;
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
        BmUser oldBmUser = getById(newBmUser.getUserId());
        if (!updateFlag(newBmUser,oldBmUser)){
            oldBmUser.setRealName(newBmUser.getRealName());
            oldBmUser.setDeptId(newBmUser.getDeptId());
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
    public boolean resetBmUserPassword(String bmUserId) {
        if (StrUtil.isEmpty(bmUserId)){
            throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.paramsError"));
        }
        BmUser bmUser = getById(bmUserId);
        if(ObjectUtil.isEmpty(bmUser)){
            throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.paramsError"));
        }

        bmUser.setPassword(PasswordUtil.pwdEncrypt(BaseConstant.BASEPASSWORD));
        bmUser.setUpdateDate(new Date());

        return updateById(bmUser);
    }

    @Override
    public boolean changeBmUserStatus(String bmUserId, String status) {
        if (StrUtil.hasEmpty(bmUserId,status)){
            throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.paramsError"));
        }
        if (!StrUtil.equals(status,BaseConstant.TRUE) && !StrUtil.equals(status,BaseConstant.FALSE)){
            throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.user.statusException"));
        }
        BmUser bmUser = getById(bmUserId);
        if (ObjectUtil.isEmpty(bmUser) || StrUtil.equals(bmUser.getDeleted(),BaseConstant.TRUE)){
            throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.paramsError"));
        }
        bmUser.setStatus(status);
        return updateById(bmUser);
    }

    @Override
    public List<BmUser> queryAllocatedUserList(BmUser bmUser) {
        if(ObjectUtil.isEmpty(bmUser) || StrUtil.isEmpty(bmUser.getRoleId())){
            throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.paramsError"));
        }
        List<String> userIds = bmUserRoleMapper.queryUserIdsByRoleId(bmUser.getRoleId());
        if (CollUtil.isEmpty(userIds)){
            return new ArrayList<>();
        }
        QueryWrapper<BmUser> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(BmUser::getUserId,userIds);
        if (StrUtil.isNotEmpty(bmUser.getUserName())){
            wrapper.lambda().like(BmUser::getUserName,bmUser.getUserName());
        }
        if (StrUtil.isNotEmpty(bmUser.getRealName())){
            wrapper.lambda().like(BmUser::getRealName,bmUser.getRealName());
        }
        wrapper.lambda().eq(BmUser::getDeleted,BaseConstant.FALSE);
        List<BmUser> results = list(wrapper);
        if(CollUtil.isNotEmpty(results)){
            results.stream().forEach(user ->{
                BmDept bmDept = bmDeptMapper.selectById(user.getDeptId());
                user.setDeptName(bmDept.getDeptName());
            });
        }
        return results;
    }

    @Override
    public List<BmUser> queryUnAllocatedUserList(BmUser bmUser) {
        if(ObjectUtil.isEmpty(bmUser) || StrUtil.isEmpty(bmUser.getRoleId())){
            throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.paramsError"));
        }
        List<String> userIds = bmUserRoleMapper.queryUserIdsByRoleId(bmUser.getRoleId());
        QueryWrapper<BmUser> wrapper = new QueryWrapper<>();
        if (CollUtil.isNotEmpty(userIds)){
            wrapper.lambda().notIn(BmUser::getUserId,userIds);
        }
        if (StrUtil.isNotEmpty(bmUser.getUserName())){
            wrapper.lambda().like(BmUser::getUserName,bmUser.getUserName());
        }
        if (StrUtil.isNotEmpty(bmUser.getRealName())){
            wrapper.lambda().like(BmUser::getRealName,bmUser.getRealName());
        }
        wrapper.lambda().eq(BmUser::getDeleted,BaseConstant.FALSE);
        List<BmUser> results = list(wrapper);
        if(CollUtil.isNotEmpty(results)){
            results.stream().forEach(user ->{
                BmDept bmDept = bmDeptMapper.selectById(user.getDeptId());
                user.setDeptName(bmDept.getDeptName());
            });
        }
        return results;
    }

    @Override
    public BmUser getUserProfile(BmUser bmUser) {
        BmDept bmDept = bmDeptMapper.selectById(bmUser.getDeptId());
        if (ObjectUtil.isNotEmpty(bmDept)){
            bmUser.setDeptName(bmDept.getDeptName());
        }
        List<String> roleIds = bmUserRoleMapper.queryRoleIdsByUserId(bmUser.getUserId());
        if (CollUtil.isNotEmpty(roleIds)){
            StringBuffer sb = new StringBuffer("");
            for (int i =0; i < roleIds.size(); i++){
                BmRole bmRole = bmRoleMapper.selectById(roleIds.get(i));
                sb.append(bmRole.getRoleName());
                if ((i + 1) != roleIds.size()){
                    sb.append("、");
                }
            }
            bmUser.setRoleName(sb.toString());
        }
        return bmUser;
    }

    @Override
    public boolean updateUserProfile(BmUser bmUser) {
        if(ObjectUtil.isEmpty(bmUser) || StrUtil.isEmpty(bmUser.getRealName())){
            throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.paramsError"));
        }
        BmUser old = getById(bmUser.getUserId());
        old.setRoleName(bmUser.getRealName());
        old.setUpdateDate(new Date());
        return updateById(old);
    }

    @Override
    public boolean updateUserPwd(BmUser bmUser) {
        if (ObjectUtil.isEmpty(bmUser)){
            throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.paramsError"));
        }
        // 原密码
        String oldPwd = String.valueOf(bmUser.getParams().get("oldPwd"));
        // 新密码
        String newPwd = String.valueOf(bmUser.getParams().get("newPwd"));
        // 确认密码
        String confirmPwd = String.valueOf(bmUser.getParams().get("confirmPwd"));
        if (StrUtil.hasEmpty(oldPwd,newPwd,confirmPwd)){
            throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.paramsError"));
        }
        // 新密码 与 确认密码 比较
        if (!StrUtil.equals(newPwd,confirmPwd)){
            throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.pwdNotEqualsError"));
        }
        BmUser oldUser = getById(bmUser.getUserId());
        if (!StrUtil.equals(PasswordUtil.pwdEncrypt(oldPwd),oldUser.getPassword())){
            throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.pwdOldNewNotEqualsError"));
        }
        oldUser.setPassword(PasswordUtil.pwdEncrypt(newPwd));
        oldUser.setUpdateDate(new Date());

        redisUtil.deleteObject("bmUserToken:"+oldUser.getUserId());

        return updateById(oldUser);
    }

    @Override
    public String uploadAvatar(MultipartFile file,BmUser bmUser) {
        if (file.isEmpty() || ObjectUtil.isEmpty(bmUser)){
            throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.paramsError"));
        }
        String avatarPath = FileUploadUtil.upload(BmConfig.getAvatarPath(),file, Arrays.asList(BaseConstant.IMAGE_EXTENSION));
        if (StrUtil.isNotEmpty(avatarPath)){
            bmUser.setAvatar(avatarPath);
            updateById(bmUser);
            return avatarPath;
        }else {
            throw new BaseException(HttpStatus.ERROR, MessageUtil.getMessage("bm.false"));
        }
    }

    //递归查询出该部门的所有子节点
    public List<String> getDeptAllChildren(List<String> parentIds){
        QueryWrapper<BmDept> wrapper = new QueryWrapper<>();
        wrapper.lambda().select(BmDept::getDeptId);
        wrapper.lambda().eq(BmDept::getDeleted,BaseConstant.FALSE);
        wrapper.lambda().in(BmDept::getParentId,parentIds);
        List<String> ids = bmDeptMapper.selectList(wrapper).stream().map(BmDept::getDeptId).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(ids)){
            ids.addAll(getDeptAllChildren(ids));
            return ids;
        }else {
            return new ArrayList<>();
        }
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
