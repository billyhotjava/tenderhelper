package com.yuzhi.tender.service;

import com.yuzhi.tender.domain.BidInfo;
import com.yuzhi.tender.web.rest.ExcelController;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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

    public List<List<BidInfo>> processExcelFile(MultipartFile file) throws Exception {
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
        Iterator<Row> rowIterator = sheet.iterator();

        //跳过标题行
        if (rowIterator.hasNext()) {
            rowIterator.next();
        }

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Iterator<Cell> cellIterator = row.cellIterator();
            List<BidInfo> rowData = new ArrayList<>();
            BidInfo bidInfo = convertRowToBidInfo(row);
            bidInfoService.save(bidInfo);
            rowData.add(bidInfo);
            data.add(rowData);
        }

        workbook.close();
        is.close();
        log.debug("=***** Finished Excel Service ****=");
        return data;
    }

    /**
     * 处理Excel每一行数据到BidInfo实例
     *
     * @param row
     * @return
     */
    public BidInfo convertRowToBidInfo(Row row) {
        BidInfo bidInfo = new BidInfo();
        bidInfo.setBidPrjId(getStringCellValue(row.getCell(0)));
        bidInfo.setBidPrjName(getStringCellValue(row.getCell(1)));
        bidInfo.setBidSectionId(getStringCellValue(row.getCell(2)));
        bidInfo.setBidSection(getStringCellValue(row.getCell(3)));
        bidInfo.setBidder(getStringCellValue(row.getCell(4)));
        bidInfo.setBidPrice(getBigDecimalCellValue(row.getCell(5)));
        //print the price value
        log.debug("===ExcelService===" + bidInfo.getBidPrice());
        return bidInfo;
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
                return new BigDecimal(cell.getStringCellValue());
            } catch (NumberFormatException e) {
                log.error("Failed to parse string to BigDecimal", e);
            }
        }
        // ... handle other cell types if necessary
        return BigDecimal.ZERO;
    }
}
