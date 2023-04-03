package com.ws.bm.service.Impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ws.bm.common.constant.HttpStatus;
import com.ws.bm.common.utils.MessageUtil;
import com.ws.bm.domain.entity.BmTransferRecord;
import com.ws.bm.exception.BaseException;
import com.ws.bm.mapper.BmClientMapper;
import com.ws.bm.mapper.BmTransferRecordMapper;
import com.ws.bm.service.IBmTransferRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BmTransferRecordImpl extends ServiceImpl<BmTransferRecordMapper,BmTransferRecord> implements IBmTransferRecordService {

    @Autowired
    private BmClientMapper bmClientMapper;

    @Override
    public List<BmTransferRecord> queryBmtransferRecord(BmTransferRecord bmTransferRecord) {
        QueryWrapper<BmTransferRecord> wrapper = new QueryWrapper<>();
        if (StrUtil.isNotEmpty(bmTransferRecord.getClientId())){
            wrapper.lambda().eq(BmTransferRecord::getClientId,bmTransferRecord.getClientId());
        }
        if (StrUtil.isNotEmpty(bmTransferRecord.getTransferWay())){
            wrapper.lambda().eq(BmTransferRecord::getTransferWay,bmTransferRecord.getTransferWay());
        }
        if (ObjectUtil.isNotEmpty(bmTransferRecord.getParams())){
            if (ObjectUtil.isNotEmpty(bmTransferRecord.getParams().get("transferDate"))){
                wrapper.lambda().ge(BmTransferRecord::getTransferDate,((List) bmTransferRecord.getParams().get("transferDate")).get(0));
                wrapper.lambda().le(BmTransferRecord::getTransferDate,((List) bmTransferRecord.getParams().get("transferDate")).get(1));
            }
        }
        wrapper.lambda().orderByDesc(BmTransferRecord::getTransferDate);

        return list(wrapper);
    }

    @Override
    public BmTransferRecord getBmTransferRecord(String id) {
        if (StrUtil.isEmpty(id)){
            throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.paramsError"));
        }
        BmTransferRecord transferRecord = getById(id);
        transferRecord.setClientName(bmClientMapper.selectById(transferRecord.getClientId()).getClientName());

        return transferRecord;
    }
}
