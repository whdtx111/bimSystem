/*
 Navicat Premium Data Transfer

 Source Server         : speckle
 Source Server Type    : PostgreSQL
 Source Server Version : 160001 (160001)
 Source Host           : 10.5.58.132:30070
 Source Catalog        : speckle
 Source Schema         : public

 Target Server Type    : PostgreSQL
 Target Server Version : 160001 (160001)
 File Encoding         : 65001

 Date: 11/07/2024 15:18:05
*/


-- ----------------------------
-- Table structure for sp_lib
-- ----------------------------
DROP TABLE IF EXISTS "public"."sp_lib";
CREATE TABLE "public"."sp_lib" (
  "id" varchar(36) COLLATE "pg_catalog"."default" NOT NULL,
  "name" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "units" varchar(50) COLLATE "pg_catalog"."default",
  "version" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
  "category" varchar(255) COLLATE "pg_catalog"."default",
  "parameter_type" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "data_type" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "parameters" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "detail" varchar(255) COLLATE "pg_catalog"."default",
  "modify_time" timestamp(6),
  "modify_user" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "status" int4 NOT NULL,
  "bim_type" varchar(255) COLLATE "pg_catalog"."default",
  "type" varchar(255) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."sp_lib"."name" IS '名称';
COMMENT ON COLUMN "public"."sp_lib"."units" IS '单位';
COMMENT ON COLUMN "public"."sp_lib"."version" IS '版本';
COMMENT ON COLUMN "public"."sp_lib"."category" IS '类别';
COMMENT ON COLUMN "public"."sp_lib"."parameter_type" IS '参数类型';
COMMENT ON COLUMN "public"."sp_lib"."data_type" IS '数据类型';
COMMENT ON COLUMN "public"."sp_lib"."parameters" IS '参数组';
COMMENT ON COLUMN "public"."sp_lib"."detail" IS '描述';
COMMENT ON COLUMN "public"."sp_lib"."modify_time" IS '修改时间';
COMMENT ON COLUMN "public"."sp_lib"."modify_user" IS '修改人';
COMMENT ON COLUMN "public"."sp_lib"."status" IS '状态';

-- ----------------------------
-- Primary Key structure for table sp_lib
-- ----------------------------
ALTER TABLE "public"."sp_lib" ADD CONSTRAINT "sp_lib_pkey" PRIMARY KEY ("id");
