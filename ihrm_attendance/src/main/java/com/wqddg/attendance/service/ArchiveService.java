package com.wqddg.attendance.service;


import com.wqddg.attendance.dao.*;
import com.wqddg.common.utils.IdWorker;
import com.wqddg.domain.atte.entity.ArchiveMonthly;
import com.wqddg.domain.atte.entity.ArchiveMonthlyInfo;
import com.wqddg.domain.system.User;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.*;

@Service
@Slf4j
public class ArchiveService {

    @Autowired
    private ArchiveMonthlyInfoDao archiveMonthlyInfoDao;

    @Autowired
    private ArchiveMonthlyDao archiveMonthlyDao;

    @Autowired
    private UserDao userDao;
    @Autowired
    private AttendanceDao attendanceDao;

    @Autowired
    private IdWorker idWorker;


    public void saveArchive(String archiveDate, String companyId) {
        //查询所有的企业用户
        List<User> byUserCompanyId = userDao.findByCompanyId(companyId);
        //保存成归档明细数据
        ArchiveMonthly archiveMonthly=new ArchiveMonthly();
        archiveMonthly.setId(idWorker.nextId()+"");
        archiveMonthly.setCompanyId(companyId);
        archiveMonthly.setArchiveYear(archiveDate.substring(0,4));
        archiveMonthly.setArchiveMonth(archiveDate.substring(5));
        //保存成归档明细数据
        for (User user : byUserCompanyId) {
            ArchiveMonthlyInfo archiveMonthlyInfo=new ArchiveMonthlyInfo(user);
            //统计每个用户的考勤用户
            Map<String,Object> maps=attendanceDao.statusByUser(user.getId(),archiveDate+"%");
            archiveMonthlyInfo.setStatisData(maps);
            archiveMonthlyInfo.setId(idWorker.nextId()+"");
            archiveMonthlyInfo.setAtteArchiveMonthlyId(archiveMonthly.getId());
            archiveMonthlyInfoDao.save(archiveMonthlyInfo);
        }
        archiveMonthly.setTotalPeopleNum(byUserCompanyId.size());
        archiveMonthly.setFullAttePeopleNum(byUserCompanyId.size());
        archiveMonthly.setIsArchived(0);
        archiveMonthlyDao.save(archiveMonthly);
    }

    public List<ArchiveMonthlyInfo> findMonthlyinfoByAmid(String id, String companyId) {
        return archiveMonthlyInfoDao.findByAtteArchiveMonthlyId(id);
    }

    /**
     *
     * @param userId
     * @param yearMonth
     * @return
     */
    public ArchiveMonthlyInfo findUserIdDetail(String userId, String yearMonth) {
        return archiveMonthlyInfoDao.findByUserIdAndArchiveDate(userId,yearMonth);
    }
}
