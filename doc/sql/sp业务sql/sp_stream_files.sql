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

 Date: 11/07/2024 15:19:07
*/


-- ----------------------------
-- Table structure for sp_stream_files
-- ----------------------------
DROP TABLE IF EXISTS "public"."sp_stream_files";
CREATE TABLE "public"."sp_stream_files" (
  "id" varchar(36) COLLATE "pg_catalog"."default" NOT NULL,
  "object_id" varchar[] COLLATE "pg_catalog"."default",
  "stream_id" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "type" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "file_url" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "modify_user" varchar(50) COLLATE "pg_catalog"."default",
  "modify_time" timestamp(0) NOT NULL,
  "status" int4 NOT NULL,
  "name" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "size" text COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."sp_stream_files"."size" IS '文件大小';

-- ----------------------------
-- Primary Key structure for table sp_stream_files
-- ----------------------------
ALTER TABLE "public"."sp_stream_files" ADD CONSTRAINT "sp_stream_files_pkey" PRIMARY KEY ("id");
