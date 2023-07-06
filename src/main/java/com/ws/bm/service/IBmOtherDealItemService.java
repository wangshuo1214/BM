package com.ws.bm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ws.bm.domain.entity.BmOtherDealItem;

import java.util.List;

public interface IBmOtherDealItemService extends IService<BmOtherDealItem> {

    boolean addBmOtherDealItem(BmOtherDealItem bmOtherDealItem);

    List<BmOtherDealItem> queryBmOtherDealItem(BmOtherDealItem bmOtherDealItem);

    boolean deleteBmOtherDealItem(List<String> ids);

    boolean updateBmOtherDealItem(BmOtherDealItem bmOtherDealItem);

    BmOtherDealItem getBmOtherDealItem(String id);

    List<BmOtherDealItem> getBmOtherDealItemByType(String type);
}
