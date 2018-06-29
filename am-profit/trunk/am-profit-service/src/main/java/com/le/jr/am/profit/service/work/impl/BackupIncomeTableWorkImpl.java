package com.le.jr.am.profit.service.work.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.le.jr.am.profit.service.HoldApartIncomeService;
import com.le.jr.am.profit.service.HoldIncomeService;
import com.le.jr.am.profit.service.HoldLevelIncomeService;
import com.le.jr.am.profit.service.work.BackupIncomeTableWork;

@Service("backupIncomeTableWork")
public class BackupIncomeTableWorkImpl implements BackupIncomeTableWork{
	
	private static final Logger logger = LoggerFactory.getLogger(BackupIncomeTableWorkImpl.class);
	
	@Resource
	private HoldIncomeService holdIncomeService;
	
	@Resource
	private HoldApartIncomeService holdApartIncomeService;
	
	@Resource
	private HoldLevelIncomeService holdLevelIncomeService;

	@Override
	public Boolean backupIncomeData() {
		
		logger.info("backupIncomeData:{}",new Date());
		
		try{
			
			holdApartIncomeService.backupHoldApartIncomeData2His();
			
			holdLevelIncomeService.backupLevelIncomeData2His();
			
			holdIncomeService.backupHoldIncomeData2His();
			
			
			
			
			
		}catch(Exception e){
			
			logger.error("backupIncomeData: excute failed",e);
			
		}
		
		
		return true;
	}

	

}
