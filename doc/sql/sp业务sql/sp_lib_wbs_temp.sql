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

 Date: 11/07/2024 15:18:13
*/


-- ----------------------------
-- Table structure for sp_lib_wbs_temp
-- ----------------------------
DROP TABLE IF EXISTS "public"."sp_lib_wbs_temp";
CREATE TABLE "public"."sp_lib_wbs_temp" (
  "id" varchar(36) COLLATE "pg_catalog"."default" NOT NULL,
  "wbs_code" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "temp_id" varchar(36) COLLATE "pg_catalog"."default" NOT NULL,
  "lib_id" varchar(36) COLLATE "pg_catalog"."default" NOT NULL,
  "status" int4 NOT NULL
)
;

-- ----------------------------
-- Primary Key structure for table sp_lib_wbs_temp
-- ----------------------------
ALTER TABLE "public"."sp_lib_wbs_temp" ADD CONSTRAINT "sp_lib_wbs_temp_pkey" PRIMARY KEY ("id");
