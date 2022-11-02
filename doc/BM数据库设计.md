#### 数据库设计

##### 1. 客户表（bm_client）

| 序号 | 字段名      | 描述     | 类型         | 允许空 | 主键 | 备注        |
| ---- | ----------- | -------- | ------------ | ------ | ---- | ----------- |
| 1    | client_id   | 客户id   | varchar(36)  | N      | Y    |             |
| 2    | client_name | 客户名   | varchar(50)  | N      |      |             |
| 3    | address     | 地址     | varchar(128) |        |      |             |
| 4    | phone       | 电话     | varchar(11)  |        |      |             |
| 5    | client_flag | 客户标识 | char(1)      | N      |      | 0销售 1采购 |
|      | weight      | 权重     | int          | N      |      |             |
| 6    | create_date | 创建日期 | datetime     | N      |      |             |
| 7    | update_date | 修改日期 | datetime     | N      |      |             |
| 8    | deleted     | 删除标志 | char(1)      | N      |      |             |

 

##### 2. 客户流动资金表(bm_client_circular_money)

| 序号 | 字段名       | 描述         | 类型          | 允许空 | 主键 | 备注 |
| ---- | ------------ | ------------ | ------------- | ------ | ---- | ---- |
|      | id           | 表主键       | varchar(36)   | N      | Y    |      |
| 1    | client_id    | 客户id       | varchar(36)   | N      |      |      |
| 2    | circular_money | 实时金额     | decimal(10,2) | N      |      |      |
| 3    | debt         | 欠款         | decimal(10,2) | N      |      |      |
| 4    | last_circular_date  | 最后转入时间 | datetime      |        |      |      |

 

##### 3. 客户转账记录表（bm_client_pay_record）

| 序号 | 字段名     | 描述     | 类型          | 允许空 | 主键 | 备注 |
| ---- | ---------- | -------- | ------------- | ------ | ---- | ---- |
|      | id         | 表主键   | varchar(36)   | N      | Y    |      |
| 1    | client_id  | 客户id   | varchar(36)   | N      |      |      |
| 2    | pay_amount | 转账金额 | decimal(10,2) | N      |      |      |
| 3    | pay_date   | 转账日期 | datetime      | N      |      |      |
| 4    | pay_way    | 转账方式 | char(1)       | N      |      |      |

 

##### 4. 产品表(bm_product)

| 序号 | 字段名       | 描述     | 类型          | 允许空 | 主键 | 备注     |
| ---- | ------------ | -------- | ------------- | ------ | ---- | -------- |
| 1    | product_id   | 表主键   | varchar(36)   | N      | Y    |          |
| 2    | product_name | 产品名   | varchar(50)   | N      |      |          |
| 3    | price        | 售价     | decimal(10,2) | N      |      |          |
| 4    | cost_price   | 总成本   | decimal(10,2) | N      |      | 包含工费 |
| 5    | wage         | 工费     | decimal(10,2) | N      |      |          |
|      | weight       | 权重     | int           | N      |      |          |
| 6    | create_date  | 创建日期 | datetime      | N      |      |          |
| 7    | update_date  | 修改日期 | datetime      | N      |      |          |
| 8    | deleted      | 删除标志 | char(1)       | N      |      |          |

 

##### 5. 订单表(bm_order)

| 序号 | 字段名         | 描述     | 类型          | 允许空 | 主键 | 备注   |
| ---- | -------------- | -------- | ------------- | ------ | ---- | ------ |
| 1    | order_id       | 订单id   | varchar(36)   | N      | Y    | 订单号 |
| 2    | order_date     | 订单日期 | datetime      | N      |      |        |
|      | pay_flag       | 付款标志 | char(1)       | N      |      |        |
| 4    | pay_money      | 付款金额 | decimal(10,2) |        |      |        |
| 5    | remark         | 备注     | varchar(1024) |       |      |        |
|      | completed_date | 结算日期 | datetime      |        |      |        |
|      | update_date    | 修改日期 | datetime      | N      |      |        |
|      | deleted        | 删除标志 | char(1)       | N      |      |        |

 

##### 6. 订单详情表(bm_order_detail)

