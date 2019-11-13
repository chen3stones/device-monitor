package com.chen.devicemonitor.service;

import com.chen.devicemonitor.dao.DeviceMapper;
import com.chen.devicemonitor.entity.User;
import com.chen.devicemonitor.enumeration.DeviceRoleEnum;
import com.chen.devicemonitor.entity.Device;
import com.chen.devicemonitor.enumeration.UserRoleEnum;
import com.chen.devicemonitor.util.StringUtil;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
@Service
public class ExcelService {
    @Autowired
    DeviceMapper deviceMapper;
    public List<Device> getDeviceFromExcel(InputStream inputStream,String fileName) throws  Exception{
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
                device.setType(changeDeviceType(getCellValue(row.getCell(k+1))));
                device.setDIP(getCellValue(row.getCell(k+2)));
                device.setDPort(getCellValue(row.getCell(k+3)));
                list.add(device);
            }
        }
        return list;
    }

    public List<User> getUserFromExcel(InputStream inputStream,String fileName) throws Exception {
        List<User> users = new ArrayList<>();
        Workbook workbook = check(inputStream,fileName);
        if(workbook == null) {
            throw new Exception("Excel格式错误");
        }
        Sheet sheet = null;
        Row row = null;
        for(int i = 0;i < workbook.getNumberOfSheets();i++){
            sheet = workbook.getSheetAt(i);
            if(sheet == null) {
                continue;
            }
            for(int j = sheet.getFirstRowNum()+1;j < sheet.getLastRowNum();j++){
                row = sheet.getRow(j);
                if(row == null){
                    continue;
                }

                User user = new User();
                int k = row.getFirstCellNum();
                user.setUName(getCellValue(row.getCell(k)));
                user.setType(changeUserType(getCellValue(row.getCell(k+1))));
                user.setPhone(getCellValue(row.getCell(k+2)));
                users.add(user);
            }
        }
        return users;
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
                return String.valueOf((int)cell.getNumericCellValue());
            default:
                return "";
        }
    }

    private int changeDeviceType(String type) {
        if(StringUtil.isEmpty(type)) {
            return -1;
        }
        return DeviceRoleEnum.getByDesc(type).getCode();
    }
    private int changeUserType(String type) {
        if(StringUtil.isEmpty(type)){
            return -1;
        }
        return UserRoleEnum.getByDesc(type).getCode();
    }

    public Workbook createDeviceExcelFile() throws Exception{
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("设备表");
        createDeviceHead(sheet);
        List<Device> deviceList = deviceMapper.getAllDevice();
        int rowNumber = 1;
        for(Device device : deviceList) {
            HSSFRow row = sheet.createRow(rowNumber++);
            row.createCell(0).setCellValue(device.getDName());
            row.createCell(1).setCellValue(DeviceRoleEnum.getByCode(device.getType()).getDesc());
            row.createCell(2).setCellValue(device.getDIP());
            row.createCell(3).setCellValue(device.getDPort());
        }
        //buildExcelFile("设备表",workbook);
        return workbook;
    }
    //创建表头
    private void createDeviceHead(HSSFSheet sheet){
        HSSFRow firstRow = sheet.createRow(0);
        //firstRow.setHeight((short) 3);
        //设置列宽
        sheet.setColumnWidth(0,10*256);
        sheet.setColumnWidth(1,15*256);
        sheet.setColumnWidth(2,32*256);
        sheet.setColumnWidth(3,8*256);
        //设置表头名称
        HSSFCell cell;
        cell = firstRow.createCell(0);
        cell.setCellValue("服务器名称");
        cell = firstRow.createCell(1);
        cell.setCellValue("服务器类型");
        cell = firstRow.createCell(2);
        cell.setCellValue("IP");
        cell = firstRow.createCell(3);
        cell.setCellValue("端口");
    }

    /**
     * 生成excel文件
     * @param fileName
     * @param workbook
     * @throws Exception
     */
    private void buildExcelFile(String fileName,HSSFWorkbook workbook) throws Exception{
        FileOutputStream fileOutputStream = new FileOutputStream(fileName);
        workbook.write(fileOutputStream);
        fileOutputStream.flush();
        fileOutputStream.close();
    }

}
