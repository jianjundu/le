package com.le.jr.am.profit.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.le.jr.am.product.common.util.ProductDecimalFormat;
import com.le.jr.am.profit.common.util.DateUtil;
import com.le.jr.am.profit.dao.HoldIncomeMapper;
import com.le.jr.am.profit.domain.HoldIncome;
import com.le.jr.am.profit.domain.input.HoldIncomeRequest;
import com.le.jr.am.profit.domain.output.HoldIncomeDetail;
import com.le.jr.am.profit.domain.output.HoldIncomeResponse;
import com.le.jr.am.profit.service.HoldIncomeService;
import com.le.jr.trade.publictools.data.Code;
import com.le.jr.trade.publictools.exception.BizException;


@Service("holdIncomeService")
public class HoldIncomeServiceImpl implements HoldIncomeService {
	
	Logger logger = LoggerFactory.getLogger(HoldIncomeServiceImpl.class);
	
	@Resource
	private HoldIncomeMapper holdIncomeMapper;
	
	@Resource
	private PlatformTransactionManager transactionManager;

	@Override
	public int saveEntity(HoldIncome holdIncome) throws BizException{
		holdIncome.setCreateTime(DateUtil.getSqlCurrentDate());
		return holdIncomeMapper.insert(holdIncome);
	}

	@Override
	public int updateEntity(HoldIncome holdIncome) throws BizException{
		holdIncome.setUpdateTime(DateUtil.getSqlCurrentDate());
		return this.holdIncomeMapper.updateByPrimaryKeySelective(holdIncome);
	}

	@Override
	public List<HoldIncome> findByInvestorOidAndConfirmDate(String investorOid, String incomeDate) throws BizException{
		return this.holdIncomeMapper.findByInvestorOidAndConfirmDate(investorOid, incomeDate);
	}

	//查询历史投资收益信息
	@Override
	public List<HoldIncome> findByInvestorOidAndConfirmDateInHis(String investorOid, String incomeDate) throws BizException{
		
		 Calendar rightNow = Calendar.getInstance();
	        SimpleDateFormat sim = new SimpleDateFormat("yyyyMMdd");
	        //得到当前时间，+3天
	        rightNow.add(java.util.Calendar.DAY_OF_MONTH, -1);   
	      
	        //进行时间转换
	    String yesterday = sim.format(rightNow.getTime()); 
		String now = sim.format(new Date());
		
		String incomeDateDeal = incomeDate.replace("-", "");
		
		if(incomeDate.equals(now) || yesterday.equals(incomeDateDeal)){
			logger.error("findByInvestorOidAndConfirmDateInHis:当日和昨日不会创建备份数据，不存在查询记录");
			return null;
		}
		
		return this.holdIncomeMapper.findByInvestorOidAndConfirmDateInHis(investorOid, incomeDateDeal);
	}

	@Override
	public HoldIncomeResponse queryHoldIncome(HoldIncomeRequest req) throws BizException{
		
		logger.info("queryHoldIncome==investorOid:{},incomeDate:{}", req.getInvestorOid(), req.getIncomeDate());
		HoldIncomeResponse rep = new HoldIncomeResponse();
		rep.setInvestorOid(req.getInvestorOid());
		rep.setIncomeDate(req.getIncomeDate());
		try {
			List<HoldIncomeDetail> holdDetailList = new ArrayList<HoldIncomeDetail>();
			List<HoldIncome> list = this.holdIncomeMapper.findByInvestorOidAndConfirmDate(req.getInvestorOid(), req.getIncomeDate());
			if (list.isEmpty()) {
				list = this.findByInvestorOidAndConfirmDateInHis(req.getInvestorOid(), req.getIncomeDate());
			}
			for (HoldIncome entity : list) {
				HoldIncomeDetail detail = new HoldIncomeDetail();
				detail.setHoldIncome(ProductDecimalFormat.format2Cent(entity.getIncomeAmount()));
				holdDetailList.add(detail);
				rep.setHoldIncome(rep.getHoldIncome() + detail.getHoldIncome());
				rep.setHoldDetailList(holdDetailList);
			}
		} catch (Exception e) {
			//e.printStackTrace();
			logger.error("queryHoldIncome=={}",e.getMessage(), e);
			
			rep.setErrorCode(-1);
			rep.setErrorMessage(e.getMessage());
		}
		return rep;
		
	}

	@Override
	public Boolean backupHoldIncomeData2His() throws BizException {
		 /**
	     * 备份任务表信息到历史表，并删除原表相应数据
	     */
			
			
			try{
				List<Date> dateList =null;
				
				dateList = holdIncomeMapper.queryBackupDate();
				
				for(Date date:dateList ){
					
					if(date !=null){
						SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
						SimpleDateFormat sf1 = new SimpleDateFormat("yyyyMMdd");
						String newTable =sf1.format(date);
						try{
						holdIncomeMapper.createHoldIncomeTable(newTable);
						}catch(Exception e){
							logger.error("backupHoldIncomeData2His:表已经存在或者创建失败：{}",newTable);
						}
						int i= holdIncomeMapper.backupHoldIncomeData(newTable,sf.format(date));
						if(i>0){
							if(holdIncomeMapper.deleteBackupHoldIncome(sf.format(date))<1){
								logger.error("backupHoldIncomeData2His删除备份数据失败或者数据为空");
								throw new BizException("删除备份数据失败或者数据为空");
							}
						}
						
						
					}
					
				}
				
				
				
				

			}catch (Exception e) {

				// 回滚事务
				logger.error("backupHoldIncomeData2His failed", e);
				
				throw new BizException(Code.SYSTEMEXCEPTION.getValue(), "备份总仓数据操作异常", e);
			}

			
			return true;
		
	}
	
	public static void main(String[] args) {
		SimpleDateFormat sf1 = new SimpleDateFormat("yyyyMMdd");
		
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		
		Date d=null;
		try {
			d = sf.parse("2016-12-11");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String newTable =sf1.format(d);
		System.out.println(newTable);
	}

}
