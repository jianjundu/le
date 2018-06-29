package com.le.jr.am.profit.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.le.jr.am.profit.domain.PublisherProductAgreement;
import com.le.jr.am.profit.domain.input.SearchAgreementByVo;


public interface PublisherProductAgreementMapper {

    int insert(PublisherProductAgreement record);


    PublisherProductAgreement selectByPrimaryKey(String oid);

    int updateByPrimaryKeySelective(PublisherProductAgreement record);


    PublisherProductAgreement findByInvestorTradeOrderAndAgreementType(@Param("orderOid")String orderOid,@Param("agreementType")String agreementType);

    public List<PublisherProductAgreement> findByOrderOid(String orderOid);

    PublisherProductAgreement findByAgreementCodeAndAgreementType(@Param("agreementCode")String agreementCode,@Param("agreementType")String agreementType);

    public int saveBatch(List<PublisherProductAgreement> lstAgreement);

    public int updateBatch(List<PublisherProductAgreement> lstAgreement);
    
    List<PublisherProductAgreement>  queryAgreementByVo(SearchAgreementByVo vo);
}