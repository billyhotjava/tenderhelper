package com.yuzhi.tender.service;

import com.yuzhi.tender.domain.BidInfo;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ExcelService {

    private final Logger log = LoggerFactory.getLogger(ExcelService.class);

    @Autowired
    private BidInfoService bidInfoService;

    /**
     *
     * @param file
     * @return
     * @throws Exception
     */
    public List<String> fetchHeadData(MultipartFile file) throws Exception {
        List<String> headers = new ArrayList<>();
        try {
            Workbook workbook = WorkbookFactory.create(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);
            Row headerRow = sheet.getRow(0);

            if (headerRow != null) {
                for (Cell cell : headerRow) {
                    headers.add(cell.getStringCellValue());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return headers;
    }

    /**
     *
     * @param file
     * @return
     * @throws Exception
     */
    public List<List<BidInfo>> processExcelFile(MultipartFile file, Map<String, String> mapFields) throws Exception {
        log.debug("=***** Begin Excel Service ****=");
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Uploaded file is empty!");
        }
        // Check for Excel file type
        String contentType = file.getContentType();
        if (!"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet".equals(contentType)) {
            throw new IllegalArgumentException("Uploaded file is not a valid Excel file!");
        }

        List<List<BidInfo>> data = new ArrayList<>();
        InputStream is = file.getInputStream();
        Workbook workbook = new XSSFWorkbook(is);
        Sheet sheet = workbook.getSheetAt(0);

        // 获取标题行
        Row headerRow = sheet.getRow(0);
        if (headerRow == null) {
            throw new IllegalArgumentException("No header row found in the Excel file!");
        }
        // 基于 key-value 对确定要读取的列
        Map<String, Integer> columnMap = new HashMap<>();
        /*
        for (Cell cell : headerRow) {
            if (cell.getCellType() == CellType.STRING) {
                String header = cell.getStringCellValue();
                log.debug("82:header is " + header);
                if (mapFields.containsValue(header)) {
                    // 将标题和对应的列索引存入映射
                    columnMap.put(header, cell.getColumnIndex());
                }
            }
        }*/
        for (Cell cell : headerRow) {
            if (cell.getCellType() == CellType.STRING) {
                String header = cell.getStringCellValue();
                // 检查此列是否是我们需要的列
                for (Map.Entry<String, String> entry : mapFields.entrySet()) {
                    if (entry.getValue().equals(header)) {
                        // 将我们需要的列名（如 "bidSection"）和它在 Excel 中的索引存入 columnMap
                        columnMap.put(entry.getKey(), cell.getColumnIndex());
                    }
                }
            }
        }

        // 跳过标题行，开始处理数据
        Iterator<Row> rowIterator = sheet.rowIterator();
        if (rowIterator.hasNext()) {
            rowIterator.next(); // Skip header row
        }

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            BidInfo bidInfo = new BidInfo();
            for (Map.Entry<String, Integer> entry : columnMap.entrySet()) {
                Cell cell = row.getCell(entry.getValue());
                // 根据 cell 的类型和列名处理数据
                log.debug("102:cell colume:" + entry.getValue().toString());
                log.debug("103:cell value is:" + cell.toString());
                switch (entry.getKey()) {
                    case "bidPrjId":
                        bidInfo.setBidPrjId(getStringCellValue(cell));
                        break;
                    case "bidPrjName":
                        bidInfo.setBidPrjName(getStringCellValue(cell));
                        break;
                    case "bidSectionId":
                        bidInfo.setBidSectionId(getStringCellValue(cell));
                        break;
                    case "bidSection":
                        bidInfo.setBidSection(getStringCellValue(cell));
                        break;
                    case "bidder":
                        bidInfo.setBidder(getStringCellValue(cell));
                        break;
                    case "bidPrice":
                        bidInfo.setBidPrice(getBigDecimalCellValue(cell));
                        break;
                    default:
                        // 处理未知或未映射的列
                        log.debug("****Fatal Exception in Excel Service:" + entry.getKey().toString());
                        break;
                }
            }
            log.debug("===ExcelService===" + bidInfo.getBidPrice());
            bidInfoService.save(bidInfo);
            List<BidInfo> rowData = new ArrayList<>();
            rowData.add(bidInfo);
            data.add(rowData);
        }

        workbook.close();
        is.close();
        log.debug("=***** Finished Excel Service ****=");
        return data;
    }

    private String getStringCellValue(Cell cell) {
        if (cell == null) return "";
        if (cell.getCellType() == CellType.STRING) {
            return cell.getStringCellValue();
        } else if (cell.getCellType() == CellType.NUMERIC) {
            return String.valueOf(cell.getNumericCellValue());
        }
        // ... handle other cell types if necessary
        return "";
    }

    private BigDecimal getBigDecimalCellValue(Cell cell) {
        if (cell == null) return BigDecimal.ZERO;
        if (cell.getCellType() == CellType.NUMERIC) {
            BigDecimal value = BigDecimal.valueOf(cell.getNumericCellValue());
            return value.setScale(6, RoundingMode.HALF_UP);
        } else if (cell.getCellType() == CellType.STRING) {
            try {
                // 移除字符串中的逗号
                String cellValue = cell.getStringCellValue().replace(",", "");
                return new BigDecimal(cellValue).setScale(6, RoundingMode.HALF_UP);
            } catch (NumberFormatException e) {
                log.error("Failed to parse string to BigDecimal", e);
            }
        }
        // ... handle other cell types if necessary
        return BigDecimal.ZERO;
    }
}
