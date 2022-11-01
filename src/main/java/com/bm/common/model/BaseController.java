package com.bm.common.model;

import com.bm.common.constant.HttpStatus;

public class BaseController {

    public Result success(){
        return Result.returnCodeMessage(HttpStatus.SUCCESS,"O(∩_∩)O 操作成功！");
    }

    public Result success(Object data) {
        return Result.returnCodeMessage(HttpStatus.SUCCESS,"O(∩_∩)O 操作成功！",data);
    }

    public Result error() {
        return Result.returnCodeMessage(HttpStatus.ERROR,"(ㄒoㄒ)/~~ 操作失败！");
    }

    public Result error(String msg){
        return Result.returnCodeMessage(HttpStatus.ERROR,msg,null);
    }

    public Result diyResut(Integer code, String msg){
        return Result.returnCodeMessage(code,msg);
    }

    public Result diyResut(Integer code, String msg, Object data){
        return Result.returnCodeMessage(code,msg,data);
    }
}
