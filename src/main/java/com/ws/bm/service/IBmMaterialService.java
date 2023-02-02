package com.ws.bm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ws.bm.domain.entity.BmMaterial;

import java.util.List;

public interface IBmMaterialService extends IService<BmMaterial> {
    boolean addBmMaterial(BmMaterial bmMaterial);

    List<BmMaterial> queryBmMaterial(BmMaterial bmMaterial);

    boolean deleteBmMaterial(List<String> bmMaterialIds);

    boolean updateBmMaterial(BmMaterial bmMaterial);

    BmMaterial getBmMaterial(String bmMaterialId);

    List<BmMaterial> queryBmMaterialOrder(String orderTag);
}
