package com.le.jr.am.profit.common.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service("decimalUtilProfit")
public class DecimalUtil {
	
	
	@Value("${bigdecimal.scale:2}")
	String intScale;
	public static int scale;
	
	@Value("${round.mode:1}")
	String intRoundMode;
	public static RoundingMode roundMode;
	
	@PostConstruct
	public void init() {
		scale = Integer.parseInt(intScale);
		roundMode = RoundingMode.valueOf(Integer.parseInt(intRoundMode));
				
	}
	
	public static BigDecimal setScaleDown(BigDecimal in) {
		if (null == in) {
			return in;
		}
		return in.setScale(scale, roundMode);
	}
	
	
   public static String changeRMB2FEN(BigDecimal data){
		
		if(data==null){
			return "0";
		}
		return String.valueOf(data.multiply(new BigDecimal("100")).longValue());
		
	}
   
   public static BigDecimal changeRMB2FEN4BigDecimal(BigDecimal data){
		if(data==null){
			return BigDecimal.ZERO;
		}
		return data.multiply(new BigDecimal("100"));
	}
   
	
}
