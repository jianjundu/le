package com.le.jr.am.profit.service;

import java.util.List;

import com.le.jr.am.order.domain.InvestorTradeOrder;
import com.le.jr.am.profit.domain.PDFUploadVo;
import com.le.jr.am.profit.domain.PublisherProductAgreement;
import com.le.jr.am.profit.domain.input.AgreementRequest;
import com.le.jr.am.profit.domain.input.SearchAgreementByVo;
import com.le.jr.am.profit.domain.output.AgreementResponse;

public interface PublisherProductAgreementService {
	
	
	
//	public void makeContract() ;



	/**
	 * 根据订单和协议类型查询协议
	 * @param order
	 * @param agreementType
	 * @return
	 */
	public PublisherProductAgreement findByInvestorTradeOrderAndAgreementType(InvestorTradeOrder order, String agreementType);
	public List<PublisherProductAgreement> findByOrderOid(String orderOid) ;
	
	
	public PublisherProductAgreement createInvestEntity(InvestorTradeOrder order) ;
	
	public PublisherProductAgreement createServiceEntity(InvestorTradeOrder order) ;
	
	public void saveBatch(List<PublisherProductAgreement> entitys) ;

	

	public int updateEntity(PublisherProductAgreement entity);
	
	//public void uploadPDF() ;


	
	
	public AgreementResponse getAgreementInfo(AgreementRequest request) ;
	
	
	//public Boolean loadAgreement2Cache();
	
	
	public List<PublisherProductAgreement> queryAgreementByVo(SearchAgreementByVo vo);
	
	
//	// 为了测试这个方法用，加了接口,并将实现类的类型由private改成了public
//	public void processProduct(Product product) throws Exception;


	/**
	 * 获得产品的信息服务模板
	 * @param product
	 * @return
	 */
	public String getServiceModel(PDFUploadVo pdfUploadVo);

	/**
	 * 获得产品的投资模板
	 * @param product
	 * @return
	 */
	public String getContractModel(PDFUploadVo pdfUploadVo);


	/**
	 * 生成投资协议和信息服务协议
	 * @param spvOid
	 * @param serviceModel
	 * @param agreementModel
	 * @param agreeList
	 * @param order
	 * @throws Exception
	 */
	public void processOneItem(PDFUploadVo pdfUploadVo,
								List<PublisherProductAgreement> agreeList, InvestorTradeOrder order)throws Exception;

}
