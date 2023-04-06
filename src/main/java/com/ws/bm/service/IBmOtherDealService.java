package com.ws.bm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ws.bm.domain.entity.BmOtherDeal;

import java.util.List;

public interface IBmOtherDealService extends IService<BmOtherDeal> {

    boolean addBmOtherDeal(BmOtherDeal bmOtherDeal);

    List<BmOtherDeal> queryBmOtherDeal(BmOtherDeal bmOtherDeal);

    boolean updateBmOtherDeal(BmOtherDeal bmOtherDeal);

    boolean deleteBmOtherDeal(List<String> ids);

    BmOtherDeal getBmOtherDeal(String id);
}
