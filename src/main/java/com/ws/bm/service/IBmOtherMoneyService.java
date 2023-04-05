package com.ws.bm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ws.bm.domain.entity.BmOtherMoeny;

import java.util.List;

public interface IBmOtherMoneyService extends IService<BmOtherMoeny> {

    boolean addBmOtherMoney(BmOtherMoeny bmOtherMoeny);

    List<BmOtherMoeny> queryBmOtherMoeny(BmOtherMoeny bmOtherMoeny);

    boolean updateBmOtherMoney(BmOtherMoeny bmOtherMoeny);

    boolean deleteBmOtherMoney(List<String> ids);

    BmOtherMoeny getBmOtherMoney(String id);
}
