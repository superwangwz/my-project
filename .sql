CREATE
DATABASE /*!32312 IF NOT EXISTS*/`pass` /*!40100 DEFAULT CHARACTER SET utf8 */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE
`pass`;

/*Table structure for table `user` */

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user`
(
    `id`        varchar(50) NOT NULL COMMENT '主键',
    `user_name` varchar(50)  DEFAULT NULL COMMENT '用户名',
    `real_name` varchar(50)  DEFAULT NULL COMMENT '真实姓名',
    `pass_word` varchar(50)  DEFAULT NULL COMMENT '密码',
    `key`       varchar(100) DEFAULT NULL COMMENT '密钥',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

CREATE TABLE `file`
(
    `id`          varchar(50) NOT NULL COMMENT '主键',
    `data_id`     varchar(50)  DEFAULT NULL COMMENT '业务id',
    `name`        varchar(200) DEFAULT NULL COMMENT '文件名',
    `suffix`      varchar(50)  DEFAULT NULL COMMENT '文件后缀',
    `create_by`   varchar(50)  DEFAULT NULL COMMENT '创建人',
    `create_time` datetime     DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

