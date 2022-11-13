package com.bm.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bm.common.constant.HttpStatus;
import com.bm.common.page.PageDomain;
import com.bm.common.page.PageQuery;
import com.bm.common.utils.CreateGsonUtil;
import com.bm.domain.model.Result;
import com.bm.common.utils.MessageUtil;
import com.bm.domain.model.TableDataInfo;
import com.bm.exception.BaseException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;

import java.util.List;

public class BaseController {

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
}
