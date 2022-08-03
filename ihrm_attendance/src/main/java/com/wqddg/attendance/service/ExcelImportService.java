package com.wqddg.attendance.service;

import com.wqddg.attendance.dao.AttendanceConfigDao;
import com.wqddg.attendance.dao.AttendanceDao;
import com.wqddg.common.utils.DateUtil;
import com.wqddg.attendance.dao.UserDao;
import com.wqddg.common.utils.IdWorker;
import com.wqddg.domain.atte.entity.Attendance;
import com.wqddg.domain.atte.entity.AttendanceConfig;
import com.wqddg.domain.atte.vo.AtteUploadVo;
import com.wqddg.domain.poi.ExcelImportUtil;
import com.wqddg.domain.system.User;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Log4j2
@Service
public class ExcelImportService {

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private UserDao userDao;

    @Autowired
    private AttendanceDao attendanceDao;

    @Autowired
    private AttendanceConfigDao attendanceConfigDao;

    /**
     * atte:
     *   holidays: 1001,1002,1003,0101
     *   wordingDays: 0927
     */
    @Value("atte.holidays")
    private String holidays;
    @Value("atte.wordingDays")
    private String wordingDays;


    public void importAttendaneExcel(MultipartFile file, String companyId) throws Exception {
        //1.将导入的excel文件解析为vo的list集合
        List<AtteUploadVo> atteUploadVos = new ExcelImportUtil<AtteUploadVo>(AtteUploadVo.class).readExcel(file.getInputStream(), 1, 0);
        //2.循环list集合
        for (AtteUploadVo atteUploadVo : atteUploadVos) {
            //2.1 更加上传的手机号查询用户
            User byMobile = userDao.findByMobile(atteUploadVo.getMobile());
            //2.2构造考勤对象
            Attendance attendance=new Attendance(atteUploadVo,byMobile);
            attendance.setDay(atteUploadVo.getAtteData());
            //2.3判断是否休假
            /**
             * 1.将国家的假日记录到数据库中
             * 2.文件
             */
            if (holidays.contains(atteUploadVo.getAtteData())){
                attendance.setAdtStatu(23);//休息
            }else if (DateUtil.isWeekend(atteUploadVo.getAtteData())||!wordingDays.contains(atteUploadVo.getAtteData())){
                attendance.setAdtStatu(23);//休息
            }else {
                //2.4判断早退、迟到的状态
                AttendanceConfig ac = attendanceConfigDao.findByCompanyIdAndDepartmentId(companyId, byMobile.getDepartmentId());
                if (DateUtil.comparingDate(ac.getAfternoonStartTime(),atteUploadVo.getInTime())){
                    attendance.setAdtStatu(3);//迟到
                }
                if (DateUtil.comparingDate(ac.getAfternoonEndTime(),atteUploadVo.getOutTime())){
                    attendance.setAdtStatu(4);//早退
                }
                if (!DateUtil.comparingDate(ac.getAfternoonStartTime(),atteUploadVo.getInTime())&&!DateUtil.comparingDate(ac.getAfternoonEndTime(),atteUploadVo.getOutTime())){
                    attendance.setAdtStatu(1);//早退
                }
            }
            //2.5查询用户已经有考勤记录，如果不存在，保存数据库
            Attendance byUserIdAndDay = attendanceDao.findByUserIdAndDay(byMobile.getId(), atteUploadVo.getAtteData());
            if (byUserIdAndDay==null){
                attendance.setId(idWorker.nextId()+"");
                attendanceDao.save(attendance);
            }
        }
    }
}
