package com.le.jr.am.profit.dao;



import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.le.jr.am.profit.domain.IncomeAllocate;


public interface IncomeAllocateMapper {

    int insert(IncomeAllocate record);
    
    List<IncomeAllocate> selectIncomeAllocates4Api(Map<String, Object> map);
    
    int selectCountIncomeAllocates4Api(Map<String, Object> map);
    
    List<IncomeAllocate> selectIncomeAllocates(Map<String, Object> map);
    


    IncomeAllocate selectByPrimaryKey(String oid);

    int updateByPrimaryKeySelective(IncomeAllocate record);

    
    
    IncomeAllocate selectByEventOid(String eventOid);
    
    Date getLatestIncomeDate(String productOid);
    
    List<IncomeAllocate>  findLatestIncomeAllocates(@Param("productOids")List<String> productOids);
    
     int isIncomeAllocated(String productOid, Date baseDate) ;
     
     List<IncomeAllocate> findLastValidIncomeAllocate(String productOid);
}