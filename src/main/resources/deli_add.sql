DROP TABLE IF EXISTS `deli_voucher_order`;
CREATE TABLE `deli_voucher_order`
(
    `id`	bigint(20) NOT NULL COMMENT '主键',
    `user_id`	int(64) unsigned NOT NULL COMMENT '下单的用户id',
    `voucher_id`	bigint(20) unsigned NOT NULL COMMENT '购买的代金券id',
    `pay_type`	tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '支付方式 1：余额支付；2：支付宝；3：微信',
    `status`	tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '订单状态 1：未支付；2：已支付；3：已核销；4：已取消',
    `create_time`	timestamp NOT NULL COMMENT '下单时间',
    `pay_time`	timestamp NOT NULL COMMENT '支付时间',
    `use_time`	timestamp NOT NULL COMMENT '核销时间',
    `refund_time`	timestamp NOT NULL COMMENT '退款时间',
    `update_time`	timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    primary key (`id`)
);

DROP TABLE IF EXISTS `deli_seckill_voucher`;
CREATE TABLE `deli_seckill_voucher`
(
    `voucher_id`  bigint(20) UNSIGNED NOT NULL COMMENT '关联的优惠券的id',
    `stock`       int(8) NOT NULL COMMENT '库存',
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `begin_time`  timestamp NOT NULL COMMENT '生效时间',
    `end_time`    timestamp NOT NULL COMMENT '失效时间',
    `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`voucher_id`) USING BTREE
);

DROP TABLE IF EXISTS `deli_voucher`;
CREATE TABLE `deli_voucher`
(
    `id`           bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
    `product_id`      int(64) UNSIGNED NULL DEFAULT NULL COMMENT '商品id',
    `title`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '代金券标题',
    `sub_title`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '副标题',
    `rules`        varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '使用规则',
    `stock`       int(8) NOT NULL COMMENT '库存',
    `pay_value`    bigint(10) UNSIGNED NOT NULL COMMENT '支付金额，单位是分。例如200代表2元',
    `actual_value` bigint(10) NOT NULL COMMENT '抵扣金额，单位是分。例如200代表2元',
    `type`         tinyint(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '0,普通券；1,秒杀券',
    `status`       tinyint(1) UNSIGNED NOT NULL DEFAULT 1 COMMENT '1,上架; 2,下架; 3,过期',
    `begin_time`  timestamp NOT NULL COMMENT '生效时间',
    `end_time`    timestamp NOT NULL COMMENT '失效时间',
    `create_time`  timestamp  NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`  timestamp  NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
);