package com.ws.bm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ws.bm.domain.entity.BmSupplier;

import java.util.List;

public interface IBmSupplierService extends IService<BmSupplier> {
    boolean addBmSupplier(BmSupplier bmSupplier);

    List<BmSupplier> queryBmSupplier(BmSupplier bmSupplier);

    boolean deleteBmSupplier(List<String> bmSupplierIds);

    boolean updateBmSupplier(BmSupplier bmSupplier);

    BmSupplier getBmSupplier(String bmSupplierId);

    List<BmSupplier> getBmSuppliers();
}
