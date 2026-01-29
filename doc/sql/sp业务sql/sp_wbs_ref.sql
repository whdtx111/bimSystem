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

 Date: 11/07/2024 15:18:42
*/


-- ----------------------------
-- Table structure for sp_wbs_ref
-- ----------------------------
DROP TABLE IF EXISTS "public"."sp_wbs_ref";
CREATE TABLE "public"."sp_wbs_ref" (
  "id" varchar(36) COLLATE "pg_catalog"."default" NOT NULL,
  "wbs_code" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "wbs_name" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "wbs_lv" int4 NOT NULL,
  "type" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
  "detail" varchar(255) COLLATE "pg_catalog"."default",
  "modify_user" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "modify_time" timestamp(0) NOT NULL,
  "status" int4 NOT NULL
)
;

-- ----------------------------
-- Uniques structure for table sp_wbs_ref
-- ----------------------------
ALTER TABLE "public"."sp_wbs_ref" ADD CONSTRAINT "sp_wbs_ref_wbs_code_key" UNIQUE ("wbs_code");

-- ----------------------------
-- Primary Key structure for table sp_wbs_ref
-- ----------------------------
ALTER TABLE "public"."sp_wbs_ref" ADD CONSTRAINT "sp_wbs_ref_pkey" PRIMARY KEY ("id");
