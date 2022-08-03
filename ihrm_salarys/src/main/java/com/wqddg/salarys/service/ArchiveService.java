package com.wqddg.salarys.service;

import com.wqddg.common.utils.IdWorker;
import com.wqddg.domain.salarys.SalaryArchive;
import com.wqddg.domain.salarys.SalaryArchiveDetail;
import com.wqddg.salarys.dao.ArchiveDao;
import com.wqddg.salarys.dao.ArchiveDetailDao;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ArchiveService {
    @Autowired
    private ArchiveDao archiveDao;
    @Autowired
    private ArchiveDetailDao archiveDetailDao;
    @Autowired
    private IdWorker idWorker;

}
