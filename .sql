/*
SQLyog Ultimate v11.13 (64 bit)
MySQL - 8.0.28 : Database - pass
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE
DATABASE /*!32312 IF NOT EXISTS*/`pass` /*!40100 DEFAULT CHARACTER SET utf8 */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE
`pass`;

/*Table structure for table `file` */

DROP TABLE IF EXISTS `file`;

CREATE TABLE `file`
(
    `id`          varchar(50) NOT NULL COMMENT '主键',
    `type`        varchar(20)  DEFAULT NULL COMMENT '加密类型',
    `data_id`     varchar(50)  DEFAULT NULL COMMENT '业务id',
    `name`        varchar(200) DEFAULT NULL COMMENT '文件名',
    `suffix`      varchar(50)  DEFAULT NULL COMMENT '文件后缀',
    `create_by`   varchar(50)  DEFAULT NULL COMMENT '创建人',
    `create_time` datetime     DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

/*Data for the table `file` */

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

/*Data for the table `user` */

insert into `user`(`id`, `user_name`, `real_name`, `pass_word`, `key`)
values ('a5b6b2eb-81bd-4d9a-b280-6bf696133427', 'wwz', 'wwz', '-10007', NULL);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