| 序号 | 字段名         | 描述     | 类型          | 允许空 | 主键 | 备注 |
| ---- | -------------- | -------- | ------------- | ------ | ---- | ---- |
| 1    | id             | 表主键   | varchar(36)   | N      | Y    |      |
| 2    | order_id       | 订单号   | varchar(36)   | N      |      |      |
| 3    | product_id     | 产品id   | varchar(36)   | N      |      |      |
| 4    | product_num    | 产品数量 | int           | N      |      |      |
|      | cost_price     | 成本   | decimal(10,2) | N      |      |      |
|      | wage           | 工费     | decimal(10,2) | N      |      |      |
| 5    | client_id      | 客户id   | varchar(36)   | N      |      |      |
|      | pay_way        | 付款方式 | varchar(36)   | N      |      |      |
|      | pay_flag       | 付款标志 | char(1)       | N      |      |      |
|      | pay_money      | 实付金额 | decimal(10,2) |        |      |      |
|      | completed_date | 结算日期 | datetime      |        |      |      |
|      | remark         | 备注     | varchar(1024) |        |      |      |
|      | create_date    | 创建日期 | datetime      | N      |      |      |
|      | update_date    | 修改日期 | datetime      | N      |      |      |
|      | deleted        | 删除标志 | char(1)       | N      |      |      |

 

##### 7. 采购表(bm_purchase)

| 序号 | 字段名         | 描述     | 类型          | 允许空 | 主键 | 备注 |
| ---- | -------------- | -------- | ------------- | ------ | ---- | ---- |
| 1    | id             | 表主键   | varchar(36)   | N      | Y    |      |
| 2    | client_ids     | 客户ids  | varchar(1024) | N      |      |      |
| 3    | purchase_money | 花费金额 | decimal(10,2) | N      |      |      |
|      | purchase_date  | 采购日期 | datetime      | N      |      |      |
|      | remark         | 备注    | varchar(1024)  | N      |      |      |
|      | update_date    | 修改日期 | datetime      | N      |      |      |
|      | deleted        | 删除标志 | char(1)       | N      |      |      |

 

##### 8. 员工表(bm_employee)

| 序号 | 字段名      | 描述     | 类型        | 允许空 | 主键 | 备注 |
| ---- | ----------- | -------- | ----------- | ------ | ---- | ---- |
| 1    | worker_id   | 员工id   | varchar(36) | N      | Y    |      |
| 2    | worker_name | 员工名   | varchar(50) | N      |      |      |
| 3    | phone       | 电话     | varchar(11) |        |      |      |
| 4    | create_date | 创建日期 | datetime    | N      |      |      |
|      | update_date | 修改日期 | datetime    | N      |      |      |
|      | deleted     | 删除标志 | char(1)     | N      |      |      |

 

##### 9. 员工生产记录表（bm_employee_production_record）

| 序号 | 字段名         | 描述         | 类型          | 允许空 | 主键 | 备注 |
| ---- | -------------- | ------------ | ------------- | ------ | ---- | ---- |
| 1    | id             | 表主键       | varchar(36)   | N      | Y    |      |
| 2    | worker_id      | 员工id       | varchar(36)   | N      |      |      |
| 3    | product_id     | 产品id       | varchar(36)   | N      |      |      |
| 4    | product_num    | 产品数量     | int           | N      |      |      |
|      | completed_date | 完成日期     | datetime      | N      |      |      |
|      | pay_wage_flag  | 支付工资标志 | char(1)       | N      |      |      |
|      | remark         | 备注         | varchar(1024) |        |      |      |
|      | update_date    | 修改日期     | datetime      | N      |      |      |
|      | deleted        | 删除标志     | char(1)       | N      |      |      |

 

##### 10. 工资记录表（bm_employee_salary_reocrd）

| 序号 | 字段名      | 描述            | 类型          | 允许空 | 主键 | 备注 |
| ---- | ----------- | --------------- | ------------- | ------ | ---- | ---- |
| 1    | id          | 表主键          | varchar(36)   | N      | Y    |      |
| 2    | worker_id   | 员工id          | varchar(36)   | N      |      |      |
|      | production_ids | 关联生产记录ids | varchar(2048) | N      |      |      |
|      | wage        | 实付工资        | Decimal(10,2) | N      |      |      |
|      | wage_date   | 结算日期        | datetime      | N       |      |      |
| 4    | remark      | 备注            | varchar(1024) |        |      |      |
|      | update_date | 修改日期        | datetime      | N      |      |      |
|      | deleted     | 删除标志        | char(1)       | N      |      |      |

 

##### 11. 日志表(bm_log)

