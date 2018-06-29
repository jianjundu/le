package com.le.jr.am.profit.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.le.jr.am.order.common.page.PageEntity;
import com.le.jr.am.product.common.util.UUIDUtil;
import com.le.jr.am.profit.dao.InvestorInterestResultMapper;
import com.le.jr.am.profit.domain.InvestorInterestResult;
import com.le.jr.am.profit.domain.enums.InterestResultStausEnum;
import com.le.jr.am.profit.domain.input.ProductIncomeReqVo;
import com.le.jr.am.profit.domain.output.ProductIncomeRespVo;
import com.le.jr.am.profit.service.IncomeDistributionService;
import com.le.jr.am.profit.service.InvestorInterestResultService;
import com.le.jr.trade.publictools.page.Page;


@Service("investorInterestResultService")
public class InvestorInterestResultServiceImpl implements InvestorInterestResultService {
	
	private static final Logger logger = LoggerFactory.getLogger(InvestorInterestResultServiceImpl.class);
	
	
	@Resource
	private IncomeDistributionService incomeDistributionService;
	
	@Resource
	private InvestorInterestResultMapper investorInterestResultMapper;
	

	public InvestorInterestResult createEntity(String productOid, String allocateOid, Date incomeDate,BigDecimal totalVolume) {
		InvestorInterestResult entity = new InvestorInterestResult();
		entity.setOid(UUIDUtil.creatUUID());
		entity.setProductOid(productOid);
		entity.setFailAllocateInvestors(0);
		entity.setSuccessAllocateInvestors(0);
		entity.setAllocateOid(allocateOid);
		entity.setSnapshotVolume(totalVolume);
		entity.setAllocateDate(incomeDate);
		entity.setStatus(InterestResultStausEnum.ALLOCATING.value);
		int i =investorInterestResultMapper.insert(entity);
		
		if(i>0){
			return entity;
		}
		
		return null;
	}

	public InvestorInterestResult updateEntity(InvestorInterestResult result) {
		
		
		int i =investorInterestResultMapper.updateByPrimaryKeySelective(result);
		
		if(i>0){
			return result;
		}
		
		return null;
	}

	public void send(InvestorInterestResult result,BigDecimal holdInterestMarketingAmount) {
		
		try {
			
			incomeDistributionService.allocateIncome(result,holdInterestMarketingAmount);
		} catch (Exception e) {
			logger.error("", e);
			e.printStackTrace();
		}
		
	}

	@Override
	public Page<ProductIncomeRespVo> getProductIncomes(PageEntity<ProductIncomeReqVo> pageEntity) {
		Page<ProductIncomeRespVo> page = new Page<ProductIncomeRespVo>();
		page.setCurrentPageNo(pageEntity.getPageNo());
		page.setPageSize(pageEntity.getSize());
		page.setTotalCount(0);
		List<ProductIncomeRespVo> pVos = new ArrayList<ProductIncomeRespVo>();
		page.setDataList(pVos);
		
		int count = this.selectInterestsCount(pageEntity.getParams());
		if (count <= 0) {
			return page;
		}
		
		page.setTotalCount(count);
		
		List<InvestorInterestResult> interests=this.selectInterests(pageEntity);
		
		for(InvestorInterestResult i:interests){
			pVos.add(this.createProductIncomeRespVo(i));
		}
		return page;
	}
	
	@Override
	public List<InvestorInterestResult> selectInterests(PageEntity<ProductIncomeReqVo> pageEntity) {
		return investorInterestResultMapper.selectInterests(pageEntity);
	}

	@Override
	public int selectInterestsCount(ProductIncomeReqVo reqVo) {
		return investorInterestResultMapper.selectInterestsCount(reqVo);
	}
	
	private ProductIncomeRespVo createProductIncomeRespVo(InvestorInterestResult interest){
		ProductIncomeRespVo respVo=new ProductIncomeRespVo();
		respVo.setProductOid(interest.getProductOid());
		respVo.setIncomeBaseDate(interest.getAllocateDate());
		respVo.setSnapshotVolume(interest.getSnapshotVolume());
		respVo.setProvisionInterestVolume(interest.getProvisionInterestVolume());
		respVo.setCloseInterestVolume(interest.getCloseInterestVolume());
		respVo.setStatus(InterestResultStausEnum.getInstance(interest.getStatus()).desc);
		return respVo;
	}
	

	
}
