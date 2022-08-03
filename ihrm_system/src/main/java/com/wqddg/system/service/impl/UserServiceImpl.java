package com.wqddg.system.service.impl;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import com.wqddg.common.service.BaseService;
import com.wqddg.common.utils.IdWorker;
import com.wqddg.common.utils.QiniuUploadUtil;
import com.wqddg.domain.company.Department;
import com.wqddg.domain.system.Role;
import com.wqddg.domain.system.User;
import com.wqddg.system.client.DepartMentFeignClient;
import com.wqddg.system.dao.RoleDao;
import com.wqddg.system.dao.UserDao;
import com.wqddg.system.service.UserService;
import com.wqddg.system.utils.BaiduAiUtil;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.IOException;
import java.util.*;

/**
 * @Author: wqddg
 * @ClassName UserServiceImpl
 * @DateTime: 2022/1/3 22:58
 * @remarks : #
 */
@Service
public class UserServiceImpl extends BaseService implements UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private IdWorker idWorker;


    @Autowired
    private DepartMentFeignClient feignClient;

    /**
     * 增加企业
     * @param company
     */
    @Override
    public void add(User company) {
        //基本属性的设置
        String id = idWorker.nextId() + "";
        company.setId(id);
        company.setLevel("user");
        String s = new Md5Hash("123456", company.getMobile(), 3).toString();
        company.setPassword(s);//设置初始密码
        company.setEnableState(1);
        company.setCreateTime(new Date());
        userDao.save(company);
    }



    /**
     * 更新企业
     * @param company
     */
    @Override
    public void update(User company) {
        User company_update = userDao.findById(company.getId()).get();
        company_update.setUsername(company.getUsername());
        company_update.setPassword(company.getPassword());
        company_update.setDepartmentId(company.getDepartmentId());
        company_update.setDepartmentName(company.getDepartmentName());
        userDao.save(company_update);
    }

    @Override
    public void delectByid(String id) {
        userDao.deleteById(id);
    }

    @Override
    public User findByid(String id) {
        return userDao.findById(id).get();
    }

    @Override
    public Page<User> finaAll(Map<String,Object> map, int page, int size) {
        //需要的查询条件
        Specification<User> specification=new Specification<User>() {
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list=new ArrayList<>();
                //根据请求的companyId 是否构造查询条件
                if (!StringUtils.isEmpty(map.get("companyId"))){
                    list.add(criteriaBuilder.equal(root.get("companyId").as(String.class),(String)map.get("companyId")));
                }
                //根据请求的部门id来设置查询条件
                if (!StringUtils.isEmpty(map.get("departmentId"))){
                    list.add(criteriaBuilder.equal(root.get("departmentId").as(String.class),(String)map.get("departmentId")));
                }
                //根据请求的HasDept判断  是否分配部门
                if (!StringUtils.isEmpty(map.get("hasDept"))){
                    if (((String)map.get("hasDept")).equals("0")){
                        list.add(criteriaBuilder.isNull(root.get("departmentId")));
                    }else {
                        list.add(criteriaBuilder.isNotNull(root.get("departmentId")));
                    }
                }



                return criteriaBuilder.and(list.toArray(new Predicate[list.size()]));
            }
        };
        //分页
        Page<User> all = userDao.findAll(specification, PageRequest.of(page-1, size));
        return all;
    }

    @Override
    public void assignRoles(String user_id, List<String> roleIds) {
        User user = userDao.findById(user_id).get();
        Set<Role> roles=new HashSet<>();
        for (String roleId : roleIds) {
            Role role = roleDao.findById(roleId).get();
            roles.add(role);
        }
        //设置用户和
        user.setRoles(roles);
        userDao.save(user);

    }

    @Override
    public User findMobile(String mobile) {
        return userDao.findByMobile(mobile);
    }

    @Override
    public void insetALl(List<User> list, String companyId, String companyName) {
        for (User user : list) {
            user.setPassword(new Md5Hash("123456",user.getMobile(),3).toString());
            user.setLevel("user");
            //基本属性的设置
            String id = idWorker.nextId() + "";
            user.setId(id);
            user.setCompanyId(companyId);
            user.setCompanyName(companyName);
            user.setEnableState(1);
            user.setInServiceStatus(1);
            Department byCode = feignClient.findByCode(user.getDepartmentId(), companyId);
            if (byCode!=null){
                user.setDepartmentId(byCode.getId());
                user.setDepartmentName(byCode.getName());
            }
            userDao.save(user);
        }

    }

//    @Override
//    public String upload(String id, MultipartFile file) throws IOException {
//        String imagUrl="data:image/png;base64,";
//        String encode = Base64.encode(file.getBytes());
////        if ("png".equalsIgnoreCase(name)){
////            imagUrl="data:image/png;base64,";
////        }else if ("jpg".equalsIgnoreCase(name)){
////            imagUrl="data:image/jpg;base64,";
////        }else if ("jpeg".equalsIgnoreCase(name)){
////            imagUrl="data:image/jpeg;base64,";
////        }else if ("pdf".equalsIgnoreCase(name)){
////            imagUrl="data:image/png;base64,";
////        }else {
////            imagUrl="data:image/png;base64,";
////        }
//        imagUrl+=encode;
//        User user = userDao.findById(id).get();
//        user.setStaffPhoto(imagUrl);
//        userDao.save(user);
//        return imagUrl;
//    }
    @Autowired
    private BaiduAiUtil aiUtil;


    @Override
    public String upload(String id, MultipartFile file) throws IOException {
        User user = userDao.findById(id).get();
        QiniuUploadUtil qiniuUploadUtil=new QiniuUploadUtil();
        String upload = qiniuUploadUtil.upload(user.getId(), file.getBytes());
        user.setStaffPhoto(upload);
        userDao.save(user);
        String imgBase64 = Base64.encode(file.getBytes());
        boolean b = aiUtil.faceExist(id);
        if (b){
            aiUtil.faceUpdate(id,imgBase64);
        }else {
            aiUtil.faceRegister(id,imgBase64);
        }
        return upload;
    }

    public static void main(String[] args) {
        String string = new Md5Hash("123456", "13800000001", 3).toString();
        System.out.println(string);
    }
}
