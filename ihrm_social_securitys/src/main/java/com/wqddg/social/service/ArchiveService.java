package com.wqddg.social.service;

import com.alibaba.fastjson.JSON;
import com.wqddg.common.entity.Result;
import com.wqddg.common.utils.IdWorker;
import com.wqddg.domain.employee.UserCompanyPersonal;
import com.wqddg.domain.social_security.*;
import com.wqddg.social.client.EmployeeFeignClient;
import com.wqddg.social.dao.ArchiveDao;
import com.wqddg.social.dao.ArchiveDetailDao;
import com.wqddg.social.dao.UserSocialSecurityDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ArchiveService {

	@Autowired
	private ArchiveDao archiveDao;

	@Autowired
	private ArchiveDetailDao archiveDetailDao_sec;

	@Autowired
	private UserSocialSecurityDao userSocialSecurityDao;

	@Autowired
	private EmployeeFeignClient employeeFeignClient;


	@Autowired
	private UserSocialService userSocialService;

	@Autowired
	private PaymentItemService paymentItemService;

	@Autowired
	private IdWorker idWorker;

	//查询归档历史
	public Archive findArchive(String companyId, String yearMonth) {
		Archive byCompanyIdAndYearsMonth = archiveDao.findByCompanyIdAndYearsMonth(companyId, yearMonth);
		return byCompanyIdAndYearsMonth;
	}

	/**
	 * 根据归档历史id、  查询归档明细
	 * @return
	 * @throws Exception
	 */
	public List<ArchiveDetail> findAllDetailByArchiveId(String id) {
		return archiveDetailDao_sec.findByArchiveId(id);
	}


	public List<ArchiveDetail> getReports(String yearMonth,String companyId) throws Exception {
		//查询用户的社保列表 (用户和基本社保数据)
		Page<Map> userSocialSecurityItemPage = userSocialSecurityDao.findPage(companyId,null);

		List<ArchiveDetail> list = new ArrayList<>();

		for (Map map : userSocialSecurityItemPage) {
			String userId = (String)map.get("id");
			String mobile = (String)map.get("mobile");
			String username = (String)map.get("username");
			String departmentName = (String)map.get("departmentName");
			ArchiveDetail vo = new ArchiveDetail(userId,mobile,username,departmentName);
			vo.setTimeOfEntry((Date) map.get("timeOfEntry"));
			//获取个人信息
			Result personalResult = employeeFeignClient.findPersonalInfo(vo.getUserId());
			if (personalResult.isSuccess()) {
				UserCompanyPersonal userCompanyPersonal = JSON.parseObject(JSON.toJSONString(personalResult.getData()), UserCompanyPersonal.class);
				vo.setUserCompanyPersonal(userCompanyPersonal);
			}
			//社保相关信息
			getOtherData(vo, yearMonth);
			list.add(vo);
		}
		return list;
	}

	public void getOtherData(ArchiveDetail vo, String yearMonth) {

		UserSocialSecurity userSocialSecurity = userSocialService.findById(vo.getUserId());
		if(userSocialSecurity == null) {
			return;
		}

		BigDecimal socialSecurityCompanyPay = new BigDecimal(0);

		BigDecimal socialSecurityPersonalPay = new BigDecimal(0);

		List<CityPaymentItem> cityPaymentItemList = paymentItemService.findAllByCityId(userSocialSecurity.getProvidentFundCityId());

		for (CityPaymentItem cityPaymentItem : cityPaymentItemList) {
			if (cityPaymentItem.getSwitchCompany()) {
				BigDecimal augend;
				if (cityPaymentItem.getPaymentItemId().equals("4") && userSocialSecurity.getIndustrialInjuryRatio() != null) {
					augend = userSocialSecurity.getIndustrialInjuryRatio().multiply(userSocialSecurity.getSocialSecurityBase());
				} else {
					augend = cityPaymentItem.getScaleCompany().multiply(userSocialSecurity.getSocialSecurityBase());
				}
				BigDecimal divideAugend = augend.divide(new BigDecimal(100));
				socialSecurityCompanyPay = socialSecurityCompanyPay.add(divideAugend);
			}
			if (cityPaymentItem.getSwitchPersonal()) {
				BigDecimal augend = cityPaymentItem.getScalePersonal().multiply(userSocialSecurity.getSocialSecurityBase());
				BigDecimal divideAugend = augend.divide(new BigDecimal(100));
				socialSecurityPersonalPay = socialSecurityPersonalPay.add(divideAugend);
			}
		}

		vo.setSocialSecurity(socialSecurityCompanyPay.add(socialSecurityPersonalPay));
		vo.setSocialSecurityEnterprise(socialSecurityCompanyPay);
		vo.setSocialSecurityIndividual(socialSecurityPersonalPay);
		vo.setUserSocialSecurity(userSocialSecurity);
		vo.setSocialSecurityMonth(yearMonth);
		vo.setProvidentFundMonth(yearMonth);
	}

	/**
	 * 社保数据归档
	 *
	 */
	public void archive(String yearMonth, String companyId) throws Exception {
		//1.查询归档明细数据
		List<ArchiveDetail> reports = getReports(yearMonth, companyId);
		//1.1 计算单月、 企业与员工指出所有的社保金额
		BigDecimal enterDecimal=new BigDecimal(0);
		BigDecimal dpersonEcimal=new BigDecimal(0);
		for (ArchiveDetail report : reports) {
			BigDecimal t1 = report.getProvidentFundEnterprises() == null ? new BigDecimal(0): report.getProvidentFundEnterprises();
			BigDecimal t2 = report.getSocialSecurityEnterprise() == null ? new BigDecimal(0): report.getSocialSecurityEnterprise();
			BigDecimal t3 = report.getProvidentFundIndividual() == null ? new BigDecimal(0): report.getProvidentFundIndividual();
			BigDecimal t4 = report.getSocialSecurityIndividual() == null ? new BigDecimal(0): report.getSocialSecurityIndividual();
			enterDecimal = enterDecimal.add(t1).add(t2);
			dpersonEcimal = dpersonEcimal.add(t3).add(t4);
		}
		//2.查询当月是否已经归档
		Archive archive = findArchive(companyId, yearMonth);
		if (archive==null){
			//3.如果存在已归档数据 覆盖
			archive=new Archive();
			archive.setCompanyId(companyId);
			archive.setYearsMonth(yearMonth);
			archive.setId(idWorker.nextId()+"");
			archive.setCreationTime(new Date());
		}
		archive.setEnterprisePayment(enterDecimal);
		archive.setPersonalPayment(dpersonEcimal);
		archive.setTotal(enterDecimal.add(dpersonEcimal));
		archiveDao.save(archive);
		for (ArchiveDetail report : reports) {
			report.setId(idWorker.nextId()+"");
			report.setArchiveId(archive.getId());
			archiveDetailDao_sec.save(report);
		}
	}

	public List<Archive> findArchiveByYear(String companyId, String year) {
		List<Archive> byCompanyIdAndYearsMonthLike = archiveDao.findByCompanyIdAndYearsMonthLike(companyId, year+"%");
		return byCompanyIdAndYearsMonthLike;
	}

	/**
	 * 根据用户id和年月查询
	 * @param userId
	 * @param yearMonth
	 * @return
	 */
	public ArchiveDetail findUserIdAndYearMonth(String userId, String yearMonth) {
		return archiveDetailDao_sec.findByUserIdAndYearsMonth(userId, yearMonth);
	}
}
