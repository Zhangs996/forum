SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for comment
-- ----------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `user_id` int(0) NULL DEFAULT NULL,
  `entity_type` int(0) NULL DEFAULT NULL,
  `entity_id` int(0) NULL DEFAULT NULL,
  `target_id` int(0) NULL DEFAULT NULL,
  `content` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `status` int(0) NULL DEFAULT NULL,
  `create_time` timestamp(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_user_id`(`user_id`) USING BTREE,
  INDEX `index_entity_id`(`entity_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 266 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for discuss_post
-- ----------------------------
DROP TABLE IF EXISTS `discuss_post`;
CREATE TABLE `discuss_post`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '�û�id',
  `title` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '����',
  `content` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '����',
  `type` int(0) NULL DEFAULT NULL COMMENT '0-��ͨ; 1-�ö�;',
  `status` int(0) NULL DEFAULT NULL COMMENT '0-����; 1-����; 2-����;',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '����ʱ��',
  `comment_count` int(0) NULL DEFAULT NULL COMMENT '������',
  `score` double NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 297 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for login_ticket
-- ----------------------------
DROP TABLE IF EXISTS `login_ticket`;
CREATE TABLE `login_ticket`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `user_id` int(0) NOT NULL,
  `ticket` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `status` int(0) NULL DEFAULT 0 COMMENT '0-��Ч; 1-��Ч;',
  `expired` timestamp(0) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_ticket`(`ticket`(20)) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 40 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for message
-- ----------------------------
DROP TABLE IF EXISTS `message`;
CREATE TABLE `message`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `from_id` int(0) NULL DEFAULT NULL COMMENT '��Ϣ�ķ����û�',
  `to_id` int(0) NULL DEFAULT NULL COMMENT '��Ϣ�Ľ����û�',
  `conversation_id` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '�Ự id',
  `content` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '��Ϣ����',
  `status` int(0) NULL DEFAULT NULL COMMENT '0-δ��;1-�Ѷ�;2-ɾ��;',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '����ʱ��',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_from_id`(`from_id`) USING BTREE,
  INDEX `index_to_id`(`to_id`) USING BTREE,
  INDEX `index_conversation_id`(`conversation_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 371 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `password` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `salt` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `email` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `type` int(0) NULL DEFAULT NULL COMMENT '0-��ͨ�û�; 1-��������Ա; 2-����;',
  `status` int(0) NULL DEFAULT NULL COMMENT '0-δ����; 1-�Ѽ���;',
  `activation_code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `header_url` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_time` timestamp(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_username`(`username`(20)) USING BTREE,
  INDEX `index_email`(`email`(20)) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 166 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;

truncate table user;
truncate table discuss_post; 
