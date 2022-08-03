package com.wqddg.attendance.service;

import com.wqddg.attendance.dao.ArchiveMonthlyInfoDao;
import com.wqddg.domain.atte.entity.ArchiveMonthlyInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: wqddg
 * @ClassName ArchiveMonthlyInfoService
 * @DateTime: 2022/1/17 16:34
 * @remarks : #
 */
@Service
public class ArchiveMonthlyInfoService {

    @Autowired
    private ArchiveMonthlyInfoDao archiveMonthlyInfoDao;





    public List<ArchiveMonthlyInfo> getReports(String atteDate, String companyId) {
        return null;
    }
}
