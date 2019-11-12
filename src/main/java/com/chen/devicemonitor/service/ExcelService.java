package com.chen.devicemonitor.service;

import com.chen.devicemonitor.en.DeviceRoleEnum;
import com.chen.devicemonitor.en.UserRoleEnum;
import com.chen.devicemonitor.entity.Device;
import com.chen.devicemonitor.util.StringUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
@Service
public class ExcelService {

    public List<Device> getDeviceByExcel(InputStream inputStream,String fileName) throws  Exception{
        List<Device> list = new ArrayList<>();
        Workbook workbook = check(inputStream,fileName);
        if(workbook == null) {
            throw new Exception("Excel文件错误");
        }
        Sheet sheet = null;
        Row row = null;
        //遍历sheet
        for(int i = 0;i <workbook.getNumberOfSheets();i++) {
            sheet = workbook.getSheetAt(i);
            if(sheet == null) {
                continue;
            }
            //遍历行,含表头，因此需要+1
            for(int j = sheet.getFirstRowNum()+1;j < sheet.getLastRowNum();j++){
                row = sheet.getRow(j);
                if(row == null){
                    continue;
                }

                Device device = new Device();
                int k = row.getFirstCellNum();
                device.setDName(getCellValue(row.getCell(k)));
                device.setType(changeType(getCellValue(row.getCell(k+1))));
                device.setDIP(getCellValue(row.getCell(k+2)));
                device.setDPort(getCellValue(row.getCell(k+3)));
                list.add(device);
            }
        }
        return list;
    }

    /**
     * 检查文件类型是否正确,如果返回null那么说明文件类型不正确
     * @param inputStream
     * @param fileName
     * @return
     */
    private static Workbook check(InputStream inputStream,String fileName) throws Exception{
        if(fileName == null){
            return null;
        }
        String fileType = fileName.substring(fileName.lastIndexOf("."));
        if(".xls".equals(fileType)){
            return new HSSFWorkbook(inputStream);
        } else if(".xlsx".equals(fileType)){
            return new XSSFWorkbook(inputStream);
        }else{
            return null;
        }
    }

    /**
     * 转换cell的类型
     * @param cell
     * @return
     */
    private String getCellValue(Cell cell) {
        if(cell == null) {
            return "";
        }
        CellType cellType = cell.getCellTypeEnum();
        switch (cellType) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
            default:
                return "";
        }
    }

    private int changeType(String type) {
        if(StringUtil.isEmpty(type)) {
            return -1;
        }
        return DeviceRoleEnum.getByDesc(type).getCode();
    }
}
