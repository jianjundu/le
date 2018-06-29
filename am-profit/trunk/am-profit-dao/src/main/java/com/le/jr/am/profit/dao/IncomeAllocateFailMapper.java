package com.le.jr.am.profit.dao;

import com.le.jr.am.profit.domain.IncomeAllocateFail;


public interface IncomeAllocateFailMapper {

    int insert(IncomeAllocateFail record);


    IncomeAllocateFail selectByPrimaryKey(String oid);

    int updateByPrimaryKeySelective(IncomeAllocateFail record);

    int updateByPrimaryKeyWithBLOBs(IncomeAllocateFail record);

}