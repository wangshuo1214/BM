package com.ws.bm.service.Impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ws.bm.common.constant.BaseConstant;
import com.ws.bm.common.constant.HttpStatus;
import com.ws.bm.common.utils.InitFieldUtil;
import com.ws.bm.common.utils.MessageUtil;
import com.ws.bm.domain.entity.*;
import com.ws.bm.exception.BaseException;
import com.ws.bm.mapper.BmMakeRecordMapper;
import com.ws.bm.mapper.BmMaterialMapper;
import com.ws.bm.service.IBmMakeRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
public class BmMakeRecordServiceImpl implements IBmMakeRecordService {

    @Autowired
    BmMakeRecordMapper bmMakeRecordMapper;

    @Autowired
    BmMaterialMapper bmMaterialMapper;

    @Override
    @Transactional
    public int addBmMakeRecord(BmMakeRecord bmMakeRecord) {
        if (checkFiled(bmMakeRecord)){
            throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.paramsError"));
        }
        if (!InitFieldUtil.initField(bmMakeRecord)){
            throw new BaseException(HttpStatus.ERROR,MessageUtil.getMessage("bm.initFieldError"));
        }
        bmMakeRecord.setWageFlag(BaseConstant.FALSE);

        //生产记录细节
        List<BmMakeRecordDetail> bmMakeRecordDetails = JSONObject.parseArray(JSONArray.toJSONString(bmMakeRecord.getParams().get("bmMakeRecordDetails")), BmMakeRecordDetail.class);
        bmMakeRecordDetails.forEach(bmMakeRecordDetail -> {
            if ( ObjectUtil.isEmpty(bmMakeRecordDetail.getNum()) ||
                    ObjectUtil.isEmpty(bmMakeRecordDetail.getWage()) || ObjectUtil.isEmpty(bmMakeRecordDetail.getSort())
            ){
                throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.paramsError"));
            }
            if (!InitFieldUtil.initField(bmMakeRecordDetail)){
                throw new BaseException(HttpStatus.ERROR,MessageUtil.getMessage("bm.initFieldError"));
            }
            bmMakeRecordDetail.setMakeRecordId(bmMakeRecord.getId());
        });
        //批量增加生产记录详情
        bmMakeRecordMapper.batchAddBmMakeRecordDetail(bmMakeRecordDetails);
        return bmMakeRecordMapper.addBmMakeRecord(bmMakeRecord);
    }

    @Override
    @Transactional
    public int updateBmMakeRecord(BmMakeRecord bmMakeRecord) {
        if (checkFiled(bmMakeRecord)){
            throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.paramsError"));
        }
        // 旧对象
        BmMakeRecord oldBmMakeRecord = bmMakeRecordMapper.getBmMakeRecord(bmMakeRecord.getId());
        if (ObjectUtil.isEmpty(oldBmMakeRecord)){
            throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.paramsError"));
        }
        // 旧订单细节
        List<BmMakeRecordDetail> oldBmMakeRecordDetails = bmMakeRecordMapper.getBmMakeRecordDetailByMakeRecordId(bmMakeRecord.getId());
        // 新生产记录细节
        List<BmMakeRecordDetail> bmMakeRecordDetails = JSONObject.parseArray(JSONArray.toJSONString(bmMakeRecord.getParams().get("bmMakeRecordDetails")), BmMakeRecordDetail.class);
        if (!StrUtil.equals(bmMakeRecordDetails.toString(),oldBmMakeRecordDetails.toString())){
            //删除之前的生产记录细节
            bmMakeRecordMapper.batchDeleteBmMakeRecordDetail(bmMakeRecord.getId());
            bmMakeRecordDetails.forEach(bmMakeRecordDetail -> {
                if ( ObjectUtil.isEmpty(bmMakeRecordDetail.getNum()) ||
                        ObjectUtil.isEmpty(bmMakeRecordDetail.getWage()) || ObjectUtil.isEmpty(bmMakeRecordDetail.getSort())
                ){
                    throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.paramsError"));
                }
                if (!InitFieldUtil.initField(bmMakeRecordDetail)){
                    throw new BaseException(HttpStatus.ERROR,MessageUtil.getMessage("bm.initFieldError"));
                }
                bmMakeRecordDetail.setMakeRecordId(bmMakeRecord.getId());
            });
            //批量增加生产记录详情
            bmMakeRecordMapper.batchAddBmMakeRecordDetail(bmMakeRecordDetails);
        }
        if (!updateFlag(bmMakeRecord,oldBmMakeRecord)){
            oldBmMakeRecord.setCompleteDate(bmMakeRecord.getCompleteDate());
            oldBmMakeRecord.setUpdateDate(new Date());
        }

        return bmMakeRecordMapper.updateBmMakeRecord(bmMakeRecord);
    }

