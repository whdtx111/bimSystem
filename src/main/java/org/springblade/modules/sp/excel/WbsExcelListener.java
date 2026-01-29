//package org.springblade.modules.sp.excel;
//
//import com.alibaba.excel.context.AnalysisContext;
//import com.alibaba.excel.read.listener.ReadListener;
//import lombok.extern.slf4j.Slf4j;
//import org.springblade.modules.sp.dto.WbsExcelDTO;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Slf4j
//public class WbsExcelListener implements ReadListener<WbsExcelDTO> {
//    private static final int BATCH_SIZE = 100;
//    private final List<Product> cachedData = new ArrayList<>(BATCH_SIZE);
//
//    @Autowired
//    private ProductMapper productMapper;
//
//    @Override
//    public void invoke(Product product, AnalysisContext analysisContext) {
//        cachedData.add(product);
//        if (cachedData.size() >= BATCH_SIZE) {
//            saveData();
//            cachedData.clear();
//        }
//    }
//
//    @Override
//    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
//        saveData();
//    }
//
//    private void saveData() {
//        log.info("Saving {} products to database...", cachedData.size());
//        // 使用 MyBatis 批量插入数据
//        productMapper.insertBatchSomeColumn(cachedData);
//    }
//}
