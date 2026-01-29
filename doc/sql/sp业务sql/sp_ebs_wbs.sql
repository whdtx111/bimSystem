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

 Date: 11/07/2024 15:17:46
*/


-- ----------------------------
-- Table structure for sp_ebs_wbs
-- ----------------------------
DROP TABLE IF EXISTS "public"."sp_ebs_wbs";
CREATE TABLE "public"."sp_ebs_wbs" (
  "id" int4 NOT NULL DEFAULT nextval('sp_ebs_wbs_id_seq'::regclass),
  "ebs_id" varchar(36) COLLATE "pg_catalog"."default" NOT NULL,
  "wbs_id" varchar(36) COLLATE "pg_catalog"."default" NOT NULL,
  "status" int4 NOT NULL
)
;

-- ----------------------------
-- Primary Key structure for table sp_ebs_wbs
-- ----------------------------
ALTER TABLE "public"."sp_ebs_wbs" ADD CONSTRAINT "sp_ebs_wbs_pkey" PRIMARY KEY ("id");
