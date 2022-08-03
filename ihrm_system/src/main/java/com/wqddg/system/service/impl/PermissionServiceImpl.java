package com.wqddg.system.service.impl;

import com.wqddg.common.entity.ResultCode;
import com.wqddg.common.exception.CommonExcption;
import com.wqddg.common.service.BaseService;
import com.wqddg.common.utils.BeanMapUtils;
import com.wqddg.common.utils.IdWorker;
import com.wqddg.common.utils.PermissionConstants;
import com.wqddg.domain.system. Permission;
import com.wqddg.domain.system.PermissionApi;
import com.wqddg.domain.system.PermissionMenu;
import com.wqddg.domain.system.PermissionPoint;
import com.wqddg.system.dao.PermissionApiDao;
import com.wqddg.system.dao. PermissionDao;
import com.wqddg.system.dao.PermissionMenuDao;
import com.wqddg.system.dao.PermissionPointDao;
import com.wqddg.system.service. PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: wqddg
 * @ClassName  PermissionServiceImpl
 * @DateTime: 2022/1/3 22:58
 * @remarks : #
 */
@Service
@Transactional
public class PermissionServiceImpl extends BaseService implements  PermissionService {
    @Autowired
    private  PermissionDao  permissionDao;


    @Autowired
    private PermissionApiDao permissionApiDao;
    @Autowired
    private PermissionPointDao permissionPointDao;
    @Autowired
    private PermissionMenuDao permissionMenuDao;

    @Autowired
    private IdWorker idWorker;

    /**
     * 增加企业
     */
    @Override
    public void add(Map<String,Object> map) throws Exception {
        //基本属性的设置
        String id = idWorker.nextId() + "";
        //1.通过map构造permission对象
        Permission permission = BeanMapUtils.mapToBean(map, Permission.class);
        permission.setId(id);
        //保存资源
        Integer type = permission.getType();
        switch (type){
            case PermissionConstants.PY_MENU:
                PermissionMenu permissionMenu = BeanMapUtils.mapToBean(map, PermissionMenu.class);
                permissionMenu.setId(id);
                permissionMenuDao.save(permissionMenu);
                break;
            case PermissionConstants.PY_POINT:
                PermissionPoint permissionPoint = BeanMapUtils.mapToBean(map, PermissionPoint.class);
                permissionPoint.setId(id);
                permissionPointDao.save(permissionPoint);
                break;
            case PermissionConstants.PY_API:
                PermissionApi permissionApi = BeanMapUtils.mapToBean(map, PermissionApi.class);
                permissionApi.setId(id);
                permissionApiDao.save(permissionApi);
                break;
            default:
                throw new CommonExcption(ResultCode.FAIL);
        }
        //保存权限
        permissionDao.save(permission);


    }

    /**
     * 更新企业
     */
    @Override
    public void update(Map<String,Object> map) throws Exception {
        Permission permission = BeanMapUtils.mapToBean(map, Permission.class);
        Permission permission_update = permissionDao.findById(permission.getId()).get();
        permission_update.setName(permission.getName());
        permission_update.setCode(permission.getCode());
        permission_update.setDescription(permission.getDescription());
        permission_update.setEnVisible(permission.getEnVisible());

        Integer type = permission.getType();
        switch (type){
            case PermissionConstants.PY_MENU:
                PermissionMenu permissionMenu = BeanMapUtils.mapToBean(map, PermissionMenu.class);
                permissionMenu.setId(permission.getId());
                permissionMenuDao.save(permissionMenu);
                break;
            case PermissionConstants.PY_POINT:
                PermissionPoint permissionPoint = BeanMapUtils.mapToBean(map, PermissionPoint.class);
                permissionPoint.setId(permission.getId());
                permissionPointDao.save(permissionPoint);
                break;
            case PermissionConstants.PY_API:
                PermissionApi permissionApi = BeanMapUtils.mapToBean(map, PermissionApi.class);
                permissionApi.setId(permission.getId());
                permissionApiDao.save(permissionApi);
                break;
            default:
                throw new CommonExcption(ResultCode.FAIL);
        }
        //保存权限
        permissionDao.save(permission);
    }

    @Override
    public void delectByid(String id) throws CommonExcption {
        Permission permission = permissionDao.findById(id).get();
        permissionDao.delete(permission);
        Integer type = permission.getType();
        switch (type){
            case PermissionConstants.PY_MENU:
                permissionMenuDao.deleteById(id);
                break;
            case PermissionConstants.PY_POINT:
                permissionPointDao.deleteById(id);
                break;
            case PermissionConstants.PY_API:
                permissionApiDao.deleteById(id);
                break;
            default:
                throw new CommonExcption(ResultCode.FAIL);
        }


    }

    @Override
    public  Map findByid(String id) {
        Permission permission = permissionDao.findById(id).get();
        Object object=null;
        int type=permission.getType();
        if (type==PermissionConstants.PY_MENU){
            object=permissionMenuDao.findById(permission.getId()).get();
        }else if (type==PermissionConstants.PY_POINT){
            object=permissionPointDao.findById(permission.getId()).get();
        }else if (type==PermissionConstants.PY_API){
            object=permissionApiDao.findById(permission.getId()).get();
        }
        Map<String, Object> stringObjectMap = BeanMapUtils.beanToMap(object);
        stringObjectMap.put("name",permission.getCode());
        stringObjectMap.put("id",permission.getId());
        stringObjectMap.put("type",permission.getType());
        stringObjectMap.put("code",permission.getCode());
        stringObjectMap.put("description",permission.getDescription());
        stringObjectMap.put("pid",permission.getPid());
        stringObjectMap.put("enVisible",permission.getEnVisible());
        return  stringObjectMap;
    }

    @Override
    public List<Permission> finaAll(Map<String,Object> map) {
        //需要的查询条件
        Specification< Permission> specification=new Specification< Permission>() {
            @Override
            public Predicate toPredicate(Root< Permission> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list=new ArrayList<>();
                //根据请求的夫id 是否构造查询条件
                if (!StringUtils.isEmpty(map.get("pid"))){
                    list.add(criteriaBuilder.equal(root.get("pid").as(String.class),(String)map.get("pid")));
                }
                //根据请求的部门id来设置查询条件
                if (!StringUtils.isEmpty(map.get("enVisible"))){
                    list.add(criteriaBuilder.equal(root.get("enVisible").as(String.class),(String)map.get("enVisible")));
                }
                //根据请求的HasDept判断  是否分配部门
                if (!StringUtils.isEmpty(map.get("type"))){
                    String ty= (String) map.get("type");
                    CriteriaBuilder.In<Object> type = criteriaBuilder.in(root.get("type"));
                    if ("0".equals(ty)){
                        type.value(1).value(2);
                    }else {
                        type.value(Integer.parseInt(ty));
                    }
                }
                return criteriaBuilder.and(list.toArray(new Predicate[list.size()]));
            }
        };

        return permissionDao.findAll(specification);
    }

}
