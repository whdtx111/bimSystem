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

 Date: 11/07/2024 15:18:20
*/


-- ----------------------------
-- Table structure for sp_meta
-- ----------------------------
DROP TABLE IF EXISTS "public"."sp_meta";
CREATE TABLE "public"."sp_meta" (
  "id" varchar(36) COLLATE "pg_catalog"."default" NOT NULL,
  "guide" varchar(255) COLLATE "pg_catalog"."default",
  "name" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "val" varchar(255) COLLATE "pg_catalog"."default",
  "val_range" varchar(255) COLLATE "pg_catalog"."default",
  "data_type" varchar(50) COLLATE "pg_catalog"."default",
  "units" varchar(20) COLLATE "pg_catalog"."default",
  "modify_time" timestamp(0) NOT NULL,
  "modify_user" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "status" int4 NOT NULL
)
;
COMMENT ON COLUMN "public"."sp_meta"."id" IS 'id';
COMMENT ON COLUMN "public"."sp_meta"."guide" IS '模板元数据说明书';
COMMENT ON COLUMN "public"."sp_meta"."name" IS '元数据名称';
COMMENT ON COLUMN "public"."sp_meta"."val" IS '值';
COMMENT ON COLUMN "public"."sp_meta"."val_range" IS '值范围';
COMMENT ON COLUMN "public"."sp_meta"."data_type" IS '数据类型';
COMMENT ON COLUMN "public"."sp_meta"."units" IS '计量单位';

-- ----------------------------
-- Primary Key structure for table sp_meta
-- ----------------------------
ALTER TABLE "public"."sp_meta" ADD CONSTRAINT "sp_meta_pkey" PRIMARY KEY ("id");
