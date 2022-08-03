package com.wqddg.common.service;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * @Author: wqddg
 * @ClassName BaseService
 * @DateTime: 2022/1/4 15:21
 * @remarks : #
 */
public class BaseService <T>{
    protected Specification<T> getSpec(String companyId){
        Specification<T> specification=new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                //根据企业id查询

                return  criteriaBuilder.equal(root.get("companyId").as(String.class),companyId);
            }
        };
        return specification;
    }
}
