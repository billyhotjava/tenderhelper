package com.yuzhi.tender.web.rest;

import com.yuzhi.tender.service.ExcelService;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
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

    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> handleFileUpload(@RequestParam("excel") MultipartFile file, HttpServletResponse response)
        throws IOException {
        // ... 其他处理代码 ...

        try {
            // ... 你的Excel处理代码 ...
            log.debug("=========== Begin to process Excel =============");
            if (file.isEmpty()) {
                //return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
            }
            excelService.processExcelFile(file); // Process the uploaded Excel file
            // 当成功处理完Excel后
            Map<String, String> responseBody = new HashMap<>();
            responseBody.put("message", "File processed successfully!");

            return ResponseEntity.ok(responseBody);
        } catch (Exception e) {
            // 处理发生异常的情况
            Map<String, String> responseBody = new HashMap<>();
            responseBody.put("message", "Error processing the file: " + e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
        }
    }
}
