package org.springblade.modules.sp.utils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class ComputeUtils {

//    public static void main(String[] args) {
//        Map<Double, Double> groupMinMax = getGroupMinMax(503.123, 11);
//
//        for(Map.Entry entry : groupMinMax.entrySet()){
//            String mapKey = entry.getKey().toString();
//            String mapValue = entry.getValue().toString();
//            System.out.println(mapKey+"~"+mapValue);
//        }
//
//    }

    public static Map<Double, Double> getGroupMinMax(double value, int divisions) {
        Map<Double, Double> map = new TreeMap<>();
        double portion = value / divisions;
        // 计算余数
        double remainder = value % divisions;

        // 初始化最小值和最大值
        double minValue = 0.0;
        double maxValue = portion;

        // 判断是否有余数，如果有，则将余数均匀分配给各份
        if (remainder > 0) {
            maxValue++; // 将多余的一份加到最大值中
            remainder--; // 减去已分配的余数
        }

        // 打印各份的值
        for (int i = 1; i <= divisions; i++) {
            map.put(new BigDecimal(minValue).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue(), new BigDecimal(maxValue).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
            // 更新最小值和最大值
            minValue = maxValue + 1;
            maxValue = minValue + portion - 1;

            // 如果还有余数，则将一个余数分配给最大值
            if (remainder > 0) {
                maxValue++;
                remainder--;
            }
        }
        return map;
    }

}
