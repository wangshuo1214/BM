package com.ws.bm.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.ws.bm.common.constant.HttpStatus;
import com.ws.bm.common.page.PageDomain;
import com.ws.bm.common.page.PageQuery;
import com.ws.bm.common.utils.CreateGsonUtil;
import com.ws.bm.common.utils.JwtTokenUtil;
import com.ws.bm.common.utils.ServletUtils;
import com.ws.bm.domain.entity.system.BmUser;
import com.ws.bm.domain.model.Result;
import com.ws.bm.common.utils.MessageUtil;
import com.ws.bm.domain.model.TableDataInfo;
import com.ws.bm.exception.BaseException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.ws.bm.service.system.IBmUserService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class BaseController {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private IBmUserService iBmUserService;

    public Result success(){
        return Result.returnCodeMessage(HttpStatus.SUCCESS, MessageUtil.getMessage("bm.sucess"));
    }

    public Result success(Object data) {
        return Result.returnCodeMessage(HttpStatus.SUCCESS,MessageUtil.getMessage("bm.sucess"),data);
    }

    public Result error() {
        return Result.returnCodeMessage(HttpStatus.ERROR,MessageUtil.getMessage("bm.false"));
    }

    public Result error(String msg){
        return Result.returnCodeMessage(HttpStatus.ERROR,msg,null);
    }

    public Result computeResult(Object t){
        if ( t instanceof Boolean){
            return (boolean)t ? success() : error();
        }else if (t instanceof Integer){
            return (int) t > 0 ? success() : error();
        }
        return error(MessageUtil.getMessage("bm.computedResultError"));
    }

    public Result diyResut(Integer code, String msg){
        return Result.returnCodeMessage(code,msg);
    }

    public Result diyResut(Integer code, String msg, Object data){
        return Result.returnCodeMessage(code,msg,data);
    }

    protected void startPage(PageQuery pageQuery){
        if (ObjectUtil.isEmpty(pageQuery)){
            throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.paramsError"));
        }
        PageDomain page = pageQuery.getPage();
        if (ObjectUtil.hasEmpty(page.getPageNum(), page.getPageSize())){
            throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.paramsError"));
        }
        Integer pageNum = page.getPageNum();
        Integer pageSize = page.getPageSize();
        String orderBy = page.getOrderBy();
        PageHelper.startPage(pageNum, pageSize, orderBy);
    }

    protected TableDataInfo formatTableData(List<?> formatList){
        TableDataInfo rspData = new TableDataInfo();
        rspData.setRows(formatList);
        rspData.setTotal(new PageInfo(formatList).getTotal());
        return rspData;
    }

    protected <T> T getPageItem(PageQuery pageQuery,Class<T> clazz){
        Gson gson = CreateGsonUtil.createGson();
        return gson.fromJson(gson.toJson(pageQuery.getItem()), clazz);
    }

    public BmUser getBmUser(){
        String token = ServletUtils.getHeader("Authorization");
        //token解析结果
        String userId = "";
        try {
            Claims claims = jwtTokenUtil.getClaimsFromToken(token);
            userId = (String) claims.get("userId");
            //删除缓存的token
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        if (StrUtil.isNotEmpty(userId)){
            return iBmUserService.getById(userId);
        }
        return null;
    }

    public String getUserId(){
        BmUser bmUser = getBmUser();
        if (ObjectUtil.isNotEmpty(bmUser)){
            return bmUser.getUserId();
        }else {
            return null;
        }

    }
}
