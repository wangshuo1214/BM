package com.ws.bm.service.Impl;

import com.ws.bm.domain.entity.BmOrder;
import com.ws.bm.service.IBmSellRecordService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BmSellRecordServiceImpl implements IBmSellRecordService {
    @Override
    public int addBmSellRecord(BmOrder bmOrder) {
        return 0;
    }

    @Override
    public int updateBmSellRecord(BmOrder bmOrder) {
        return 0;
    }

    @Override
    public List<BmOrder> queryBmSellRecord(BmOrder bmOrder) {
        return null;
    }

    @Override
    public BmOrder getBmSellRecord(String bmOrderId) {
        return null;
    }

    @Override
    public int deleteBmSellRecord(List<String> bmOrderIds) {
        return 0;
    }
}
