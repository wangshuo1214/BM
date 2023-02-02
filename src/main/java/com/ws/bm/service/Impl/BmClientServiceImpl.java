package com.ws.bm.service.Impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ws.bm.common.constant.BaseConstant;
import com.ws.bm.common.constant.HttpStatus;
import com.ws.bm.common.utils.InitFieldUtil;
import com.ws.bm.common.utils.MessageUtil;
import com.ws.bm.domain.entity.BmClient;
import com.ws.bm.exception.BaseException;
import com.ws.bm.mapper.BmClientMapper;
import com.ws.bm.service.IBmClientService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class BmClientServiceImpl extends ServiceImpl<BmClientMapper, BmClient> implements IBmClientService {
    @Override
    public boolean addBmClient(BmClient bmClient) {
        //必填字段校验
        if (checkFiled(bmClient)){
            throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.paramsError"));
        }
        //客户名称重复校验
        QueryWrapper<BmClient> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(BmClient::getClientName,bmClient.getClientName().trim());
        wrapper.lambda().eq(BmClient::getDeleted, BaseConstant.FALSE);
        if (CollUtil.isNotEmpty(list(wrapper))){
            throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.client.nameRepeat"));
        }
        if (!InitFieldUtil.initField(bmClient)){
            throw new BaseException(HttpStatus.ERROR,MessageUtil.getMessage("bm.initFieldError"));
        }
        bmClient.setClientId(UUID.randomUUID().toString());
        bmClient.setDebt(BigDecimal.ZERO);
        bmClient.setWeight(0);
        return save(bmClient);
    }

    @Override
    public List<BmClient> queryBmClient(BmClient bmClient) {
        if (ObjectUtil.isEmpty(bmClient)){
            throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.paramsError"));
        }
        QueryWrapper<BmClient> wrapper = new QueryWrapper<>();
        if (StrUtil.isNotEmpty(bmClient.getClientName())){
            wrapper.lambda().like(BmClient::getClientName,bmClient.getClientName());
        }
        if (StrUtil.isNotEmpty(bmClient.getPhone())){
            wrapper.lambda().like(BmClient::getPhone,bmClient.getPhone());
        }
        if (StrUtil.isNotEmpty(bmClient.getAddress())){
            wrapper.lambda().like(BmClient::getAddress,bmClient.getAddress());
        }
        wrapper.lambda().eq(BmClient::getDeleted,BaseConstant.FALSE);
        wrapper.lambda().orderByDesc(BmClient::getWeight).orderByDesc(BmClient::getUpdateDate);
        return list(wrapper);
    }

    @Override
    public boolean deleteBmClient(List<String> bmClientIds) {
        //参数校验
        if (CollUtil.isEmpty(bmClientIds)){
            throw new BaseException(HttpStatus.BAD_REQUEST,MessageUtil.getMessage("bm.paramsError"));
        }
        List<BmClient> clientList = listByIds(bmClientIds);
        if (CollUtil.isEmpty(clientList)){
            throw new BaseException(HttpStatus.BAD_REQUEST,MessageUtil.getMessage("bm.paramsError"));
        }
        clientList.forEach(client -> {
            client.setDeleted(BaseConstant.TRUE);
            client.setUpdateDate(new Date());
        });
        return updateBatchById(clientList);
    }

    @Override
    public boolean updateBmClient(BmClient bmClient) {
        if (checkFiled(bmClient)){
            throw new BaseException(HttpStatus.BAD_REQUEST,MessageUtil.getMessage("bm.paramsError"));
        }
        //判断客户名称是否重复
        QueryWrapper<BmClient> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(BmClient::getClientName,bmClient.getClientName().trim());
        wrapper.lambda().eq(BmClient::getDeleted,BaseConstant.FALSE);
        wrapper.lambda().ne(BmClient::getClientId,bmClient.getClientId());
        if (CollUtil.isNotEmpty(list(wrapper))){
            throw new BaseException(HttpStatus.BAD_REQUEST,MessageUtil.getMessage("bm.client.nameRepeat"));
        }

        //旧对象
        BmClient oldClient = getById(bmClient.getClientId());
        if (!updateFlag(bmClient,oldClient)){
            oldClient.setClientName(bmClient.getClientName());
            oldClient.setAddress(bmClient.getAddress());
            oldClient.setPhone(bmClient.getPhone());
            oldClient.setRemark(bmClient.getRemark());
        }

        return updateById(oldClient);
    }

    @Override
    public BmClient getBmClient(String bmClientId) {
        if (StrUtil.isEmpty(bmClientId)){
            throw new BaseException(HttpStatus.BAD_REQUEST,MessageUtil.getMessage("bm.paramsError"));
        }
        BmClient bmClient = getById(bmClientId);
        if (ObjectUtil.isEmpty(bmClient) || StrUtil.equals(bmClient.getDeleted(),BaseConstant.TRUE)){
            throw new BaseException(HttpStatus.BAD_REQUEST,MessageUtil.getMessage("bm.client.notexist"));
        }
        return bmClient;
    }

    private boolean checkFiled(BmClient bmClient){
        if(ObjectUtil.isEmpty(bmClient) || StrUtil.isEmpty(bmClient.getClientName())){
            return true;
        }
        return false;
    }

    private boolean updateFlag(BmClient newObj, BmClient oldObj){
        StringBuffer sb1 = new StringBuffer("");
        StringBuffer sb2 = new StringBuffer("");
        sb1.append(newObj.getClientName());
        sb2.append(oldObj.getClientName());
        sb1.append(newObj.getAddress());
        sb2.append(oldObj.getAddress());
        sb1.append(newObj.getPhone());
        sb2.append(oldObj.getPhone());
        sb1.append(newObj.getRemark());
        sb2.append(oldObj.getRemark());
        return sb1.toString().equals(sb2.toString());
    }
}
