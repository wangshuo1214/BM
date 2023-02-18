package com.ws.bm.service.Impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ws.bm.common.constant.BaseConstant;
import com.ws.bm.common.constant.HttpStatus;
import com.ws.bm.common.utils.MessageUtil;
import com.ws.bm.domain.entity.BmEmployee;
import com.ws.bm.domain.entity.BmMakeRecord;
import com.ws.bm.domain.entity.BmMakeRecordDetail;
import com.ws.bm.domain.entity.BmSalaryRecord;
import com.ws.bm.exception.BaseException;
import com.ws.bm.mapper.BmEmployeeMapper;
import com.ws.bm.mapper.BmMakeRecordMapper;
import com.ws.bm.mapper.BmMaterialMapper;
import com.ws.bm.mapper.BmSalaryMapper;
import com.ws.bm.service.IBmSalaryRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

@Service
public class BmSalaryRecordServiceImpl extends ServiceImpl<BmSalaryMapper, BmSalaryRecord> implements IBmSalaryRecordService {

    @Autowired
    BmMakeRecordMapper bmMakeRecordMapper;

    @Autowired
    BmMaterialMapper bmMaterialMapper;

    @Autowired
    BmEmployeeMapper bmEmployeeMapper;

    @Override
    public List<BmSalaryRecord> queryBmSalaryRecord(BmSalaryRecord bmSalaryRecord) {

        QueryWrapper<BmSalaryRecord> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(BmSalaryRecord::getDeleted, BaseConstant.FALSE);
        // 员工
        if (StrUtil.isNotEmpty(bmSalaryRecord.getEmployeeId())){
            wrapper.lambda().eq(BmSalaryRecord::getEmployeeId,bmSalaryRecord.getEmployeeId());
        }
        if (ObjectUtil.isNotEmpty(bmSalaryRecord.getParams())){
            //工资发放日期
            if (ObjectUtil.isNotEmpty(bmSalaryRecord.getParams().get("salaryDate"))){
                wrapper.lambda().ge(BmSalaryRecord::getSalaryDate, ( (List) (bmSalaryRecord.getParams().get("salaryDate")) ).get(0));
                wrapper.lambda().le(BmSalaryRecord::getSalaryDate,( (List) (bmSalaryRecord.getParams().get("salaryDate")) ).get(1));
            }
        }
        wrapper.lambda().orderByDesc(BmSalaryRecord::getSalaryDate).orderByDesc(BmSalaryRecord::getEmployeeId);
        List<BmSalaryRecord> results = list(wrapper);
        if (CollUtil.isNotEmpty(results)){
            results.forEach(result -> {
                BmEmployee bmEmployee = bmEmployeeMapper.selectById(result.getEmployeeId());
                result.setEmployeeName(bmEmployee.getEmployeeName());
                result.setMakeRecords(getBmSalaryRecords(result));
            });
        }
        return results;
    }

    @Override
    public BmSalaryRecord getBmSalaryRecord(String id) {
        if (StrUtil.isEmpty(id)){
            throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.paramsError"));
        }
        BmSalaryRecord bmSalaryRecord = getById(id);
        if (ObjectUtil.isEmpty(bmSalaryRecord) || StrUtil.equals(bmSalaryRecord.getDeleted(),BaseConstant.TRUE)){
            throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.paramsError"));
        }
        bmSalaryRecord.setMakeRecords(getBmSalaryRecords(bmSalaryRecord));
        return bmSalaryRecord;
    }

    // 获取工资明细记录
    private List<BmMakeRecord> getBmSalaryRecords(BmSalaryRecord bmSalaryRecord){
        List<BmMakeRecord> bmMakeRecords = bmMakeRecordMapper.getMakeRecordBySalaryId(bmSalaryRecord.getId());
        if (CollUtil.isNotEmpty(bmMakeRecords)){
            bmMakeRecords.forEach(bmMakeRecord -> {
                StringBuffer detail = new StringBuffer("");
                List<BmMakeRecordDetail> bmMakeDetails = bmMakeRecordMapper.getBmMakeRecordDetailByMakeRecordId(bmMakeRecord.getId());
                if (CollUtil.isNotEmpty(bmMakeDetails)){
                    bmMakeDetails.forEach(bmMakeRecordDetail -> {
                        detail.append(bmMaterialMapper.selectById(bmMakeRecordDetail.getMaterialId()).getMaterialName() + "×"
                                + bmMakeRecordDetail.getNum() + " " + bmMakeRecordDetail.getWage().stripTrailingZeros().toPlainString() +"元   ");
                    });
                }
                bmMakeRecord.setMakeRecordDeatil(detail.toString());
            });
        }
        return bmMakeRecords;
    }
}
