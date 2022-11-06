package com.bm.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bm.mapper.BmUserMapper;
import com.bm.domain.entity.BmUser;
import com.bm.service.IBmUserService;
import org.springframework.stereotype.Service;

@Service
public class BmUserServiceImpl extends ServiceImpl<BmUserMapper, BmUser> implements IBmUserService {
}
