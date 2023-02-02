package com.ws.bm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ws.bm.domain.entity.BmEmployee;

import java.util.List;

public interface IBmEmployeeService extends IService<BmEmployee> {
    boolean addBmEmployee(BmEmployee bmEmployee);

    List<BmEmployee> queryBmEmployee(BmEmployee bmEmployee);

    boolean deleteBmEmployee(List<String> bmEmployeeIds);

    boolean updateBmEmployee(BmEmployee bmEmployee);

    BmEmployee getBmEmployee(String bmEmployeeId);
}
