package com.ws.bm.service.Impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.ws.bm.common.constant.BaseConstant;
import com.ws.bm.mapper.BmOrderMapper;
import com.ws.bm.mapper.BmOtherDealMapper;
import com.ws.bm.mapper.BmSalaryRecordMapper;
import com.ws.bm.mapper.BmTransferRecordMapper;
import com.ws.bm.service.IBmHomePageService;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class BmHomePageServiceImpl implements IBmHomePageService {

    @Autowired
    BmOrderMapper bmOrderMapper;

    @Autowired
    BmOtherDealMapper bmOtherDealMapper;

    @Autowired
    BmSalaryRecordMapper bmSalaryRecordMapper;

    @Autowired
    BmTransferRecordMapper bmTransferRecordMapper;

    private String yearFormat = "'%Y'";

    private String monthFormat = "'%Y-%m'";

    private String dayFormat = "'%Y-%m-%d'";

    @Override
    public JSONObject getHomePageStatistic() {
        Map<String,String> map = new HashMap<>();
        JSONObject result = new JSONObject();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String format = sdf.format(new Date());

        map.put("format",dayFormat);
        map.put("time",format);
        //今日收入
        result.put("daySell",getSellAdd(map));
        //今日支出
        result.put("dayCost",getCostAdd(map));

        map.put("format",monthFormat);
        map.put("time",format);
        //本月收入
        result.put("monthSell",getSellAdd(map));
        //本月支出
        result.put("monthCost",getCostAdd(map));

        map.put("format",yearFormat);
        map.put("time",format);
        //本年收入
        result.put("yearSell",getSellAdd(map));
        //本年支出
        result.put("yearCost",getCostAdd(map));

        map.put("format",null);
        map.put("time",null);
        // 累计收入
        result.put("totalSell",getSellAdd(map));
        // 累计支出
        result.put("totalCost",getCostAdd(map));

        //近7天收入
        result.put("sevenDaySell",get7DaySell());

        //近12个月收入
        result.put("twelveMonthSell",get12MonthSell());

        //近7天支出
        result.put("sevenDayCost",get7DayCost());

        //近12个月支出
        result.put("twelveMonthCost",get12MonthCost());

        return result;
    }

    // 求销售的和
    private BigDecimal getSellAdd(Map<String,String> map){
        // 转账记录收到的钱
        String sellMoneyStatistic = bmTransferRecordMapper.getSellMoneyStatistic(map);
        if (StrUtil.isEmpty(sellMoneyStatistic)){
            sellMoneyStatistic = "0";
        }
        // 其他销售收到的钱
        map.put("type", BaseConstant.SellOrder);
        String otherMoneyStatistic = bmOtherDealMapper.getMoneyStatistic(map);
        if (StrUtil.isEmpty(otherMoneyStatistic)){
            otherMoneyStatistic = "0";
        }
        // 求和
        BigDecimal result = new BigDecimal(sellMoneyStatistic).add(new BigDecimal(otherMoneyStatistic));
        return result;
    }

    // 求7天内销售的和
    private Map<String,BigDecimal> get7DaySell(){
        Map<String,BigDecimal> result = new HashMap<>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Map<String,String> map = new HashMap<>();
        map.put("type",BaseConstant.SellOrder);

        for (int i = 0; i < 7; i++){
            String time = sdf.format(DateUtils.addDays(new Date(), -i));
            map.put("time",time);
            map.put("format",dayFormat);
            String sellMoneyStatistic = bmTransferRecordMapper.getSellMoneyStatistic(map);
            if (StrUtil.isEmpty(sellMoneyStatistic)){
                sellMoneyStatistic = "0";
            }
            String otherMoneyStatistic = bmOtherDealMapper.getMoneyStatistic(map);
            if (StrUtil.isEmpty(otherMoneyStatistic)){
                otherMoneyStatistic = "0";
            }
            BigDecimal add = new BigDecimal(sellMoneyStatistic).add(new BigDecimal(otherMoneyStatistic));
            result.put(time,add);

        }
        return result;
    }

    // 求12月内销售的和
    private Map<String,BigDecimal> get12MonthSell(){
        Map<String,BigDecimal> result = new HashMap<>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        Map<String,String> map = new HashMap<>();
        map.put("type",BaseConstant.SellOrder);

        for (int i = 0; i < 12; i++){
            String time = sdf.format(DateUtils.addMonths(new Date(), -i));
            map.put("time",time+"-00");
            map.put("format",monthFormat);
            String sellMoneyStatistic = bmTransferRecordMapper.getSellMoneyStatistic(map);
            if (StrUtil.isEmpty(sellMoneyStatistic)){
                sellMoneyStatistic = "0";
            }
            String otherMoneyStatistic = bmOtherDealMapper.getMoneyStatistic(map);
            if (StrUtil.isEmpty(otherMoneyStatistic)){
                otherMoneyStatistic = "0";
            }
            BigDecimal add = new BigDecimal(sellMoneyStatistic).add(new BigDecimal(otherMoneyStatistic));
            result.put(time,add);

        }
        return result;
    }

    // 求支出的和
    private BigDecimal getCostAdd(Map<String,String> map){
        // 采购的支出
        String buyMoneyStatistic = bmOrderMapper.getCostMoneyStatistic(map);
        if (StrUtil.isEmpty(buyMoneyStatistic)){
            buyMoneyStatistic = "0";
        }
        // 其他采购的支出
        String otherMoneyStatistic = bmOtherDealMapper.getMoneyStatistic(map);
        if (StrUtil.isEmpty(otherMoneyStatistic)){
            otherMoneyStatistic = "0";
        }
        // 工资的支出
        String salaryStatistic = bmSalaryRecordMapper.getSalaryStatistic(map);
        if (StrUtil.isEmpty(salaryStatistic)){
            salaryStatistic = "0";
        }
        BigDecimal result = new BigDecimal(buyMoneyStatistic).add(new BigDecimal(otherMoneyStatistic)).add(new BigDecimal(salaryStatistic));
        return result;
    }


    // 求7天内支出的和
    private Map<String,BigDecimal> get7DayCost(){
        Map<String,BigDecimal> result = new HashMap<>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Map<String,String> map = new HashMap<>();
        map.put("type",BaseConstant.BuyOrder);

        for (int i = 0; i < 7; i++){
            String time = sdf.format(DateUtils.addDays(new Date(), -i));
            map.put("time",time);
            map.put("format",dayFormat);
            String buyMoneyStatistic = bmOrderMapper.getCostMoneyStatistic(map);
            if (StrUtil.isEmpty(buyMoneyStatistic)){
                buyMoneyStatistic = "0";
            }
            String otherMoneyStatistic = bmOtherDealMapper.getMoneyStatistic(map);
            if (StrUtil.isEmpty(otherMoneyStatistic)){
                otherMoneyStatistic = "0";
            }
            String salaryStatistic = bmSalaryRecordMapper.getSalaryStatistic(map);
            if (StrUtil.isEmpty(salaryStatistic)){
                salaryStatistic = "0";
            }
            BigDecimal add = new BigDecimal(buyMoneyStatistic).add(new BigDecimal(otherMoneyStatistic)).add(new BigDecimal(salaryStatistic));
            result.put(time,add);

        }
        return result;
    }


    // 求12月内支出的和
    private Map<String,BigDecimal> get12MonthCost(){
        Map<String,BigDecimal> result = new HashMap<>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        Map<String,String> map = new HashMap<>();
        map.put("type",BaseConstant.BuyOrder);

        for (int i = 0; i < 12; i++){
            String time = sdf.format(DateUtils.addMonths(new Date(), -i));
            map.put("time",time+"-00");
            map.put("format",monthFormat);
            String buyMoneyStatistic = bmOrderMapper.getCostMoneyStatistic(map);
            if (StrUtil.isEmpty(buyMoneyStatistic)){
                buyMoneyStatistic = "0";
            }
            String otherMoneyStatistic = bmOtherDealMapper.getMoneyStatistic(map);
            if (StrUtil.isEmpty(otherMoneyStatistic)){
                otherMoneyStatistic = "0";
            }
            String salaryStatistic = bmSalaryRecordMapper.getSalaryStatistic(map);
            if (StrUtil.isEmpty(salaryStatistic)){
                salaryStatistic = "0";
            }
            BigDecimal add = new BigDecimal(buyMoneyStatistic).add(new BigDecimal(otherMoneyStatistic)).add(new BigDecimal(salaryStatistic));
            result.put(time,add);

        }
        return result;
    }

}
