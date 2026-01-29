/*
 Navicat Premium Data Transfer

 Source Server         : 10.5.56.20_sp_psql
 Source Server Type    : PostgreSQL
 Source Server Version : 140005
 Source Host           : 10.5.56.20:5432
 Source Catalog        : speckle
 Source Schema         : public

 Target Server Type    : PostgreSQL
 Target Server Version : 140005
 File Encoding         : 65001

 Date: 03/08/2023 15:14:30
*/


-- ----------------------------
-- Table structure for sp_file_common
-- ----------------------------
DROP TABLE IF EXISTS "public"."sp_file_common";
CREATE TABLE "public"."sp_file_common" (
  "id" int8 NOT NULL,
  "create_user" int8,
  "create_dept" int8,
  "create_time" timestamp(6),
  "update_user" int8,
  "update_time" timestamp(6),
  "status" int4,
  "is_deleted" int4 DEFAULT 0,
  "streams_id" varchar(64) COLLATE "pg_catalog"."default",
  "objects_id" varchar(64) COLLATE "pg_catalog"."default",
  "file_name" varchar(255) COLLATE "pg_catalog"."default",
  "file_type" varchar(255) COLLATE "pg_catalog"."default",
  "file_size" int4,
  "file_link" varchar(255) COLLATE "pg_catalog"."default",
  "file_domain" varchar(255) COLLATE "pg_catalog"."default",
  "file_original_name" varchar(255) COLLATE "pg_catalog"."default",
  "commits_id" varchar(255) COLLATE "pg_catalog"."default",
  "resource_type" varchar(255) COLLATE "pg_catalog"."default",
  "element_id" varchar(255) COLLATE "pg_catalog"."default",
  "remark" varchar(255) COLLATE "pg_catalog"."default",
  "user_id" varchar(10) COLLATE "pg_catalog"."default",
  "file_coord" text COLLATE "pg_catalog"."default"
)
;
ALTER TABLE "public"."sp_file_common" OWNER TO "speckle";

-- ----------------------------
-- Primary Key structure for table sp_file_common
-- ----------------------------
ALTER TABLE "public"."sp_file_common" ADD CONSTRAINT "sp_wbs_info_copy1_pkey" PRIMARY KEY ("id");
