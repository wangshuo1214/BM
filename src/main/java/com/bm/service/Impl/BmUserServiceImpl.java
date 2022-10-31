package com.bm.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bm.dao.BmUserDao;
import com.bm.entity.BmUser;
import com.bm.service.IBmUserService;
import org.springframework.stereotype.Service;

@Service
public class BmUserServiceImpl extends ServiceImpl<BmUserDao, BmUser> implements IBmUserService {
}
