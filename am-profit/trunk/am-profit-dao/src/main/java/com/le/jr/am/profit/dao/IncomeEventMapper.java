package com.le.jr.am.profit.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.le.jr.am.profit.domain.IncomeEvent;


public interface IncomeEventMapper {

    int insert(IncomeEvent record);


    IncomeEvent selectByPrimaryKey(String oid);

    int updateByPrimaryKeySelective(IncomeEvent record);
    
    
    List<IncomeEvent> searchIncomeEvents(Map<String,Object> map);
    
    
    List<String> searchDistincProductEvents(Map<String,Object> map);

    
    
    IncomeEvent getLastIncomeEventByAssetPoolId(@Param("assetpooloid")String  assetpooloid,@Param("productOid")String productOid);
    
   
}