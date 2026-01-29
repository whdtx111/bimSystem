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

 Date: 11/07/2024 15:15:37
*/


-- ----------------------------
-- Table structure for sp_dwg_file
-- ----------------------------
DROP TABLE IF EXISTS "public"."sp_dwg_file";
CREATE TABLE "public"."sp_dwg_file" (
  "id" varchar(36) COLLATE "pg_catalog"."default" NOT NULL,
  "project_id" varchar(36) COLLATE "pg_catalog"."default" NOT NULL,
  "name" varchar(255) COLLATE "pg_catalog"."default",
  "map_id" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "detail" jsonb,
  "modify_user" varchar(255) COLLATE "pg_catalog"."default",
  "modify_time" timestamp(0),
  "status" int4,
  "object_id" varchar[] COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."sp_dwg_file"."project_id" IS '项目id';
COMMENT ON COLUMN "public"."sp_dwg_file"."detail" IS '描述';

-- ----------------------------
-- Indexes structure for table sp_dwg_file
-- ----------------------------
CREATE UNIQUE INDEX "pmkey" ON "public"."sp_dwg_file" USING btree (
  "project_id" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "map_id" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);

-- ----------------------------
-- Primary Key structure for table sp_dwg_file
-- ----------------------------
ALTER TABLE "public"."sp_dwg_file" ADD CONSTRAINT "sp_dwg_file_pkey" PRIMARY KEY ("id");
