package com.le.jr.am.profit.dao;

import com.le.jr.am.profit.domain.HoldApartTypeChangeLog;

public interface HoldApartTypeChangeLogMapper {
    int insert(HoldApartTypeChangeLog record);

    int insertSelective(HoldApartTypeChangeLog record);
}