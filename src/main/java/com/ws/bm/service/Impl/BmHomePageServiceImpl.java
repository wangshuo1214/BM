package com.ws.bm.service.Impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.ws.bm.common.constant.BaseConstant;
import com.ws.bm.mapper.BmOrderMapper;
import com.ws.bm.mapper.BmOtherDealMapper;
import com.ws.bm.mapper.BmSalaryRecordMapper;
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

    private String yearFormat = "'%Y'";

    private String monthFormat = "'%Y-%m'";

    private String dayFormat = "'%Y-%m-%d'";

    @Override
    public JSONObject getHomePageStatistic() {
        Map<String,String> map = new HashMap<>();
        JSONObject result = new JSONObject();

        String format = new SimpleDateFormat().format(new Date());

        map.put("format",dayFormat);
        map.put("time",format);
        //今日销售
        result.put("daySell",getSellAdd(map));
        //今日开支
        result.put("dayCost",getCostAdd(map));

        map.put("format",monthFormat);
        map.put("time",format);
        //本月销售
        result.put("monthSell",getSellAdd(map));
        //本月开支
        result.put("monthCost",getCostAdd(map));

        map.put("format",yearFormat);
        map.put("time",format);
        //本年销售
        result.put("yearSell",getSellAdd(map));
        //本年开支
        result.put("yearCost",getCostAdd(map));

        //近7天销售
        result.put("sevenDaySell",get7DaySell());

        //近12个月销售
        result.put("twelveMonthSell",get12MonthSell());

        //近7天支出
        result.put("sevenDayCost",get7DayCost());

        //近12个月支出
        result.put("twelveMonthCost",get12MonthCost());

        return result;
    }

    // 求所有销售的和
    private BigDecimal getSellAdd(Map<String,String> map){
        map.put("type", BaseConstant.SellOrder);
        String sellMoneyStatistic = StrUtil.isEmpty(bmOrderMapper.getMoneyStatistic(map)) ? "0" : bmOrderMapper.getMoneyStatistic(map);
        String otherMoneyStatistic = StrUtil.isEmpty(bmOtherDealMapper.getMoneyStatistic(map)) ? "0" : bmOtherDealMapper.getMoneyStatistic(map);

        BigDecimal result = new BigDecimal(sellMoneyStatistic).add(new BigDecimal(otherMoneyStatistic));
        return result;
    }

    // 求7天内销售的和
    private Map<String,BigDecimal> get7DaySell(){
        Map<String,BigDecimal> result = new HashMap<>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Map<String,String> map = new HashMap<>();
        map.put("type",BaseConstant.SellOrder);

        for (int i = 0; i <= 7; i++){
            String time = sdf.format(DateUtils.addDays(new Date(), -i));
            map.put("time",time);
            map.put("format",dayFormat);
            String sellMoneyStatistic = StrUtil.isEmpty(bmOrderMapper.getMoneyStatistic(map)) ? "0" : bmOrderMapper.getMoneyStatistic(map);
            String otherMoneyStatistic = StrUtil.isEmpty(bmOtherDealMapper.getMoneyStatistic(map)) ? "0" : bmOtherDealMapper.getMoneyStatistic(map);
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

        for (int i = 0; i <= 12; i++){
            String time = sdf.format(DateUtils.addMonths(new Date(), -i));
            map.put("time",time+"-00");
            map.put("format",monthFormat);
            String sellMoneyStatistic = StrUtil.isEmpty(bmOrderMapper.getMoneyStatistic(map)) ? "0" : bmOrderMapper.getMoneyStatistic(map);
            String otherMoneyStatistic = StrUtil.isEmpty(bmOtherDealMapper.getMoneyStatistic(map)) ? "0" : bmOtherDealMapper.getMoneyStatistic(map);
            BigDecimal add = new BigDecimal(sellMoneyStatistic).add(new BigDecimal(otherMoneyStatistic));
            result.put(time,add);

        }
        return result;
    }

    // 求所有支出的和
    private BigDecimal getCostAdd(Map<String,String> map){
        map.put("type", BaseConstant.BuyOrder);
        String buyMoneyStatistic = StrUtil.isEmpty(bmOrderMapper.getMoneyStatistic(map)) ? "0" : bmOrderMapper.getMoneyStatistic(map);
        String otherMoneyStatistic = StrUtil.isEmpty(bmOtherDealMapper.getMoneyStatistic(map)) ? "0" : bmOtherDealMapper.getMoneyStatistic(map);
        String salaryStatistic = StrUtil.isEmpty(bmSalaryRecordMapper.getSalaryStatistic(map)) ? "0" : bmSalaryRecordMapper.getSalaryStatistic(map);

        BigDecimal result = new BigDecimal(buyMoneyStatistic).add(new BigDecimal(otherMoneyStatistic)).add(new BigDecimal(salaryStatistic));
        return result;
    }


    // 求7天内支出的和
    private Map<String,BigDecimal> get7DayCost(){
        Map<String,BigDecimal> result = new HashMap<>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Map<String,String> map = new HashMap<>();
        map.put("type",BaseConstant.BuyOrder);

        for (int i = 0; i <= 7; i++){
            String time = sdf.format(DateUtils.addDays(new Date(), -i));
            map.put("time",time);
            map.put("format",dayFormat);
            String buyMoneyStatistic = StrUtil.isEmpty(bmOrderMapper.getMoneyStatistic(map)) ? "0" : bmOrderMapper.getMoneyStatistic(map);
            String otherMoneyStatistic = StrUtil.isEmpty(bmOtherDealMapper.getMoneyStatistic(map)) ? "0" : bmOtherDealMapper.getMoneyStatistic(map);
            String salaryStatistic = StrUtil.isEmpty(bmSalaryRecordMapper.getSalaryStatistic(map)) ? "0" : bmSalaryRecordMapper.getSalaryStatistic(map);
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

        for (int i = 0; i <= 12; i++){
            String time = sdf.format(DateUtils.addMonths(new Date(), -i));
            map.put("time",time+"-00");
            map.put("format",monthFormat);
            String buyMoneyStatistic = StrUtil.isEmpty(bmOrderMapper.getMoneyStatistic(map)) ? "0" : bmOrderMapper.getMoneyStatistic(map);
            String otherMoneyStatistic = StrUtil.isEmpty(bmOtherDealMapper.getMoneyStatistic(map)) ? "0" : bmOtherDealMapper.getMoneyStatistic(map);
            String salaryStatistic = StrUtil.isEmpty(bmSalaryRecordMapper.getSalaryStatistic(map)) ? "0" : bmSalaryRecordMapper.getSalaryStatistic(map);
            BigDecimal add = new BigDecimal(buyMoneyStatistic).add(new BigDecimal(otherMoneyStatistic)).add(new BigDecimal(salaryStatistic));
            result.put(time,add);

        }
        return result;
    }

}
