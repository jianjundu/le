package com.le.jr.am.profit.interfaces;

import java.util.List;

import com.le.jr.am.profit.domain.output.PracticeInRep;
import com.le.jr.trade.publictools.data.Message;

public interface PracticeInterface {
	
	
	public Message<List<PracticeInRep>> findByProduct(String productOid, String tDate);
	
	public Message<List<PracticeInRep>> findByProductLr(String productOid, String tDate);
	
	

}
