package com.wqddg.employee.controller;

import com.alibaba.fastjson.JSON;
import com.wqddg.common.controller.BaseController;
import com.wqddg.common.entity.PageResult;
import com.wqddg.common.entity.Result;
import com.wqddg.common.entity.ResultCode;
//import com.wqddg.common.exception.CommonException;
//import com.wqddg.common.poi.utils.ExcelExportUtil;
import com.wqddg.common.utils.BeanMapUtils;
import com.wqddg.common.utils.DownloadUtils;
import com.wqddg.domain.employee.*;
import com.wqddg.domain.employee.response.EmployeeReportResult;
//import com.wqddg.domain.system.vo.UserVo;
import com.wqddg.employee.service.*;
import io.jsonwebtoken.Claims;
//import org.apache.poi.hssf.usermodel.HSSFWorkbook;
//import org.apache.poi.poifs.filesystem.POIFSFileSystem;
//import org.apache.poi.ss.formula.functions.T;
//import org.apache.poi.ss.usermodel.*;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import java.io.*;
import java.net.URLEncoder;
import java.util.*;


@RestController
@RequestMapping("/employees")
public class EmployeeController extends BaseController {
    @Autowired
    private UserCompanyPersonalService userCompanyPersonalService;
    @Autowired
    private UserCompanyJobsService userCompanyJobsService;
    @Autowired
    private ResignationService resignationService;
    @Autowired
    private TransferPositionService transferPositionService;
    @Autowired
    private PositiveService positiveService;
    @Autowired
    private ArchiveService archiveService;


    @GetMapping("{id}/pdf")
    public void pdf(@PathVariable String id) throws Exception{
        Resource resource = new ClassPathResource("templates/profile.jasper");
        FileInputStream fileInputStream = new FileInputStream(resource.getFile());
        //????????????
        UserCompanyPersonal personals = userCompanyPersonalService.findById(id);
        UserCompanyJobs jobs = userCompanyJobsService.findById(id);
        String staffPhoto="http://r5gaoz43g.hn-bkt.clouddn.com/"+id+"?t=1641745279160";
        Map<String, Object> maps = new HashMap<>();
        maps.put("staffPhoto",staffPhoto);
        Map<String, Object> map1 = BeanMapUtils.beanToMap(personals);
        Map<String, Object> map2 = BeanMapUtils.beanToMap(jobs);
        maps.putAll(map1);
        maps.putAll(map2);
        ServletOutputStream outputStream = response.getOutputStream();
        JasperPrint print= JasperFillManager.fillReport(fileInputStream,maps,new JREmptyDataSource());
        JasperExportManager.exportReportToPdfStream(print,outputStream);
    }



