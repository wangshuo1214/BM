package com.bm.common.domain;

public class BaseController {

    public Result success(){
        return Result.returnCodeMessage("200","O(∩_∩)O 操作成功！");
    }
    public Result success(Object data) {
        return Result.returnCodeMessage("200","O(∩_∩)O 操作成功！",data);
    }
    public Result error() {
        return Result.returnCodeMessage("500","(ㄒoㄒ)/~~ 操作失败！");
    }
    public Result error(String msg){
        return Result.returnCodeMessage("500",msg,null);
    }
}
