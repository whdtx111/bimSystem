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

 Date: 24/11/2023 15:25:50
*/


-- ----------------------------
-- Table structure for sp_snapshot
-- ----------------------------
DROP TABLE IF EXISTS "public"."sp_snapshot";
CREATE TABLE "public"."sp_snapshot" (
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
  "snapshot_type" varchar(45) COLLATE "pg_catalog"."default",
  "color_value" varchar(25) COLLATE "pg_catalog"."default",
  "remark" varchar(255) COLLATE "pg_catalog"."default",
  "obj_id" varchar(255) COLLATE "pg_catalog"."default",
  "stream_id" varchar(15) COLLATE "pg_catalog"."default",
  "commit_id" varchar(15) COLLATE "pg_catalog"."default",
  "snapshot_uid" varchar(255) COLLATE "pg_catalog"."default"
)
;
ALTER TABLE "public"."sp_snapshot" OWNER TO "speckle";
COMMENT ON COLUMN "public"."sp_snapshot"."snapshot_name" IS '快照名称';
COMMENT ON COLUMN "public"."sp_snapshot"."snapshot_type" IS '1-着色,2-属性';
COMMENT ON COLUMN "public"."sp_snapshot"."color_value" IS '色值';
COMMENT ON COLUMN "public"."sp_snapshot"."remark" IS '备注';
COMMENT ON COLUMN "public"."sp_snapshot"."snapshot_uid" IS '快照id';
COMMENT ON TABLE "public"."sp_snapshot" IS '快照';

-- ----------------------------
-- Primary Key structure for table sp_snapshot
-- ----------------------------
ALTER TABLE "public"."sp_snapshot" ADD CONSTRAINT "sp_snapshot_pkey" PRIMARY KEY ("id");