| 序号 | 字段名        | 描述       | 类型         | 允许空 | 主键 | 备注                    |
| ---- | ------------- | ---------- | ------------ | ------ | ---- | ----------------------- |
| 1    | id            | 表主键     | varchar(36)  | N      | Y    |                         |
|      | title         | 模块名称   | varchar(50)  | N      |      |                         |
|      | business_type | 业务类型   | char(1)      | N      |      | 0列表查询 1表单查询 2新增 3修改 4删除 5其他|
|      | oper_id       | 操作人id   | varchar(36)  | N      |      |                         |
|      | oper_name     | 操作人名称 | varchar(50)  | N      |      |                         |
|      | oper_date     | 操作日期   | datetime     | N      |      |                         |
|      | oper_ip       | 操作人ip   | varchar(128) | N      |      |                         |
|      | dept_id       | 部门id     | varchar(36)  | N      |      |                         |
|      | dept_name     | 部门名称   | varchar(50)  | N      |      |                         |

 

##### 12. 字典类型表(bm_dict_type)

| 序号 | 字段名      | 描述     | 类型          | 允许空 | 主键 | 备注       |
| ---- | ----------- | -------- | ------------- | ------ | ---- | ---------- |
| 1    | id          | 表主键   | varchar(36)   | N      | Y    |            |
| 2    | dict_type   | 字典类型 | varchar(100)  | N      |      |            |
|      | dict_name   | 字典名称 | varchar(100)  | N      |      |            |
|      | status      | 状态     | char(1)       | N      |      | 0关闭1开启 |
|      | order_num   | 序号     | int           | N      |      |            |
|      | remark      | 备注     | varchar(1024) |        |      |            |
|      | create_date | 创建日期 | datetime      | N      |      |            |
|      | update_date | 修改日期 | datetime      | N      |      |            |
|      | deleted     | 删除标志 | char(1)       | N      |      |            |

##### 13. 字典数据表(bm_dict_data)

| 序号 | 字段名      | 描述         | 类型          | 允许空 | 主键 | 备注       |
| ---- | ----------- | ------------ | ------------- | ------ | ---- | ---------- |
| 1    | id          | 表主键       | varchar(36)   | N      | Y    |            |
| 2    | dict_code   | 字典编码     | varchar(50)   | N      |      |            |
|      | dict_name   | 字典名称     | varchar(50)   | N      |      |            |
|      | table_type  | 父类型       | varchar(36)   | N      |      |            |
|      | order_num   | 序号         | Int           | N      |      |            |
|      | status      | 状态         | char(1)       |        |      | 0关闭1开启 |
|      | remark      | 备注         | Varchar(1024) |        |      |            |
|      | list_class  | 表单回显样式 | Varchar(100)  |        |      |            |
|      | css_class   | 自定义样式   | Varchar(1024) |        |      |            |
|      | create_date | 创建日期     | datetime      | N      |      |            |
|      | update_date | 修改日期     | datetime      | N      |      |            |
|      | deleted     | 删除标志     | char(1)       | N      |      |            |

 

##### 14. 用户表(bm_user)

| 序号 | 字段名      | 描述       | 类型          | 允许空 | 主键 | 备注       |
| ---- | ----------- | ---------- | ------------- | ------ | ---- | ---------- |
| 1    | user_id     | 表主键     | varchar(36)   | N      | Y    |            |
|      | dept_id     | 部门id   | varchar(36)   | N      |      |            |
|      | user_name   | 用户账号   | varchar(30)   | N      |      |            |
|      | password    | 密码       | varchar(100)  | N      |      |            |
|      | real_name   | 真实姓名    | varchar(50)  | N      |      |            |
|      | status      | 账号状态   | char(1)       | N      |      | 0停用1正常 |
|      | login_ip    | 最后登陆ip | varchar(100)  | N      |      |            |
|      | remark      | 备注       | varchar(1024) |        |      |            |
|      | create_date | 创建日期   | datetime      | N      |      |            |
|      | update_date | 修改日期   | datetime      | N      |      |            |
|      | deleted     | 删除标志   | char(1)       | N      |      |            |

 

##### 15. 部门表(bm_dept)

