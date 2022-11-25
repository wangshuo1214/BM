package com.ws.bm.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ws.bm.mapper.BmUserMapper;
import com.ws.bm.domain.entity.BmUser;
import com.ws.bm.service.IBmUserService;
import org.springframework.stereotype.Service;

@Service
public class BmUserServiceImpl extends ServiceImpl<BmUserMapper, BmUser> implements IBmUserService {
}
