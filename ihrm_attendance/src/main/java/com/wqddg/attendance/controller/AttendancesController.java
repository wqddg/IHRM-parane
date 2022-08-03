package com.wqddg.attendance.controller;

import com.wqddg.attendance.dao.ArchiveMonthlyInfoDao;
import com.wqddg.attendance.service.ArchiveMonthlyInfoService;
import com.wqddg.attendance.service.ArchiveService;
import com.wqddg.attendance.service.AtteService;
import com.wqddg.attendance.service.ExcelImportService;
import com.wqddg.common.controller.BaseController;
import com.wqddg.common.entity.Result;
import com.wqddg.common.entity.ResultCode;
import com.wqddg.domain.atte.entity.ArchiveMonthly;
import com.wqddg.domain.atte.entity.ArchiveMonthlyInfo;
import com.wqddg.domain.atte.entity.Attendance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.ws.rs.GET;
import java.util.List;
import java.util.Map;

/**
 * @Author: wqddg
 * @ClassName AttendancesController
 * @DateTime: 2022/1/17 14:46
 * @remarks : #
 */
@RequestMapping("attendances")
@RestController
public class AttendancesController extends BaseController {

    @Autowired
    private ExcelImportService excelImportService;

    @Autowired
    private AtteService atteService;
    @Autowired
    private ArchiveService archiveService;

    @PostMapping("import")
    public Result importExcel(@RequestParam(name = "file")MultipartFile file) throws Exception {


        excelImportService.importAttendaneExcel(file,companyId);

        return new Result(ResultCode.SUCCESS);
    }

    @GetMapping("")
    public Result importWqddg(Integer page,Integer pagesize) throws Exception {
        Map map=atteService.getAtteData(companyId,page,pagesize);
        return new Result(ResultCode.SUCCESS,map);
    }
    @PutMapping("{id}")
    public Result editWqddg(@RequestBody Attendance attendance) throws Exception {
        atteService.save(attendance);
        return new Result(ResultCode.SUCCESS);
    }


    @GetMapping("reports")
    public Result reports(String atteDate){
        List<ArchiveMonthlyInfo> lists=atteService.getReports(atteDate,companyId);
        return new Result(ResultCode.SUCCESS,lists);
    }
    @GetMapping("archive/item")
    public Result archiveItem(String archiveDate){
        archiveService.saveArchive(archiveDate,companyId);
        return new Result(ResultCode.SUCCESS);
    }


    @PostMapping("reports")
    public Result newReports(@RequestBody String atteDate){
//        atteDate=atteDate.substring(0,5);

        atteService.newReports(companyId,atteDate);
        return new Result(ResultCode.SUCCESS);
    }


    /**
     * http://localhost:8080/api/attendances/reports/year?departmentId=1175310929766055936&year=2021
     */
    @GetMapping("reports/year")
    public Result newReports(String departmentId,String year){
        List<ArchiveMonthly> lists=atteService.fingByDepartmentIdAndYear(departmentId,year,companyId);
        return new Result(ResultCode.SUCCESS,lists);
    }


    @PostMapping("reports/{id}")
    public Result findInfoByID(@PathVariable String id){
        List<ArchiveMonthlyInfo> lists=archiveService.findMonthlyinfoByAmid(id,companyId);
        return new Result(ResultCode.SUCCESS,lists);
    }


    /**
     * 根据用户id和月份  查询已归档的考勤明细
     */
    @GetMapping("archive/{userId}/{yearMonth}")
    public Result historyData(@PathVariable String userId,@PathVariable String yearMonth){
        ArchiveMonthlyInfo info=archiveService.findUserIdDetail(userId,yearMonth);
        return new Result(ResultCode.SUCCESS,info);
    }


}
