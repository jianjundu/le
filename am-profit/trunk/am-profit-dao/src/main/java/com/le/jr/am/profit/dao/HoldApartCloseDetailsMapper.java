package com.le.jr.am.profit.dao;

import com.le.jr.am.profit.domain.HoldApartCloseDetails;


public interface HoldApartCloseDetailsMapper {

    int insert(HoldApartCloseDetails record);


    HoldApartCloseDetails selectByPrimaryKey(String oid);

    int updateByPrimaryKeySelective(HoldApartCloseDetails record);

    
    
}