    /**
     * ????????????????????????
     */
    @RequestMapping(value = "/{id}/personalInfo", method = RequestMethod.PUT)
    public Result savePersonalInfo(@PathVariable(name = "id") String uid, @RequestBody Map map) throws Exception {
        UserCompanyPersonal sourceInfo = BeanMapUtils.mapToBean(map, UserCompanyPersonal.class);
        if (sourceInfo == null) {
            sourceInfo = new UserCompanyPersonal();
        }
        sourceInfo.setUserId(uid);
        sourceInfo.setCompanyId(super.companyId);
        userCompanyPersonalService.save(sourceInfo);
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * ????????????????????????
     */
    @RequestMapping(value = "/{id}/personalInfo", method = RequestMethod.GET)
    public Result findPersonalInfo(@PathVariable(name = "id") String uid) throws Exception {
        UserCompanyPersonal info = userCompanyPersonalService.findById(uid);
        if(info == null) {
            info = new UserCompanyPersonal();
            info.setUserId(uid);
        }
        return new Result(ResultCode.SUCCESS,info);
    }

    /**
     * ????????????????????????
     */
    @RequestMapping(value = "/{id}/jobs", method = RequestMethod.PUT)
    public Result saveJobsInfo(@PathVariable(name = "id") String uid, @RequestBody UserCompanyJobs sourceInfo) throws Exception {
        //????????????????????????
        if (sourceInfo == null) {
            sourceInfo = new UserCompanyJobs();
            sourceInfo.setUserId(uid);
            sourceInfo.setCompanyId(super.companyId);
        }
        userCompanyJobsService.save(sourceInfo);
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * ????????????????????????
     */
    @RequestMapping(value = "/{id}/jobs", method = RequestMethod.GET)
    public Result findJobsInfo(@PathVariable(name = "id") String uid) throws Exception {
        UserCompanyJobs info = userCompanyJobsService.findById(super.userId);
        if(info == null) {
            info = new UserCompanyJobs();
            info.setUserId(uid);
            info.setCompanyId(companyId);
        }
        return new Result(ResultCode.SUCCESS,info);
    }

    /**
     * ??????????????????
     */
    @RequestMapping(value = "/{id}/leave", method = RequestMethod.PUT)
    public Result saveLeave(@PathVariable(name = "id") String uid, @RequestBody EmployeeResignation resignation) throws Exception {
        resignation.setUserId(uid);
        resignationService.save(resignation);
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * ??????????????????
     */
    @RequestMapping(value = "/{id}/leave", method = RequestMethod.GET)
    public Result findLeave(@PathVariable(name = "id") String uid) throws Exception {
        EmployeeResignation resignation = resignationService.findById(uid);
        if(resignation == null) {
            resignation = new EmployeeResignation();
            resignation.setUserId(uid);
        }
        return new Result(ResultCode.SUCCESS,resignation);
    }

    /**
     * ????????????
     */
    @RequestMapping(value = "/import", method = RequestMethod.POST)
    public Result importDatas(@RequestParam(name = "file") MultipartFile attachment) throws Exception {
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * ??????????????????
     */
    @RequestMapping(value = "/{id}/transferPosition", method = RequestMethod.PUT)
    public Result saveTransferPosition(@PathVariable(name = "id") String uid, @RequestBody EmployeeTransferPosition transferPosition) throws Exception {
        transferPosition.setUserId(uid);
        transferPositionService.save(transferPosition);
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * ??????????????????
     */
    @RequestMapping(value = "/{id}/transferPosition", method = RequestMethod.GET)
    public Result findTransferPosition(@PathVariable(name = "id") String uid) throws Exception {
        UserCompanyJobs jobsInfo = userCompanyJobsService.findById(uid);
        if(jobsInfo == null) {
            jobsInfo = new UserCompanyJobs();
            jobsInfo.setUserId(uid);
        }
        return new Result(ResultCode.SUCCESS,jobsInfo);
    }

    /**
     * ??????????????????
     */
    @RequestMapping(value = "/{id}/positive", method = RequestMethod.PUT)
    public Result savePositive(@PathVariable(name = "id") String uid, @RequestBody EmployeePositive positive) throws Exception {
        positiveService.save(positive);
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * ??????????????????
     */
    @RequestMapping(value = "/{id}/positive", method = RequestMethod.GET)
    public Result findPositive(@PathVariable(name = "id") String uid) throws Exception {
        EmployeePositive positive = positiveService.findById(uid);
        if(positive == null) {
            positive = new EmployeePositive();
            positive.setUserId(uid);
        }
        return new Result(ResultCode.SUCCESS,positive);
    }

    /**
     * ????????????????????????
     */
    @RequestMapping(value = "/archives/{month}", method = RequestMethod.GET)
    public Result archives(@PathVariable(name = "month") String month, @RequestParam(name = "type") Integer type) throws Exception {
        return new Result(ResultCode.SUCCESS);
    }

// ?????????????????????????????????
//    @GetMapping("export/{month}")
//    public void exportS(@PathVariable String month) throws Exception {
//        //1.??????????????????
//        List<EmployeeReportResult> employeeReportResult=userCompanyPersonalService.findByReport(companyId,month);
//        //2.??????Excel
//        Workbook workbook=new XSSFWorkbook();
//        //??????sheet
//        Sheet sheet = workbook.createSheet();
//        //??????
//        String[] titles="??????,??????,??????,????????????,????????????,?????????,??????,??????,??????,????????????,????????????,????????????,????????????".split(",");
//        //????????????
//        Row row = sheet.createRow(0);
//        for (int i = 0; i < titles.length; i++) {
//            Cell cell=row.createCell(i);
//            cell.setCellValue(titles[i]);
//        }
//        Cell cell=null;
//        //????????????
//        for (int i = 0; i < employeeReportResult.size(); i++) {
//            row = sheet.createRow(i + 1);
//            // ??????,
//            cell = row.createCell(0);
//            cell.setCellValue(employeeReportResult.get(i).getUserId());
//            // ??????,
//            cell = row.createCell(1);
//            cell.setCellValue(employeeReportResult.get(i).getUsername());
//            // ??????,
//            cell = row.createCell(2);
//            cell.setCellValue(employeeReportResult.get(i).getMobile());
//            // ????????????,
//            cell = row.createCell(3);
//            cell.setCellValue(employeeReportResult.get(i).getTheHighestDegreeOfEducation());
//            // ????????????,
//            cell = row.createCell(4);
//            cell.setCellValue(employeeReportResult.get(i).getNationalArea());
//            // ?????????,
//            cell = row.createCell(5);
//            cell.setCellValue(employeeReportResult.get(i).getPassportNo());
//            // ??????,
//            cell = row.createCell(6);
//            cell.setCellValue(employeeReportResult.get(i).getNativePlace());
//            // ??????,
//            cell = row.createCell(7);
//            cell.setCellValue(employeeReportResult.get(i).getBirthday());
//            // ??????,
//            cell = row.createCell(8);
//            cell.setCellValue(employeeReportResult.get(i).getZodiac());
//            // ????????????,
//            cell = row.createCell(9);
//            cell.setCellValue(employeeReportResult.get(i).getTimeOfEntry());
//            // ????????????,
//            cell = row.createCell(10);
//            cell.setCellValue(employeeReportResult.get(i).getTypeOfTurnover());
//            // ????????????,
//            cell = row.createCell(11);
//            cell.setCellValue(employeeReportResult.get(i).getReasonsForLeaving());
//            // ????????????
//            cell = row.createCell(12);
//            cell.setCellValue(employeeReportResult.get(i).getResignationTime());
//        }
//        //????????????
//        ByteArrayOutputStream os = new ByteArrayOutputStream();
//        FileOutputStream streams=new FileOutputStream("E:\\test.xlsx");
//        workbook.write(streams);
//        workbook.write(os);
//        String fileName = URLEncoder.encode(month+"??????.xlsx", "UTF-8");
//        response.setContentType("application/octet-stream");
//        response.setHeader("content-disposition", "attachment;filename=" + new
//                String(fileName.getBytes("ISO8859-1")));
//        response.setHeader("filename", fileName);
//        workbook.write(response.getOutputStream());
//        streams.close();
//    }
    @RequestMapping(value = "export/{month}",method = RequestMethod.GET)
    public void exportS(@PathVariable String month) throws Exception {
        //1.??????????????????
        List<EmployeeReportResult> employeeReportResult=userCompanyPersonalService.findByReport(companyId,month);
        //2.????????????
        Resource resource = new ClassPathResource("excel-template/hr-demo.xlsx");
        FileInputStream fis = new FileInputStream(resource.getFile());

        //3.???????????????????????????
        if (fis==null){
            System.out.println("??????");
            return;
        }
        Workbook workbook=new XSSFWorkbook(fis);
        //4.???????????????
        Sheet sheetAt = workbook.getSheetAt(0);
        //5.??????????????????
        Row row = sheetAt.getRow(0);
        Cell cell = row.getCell(0);
        cell.setCellValue(month+"????????????");
        row=sheetAt.getRow(2);
        CellStyle style[]=new CellStyle[row.getLastCellNum()];
        for (int i = 0; i < row.getLastCellNum(); i++) {
            Cell cell1 = row.getCell(i);
            style[i]=cell1.getCellStyle();
        }
        //6??????????????????

        //????????????
        for (int i = 0; i < employeeReportResult.size(); i++) {
            row = sheetAt.createRow(i + 2);
            // ??????,
            cell = row.createCell(0);
            cell.setCellValue(employeeReportResult.get(i).getUserId());
            cell.setCellStyle(style[0]);
            // ??????,
            cell = row.createCell(1);
            cell.setCellValue(employeeReportResult.get(i).getUsername());
            cell.setCellStyle(style[1]);
            // ??????,
            cell = row.createCell(2);
            cell.setCellValue(employeeReportResult.get(i).getMobile());
            cell.setCellStyle(style[2]);
            // ????????????,
            cell = row.createCell(3);
            cell.setCellValue(employeeReportResult.get(i).getTheHighestDegreeOfEducation());
            cell.setCellStyle(style[3]);
            // ????????????,
            cell = row.createCell(4);
            cell.setCellValue(employeeReportResult.get(i).getNationalArea());
            cell.setCellStyle(style[4]);
            // ?????????,
            cell = row.createCell(5);
            cell.setCellValue(employeeReportResult.get(i).getPassportNo());
            cell.setCellStyle(style[5]);
            // ??????,
            cell = row.createCell(6);
            cell.setCellValue(employeeReportResult.get(i).getNativePlace());
            cell.setCellStyle(style[6]);
            // ??????,
            cell = row.createCell(7);
            cell.setCellValue(employeeReportResult.get(i).getBirthday());
            cell.setCellStyle(style[7]);
            // ??????,
            cell = row.createCell(8);
            cell.setCellValue(employeeReportResult.get(i).getZodiac());
            cell.setCellStyle(style[8]);
            // ????????????,
            cell = row.createCell(9);
            cell.setCellValue(employeeReportResult.get(i).getTimeOfEntry());
            cell.setCellStyle(style[9]);
            // ????????????,
            cell = row.createCell(10);
            cell.setCellValue(employeeReportResult.get(i).getTypeOfTurnover());
            cell.setCellStyle(style[10]);
            // ????????????,
            cell = row.createCell(11);
            cell.setCellValue(employeeReportResult.get(i).getReasonsForLeaving());
            cell.setCellStyle(style[11]);
            // ????????????
            cell = row.createCell(12);
            cell.setCellValue(employeeReportResult.get(i).getResignationTime());
            cell.setCellStyle(style[12]);
        }

        //7.??????
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        FileOutputStream streams=new FileOutputStream("E:\\test.xlsx");
        workbook.write(streams);
        workbook.write(os);
        String fileName = URLEncoder.encode(month+"??????.xlsx", "UTF-8");
        response.setContentType("application/octet-stream");
        response.setHeader("content-disposition", "attachment;filename=" + new
                String(fileName.getBytes("ISO8859-1")));
        response.setHeader("filename", fileName);
        workbook.write(response.getOutputStream());
        streams.close();
    }

    /**
     * ????????????
     */
    @RequestMapping(value = "/archives/{month}", method = RequestMethod.PUT)
    public Result saveArchives(@PathVariable(name = "month") String month) throws Exception {


        return new Result(ResultCode.SUCCESS);
    }

    /**
     * ??????????????????
     */
    @RequestMapping(value = "/archives", method = RequestMethod.GET)
    public Result findArchives(@RequestParam(name = "pagesize") Integer pagesize, @RequestParam(name = "page") Integer page, @RequestParam(name = "year") String year) throws Exception {
        Map map = new HashMap();
        map.put("year",year);
        map.put("companyId",companyId);
        Page<EmployeeArchive> searchPage = archiveService.findSearch(map, page, pagesize);
        PageResult<EmployeeArchive> pr = new PageResult(searchPage.getTotalElements(),searchPage.getContent());
        return new Result(ResultCode.SUCCESS,pr);
    }
}
