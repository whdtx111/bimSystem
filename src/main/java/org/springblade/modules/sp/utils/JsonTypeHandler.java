package org.springblade.modules.sp.utils;

import com.alibaba.fastjson.JSONException;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.postgresql.util.PGobject;
import org.springframework.stereotype.Component;

import java.sql.*;

/** JSONtoJSONB类型转换器
 * @author dengtx
 * @since 2024年5月15日18:05:48
 */
@MappedTypes(JSONObject.class)
@MappedJdbcTypes(JdbcType.OTHER)
@Component
public class JsonTypeHandler extends BaseTypeHandler<JSONObject> {
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, JSONObject parameter, JdbcType jdbcType) throws SQLException {
        if (parameter != null) {
            PGobject jsonObject = new PGobject();
            jsonObject.setType("jsonb");
            jsonObject.setValue(parameter.toJSONString());
            ps.setObject(i, jsonObject);
        } else {
            ps.setNull(i, Types.OTHER);
        }
    }
    @Override
    public JSONObject getNullableResult(ResultSet rs, String columnName) throws SQLException {
        try {
            if (columnName.equals("createdAt") || columnName.equals("category") || columnName.equals("streamId")){
                return null;
            }
            String content = rs.getString(columnName);
            return content == null ? null : (JSONObject) JSON.parse(content);
        } catch (JSONException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
    @Override
    public JSONObject getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String content = rs.getString(columnIndex);
        return content == null ? null : (JSONObject) JSON.parse(content);
    }
    @Override
    public JSONObject getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String content = cs.getString(columnIndex);
        return content == null ? null : (JSONObject) JSON.parse(content);
    }
}
