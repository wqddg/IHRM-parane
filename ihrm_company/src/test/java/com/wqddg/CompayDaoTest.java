package com.wqddg;

import com.wqddg.company.CompanyApplication;
import com.wqddg.company.dao.CompanyDao;
import com.wqddg.domain.company.Company;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * @Author: wqddg
 * @ClassName CompayDaoTest
 * @DateTime: 2022/1/3 22:52
 * @remarks : #
 */
@SpringBootTest(classes = CompanyApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class CompayDaoTest {

    @Autowired
    private CompanyDao companyDao;

    @Test
    public void test(){
        List<Company> all =
                companyDao.findAll();
        for (Company company : all) {
            System.out.println(company);
        }
    }
}
