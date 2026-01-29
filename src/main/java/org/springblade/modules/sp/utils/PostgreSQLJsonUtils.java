package org.springblade.modules.sp.utils;

public class PostgreSQLJsonUtils {

    /**
     * 将PostgreSQL查询结果中的JSON字段转换为字符串
     * @param obj 数据库查询结果对象
     * @return JSON字符串
     */
    public static String convertToJsonString(Object obj) {
        if (obj == null) {
            return null;
        }

        if (obj instanceof String) {
            return (String) obj;
        }

        // 处理PostgreSQL的PGobject类型
        if (obj.getClass().getName().equals("org.postgresql.util.PGobject")) {
            return obj.toString();
        }

        return obj.toString();
    }
}