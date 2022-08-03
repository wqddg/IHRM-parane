package com.wqddg.social.dao;

import com.wqddg.domain.social_security.Archive;
import com.wqddg.domain.social_security.ArchiveDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 自定义dao接口继承
 * JpaRepository<实体类，主键>
 * JpaSpecificationExecutor<实体类>
 */
@Component("archiveDetailDao_sec")
public interface ArchiveDetailDao extends JpaRepository<ArchiveDetail, String>, JpaSpecificationExecutor<ArchiveDetail> {

    void deleteByArchiveId(String archiveId);

    List<ArchiveDetail> findByArchiveId(String archiveId);

    ArchiveDetail findByUserIdAndYearsMonth(String userId,String yearsMonth);
}
