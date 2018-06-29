package com.le.jr.am.profit.service.config;


import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.le.diamond.service.AbstractDiamondService;
import com.le.jr.trade.publictools.util.JsonUtils;
import com.le.jr.trade.publictools.util.StringUtil;


@Service("profitDiamondService")
public class ProfitDiamondService extends AbstractDiamondService{


	private static final Logger logger = LoggerFactory.getLogger(ProfitDiamondService.class);

	

	 private Map<String, Object> codeConfigMap = new HashMap<>();

	    public Object getMessage(String key) {
	        return codeConfigMap.get(key);
	    }

	    @Override
	    protected void setConfigInfo(String codeConfig) {
	    	
	    	logger.info("setConfigInfo:configString:{}",codeConfig);
	    	
	        codeConfigMap.clear();
	        if (!StringUtil.isEmpty(codeConfig)) {
	            codeConfigMap = JsonUtils.readValue(codeConfig);
	        }
	    }
	

	

	
}
