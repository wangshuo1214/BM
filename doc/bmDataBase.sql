-- 客户表bm_client
CREATE TABLE `bm_client` (
                             `client_id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '客户ID',
                             `client_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '客户名',
                             `address` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '地址',
                             `phone` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '电话',
                             `client_flag` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '客户标识（0:销售客户,1:采购客户）',
                             `weight` int NOT NULL COMMENT '权重',
                             `create_date` datetime NOT NULL COMMENT '创建时间',
                             `update_date` datetime NOT NULL COMMENT '修改时间',
                             `deleted` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '删除标志（0:未删除,1:已删除）',
                             PRIMARY KEY (`client_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 客户流动资金表
CREATE TABLE `bm_client_circular_moeny` (
                                            `id` varchar(36) NOT NULL COMMENT '表主键',
                                            `client_id` varchar(36) NOT NULL COMMENT '客户id',
                                            `circular_moeny` decimal(10,2) NOT NULL COMMENT '流动金额',
                                            `debt` decimal(10,2) NOT NULL COMMENT '欠款',
                                            `last_circular_date` datetime DEFAULT NULL COMMENT '流动金额最后转入时间',
                                            PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 客户转账记录表
CREATE TABLE `bm_client_pay_record` (
                                        `id` varchar(36) NOT NULL COMMENT '表主键',
                                        `client_id` varchar(36) NOT NULL COMMENT '客户id',
                                        `pay_amount` decimal(10,2) NOT NULL COMMENT '转账金额',
                                        `pay_date` datetime NOT NULL COMMENT '转账日期',
                                        `pay_way` char(1) NOT NULL COMMENT '转账方式',
                                        PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 产品表
CREATE TABLE `bm_product` (
                              `product_id` varchar(36) NOT NULL COMMENT '表主键',
                              `product_name` varchar(50) NOT NULL COMMENT '产品名',
                              `price` decimal(10,2) NOT NULL COMMENT '售价',
                              `cost_price` decimal(10,2) NOT NULL COMMENT '总成本（包含公费）',
                              `wage` decimal(10,2) NOT NULL COMMENT '工费',
                              `weight` int NOT NULL COMMENT '权重',
                              `create_date` datetime NOT NULL COMMENT '创建日期',
                              `update_date` datetime NOT NULL COMMENT '修改日期',
                              `deleted` char(1) NOT NULL COMMENT '删除标志',
                              PRIMARY KEY (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 订单表
CREATE TABLE `bm_order` (
                            `order_id` varchar(36) NOT NULL COMMENT '订单id',
                            `order_date` datetime NOT NULL COMMENT '订单日期',
                            `pay_flag` char(1) NOT NULL COMMENT '付款标志',
                            `pay_money` decimal(10,2) DEFAULT NULL COMMENT '付款金额',
                            `remark` varchar(1024) DEFAULT NULL COMMENT '备注',
                            `completed_date` datetime DEFAULT NULL COMMENT '结算日期',
                            `update_date` datetime NOT NULL COMMENT '修改日期',
                            `deleted` char(255) NOT NULL COMMENT '删除标志',
                            PRIMARY KEY (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 订单详情表
CREATE TABLE `bm_order_detail` (
                                   `id` varchar(36) NOT NULL COMMENT '表主键',
                                   `order_id` varchar(36) NOT NULL COMMENT '订单id',
                                   `product_id` varchar(36) NOT NULL COMMENT '产品id',
                                   `product_num` int NOT NULL COMMENT '产品数量',
                                   `cost_price` decimal(10,2) NOT NULL COMMENT '成本（单个）',
                                   `wage` decimal(10,2) NOT NULL COMMENT '工费（单个）',
                                   `client_id` varchar(36) NOT NULL COMMENT '客户id',
                                   `pay_way` char(1) NOT NULL COMMENT '付款方式',
                                   `pay_flag` char(1) NOT NULL COMMENT '付款标志',
                                   `pay_money` decimal(10,2) DEFAULT NULL COMMENT '付款金额',
                                   `completed_date` datetime DEFAULT NULL COMMENT '结算日期',
                                   `remark` varchar(1024) DEFAULT NULL COMMENT '备注',
                                   `create_date` datetime NOT NULL COMMENT '创建日期',
                                   `update_date` datetime NOT NULL COMMENT '修改日期',
                                   `deleted` char(1) NOT NULL COMMENT '删除标志',
                                   PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 采购表
CREATE TABLE `bm_purchase` (
                               `id` varchar(36) NOT NULL COMMENT '表主键',
                               `client_ids` varchar(1024) NOT NULL COMMENT '客户ids',
                               `purchase_money` decimal(10,2) NOT NULL COMMENT '花费金额',
                               `purchase_date` datetime NOT NULL COMMENT '花费时间',
                               `remark` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '备注',
                               `update_date` datetime NOT NULL COMMENT '修改日期',
                               `deleted` char(1) NOT NULL COMMENT '删除标志',
                               PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 员工表
CREATE TABLE `bm_employee` (
                               `worker_id` varchar(36) NOT NULL COMMENT '员工id',
                               `worker_name` varchar(36) NOT NULL COMMENT '员工姓名',
                               `phone` varchar(11) DEFAULT NULL,
                               `create_date` datetime NOT NULL COMMENT '创建日期',
                               `update_date` datetime NOT NULL COMMENT '修改日期',
                               `deleted` char(1) NOT NULL COMMENT '删除标志',
                               PRIMARY KEY (`worker_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 员工生产记录表
CREATE TABLE `bm_employee_production_record` (
                                                 `id` varchar(36) NOT NULL COMMENT '表主键',
                                                 `worker_id` varchar(36) NOT NULL COMMENT '员工id',
                                                 `product_id` varchar(36) NOT NULL COMMENT '产品id',
                                                 `product_num` int NOT NULL COMMENT '产品数量',
                                                 `completed_date` datetime NOT NULL COMMENT '完成日期',
                                                 `pay_wage_flag` char(1) NOT NULL COMMENT '支付工资标志',
                                                 `remark` varchar(1024) DEFAULT NULL COMMENT '备注',
                                                 `update_date` datetime NOT NULL COMMENT '修改日期',
                                                 `deleted` char(1) NOT NULL COMMENT '删除标志',
                                                 PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 工资记录表
CREATE TABLE `bm_employee_salary_record` (
                                             `id` varchar(36) NOT NULL COMMENT '表主键',
                                             `worker_id` varchar(36) NOT NULL COMMENT '员工id',
                                             `production_ids` varchar(2048) NOT NULL COMMENT '关联生产记录ids',
                                             `wage` decimal(10,2) NOT NULL COMMENT '实付工资',
                                             `wage_date` datetime NOT NULL COMMENT '结算日期',
                                             `remark` varchar(1024) DEFAULT NULL COMMENT '备注',
                                             `update_date` datetime NOT NULL COMMENT '修改日期',
                                             `deleted` char(1) NOT NULL COMMENT '删除标志',
                                             PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 日志表
CREATE TABLE `bm_log` (
                          `id` varchar(36) NOT NULL COMMENT '表主键',
                          `title` varchar(50) NOT NULL COMMENT '标题',
                          `business_type` char(1) NOT NULL COMMENT '业务类型（0列表查询 1表单查询 2新增 3修改 4删除 5其他|）',
                          `oper_id` varchar(36) NOT NULL COMMENT '操作人id',
                          `oper_name` varchar(50) NOT NULL COMMENT '操作人姓名',
                          `oper_ip` varchar(128) NOT NULL COMMENT '操作人ip',
                          `dept_id` varchar(36) NOT NULL COMMENT '部门id',
                          `dept_name` varchar(50) NOT NULL COMMENT '部门名称',
                          PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 字典类型表
CREATE TABLE `bm_dict_type` (
                                `id` varchar(36) NOT NULL COMMENT '表主键',
                                `dict_type` varchar(100) NOT NULL COMMENT '字典类型',
                                `dict_name` varchar(100) NOT NULL COMMENT '字典名称',
                                `order_num` int NOT NULL COMMENT '序号',
                                `remark` varchar(1024) DEFAULT NULL COMMENT '备注',
                                `create_date` datetime NOT NULL COMMENT '创建日期',
                                `update_date` datetime NOT NULL COMMENT '修改日期',
                                `deleted` char(1) NOT NULL COMMENT '删除标志',
                                PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 字典数据表
CREATE TABLE `bm_dict_data` (
                                `id` varchar(36) NOT NULL COMMENT '表主键',
                                `dict_code` varchar(50) NOT NULL COMMENT '字典编码',
                                `dict_name` varchar(50) NOT NULL COMMENT '字典名称',
                                `dict_type_id` varchar(36) NOT NULL COMMENT '父类型',
                                `order_num` int NOT NULL COMMENT '序号',
                                `remark` varchar(1024) DEFAULT NULL COMMENT '备注',
                                `create_date` datetime NOT NULL COMMENT '创建日期',
                                `update_date` datetime NOT NULL COMMENT '修改日期',
                                `deleted` char(1) NOT NULL COMMENT '删除标志',
                                PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 用户表
CREATE TABLE `bm_user` (
                           `user_id` varchar(36) NOT NULL COMMENT '表主键',
                           `dept_id` varchar(36) NOT NULL COMMENT '部门id',
                           `user_name` varchar(36) NOT NULL COMMENT '用户账号',
                           `password` varchar(100) NOT NULL COMMENT '密码',
                           `real_name` varchar(50) NOT NULL COMMENT '真实姓名',
                           `status` char(1) NOT NULL COMMENT '状态（0停用，1正常）',
                           `login_ip` varchar(100) NOT NULL COMMENT '最后登录ip',
                           `remark` varchar(1024) DEFAULT NULL COMMENT '备注',
                           `create_date` datetime NOT NULL COMMENT '创建日期',
                           `update_date` datetime NOT NULL COMMENT '修改日期',
                           `deleted` char(1) NOT NULL COMMENT '删除标志',
                           PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--部门表
CREATE TABLE `bm_dept` (
                           `dept_id` varchar(36) NOT NULL COMMENT '部门id',
                           `dept_name` varchar(50) NOT NULL COMMENT '部门名称',
                           `parent_id` varchar(36) NOT NULL COMMENT '父节点',
                           `order_num` int NOT NULL COMMENT '序号',
                           `leader` varchar(36) DEFAULT NULL COMMENT '负责人',
                           `phone` varchar(11) DEFAULT NULL COMMENT '电话',
                           `create_date` datetime NOT NULL COMMENT '创建日期',
                           `update_date` datetime NOT NULL COMMENT '修改日期',
                           `deleted` char(1) NOT NULL COMMENT '删除标志',
                           PRIMARY KEY (`dept_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 菜单表
CREATE TABLE `bm_menu` (
                           `menu_id` varchar(36) NOT NULL COMMENT '菜单id',
                           `menu_name` varchar(50) NOT NULL COMMENT '菜单名称',
                           `parent_id` varchar(36) NOT NULL COMMENT '父节点',
                           `order_num` int NOT NULL COMMENT '序号',
                           `path` varchar(100) DEFAULT NULL COMMENT '路由地址',
                           `compoent` varchar(255) DEFAULT NULL COMMENT '组件路径',
                           `query` varchar(255) DEFAULT NULL COMMENT '路由参数',
                           `is_frame` char(1) NOT NULL COMMENT '是否为外链',
                           `is_cache` char(1) NOT NULL COMMENT '是否缓存',
                           `menu_type` char(1) NOT NULL COMMENT '菜单类型（M目录 C菜单 F按钮）',
                           `visible` char(1) NOT NULL COMMENT '显示标志',
                           `perms` varchar(100) DEFAULT NULL COMMENT '权限标识',
                           `icon` varchar(255) NOT NULL COMMENT '菜单图标',
                           `remark` varchar(1024) DEFAULT NULL COMMENT '备注',
                           `create_date` datetime NOT NULL COMMENT '创建日期',
                           `update_date` datetime NOT NULL COMMENT '修改日期',
                           `deleted` char(1) NOT NULL COMMENT '删除标志',
                           PRIMARY KEY (`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 角色表
CREATE TABLE `bm_role` (
                           `role_id` varchar(36) NOT NULL COMMENT '角色id',
                           `role_name` varchar(100) NOT NULL COMMENT '角色名称',
                           `role_key` varchar(100) NOT NULL COMMENT '角色编码',
                           `order_num` int NOT NULL COMMENT '序号',
                           `data_scope` char(1) DEFAULT NULL COMMENT '数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）',
                           `menu_check_strictly` tinyint(1) NOT NULL COMMENT '菜单树选择项是否关联显示',
                           `dept_check_strictly` tinyint(1) NOT NULL COMMENT '部门树选择项是否关联显示',
                           `remark` varchar(1024) DEFAULT NULL COMMENT '备注',
                           `create_date` datetime NOT NULL COMMENT '创建日期',
                           `update_date` datetime NOT NULL COMMENT '修改日期',
                           `deletd` char(1) NOT NULL COMMENT '删除标志',
                           PRIMARY KEY (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 角色菜单关联表
CREATE TABLE `bm_role_menu` (
                                `role_id` varchar(36) NOT NULL COMMENT '角色id',
                                `menu_id` varchar(36) NOT NULL COMMENT '菜单id',
                                PRIMARY KEY (`role_id`,`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 用户角色关联表
CREATE TABLE `bm_user_role` (
                                `user_id` varchar(36) NOT NULL COMMENT '用户id',
                                `role_id` varchar(36) NOT NULL COMMENT '角色id',
                                PRIMARY KEY (`user_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
