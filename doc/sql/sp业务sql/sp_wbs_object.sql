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

 Date: 06/06/2024 16:38:42
*/


-- ----------------------------
-- Table structure for sp_wbs_object
-- ----------------------------
DROP TABLE IF EXISTS "public"."sp_wbs_object";
CREATE TABLE "public"."sp_wbs_object" (
  "wbs_id" varchar(36) COLLATE "pg_catalog"."default" NOT NULL,
  "object_id" varchar(36) COLLATE "pg_catalog"."default" NOT NULL,
  "id" varchar(36) COLLATE "pg_catalog"."default" NOT NULL,
  "status" int4,
  "modify_user" varchar(50) COLLATE "pg_catalog"."default",
  "modify_time" timestamp(6)
)
;
COMMENT ON COLUMN "public"."sp_wbs_object"."wbs_id" IS 'wbsId';
COMMENT ON COLUMN "public"."sp_wbs_object"."object_id" IS '模型id';
COMMENT ON COLUMN "public"."sp_wbs_object"."id" IS 'id';
COMMENT ON COLUMN "public"."sp_wbs_object"."status" IS ' ';

-- ----------------------------
-- Primary Key structure for table sp_wbs_object
-- ----------------------------
ALTER TABLE "public"."sp_wbs_object" ADD CONSTRAINT "sp_wbs_object_pkey" PRIMARY KEY ("id");
