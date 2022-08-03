package com.wqddg.social.dao;

import com.wqddg.domain.social_security.UserSocialSecurity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Map;

public interface UserSocialSecurityDao extends JpaRepository<UserSocialSecurity, String>, JpaSpecificationExecutor<UserSocialSecurity> {

    @Query(
            nativeQuery = true,
            value = "SELECT bu.id," +
                    "       bu.username," +
                    "       bu.mobile," +
                    "       bu.work_number  workNumber," +
                    "       bu.department_name departmentName," +
                    "       bu.time_of_entry timeOfEntry," +
                    "       bu.time_of_dimission leaveTime, " +
                    "       ssuss.participating_in_the_city participatingInTheCity, " +
                    "       ssuss.participating_in_the_city_id participatingInTheCityId," +
                    "       ssuss.provident_fund_city_id providentFundCityId, " +
                    "       ssuss.provident_fund_city providentFundCity," +
                    "       ssuss.social_security_base socialSecurityBase," +
                    "       ssuss.provident_fund_base providentFundBase FROM bs_user bu LEFT JOIN ss_user_social_security ssuss ON bu.id=ssuss.user_id WHERE bu.company_id=?1",
            countQuery = "SELECT count(*) FROM bs_user u LEFT JOIN ss_user_social_security s on u.id=s.user_id WHERE u.company_id=?1"
    )
    Page<Map> findPage(String companyId, Pageable pageable);
}
