package com.wqddg.company.serive.impl;

import com.wqddg.common.service.BaseService;
import com.wqddg.common.utils.IdWorker;
import com.wqddg.company.dao.DepartmentDao;
import com.wqddg.company.serive.DepartmentService;
import com.wqddg.domain.company.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @Author: wqddg
 * @ClassName DepartmentServiceImpl
 * @DateTime: 2022/1/4 14:22
 * @remarks : #
 */
@Service
public class DepartmentServiceImpl  extends BaseService implements DepartmentService{
    @Autowired
    private DepartmentDao departmentDao;
    @Autowired
    private IdWorker idWorker;
    @Override
    public void addDepartment(Department department) {
        department.setCreateTime(new Date());
        department.setId(idWorker.nextId()+"");
        departmentDao.save(department);

    }

    @Override
    public void updateDepartment(Department department) {
        Department department1 = departmentDao.findById(department.getId()).get();
        department1.setName(department.getName());
        department1.setCode(department.getCode());
        department1.setIntroduce(department.getIntroduce());
        departmentDao.save(department1);
    }

    @Override
    public Department findByid(String id) {
        return departmentDao.findById(id).get();
    }

    @Override
    public List<Department> findAll(String companyId) {
//        Specification<Department> specification=new Specification<Department>() {
//            @Override
//            public Predicate toPredicate(Root<Department> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
//                //根据企业id查询
//
//                return  criteriaBuilder.equal(root.get("companyId").as(String.class),companyId);
//            }
//        };
        return departmentDao.findAll(getSpec(companyId));
    }

    @Override
    public void delectByid(String id) {
        departmentDao.deleteById(id);
    }

    @Override
    public Department findbyCode(String code, String companyId) {

        return  departmentDao.findByCodeAndCompanyId(code,companyId);
    }
}
