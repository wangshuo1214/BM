package com.ws.bm.service.system.Impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ws.bm.common.constant.HttpStatus;
import com.ws.bm.common.constant.BaseConstant;
import com.ws.bm.common.utils.JwtTokenUtil;
import com.ws.bm.common.utils.MessageUtil;
import com.ws.bm.common.utils.PasswordUtil;
import com.ws.bm.common.utils.RedisUtil;
import com.ws.bm.domain.entity.system.BmMenu;
import com.ws.bm.domain.entity.system.BmRoleMenu;
import com.ws.bm.domain.entity.system.BmUser;
import com.ws.bm.domain.model.LoginBody;
import com.ws.bm.exception.BaseException;
import com.ws.bm.exception.UserException;
import com.ws.bm.mapper.system.BmRoleMenuMapper;
import com.ws.bm.mapper.system.BmUserRoleMapper;
import com.ws.bm.service.system.IBmLoginService;
import com.ws.bm.service.system.IBmMenuService;
import com.ws.bm.service.system.IBmUserService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class BmLoginServiceImpl implements IBmLoginService {

    @Autowired
    private IBmUserService iBmUserService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private BmUserRoleMapper bmUserRoleMapper;

    @Autowired
    private BmRoleMenuMapper bmRoleMenuMapper;

    @Autowired
    private IBmMenuService iBmMenuService;

    @Override
    public String login(LoginBody loginBody) {

        if (loginBody == null || StrUtil.hasEmpty(loginBody.getUsername(),loginBody.getPassword())){
            throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.paramsError"));
        }
        QueryWrapper<BmUser> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(BmUser::getUserName,loginBody.getUsername());
        wrapper.lambda().eq(BmUser::getDeleted,BaseConstant.FALSE);
        BmUser user = iBmUserService.getOne(wrapper);
        //用户不存在
        if (user == null){
            throw new UserException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.user.notexist"));
        }
        //密码不正确
        if (!StrUtil.equals(PasswordUtil.pwdEncrypt(loginBody.getPassword()),user.getPassword())){
            throw new UserException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.user.passwordError"));
        }
        //用户状态异常（停用）
        if (StrUtil.equals(BaseConstant.EXCEPTION,user.getStatus())){
            throw new UserException(HttpStatus.BAD_REQUEST,MessageUtil.getMessage("bm.user.statusError"));
        }

        //生成Token
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId",user.getUserId());
        String token = jwtTokenUtil.createToken(claims);

        //Token放在缓存里,用户每次请求的时候使用
        redisUtil.setCacheObject("bmUserToken:"+user.getUserId(),token,5, TimeUnit.HOURS);

        return token;
    }

    @Override
    public BmUser getUserInfoByToken(String token) {
        
        if (StrUtil.isEmpty(token)){
            throw new UserException(HttpStatus.BAD_REQUEST,MessageUtil.getMessage("bm.paramsError"));
        }

        //token解析结果
        Claims claims = null;
        try {
            claims = jwtTokenUtil.getClaimsFromToken(token);
            //用户id
            String userId = (String) claims.get("userId");
            BmUser bmUser = iBmUserService.getById(userId);
            //用户关联角色ids
            List<String> roleIds = bmUserRoleMapper.queryRoleIdsByUserId(userId);
            bmUser.setRoles(roleIds);
            if (CollUtil.isNotEmpty(roleIds)){
                //角色关联的菜单集合
                List<String> menuIds = bmRoleMenuMapper.selectMenuIdsByRoleIds(roleIds);
                if (CollUtil.isNotEmpty(menuIds)){
                    bmUser.setPermissions(iBmMenuService.listByIds(menuIds).stream().map(BmMenu::getPerms).collect(Collectors.toList()));
                }
            }
            if (ObjectUtil.isNotEmpty(bmUser)){
                return bmUser;
            }
            return null;
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Boolean logout(HttpServletRequest httpServletRequest) {

        String token = httpServletRequest.getHeader("Authorization");

        //token解析结果
        Claims claims = null;
        try {
            claims = jwtTokenUtil.getClaimsFromToken(token);
            String userId = (String) claims.get("userId");
            //删除缓存的token
            redisUtil.deleteObject("bmUserToken:"+userId);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
