package com.ws.bm.service.Impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.ws.bm.common.constant.BaseConstant;
import com.ws.bm.mapper.BmOrderMapper;
import com.ws.bm.mapper.BmOtherDealMapper;
import com.ws.bm.mapper.BmSalaryRecordMapper;
import com.ws.bm.service.IBmHomePageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

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

        map.put("time",dayFormat);
        //今日销售
        result.put("daySell",getSellAdd(map));

        //今日开支
        result.put("dayCost",getCostAdd(map));

        map.put("time",monthFormat);
        //本月销售
        result.put("monthSell",getSellAdd(map));

        //本月开支
        result.put("monthCost",getCostAdd(map));

        map.put("time",yearFormat);
        //本年销售
        result.put("yearSell",getSellAdd(map));

        //本年开支
        result.put("yearCost",getCostAdd(map));

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

    // 求所有支出的和
    private BigDecimal getCostAdd(Map<String,String> map){
        map.put("type", BaseConstant.BuyOrder);
        String buyMoneyStatistic = StrUtil.isEmpty(bmOrderMapper.getMoneyStatistic(map)) ? "0" : bmOrderMapper.getMoneyStatistic(map);
        String otherMoneyStatistic = StrUtil.isEmpty(bmOtherDealMapper.getMoneyStatistic(map)) ? "0" : bmOtherDealMapper.getMoneyStatistic(map);
        String salaryStatistic = StrUtil.isEmpty(bmSalaryRecordMapper.getSalaryStatistic(map)) ? "0" : bmSalaryRecordMapper.getSalaryStatistic(map);

        BigDecimal result = new BigDecimal(buyMoneyStatistic).add(new BigDecimal(otherMoneyStatistic)).add(new BigDecimal(salaryStatistic));
        return result;
    }
}
