CREATE TABLE `bm_client` (
                             `client_id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '客户ID',
                             `client_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '客户名',
                             `debt` decimal(6,2) DEFAULT NULL COMMENT '欠款',
                             `phone` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '电话',
                             `address` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '地址',
                             `weight` int NOT NULL COMMENT '权重',
                             `remark` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '备注',
                             `create_date` datetime NOT NULL COMMENT '创建时间',
                             `update_date` datetime NOT NULL COMMENT '修改时间',
                             `deleted` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '删除标志（0:未删除,1:已删除）',
                             PRIMARY KEY (`client_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `bm_employee` (
                               `employee_id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '员工id',
                               `employee_name` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '员工姓名',
                               `phone` varchar(11) DEFAULT NULL,
                               `remark` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '备注',
                               `create_date` datetime NOT NULL COMMENT '创建日期',
                               `update_date` datetime NOT NULL COMMENT '修改日期',
                               `deleted` char(1) NOT NULL COMMENT '删除标志',
                               PRIMARY KEY (`employee_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `bm_make_record` (
                                  `id` varchar(36) NOT NULL COMMENT '主键',
                                  `employee_id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '员工id',
                                  `material_id` varchar(36) NOT NULL COMMENT '商品id',
                                  `num` int NOT NULL COMMENT '数量',
                                  `wage` decimal(5,2) NOT NULL COMMENT '工费',
                                  `complete_date` datetime NOT NULL COMMENT '完成日期',
                                  `wage_flag` char(1) NOT NULL COMMENT '支付工资标志',
                                  `salary_id` varchar(36) NOT NULL COMMENT '工资记录id',
                                  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
                                  `create_date` datetime NOT NULL COMMENT '创建日期',
                                  `update_date` datetime NOT NULL COMMENT '修改日期',
                                  `deleted` char(1) NOT NULL COMMENT '删除标志',
                                  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `bm_material` (
                               `material_id` varchar(36) NOT NULL COMMENT '商品id',
                               `material_name` varchar(50) NOT NULL COMMENT '商品名称',
                               `material_type` char(1) NOT NULL COMMENT '商品类型',
                               `remark` varchar(500) DEFAULT NULL COMMENT '备注',
                               `sell_weight` int NOT NULL COMMENT '销售权重',
                               `buy_weight` int NOT NULL COMMENT '采购权重',
                               `make_weight` int NOT NULL COMMENT '制作权重',
                               `create_date` datetime NOT NULL COMMENT '创建日期',
                               `update_date` datetime NOT NULL COMMENT '修改日期',
                               `deleted` char(1) NOT NULL COMMENT '删除标志',
                               PRIMARY KEY (`material_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `bm_order` (
                            `order_id` varchar(36) NOT NULL COMMENT '订单id',
                            `order_name` varchar(200) NOT NULL COMMENT '订单名称',
                            `order_type` char(1) NOT NULL COMMENT '订单类型',
                            `dealer_id` varchar(36) NOT NULL COMMENT '交易人员id',
                            `order_date` datetime NOT NULL COMMENT '订单日期',
                            `pay_flag` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '付款标志',
                            `remark` varchar(500) DEFAULT NULL COMMENT '备注',
                            `create_date` datetime NOT NULL COMMENT '创建日期',
                            `update_date` datetime NOT NULL COMMENT '修改日期',
                            `deleted` char(1) NOT NULL COMMENT '删除标志',
                            PRIMARY KEY (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `bm_order_detail` (
                                   `id` varchar(36) NOT NULL COMMENT '采购记录id',
                                   `material_id` varchar(36) NOT NULL COMMENT '商品id',
                                   `num` int NOT NULL COMMENT '数量',
                                   `money` decimal(6,2) NOT NULL COMMENT '金额',
                                   `remark` varchar(500) DEFAULT NULL COMMENT '备注',
                                   `order_id` varchar(36) NOT NULL COMMENT '订单id',
                                   `create_date` datetime NOT NULL COMMENT '创建日期',
                                   `update_date` datetime NOT NULL COMMENT '修改日期',
                                   `deleted` char(1) NOT NULL COMMENT '删除标志',
                                   PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `bm_return_record` (
                                    `id` varchar(36) NOT NULL COMMENT '主键',
                                    `client_id` varchar(36) NOT NULL COMMENT '客户id',
                                    `material_id` varchar(36) NOT NULL COMMENT '商品id',
                                    `order_id` varchar(36) NOT NULL COMMENT '订单id',
                                    `num` int NOT NULL COMMENT '数量',
                                    `moeny` decimal(6,2) DEFAULT NULL COMMENT '金额',
                                    `return_date` datetime NOT NULL COMMENT '退货日期',
                                    `remark` varchar(500) DEFAULT NULL COMMENT '备注',
                                    `create_date` datetime NOT NULL COMMENT '创建日期',
                                    `update_date` datetime NOT NULL COMMENT '修改日期',
                                    `deleted` char(1) NOT NULL COMMENT '删除标志',
                                    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `bm_salary_record` (
                                    `id` varchar(36) NOT NULL COMMENT '主键',
                                    `salary` decimal(6,2) NOT NULL COMMENT '工资',
                                    `salary_date` datetime NOT NULL COMMENT '日期',
                                    `remark` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '备注',
                                    `create_date` datetime NOT NULL COMMENT '创建日期',
                                    `update_date` datetime NOT NULL COMMENT '修改日期',
                                    `deleted` char(1) NOT NULL COMMENT '删除标志',
                                    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `bm_supplier` (
                               `supplier_id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '表主键',
                               `supplier_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '供应商名称',
                               `phone` varchar(11) DEFAULT NULL COMMENT '电话',
                               `address` varchar(128) DEFAULT NULL COMMENT '地址',
                               `remark` varchar(500) DEFAULT NULL COMMENT '备注',
                               `weight` int NOT NULL COMMENT '权重',
                               `create_date` datetime NOT NULL COMMENT '创建时间',
                               `update_date` datetime NOT NULL COMMENT '修改时间',
                               `deleted` char(1) NOT NULL COMMENT '删除标志',
                               PRIMARY KEY (`supplier_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `bm_transfer_record` (
                                      `id` varchar(36) NOT NULL COMMENT '表主键',
                                      `client_id` varchar(36) NOT NULL COMMENT '客户id',
                                      `transfer_money` decimal(10,2) NOT NULL COMMENT '转账金额',
                                      `transfer_way` char(1) NOT NULL COMMENT '转账日期',
                                      `transfer_date` datetime(1) NOT NULL COMMENT '转账方式',
                                      `remark` varchar(500) DEFAULT NULL COMMENT '备注',
                                      `create_date` datetime NOT NULL COMMENT '创建日期',
                                      `update_date` datetime NOT NULL COMMENT '修改日期',
                                      `deleted` char(1) NOT NULL COMMENT '删除标志',
                                      PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

