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

 Date: 11/07/2024 15:17:56
*/


-- ----------------------------
-- Table structure for sp_element_wbs
-- ----------------------------
DROP TABLE IF EXISTS "public"."sp_element_wbs";
CREATE TABLE "public"."sp_element_wbs" (
  "id" varchar(36) COLLATE "pg_catalog"."default" NOT NULL,
  "element_id" varchar[] COLLATE "pg_catalog"."default",
  "wbs_code" varchar(255) COLLATE "pg_catalog"."default",
  "status" int4 NOT NULL,
  "branch_id" varchar(255) COLLATE "pg_catalog"."default",
  "temp_id" varchar(255) COLLATE "pg_catalog"."default"
)
;

-- ----------------------------
-- Primary Key structure for table sp_element_wbs
-- ----------------------------
ALTER TABLE "public"."sp_element_wbs" ADD CONSTRAINT "sp_element_wbs_pkey" PRIMARY KEY ("id");
