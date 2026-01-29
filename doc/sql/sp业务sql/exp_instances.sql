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

 Date: 11/07/2024 15:15:19
*/


-- ----------------------------
-- Table structure for exp_instances
-- ----------------------------
DROP TABLE IF EXISTS "public"."exp_instances";
CREATE TABLE "public"."exp_instances" (
  "id" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "type" varchar(255) COLLATE "pg_catalog"."default",
  "level" jsonb,
  "units" varchar(255) COLLATE "pg_catalog"."default",
  "slope" varchar(255) COLLATE "pg_catalog"."default",
  "family" varchar(255) COLLATE "pg_catalog"."default",
  "category" varchar(255) COLLATE "pg_catalog"."default",
  "elementId" varchar(255) COLLATE "pg_catalog"."default",
  "parameters" jsonb,
  "phaseCreated" varchar(255) COLLATE "pg_catalog"."default",
  "smartLinkType" varchar(255) COLLATE "pg_catalog"."default",
  "applicationId" varchar(255) COLLATE "pg_catalog"."default",
  "slopeDirection" varchar(255) COLLATE "pg_catalog"."default",
  "builtInCategory" varchar(255) COLLATE "pg_catalog"."default",
  "isRevitLinkedModel" varchar(255) COLLATE "pg_catalog"."default",
  "materialQuantities" jsonb,
  "createdAt" timestamptz(6) DEFAULT CURRENT_TIMESTAMP,
  "streamId" varchar(10) COLLATE "pg_catalog"."default" NOT NULL,
  "commitId" varchar(255) COLLATE "pg_catalog"."default" DEFAULT ''::character varying
)
;
COMMENT ON COLUMN "public"."exp_instances"."type" IS 'type';
COMMENT ON COLUMN "public"."exp_instances"."level" IS '楼层';
COMMENT ON COLUMN "public"."exp_instances"."units" IS '单位';
COMMENT ON COLUMN "public"."exp_instances"."family" IS '族';
COMMENT ON COLUMN "public"."exp_instances"."category" IS 'category';
COMMENT ON COLUMN "public"."exp_instances"."elementId" IS '实例id';
COMMENT ON COLUMN "public"."exp_instances"."isRevitLinkedModel" IS 'false/true';

-- ----------------------------
-- Indexes structure for table exp_instances
-- ----------------------------
CREATE INDEX "exp_instances_combined_idx" ON "public"."exp_instances" USING btree (
  "streamId" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "type" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "family" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "category" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "elementId" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
CREATE INDEX "exp_instances_streamid_idx" ON "public"."exp_instances" USING btree (
  "streamId" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
CREATE INDEX "idx_exp_instances_stream_commit" ON "public"."exp_instances" USING btree (
  "streamId" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "commitId" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
CREATE INDEX "iskey" ON "public"."exp_instances" USING btree (
  "id" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "streamId" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);

-- ----------------------------
-- Primary Key structure for table exp_instances
-- ----------------------------
ALTER TABLE "public"."exp_instances" ADD CONSTRAINT "exp_instances_pkey" PRIMARY KEY ("id", "streamId");
