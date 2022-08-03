package com.wqddg.salarys.controller;

import com.wqddg.common.controller.BaseController;
import com.wqddg.common.entity.PageResult;
import com.wqddg.common.entity.Result;
import com.wqddg.common.entity.ResultCode;
import com.wqddg.domain.salarys.UserSalary;
import com.wqddg.salarys.dao.UserSalaryDao;
import com.wqddg.salarys.service.SalaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping(value = "/salarys")
public class SalaryController extends BaseController {

	@Autowired
	private SalaryService salaryService;

	//查询用户薪资
	@RequestMapping(value = "/modify/{userId}", method = RequestMethod.GET)
	public Result modifyGet(@PathVariable(value = "userId") String userId) throws Exception {
		UserSalary userSalary = salaryService.findUserSalary(userId);
		return new Result(ResultCode.SUCCESS, userSalary);
	}

	//调薪
	@RequestMapping(value = "/modify/{userId}", method = RequestMethod.POST)
	public Result modify(@RequestBody UserSalary userSalary) throws Exception {
		salaryService.saveUserSalary(userSalary);
		return new Result(ResultCode.SUCCESS);
	}

	//定薪
	@RequestMapping(value = "/init/{userId}", method = RequestMethod.POST)
	public Result init(@RequestBody UserSalary userSalary) {
		userSalary.setFixedBasicSalary(userSalary.getCurrentBasicSalary());
		userSalary.setFixedPostWage(userSalary.getCurrentPostWage());
		salaryService.saveUserSalary(userSalary);
		return new Result(ResultCode.SUCCESS);
	}

	//查询列表
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	public Result list(@RequestBody Map map) {
		//1.获取请求参数,page,size
		Integer page = (Integer)map.get("page");
		Integer pageSize = (Integer)map.get("pageSize");
		//2.调用service查询
		PageResult pr = salaryService.findAll(page,pageSize,companyId);
		return new Result(ResultCode.SUCCESS, pr);
	}
}