| 序号 | 字段名      | 描述     | 类型        | 允许空 | 主键 | 备注       |
| ---- | ----------- | -------- | ----------- | ------ | ---- | ---------- |
| 1    | dept_id     | 表主键   | varchar(36) | N      | Y    |            |
|      | dept_name   | 部门名称 | varchar(50) | N      |      |            |
|      | parent_id   | 父节点   | varchar(36) | N      |      |            |
|      | order_num   | 序号     | int         | N      |      |            |
|      | leader      | 负责人   | varchar(36) |        |      |            |
|      | phone       | 电话     | varchar(11) |        |      |            |
|      | status      | 账号状态 | char(1)     | N      |      | 0停用1正常 |
|      | create_date | 创建日期 | datetime    | N      |      |            |
|      | update_date | 修改日期 | datetime    | N      |      |            |
|      | deleted     | 删除标志 | char(1)     | N      |      |            |

 

##### 16. 菜单表(bm_menu)

| 序号 | 字段名      | 描述       | 类型          | 允许空 | 主键 | 备注              |
| ---- | ----------- | ---------- | ------------- | ------ | ---- | ----------------- |
| 1    | menu_id     | 菜单ID     | varchar(36)   | N      | Y    |                   |
|      | menu_name   | 菜单名称   | varchar(50)   | N      |      |                   |
|      | parent_id   | 父节点     | varchar(36)   | N      |      |                   |
|      | order_num   | 序号       | int           | N      |      |                   |
|      | path        | 路由地址   | varchar(100)  |        |      |                   |
|      | componet   | 组件路径   | varchar(255)  |        |      |                   |
|      | query       | 路由参数   | varchar(255)  |        |      |                   |
|      | is_frame    | 是否为外链 | char(1)       | N      |      |                   |
|      | is_cache    | 是否缓存   | char(1)       | N      |      |                   |
|      | menu_type   | 菜单类型   | char(1)       | N      |      | M目录 C菜单 F按钮 |
|      | visible     | 显示标志   | char(1)       | N      |      |                   |
|      | status      | 菜单状态   | char(1)       | N      |      | 0停用1正常        |
|      | perms       | 权限标识   | varchar(100)  |        |      |                   |
|      | icon        | 菜单图标   | varchar(100)  | N      |      |                   |
|      | remark      | 备注       | varchar(1024) |        |      |                   |
|      | create_date | 创建日期   | datetime      | N      |      |                   |
|      | update_date | 修改日期   | datetime      | N      |      |                   |
|      | deletde     | 删除标志   | char(1)       | N      |      |                   |

 

##### 17. 角色表(bm_role)

| 序号 | 字段名              | 描述                     | 类型          | 允许空 | 主键 | 备注                                                         |
| ---- | ------------------- | ------------------------ | ------------- | ------ | ---- | ------------------------------------------------------------ |
| 1    | role_id             | 角色id                   | varchar(36)   | N      | Y    |                                                              |
|      | role_name           | 角色名称                 | varchar(100)  | N      |      |                                                              |
|      | role_key            | 角色编码                 | varchar(100)  | N      |      |                                                              |
|      | oder_num           | 序号                     | int           | N      |      |                                                              |
|      | data_scope          | 数据范围                 | char(1)      |        |      | 1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限 |
|      | menu_check_strictly | 菜单树选择项是否关联显示 | tinyint(1)    | N      |      |                                                              |
|      | dept_check_strictly | 部门树选择项是否关联显示 | tinyint(1)    | N      |      |                                                              |
|      | status              | 角色状态                 | char(1)       | N      |      | 0停用 1正常                                                  |
|      | remark              | 备注                     | varchar(1024) |        |      |                                                              |
|      | create_date         | 创建日期                 | datetime      | N      |      |                                                              |
|      | update_date         | 修改日期                 | datetime      | N      |      |                                                              |
|      | delted              | 删除标志                 | char(1)       | N      |      |                                                              |

 

##### 18. 角色菜单关联表(bm_role_menu)

| 序号 | 字段名  | 描述   | 类型        | 允许空 | 主键 | 备注 |
| ---- | ------- | ------ | ----------- | ------ | ---- | ---- |
| 1    | role_id | 角色id | varchar(36) | N      | Y    |      |
|      | menu_id | 菜单id | varchar(36) | N      | Y    |      |

 

##### 19. 用户角色表(bm_user_role)

| 序号 | 字段名  | 描述   | 类型        | 允许空 | 主键 | 备注 |
| ---- | ------- | ------ | ----------- | ------ | ---- | ---- |
| 1    | user_id | 用户id | varchar(36) | N      | Y    |      |
|      | role_id | 角色id | varchar(36) | N      | Y    |      |

 