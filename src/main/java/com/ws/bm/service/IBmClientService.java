package com.ws.bm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ws.bm.domain.entity.BmClient;
import com.ws.bm.domain.model.TreeSelect;

import java.util.List;

public interface IBmClientService extends IService<BmClient> {
    boolean addBmClient(BmClient bmClient);

    List<BmClient> queryBmClient(BmClient bmClient);

    boolean deleteBmClient(List<String> bmClientIds);

    boolean updateBmClient(BmClient bmClient);

    BmClient getBmClient(String bmClientId);

    List<TreeSelect> getClientTree();

    List<BmClient> getAllClient();
}
