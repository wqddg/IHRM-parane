package com.wqddg.system.controller;

import com.wqddg.common.controller.BaseController;
import com.wqddg.common.entity.PageResult;
import com.wqddg.common.entity.Result;
import com.wqddg.common.entity.ResultCode;
import com.wqddg.common.utils.JwtUtils;
import com.wqddg.common.utils.PermissionConstants;
import com.wqddg.domain.company.Company;
import com.wqddg.domain.company.Department;
import com.wqddg.domain.company.response.DeptListResult;
import com.wqddg.domain.system.Permission;
import com.wqddg.domain.system.Role;
import com.wqddg.domain.system.User;
import com.wqddg.domain.system.response.ProfileResult;
import com.wqddg.domain.system.response.UserResult;
import com.wqddg.system.client.DepartMentFeignClient;
import com.wqddg.system.service.PermissionService;
import com.wqddg.system.service.UserService;
import io.jsonwebtoken.Claims;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: wqddg
 * @ClassName UserController
 * @DateTime: 2022/1/5 22:26
 * @remarks : #
 */
@CrossOrigin
@RestController
@RequestMapping("sys")
public class UserController extends BaseController {
    @Autowired
    private UserService userService;
    @Autowired
    private PermissionService permissionService;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("user/assignRoles")
    public Result assignRoles(@RequestBody Map<String,Object> map){
        String user_id = (String) map.get("id");
        List<String> roleIds= (List<String>) map.get("roleIds");
        userService.assignRoles(user_id,roleIds);
        return new Result(ResultCode.SUCCESS);
    }





    @PostMapping("user")
    public Result save(@RequestBody User user){
        //?????????????????????id
        user.setCompanyId(companyId);
        userService.add(user);
        return new Result(ResultCode.SUCCESS);
    }



    @GetMapping("user")
    public Result findAll(int page, int size, @RequestParam Map<String,Object> map){
        //?????????????????????id
        map.put("companyId",companyId);
        Page<User> users = userService.finaAll(map, page, size);
        PageResult result=new PageResult<User>(users.getTotalElements(),users.getContent());
        return new Result(ResultCode.SUCCESS,result);
    }

    @GetMapping("user/{id}")
    public Result findbyId(@PathVariable("id") String id){
       User byid = userService.findByid(id);
        UserResult userResult=new UserResult(byid);
        return new Result(ResultCode.SUCCESS,userResult);
    }


    @PutMapping("user/{id}")
    public Result update(@PathVariable("id") String id,@RequestBody User user){
        user.setId(id);
        userService.update(user);
        return new Result(ResultCode.SUCCESS);
    }

    @DeleteMapping("user/{id}")
    public Result delete(@PathVariable("id") String id){
        userService.delectByid(id);
        return new Result(ResultCode.SUCCESS);
    }

    @PostMapping("/user/import")
    public Result importUser(@RequestParam(name = "file")MultipartFile file) throws IOException {
        //1.??????excel????????????
        Workbook workbook=new XSSFWorkbook(file.getInputStream());
        //2.??????Sheet
        Sheet sheetAt = workbook.getSheetAt(0);
        //3.??????sheet?????????????????????????????????
        List<User> list=new ArrayList<>();
        for (int i = 1; i < sheetAt.getLastRowNum()+1; i++) {
            Row row = sheetAt.getRow(i);
            Object[] values=new Object[row.getLastCellNum()];
            for (int j = 1; j < row.getLastCellNum(); j++) {
                Cell cell = row.getCell(j);
                if (cell == null) {
                    continue;
                }
                Object cellValue = getCellValue(cell);
                values[j]=cellValue;
            }
            User user=new User(values);
            list.add(user);
        }
        //????????????
        userService.insetALl(list,companyId,companyName);
        return new Result(ResultCode.SUCCESS);
    }

