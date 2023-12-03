package com.yuzhi.tender.web.rest;

import com.yuzhi.tender.service.BidInfoService;
import com.yuzhi.tender.service.ExcelService;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class ExcelController {

    private final Logger log = LoggerFactory.getLogger(ExcelController.class);

    @Autowired
    private ExcelService excelService; // Assuming you have a service class for processing the Excel file

    @Autowired
    private BidInfoService bidInfoService; // Assuming you have a service class for processing the Excel file

    @PostMapping("/upload")
    public ResponseEntity<?> confirmUploadAndData(@RequestParam("excel") MultipartFile file, @RequestParam Map<String, String> mapFields) {
        try {
            //---首先删除之前的计算过的数据---
            bidInfoService.deleteAll();

            // 这里处理带有键值对的确认请求和Excel处理代码
            log.debug("========== Begin to process Excel =============");
            if (file.isEmpty()) {
                //return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
            }
            //打印所有接收到的键值对
            log.debug("========== Get all maps of label and head =============");
            mapFields.forEach((key, value) -> log.debug("(Key:" + key + ";Value: " + value + ")"));

            excelService.processExcelFile(file, mapFields); // Process the uploaded Excel file
            // 使用 request.getParameterMap() 或类似方法获取其他表单字段
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/heads")
    public ResponseEntity<List<String>> fetchHeadData(@RequestParam("excel") MultipartFile file, HttpServletResponse response) {
        try {
            List<String> headers;
            //获取Excel的第一个sheet的第一行的所有标题
            headers = excelService.fetchHeadData(file);
            return ResponseEntity.ok(headers);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}
