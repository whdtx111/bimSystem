package org.springblade.modules.sp.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.postgresql.util.PGobject;

import java.sql.*;
import java.util.List;

public class JsonListTypeHandler extends BaseTypeHandler<List<JSONObject>> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<JSONObject> parameter, JdbcType jdbcType) throws SQLException {
        try {
            // 添加调试日志
            System.out.println("JsonListTypeHandler.setNonNullParameter: 处理数据条目数=" + 
                             (parameter != null ? parameter.size() : 0));
            
            // 使用WriteMapNullValue特性，确保null值字段也被序列化
            String jsonString = JSON.toJSONString(parameter, SerializerFeature.WriteMapNullValue);
            
            // 记录JSON字符串长度
            System.out.println("JsonListTypeHandler: JSON字符串长度=" + jsonString.length());
            
            // 针对PostgreSQL的jsonb类型处理
            PGobject pgObject = new PGobject();
            pgObject.setType("jsonb");
            pgObject.setValue(jsonString);
            ps.setObject(i, pgObject);
            
            System.out.println("JsonListTypeHandler: PGobject设置成功");
        } catch (Exception e) {
            System.err.println("JsonListTypeHandler.setNonNullParameter异常: " + e.getMessage());
            e.printStackTrace();
            throw new SQLException("JSON序列化失败", e);
        }
    }

    @Override
    public List<JSONObject> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        try {
            String json = rs.getString(columnName);
            if (json == null) {
                System.out.println("JsonListTypeHandler.getNullableResult(columnName): 返回null");
                return null;
            }
            
            List<JSONObject> result = JSON.parseArray(json, JSONObject.class);
            System.out.println("JsonListTypeHandler.getNullableResult(columnName): 解析出数据条目数=" + 
                             (result != null ? result.size() : 0));
            return result;
        } catch (Exception e) {
            System.err.println("JsonListTypeHandler.getNullableResult(columnName)异常: " + e.getMessage());
            e.printStackTrace();
            throw new SQLException("JSON反序列化失败", e);
        }
    }

    @Override
    public List<JSONObject> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        try {
            String json = rs.getString(columnIndex);
            if (json == null) {
                System.out.println("JsonListTypeHandler.getNullableResult(columnIndex): 返回null");
                return null;
            }
            
            List<JSONObject> result = JSON.parseArray(json, JSONObject.class);
            System.out.println("JsonListTypeHandler.getNullableResult(columnIndex): 解析出数据条目数=" + 
                             (result != null ? result.size() : 0));
            return result;
        } catch (Exception e) {
            System.err.println("JsonListTypeHandler.getNullableResult(columnIndex)异常: " + e.getMessage());
            e.printStackTrace();
            throw new SQLException("JSON反序列化失败", e);
        }
    }

    @Override
    public List<JSONObject> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        try {
            String json = cs.getString(columnIndex);
            if (json == null) {
                System.out.println("JsonListTypeHandler.getNullableResult(CallableStatement): 返回null");
                return null;
            }
            
            List<JSONObject> result = JSON.parseArray(json, JSONObject.class);
            System.out.println("JsonListTypeHandler.getNullableResult(CallableStatement): 解析出数据条目数=" + 
                             (result != null ? result.size() : 0));
            return result;
        } catch (Exception e) {
            System.err.println("JsonListTypeHandler.getNullableResult(CallableStatement)异常: " + e.getMessage());
            e.printStackTrace();
            throw new SQLException("JSON反序列化失败", e);
        }
    }
}
