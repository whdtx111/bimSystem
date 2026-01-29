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

 Date: 11/07/2024 15:17:39
*/


-- ----------------------------
-- Table structure for sp_ebs_ref
-- ----------------------------
DROP TABLE IF EXISTS "public"."sp_ebs_ref";
CREATE TABLE "public"."sp_ebs_ref" (
  "id" varchar(36) COLLATE "pg_catalog"."default" NOT NULL,
  "ebs_code" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
  "ebs_name" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "ebs_lv" int4 NOT NULL,
  "type" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
  "modify_user" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "modify_time" timestamp(0) NOT NULL,
  "status" int4 NOT NULL,
  "detail" varchar(255) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."sp_ebs_ref"."detail" IS '描述';

-- ----------------------------
-- Uniques structure for table sp_ebs_ref
-- ----------------------------
ALTER TABLE "public"."sp_ebs_ref" ADD CONSTRAINT "sp_ebs_ref_ebs_code_key" UNIQUE ("ebs_code");

-- ----------------------------
-- Primary Key structure for table sp_ebs_ref
-- ----------------------------
ALTER TABLE "public"."sp_ebs_ref" ADD CONSTRAINT "sp_ebs_ref_pkey" PRIMARY KEY ("id");
