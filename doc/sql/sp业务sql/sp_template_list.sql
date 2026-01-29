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

 Date: 11/07/2024 15:18:31
*/


-- ----------------------------
-- Table structure for sp_template_list
-- ----------------------------
DROP TABLE IF EXISTS "public"."sp_template_list";
CREATE TABLE "public"."sp_template_list" (
  "id" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "pid" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "name" varchar(255) COLLATE "pg_catalog"."default",
  "branch_id" varchar COLLATE "pg_catalog"."default" NOT NULL,
  "modify_time" timestamp(0) DEFAULT CURRENT_TIMESTAMP,
  "type" varchar COLLATE "pg_catalog"."default",
  "status" int4 NOT NULL DEFAULT 0,
  "modify_user" varchar(255) COLLATE "pg_catalog"."default" NOT NULL DEFAULT 'admin'::character varying,
  "detail" text COLLATE "pg_catalog"."default",
  "ranknum" float4
)
;
COMMENT ON COLUMN "public"."sp_template_list"."pid" IS '父节点';
COMMENT ON COLUMN "public"."sp_template_list"."name" IS '名称';
COMMENT ON COLUMN "public"."sp_template_list"."branch_id" IS '绑定分支';
COMMENT ON COLUMN "public"."sp_template_list"."modify_time" IS '创建时间';
COMMENT ON COLUMN "public"."sp_template_list"."type" IS '类型：文件夹、模板';
COMMENT ON COLUMN "public"."sp_template_list"."status" IS '状态码, 0-正常 9-删除';
COMMENT ON COLUMN "public"."sp_template_list"."modify_user" IS '修改人的名称';
COMMENT ON COLUMN "public"."sp_template_list"."detail" IS '描述';
COMMENT ON COLUMN "public"."sp_template_list"."ranknum" IS '排序';

-- ----------------------------
-- Primary Key structure for table sp_template_list
-- ----------------------------
ALTER TABLE "public"."sp_template_list" ADD CONSTRAINT "sp_template_list_pkey" PRIMARY KEY ("id", "branch_id");
