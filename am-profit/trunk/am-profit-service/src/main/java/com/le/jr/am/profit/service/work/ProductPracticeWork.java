package com.le.jr.am.profit.service.work;

import java.util.Date;
import java.util.List;

import com.le.jr.am.product.domain.Product;
import com.le.jr.am.profit.domain.vo.PracticeParams;

public interface ProductPracticeWork {
	
	
	public void isSnapshotVolume(String batchCode, String jobId);
	
	
	public void practiceOneProduct(PracticeParams vo);
	
	
	public void createPracticeSerial(Product product,Date incomeDate,String batchCode);
	
	
	public List<Product> queryCanPracticeProduct();

	
	

}
