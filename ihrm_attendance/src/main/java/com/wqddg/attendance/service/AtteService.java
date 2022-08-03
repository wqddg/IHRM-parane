package com.wqddg.attendance.service;


import com.wqddg.attendance.dao.ArchiveMonthlyDao;
import com.wqddg.attendance.dao.AttendanceDao;
import com.wqddg.attendance.dao.CompanySettingsDao;
import com.wqddg.attendance.dao.UserDao;
import com.wqddg.common.entity.PageResult;
import com.wqddg.common.utils.DateUtil;
import com.wqddg.common.utils.IdWorker;
import com.wqddg.domain.atte.bo.AtteItemBO;
import com.wqddg.domain.atte.entity.ArchiveMonthly;
import com.wqddg.domain.atte.entity.ArchiveMonthlyInfo;
import com.wqddg.domain.atte.entity.Attendance;
import com.wqddg.domain.social_security.CompanySettings;
import com.wqddg.domain.system.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class AtteService  {

    @Autowired
    private CompanySettingsDao companySettingsDao;

    @Autowired
    private UserDao userDao;
    @Autowired
    private AttendanceDao attendanceDao;

    @Autowired
    private ArchiveMonthlyDao archiveMonthlyDao;

    @Autowired
    private IdWorker idWorker;

    public Map getAtteData(String companyId, Integer page, Integer pagesize) throws Exception {
        //1.考勤月
        CompanySettings companySettings = companySettingsDao.findById(companyId).get();
        String dataMonth=companySettings.getDataMonth();
        //分页查询用户
        Page<User> page1 = userDao.findPage(companyId, PageRequest.of(page - 1, pagesize));
        //3.循环所有的用户、获取每个用户每天的考勤情况
        List<AtteItemBO> lists=new ArrayList<>();
        for (User user : page1.getContent()) {
            AtteItemBO atteItemBO=new AtteItemBO();
            BeanUtils.copyProperties(user,atteItemBO);
            List<Attendance> attendances=new ArrayList<>();
            //获取当前月所有的天数
            String[] daysByYearMonth = DateUtil.getDaysByYearMonth(dataMonth);
            //循环每天查询考勤状态
            for (String s : daysByYearMonth) {
                Attendance attendance = attendanceDao.findByUserIdAndDay(user.getId(), s);
                if (attendance==null){
                    attendance=new Attendance();
                    attendance.setAdtStatu(2);
                    attendance.setId(user.getId());
                    attendance.setDay(s);
                }
                attendances.add(attendance);
            }
            atteItemBO.setAttendanceRecord(attendances);
            //封装到我们的集合中
            lists.add(atteItemBO);
        }
        Map<String,Object> maps=new HashMap<>();
        //数据分页对象
        PageResult result = new PageResult(page1.getTotalElements(), lists);
        maps.put("data",result);
        //待处理的考勤数量
        maps.put("tobeTaskCount",0);
        //3.当前考勤的月份
        int i=Integer.parseInt(dataMonth.substring(4));
        maps.put("monthOfReport",i);
        return maps;
    }

    public void save(Attendance attendance) {
        Attendance byUserIdAndDay = attendanceDao.findByUserIdAndDay(attendance.getUserId(), attendance.getDay());
        if (byUserIdAndDay==null){
            attendance.setId(idWorker.nextId()+"");
        }else {
            attendance.setId(byUserIdAndDay.getId());
        }
        attendanceDao.save(attendance);
    }

    public List<ArchiveMonthlyInfo> getReports(String atteDate, String companyId) {
        //查询所有的企业用户
        List<User> byUserCompanyId = userDao.findByCompanyId(companyId);
        List<ArchiveMonthlyInfo> lists=new ArrayList<>();
        for (User user : byUserCompanyId) {
            ArchiveMonthlyInfo archiveMonthlyInfo=new ArchiveMonthlyInfo(user);
            //统计每个用户的考勤用户
            Map<String,Object> maps=attendanceDao.statusByUser(user.getId(),atteDate+"%");
            archiveMonthlyInfo.setStatisData(maps);
            lists.add(archiveMonthlyInfo);
        }
        return lists;
    }

    /**
     * 新建报表
     * @param companyId
     * @param yearMonth
     */
    public void newReports(String companyId, String yearMonth) {
        CompanySettings companySettings = companySettingsDao.findById(companyId).get();
        companySettings.setDataMonth(yearMonth);
        companySettingsDao.save(companySettings);
    }

    public List<ArchiveMonthly> fingByDepartmentIdAndYear(String departmentId, String year, String companyId) {
        return archiveMonthlyDao.findByCompanyIdAndArchiveYear(companyId,year);
    }
}
