package com.wqddg.social.controller;

import com.wqddg.common.controller.BaseController;
import com.wqddg.common.entity.PageResult;
import com.wqddg.common.entity.Result;
import com.wqddg.common.entity.ResultCode;
import com.wqddg.domain.social_security.*;
import com.wqddg.social.client.SystemFeignClient;
import com.wqddg.social.service.ArchiveService;
import com.wqddg.social.service.CompanySettingsService;
import com.wqddg.social.service.PaymentItemService;
import com.wqddg.social.service.UserSocialService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: wqddg
 * @ClassName SocialSecurityController
 * @DateTime: 2022/1/14 23:42
 * @remarks : #
 */
@RestController
@RequestMapping("social_securitys")
public class SocialSecurityController extends BaseController {

    @Autowired
    private CompanySettingsService settingsService;

    @Autowired
    private UserSocialService userSocialService;

    @Autowired
    private SystemFeignClient systemFeignClient;

    @Autowired
    private PaymentItemService paymentItemService;
    @Autowired
    private ArchiveService archiveService;

    /**
     * 查询企业是否设置过社保
     * @return
     */
    @GetMapping("settings")
    public Result settings(){
        CompanySettings settings =settingsService.findByid(companyId);
        return new Result(ResultCode.SUCCESS,settings);
    }

    @PostMapping("settings")
    public Result saveSettings(@RequestBody CompanySettings companySettings){
        companySettings.setCompanyId(companyId);
        settingsService.save(companySettings);
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 查询企业员工的社保信息列表
     * @return
     */
    @PostMapping("list")
    public Result listSettings(@RequestBody Map<String,Object> maps){
        Integer page= (Integer) maps.get("page");
        Integer pageSize= (Integer) maps.get("pageSize");
        PageResult result =userSocialService.findAll(page,pageSize,companyId);
        return new Result(ResultCode.SUCCESS,result);
    }

    /**
     * 查询企业员工的社保信息列表
     * @return
     */
    @GetMapping("{id}")
    public Result listByidSettings(@PathVariable String id){
        Map map=new HashMap();
        //1.根据用户id查询用户数据
        Object data = systemFeignClient.findById(id).getData();
        map.put("user",data);
        //2.根据用户id查询社保数据
        UserSocialSecurity socialSecurity =userSocialService.finbyId(id);
        map.put("userSocialSecurity",socialSecurity);
        return new Result(ResultCode.SUCCESS,map);
    }
    /**
     * 查询城市id的社保信息列表
     * @return
     */
    @GetMapping("payment_item/{id}")
    public Result findByidSettingsItem(@PathVariable String id){
        List<CityPaymentItem> list=  paymentItemService.findAllByCityId(id);

        return new Result(ResultCode.SUCCESS,list);
    }


    @PutMapping("{id}")
    public Result saveSettings(@PathVariable String id,@RequestBody UserSocialSecurity uss){
        userSocialService.save(uss);
        return new Result(ResultCode.SUCCESS);
    }

    /**
     *
     * @param yearMonth
     * @param opType  1当月数据    其他 (历史归档的详细记录)
     * @return
     */
    @GetMapping("historys/{yearMonth}")
    public Result historys(@PathVariable String yearMonth,Integer opType) throws Exception {
        List<ArchiveDetail> lists=new ArrayList<>();
        if (opType==1){
            //未归档  查询当月的详细记录
            lists= archiveService.getReports( yearMonth,companyId);
        }else {
            //已归档的数据
            //1.根据月和企业id 查询归档记录
            Archive archive = archiveService.findArchive(companyId, yearMonth);
            if (archive!=null){
                lists=archiveService.findAllDetailByArchiveId(archive.getId());
            }
        }
        return new Result(ResultCode.SUCCESS,lists);
    }
    /**
     *historys/202201/archive
     * @param yearMonth
     * @return
     */
    @PostMapping("historys/{yearMonth}/archive")
    public Result historysArchive(@PathVariable String yearMonth) throws Exception {
        archiveService.archive(yearMonth,companyId);
        return new Result(ResultCode.SUCCESS);
    }
    /**
     *historys/202202?yearMonth=202202&opType=1  制作新的报表
     * @param yearMonth
     * @return
     */
    @PutMapping("historys/{yearMonth}/newReport")
    public Result historysAndArchive(@PathVariable String yearMonth) {
        CompanySettings companySettings=settingsService.findByid(companyId);
        if (companySettings==null){
            companySettings=new CompanySettings();
        }
        companySettings.setCompanyId(companyId);
        companySettings.setDataMonth(yearMonth);
        settingsService.save(companySettings);
        return new Result(ResultCode.SUCCESS);
    }
    /**
     *historys/2019/list?year=2019  查询历史归档的列表
     * @param year
     * @return
     */
    @GetMapping("historys/{year}/list")
    public Result historysAndYearArchive(@PathVariable String year) {
        List<Archive> lists=archiveService.findArchiveByYear(companyId,year);
        return new Result(ResultCode.SUCCESS,lists);
    }


    /**
     * 根据用户id 和考勤年月查询用户考勤归档明细
     * @return
     */
    @GetMapping("historys/data")
    public ArchiveDetail history(String userId,String yearMonth) {
        return archiveService.findUserIdAndYearMonth(userId,yearMonth);
    }








}