    @Override
    public List<BmMakeRecord> queryBmMakeRecord(BmMakeRecord bmMakeRecord) {
        if (ObjectUtil.isEmpty(bmMakeRecord) || StrUtil.isEmpty(bmMakeRecord.getEmployeeId())){
            throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.paramsError"));
        }
        List<BmMakeRecord> bmMakeRecords = bmMakeRecordMapper.queryBmMakeRecord(bmMakeRecord);
        if (CollUtil.isNotEmpty(bmMakeRecords)){
            bmMakeRecords.forEach(record -> {
                StringBuffer makeRecordDetailName = new StringBuffer("");
                BigDecimal totalWage = new BigDecimal(0);
                List<BmMakeRecordDetail> bmMakeRecordDetails = bmMakeRecordMapper.getBmMakeRecordDetailByMakeRecordId(record.getId());
                if (CollUtil.isNotEmpty(bmMakeRecordDetails)){
                    for (int i = 0; i < bmMakeRecordDetails.size(); i++) {
                        BmMakeRecordDetail bmMakeRecordDetail = bmMakeRecordDetails.get(i);
                        totalWage = totalWage.add(bmMakeRecordDetail.getWage());
                        BmMaterial bmMaterial = bmMaterialMapper.selectById(bmMakeRecordDetail.getMaterialId());
                        if (i != bmMakeRecordDetails.size()-1){
                            makeRecordDetailName.append(bmMaterial.getMaterialName() + "(" +
                                    bmMakeRecordDetail.getWage().stripTrailingZeros().toPlainString()+"元) 、 ");
                        }else {
                            makeRecordDetailName.append(bmMaterial.getMaterialName() + "(" +
                                    bmMakeRecordDetail.getWage().stripTrailingZeros().toPlainString()+"元)");
                        }
                    }
                    record.setMakeRecordDeatil(makeRecordDetailName.toString());
                    record.setTotalWage(totalWage);
                }
            });
        }
        return bmMakeRecords;
    }

    @Override
    public BmMakeRecord getBmMakeRecord(String id) {
        if (StrUtil.isEmpty(id)){
            throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.paramsError"));
        }
        BmMakeRecord bmMakeRecord = bmMakeRecordMapper.getBmMakeRecord(id);
        if (ObjectUtil.isEmpty(bmMakeRecord)){
            throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.paramsError"));
        }
        Map<String,Object> bmMakeRecordDetails = new HashMap<>();
        bmMakeRecordDetails.put("bmMakeRecordDetails",bmMakeRecordMapper.getBmMakeRecordDetailByMakeRecordId(id));
        bmMakeRecord.setParams(bmMakeRecordDetails);
        return bmMakeRecord;
    }

    @Override
    @Transactional
    public int deleteBmMakeRecord(List<String> ids) {
        if (CollUtil.isEmpty(ids)){
            throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.paramsError"));
        }
        bmMakeRecordMapper.deleteBmMakeRecordDetail(ids);
        return bmMakeRecordMapper.deleteBmMakeRecord(ids);
    }

    @Override
    @Transactional
    public JSONObject payWage(List<String> ids) {
        List<BmMakeRecord> bmMakeRecords = new ArrayList<>();
        if (CollUtil.isEmpty(ids)){
            bmMakeRecords = bmMakeRecordMapper.getNoPayMakeRecord();
        }else {
            bmMakeRecords = bmMakeRecordMapper.getMakeRecordByIds(ids);
        }
        if (CollUtil.isNotEmpty(bmMakeRecords)){
            // 获取总共支付的工资总数
            BigDecimal totalPayWage = new BigDecimal(bmMakeRecordMapper.getTotalPayWage(ids));
            // 新增工资发放记录
            BmSalaryRecord bmSalaryRecord = new BmSalaryRecord();
            if (!InitFieldUtil.initField(bmSalaryRecord)){
                throw new BaseException(HttpStatus.ERROR,MessageUtil.getMessage("bm.initFieldError"));
            }
            bmSalaryRecord.setSalary(totalPayWage);
            bmSalaryRecord.setSalaryDate(new Date());
            bmMakeRecordMapper.addBmSalaryRecord(bmSalaryRecord);
            // 支付工资，修改生产记录的支付标志
            bmMakeRecordMapper.payWage(ids);
            // 生产记录绑定工资发放标识
            bmMakeRecordMapper.setSalaryIdForMakeRecord(bmSalaryRecord.getId());
            JSONObject result = new JSONObject();
            result.put("totalPayWage",totalPayWage);
            return result;

        }else {
            throw new BaseException(HttpStatus.ERROR,MessageUtil.getMessage("bm.makeRecord.noPayWage"));
        }

    }

    private boolean checkFiled(BmMakeRecord bmMakeRecord){
        if(ObjectUtil.isEmpty(bmMakeRecord)  || StrUtil.isEmpty(bmMakeRecord.getEmployeeId()) ||
                ObjectUtil.isEmpty(bmMakeRecord.getCompleteDate()) || ObjectUtil.isEmpty(bmMakeRecord.getParams())
        ){
            return true;
        }
        List<BmMakeRecordDetail> bmMakeRecordDetails = JSONObject.parseArray(JSONArray.toJSONString(bmMakeRecord.getParams().get("bmMakeRecordDetails")), BmMakeRecordDetail.class);
        if (CollUtil.isEmpty(bmMakeRecordDetails)){
            throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.makeRecord.deatilNotNull"));
        }
        return false;
    }

    private boolean updateFlag(BmMakeRecord newObj,BmMakeRecord oldObj){
        StringBuffer sb1 = new StringBuffer("");
        StringBuffer sb2 = new StringBuffer("");
        sb1.append(newObj.getCompleteDate());
        sb2.append(oldObj.getCompleteDate());

        return sb1.toString().equals(sb2.toString());
    }
}
