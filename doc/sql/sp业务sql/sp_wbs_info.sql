/*
 Navicat Premium Data Transfer

 Source Server         : 10.5.58.132_speckle
 Source Server Type    : PostgreSQL
 Source Server Version : 110011
 Source Host           : 10.5.58.132:31335
 Source Catalog        : speckle
 Source Schema         : public

 Target Server Type    : PostgreSQL
 Target Server Version : 110011
 File Encoding         : 65001

 Date: 13/06/2023 16:44:43
*/


-- ----------------------------
-- Table structure for sp_wbs_info
-- ----------------------------
DROP TABLE IF EXISTS "public"."sp_wbs_info";
CREATE TABLE "public"."sp_wbs_info" (
  "id" int8 NOT NULL,
  "wbs_code" varchar(255) COLLATE "pg_catalog"."default",
  "text" varchar(255) COLLATE "pg_catalog"."default",
  "english_text" varchar(255) COLLATE "pg_catalog"."default",
  "wbs_level" varchar(32) COLLATE "pg_catalog"."default",
  "parent_wbs_code" varchar(155) COLLATE "pg_catalog"."default",
  "create_user" int8,
  "create_dept" int8,
  "create_time" timestamp(6),
  "update_user" int8,
  "update_time" timestamp(6),
  "status" int4,
  "is_deleted" int4 DEFAULT 0,
  "streams_id" varchar(64) COLLATE "pg_catalog"."default",
  "element_id" varchar(64) COLLATE "pg_catalog"."default",
  "objects_id" varchar(64) COLLATE "pg_catalog"."default",
  "duration" varchar(12) COLLATE "pg_catalog"."default",
  "progress" varchar(10) COLLATE "pg_catalog"."default" DEFAULT 0,
  "sortorder" varchar(12) COLLATE "pg_catalog"."default",
  "start_date" timestamp(6),
  "deadline" timestamp(6),
  "planned_start" timestamp(6),
  "planned_end" timestamp(6),
  "open" varchar(10) COLLATE "pg_catalog"."default" DEFAULT 0,
  "info_type" int4,
  "model_code" varchar(255) COLLATE "pg_catalog"."default",
  "model_name" varchar(255) COLLATE "pg_catalog"."default",
  "model_id" varchar(255) COLLATE "pg_catalog"."default",
  "cd_type" varchar(255) COLLATE "pg_catalog"."default",
  "cd_profundity" varchar(255) COLLATE "pg_catalog"."default"
)
;
ALTER TABLE "public"."sp_wbs_info" OWNER TO "postgres";
COMMENT ON COLUMN "public"."sp_wbs_info"."wbs_code" IS '编码';
COMMENT ON COLUMN "public"."sp_wbs_info"."text" IS '任务名称';
COMMENT ON COLUMN "public"."sp_wbs_info"."english_text" IS '英文任务名称';
COMMENT ON COLUMN "public"."sp_wbs_info"."wbs_level" IS '层级';
COMMENT ON COLUMN "public"."sp_wbs_info"."parent_wbs_code" IS '父编码';
COMMENT ON COLUMN "public"."sp_wbs_info"."create_user" IS '创建人';
COMMENT ON COLUMN "public"."sp_wbs_info"."create_dept" IS '创建部门';
COMMENT ON COLUMN "public"."sp_wbs_info"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."sp_wbs_info"."update_user" IS '修改人';
COMMENT ON COLUMN "public"."sp_wbs_info"."update_time" IS '修改时间';
COMMENT ON COLUMN "public"."sp_wbs_info"."status" IS '状态';
COMMENT ON COLUMN "public"."sp_wbs_info"."is_deleted" IS '是否已删除';
COMMENT ON COLUMN "public"."sp_wbs_info"."duration" IS 'gantt_持续时间';
COMMENT ON COLUMN "public"."sp_wbs_info"."progress" IS 'gantt_完成度0.1 1百分比';
COMMENT ON COLUMN "public"."sp_wbs_info"."sortorder" IS 'gantt_排序';
COMMENT ON COLUMN "public"."sp_wbs_info"."start_date" IS 'gantt_开始时间';
COMMENT ON COLUMN "public"."sp_wbs_info"."deadline" IS 'gantt_最后期限';
COMMENT ON COLUMN "public"."sp_wbs_info"."planned_start" IS 'gantt_当成实际开始用';
COMMENT ON COLUMN "public"."sp_wbs_info"."planned_end" IS 'gantt_当成实际结束用';
COMMENT ON COLUMN "public"."sp_wbs_info"."open" IS 'gantt_展开';
COMMENT ON COLUMN "public"."sp_wbs_info"."info_type" IS '1wbs，2构件';
COMMENT ON COLUMN "public"."sp_wbs_info"."model_code" IS '构件-模型编码';
COMMENT ON COLUMN "public"."sp_wbs_info"."model_name" IS '构件-模型名称';
COMMENT ON COLUMN "public"."sp_wbs_info"."model_id" IS '构件-模型编号';
COMMENT ON COLUMN "public"."sp_wbs_info"."cd_type" IS '槽段类型';
COMMENT ON COLUMN "public"."sp_wbs_info"."cd_profundity" IS '槽段深度';
COMMENT ON TABLE "public"."sp_wbs_info" IS 'Wbs任务表';

-- ----------------------------
-- Indexes structure for table sp_wbs_info
-- ----------------------------
CREATE INDEX "WBS_CODE" ON "public"."sp_wbs_info" USING btree (
  "wbs_code" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
CREATE INDEX "WBS_ENGLISH_TEXT" ON "public"."sp_wbs_info" USING btree (
  "english_text" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
CREATE INDEX "WBS_TEXT" ON "public"."sp_wbs_info" USING btree (
  "text" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);

-- ----------------------------
-- Primary Key structure for table sp_wbs_info
-- ----------------------------
ALTER TABLE "public"."sp_wbs_info" ADD CONSTRAINT "sp_wbs_info_this_pkey" PRIMARY KEY ("id");
