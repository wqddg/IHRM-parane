package com.wqddg.attendance.service;

import com.wqddg.attendance.dao.AttendanceConfigDao;
import com.wqddg.common.utils.IdWorker;
import com.wqddg.domain.atte.entity.AttendanceConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;


@Service
public class ConfigurationService{

    @Autowired
    private AttendanceConfigDao attendanceConfigDao;

    @Autowired
    private IdWorker idWorker;

    /**
     *查询考勤设置
     * @param companyId
     * @param departmentId
     * @return
     */
    public AttendanceConfig getAtteConfig(String companyId, String departmentId) {
        return attendanceConfigDao.findByCompanyIdAndDepartmentId(companyId,departmentId);
    }

    /**
     * 保存或者更新考勤设置
     * @param attendanceConfig
     */
    public void saveAtteConfig(AttendanceConfig attendanceConfig) {
        AttendanceConfig byCompanyIdAndDepartmentId = attendanceConfigDao.findByCompanyIdAndDepartmentId(attendanceConfig.getCompanyId(), attendanceConfig.getDepartmentId());
        if (byCompanyIdAndDepartmentId==null){
            attendanceConfig.setId(idWorker.nextId()+"");
            attendanceConfig.setCreateDate(new Date());
        }else {
            attendanceConfig.setId(byCompanyIdAndDepartmentId.getId());
            attendanceConfig.setUpdateDate(new Date());
        }
        attendanceConfigDao.save(attendanceConfig);
    }
}
