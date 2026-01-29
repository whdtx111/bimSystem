/*
 Navicat Premium Data Transfer

 Source Server         : 10.5.58.132,32382,speckle,speckle
 Source Server Type    : PostgreSQL
 Source Server Version : 160001 (160001)
 Source Host           : 10.5.58.132:32382
 Source Catalog        : speckle
 Source Schema         : public

 Target Server Type    : PostgreSQL
 Target Server Version : 160001 (160001)
 File Encoding         : 65001

 Date: 06/12/2023 14:54:07
*/


-- ----------------------------
-- Table structure for sp_obj_snapshot
-- ----------------------------
DROP TABLE IF EXISTS "public"."sp_obj_snapshot";
CREATE TABLE "public"."sp_obj_snapshot" (
  "id" int8 NOT NULL,
  "user_id" varchar(15) COLLATE "pg_catalog"."default",
  "snapshot_name" varchar(45) COLLATE "pg_catalog"."default",
  "create_user" int8,
  "create_dept" int8,
  "create_time" timestamp(6),
  "update_user" int8,
  "update_time" timestamp(6),
  "status" int4,
  "is_deleted" int4 DEFAULT 0,
  "model_data" text COLLATE "pg_catalog"."default",
  "color_data" text COLLATE "pg_catalog"."default",
  "remark" varchar(255) COLLATE "pg_catalog"."default",
  "stream_id" varchar(15) COLLATE "pg_catalog"."default",
  "commit_id" varchar(15) COLLATE "pg_catalog"."default"
)
;
ALTER TABLE "public"."sp_obj_snapshot" OWNER TO "speckle";
COMMENT ON COLUMN "public"."sp_obj_snapshot"."snapshot_name" IS '快照名称';
COMMENT ON COLUMN "public"."sp_obj_snapshot"."model_data" IS '模型数据';
COMMENT ON COLUMN "public"."sp_obj_snapshot"."color_data" IS '颜色数据';
COMMENT ON COLUMN "public"."sp_obj_snapshot"."remark" IS '备注';
COMMENT ON TABLE "public"."sp_obj_snapshot" IS '模型 色值 和 剖切快照';

-- ----------------------------
-- Primary Key structure for table sp_obj_snapshot
-- ----------------------------
ALTER TABLE "public"."sp_obj_snapshot" ADD CONSTRAINT "sp_snapshot_copy1_pkey" PRIMARY KEY ("id");