    public static Object getCellValue(Cell cell){
        //???????????????????????????
        CellType cellType = cell.getCellType();
        Object value=null;
        //?????????????????????????????????
        switch (cellType){
            case STRING:
                value=cell.getStringCellValue();
                break;
            case BOOLEAN:
                value=cell.getBooleanCellValue();
                break;
            case NUMERIC:
                //??????????????????
                if (DateUtil.isCellDateFormatted(cell)){
                    value=cell.getDateCellValue();
                }else {
                    value=cell.getNumericCellValue();
                }
                break;
            case FORMULA:
                //??????
                value=cell.getCellFormula();
                break;
            default:
                break;
        }
        return value;
    }


    @PostMapping("user/upload/{id}")
    public Result upload(@PathVariable("id") String id,MultipartFile file) throws IOException {
        String imgUrl=userService.upload(id,file);
        return new Result(ResultCode.SUCCESS,imgUrl);
    }

    @PostMapping("login")
    public Result login(@RequestBody Map<String,String> maps){
        String mobile=maps.get("mobile");
        String password=maps.get("password");
        try {
            //1.?????????????????? UsernamePasswordToken
            password = new Md5Hash(password, mobile, 3).toString();
            UsernamePasswordToken usernamePasswordToken=new UsernamePasswordToken(mobile,password);
            //2.??????Subject
            Subject subject = SecurityUtils.getSubject();
            //3.??????login?????? ??????realm????????????
            subject.login(usernamePasswordToken);
            //4.??????sessionID
            String sessionId = (String) subject.getSession().getId();
            return new Result(ResultCode.SUCCESS,sessionId);
        }catch (Exception e){
            return new Result(ResultCode.MOBILEORPASSWORDERROR);
        }


//        User user=userService.findMobile(mobile);
//        StringBuilder builder=new StringBuilder();
//        if (user==null||!user.getPassword().equals(password)){
//            return new Result(ResultCode.MOBILEORPASSWORDERROR);
//        }
//        for (Role role : user.getRoles()) {
//            for (Permission permission : role.getPermissions()) {
//                if (permission.getType()== PermissionConstants.PY_API){
//                    builder.append(permission.getCode()).append(",");
//                }
//            }
//        }
//        Map<String,Object> mapss=new HashMap<>();
//        mapss.put("apis",builder.toString());//??????????????????
//        mapss.put("companyId",user.getCompanyId());
//        mapss.put("companyName",user.getCompanyName());
//        String jwt = jwtUtils.createJwt(user.getId(), user.getUsername(), mapss);

    }

//    @RequestMapping(value = "/user/simple", method = RequestMethod.GET)
//    public Result simple() throws Exception {
//        List<UserSimpleResult> list = new ArrayList<>();
//        List<User> users = userService.finaAll(companyId);
//        for (User user : users) {
//            list.add(new UserSimpleResult(user.getId(),user.getUsername()));
//        }
//        return new Result(ResultCode.SUCCESS,list);
//    }



    @PostMapping("profile")
    public Result profile(HttpServletRequest request){

        //??????session??????????????????
        Subject subject = SecurityUtils.getSubject();
        //1.subject?????????????????????????????????
        PrincipalCollection principals = subject.getPrincipals();
        //2.??????????????????
        ProfileResult result = (ProfileResult)principals.getPrimaryPrincipal();
        return new Result(ResultCode.SUCCESS,result);

//        String userid=claims.getId();
//        User byid = userService.findByid(userid);
//        //???????????????????????????????????????
//        ProfileResult result=null;
//        if ("user".equals(byid.getLevel())){
//            //3.???????????????????????????????????????
//            result=new ProfileResult(byid);
//        }else {
//            Map<String,Object> maps=new HashMap<>();
//            if ("coAdmin".equals(byid.getLevel())){
//                //1.saas????????????????????????????????????
//                maps.put("enVisible","1");
//            }
//            List<Permission> permissions = permissionService.finaAll(maps);
//            result=new ProfileResult(byid,permissions);
//        }
//        return new Result(ResultCode.SUCCESS,result);
    }
}
