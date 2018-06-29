package com.le.jr.am.profit.dao;

import com.le.jr.am.profit.domain.VolumeConfirmLog;

public interface VolumeConfirmLogMapper {
    int deleteByPrimaryKey(String oid);

    int insert(VolumeConfirmLog record);

    int insertSelective(VolumeConfirmLog record);

    VolumeConfirmLog selectByPrimaryKey(String oid);

    int updateByPrimaryKeySelective(VolumeConfirmLog record);

    int updateByPrimaryKey(VolumeConfirmLog record);
    
    /**
     * 根据orderCode查询日志记录
     * @param orderCode
     * @return
     */
    VolumeConfirmLog selectByOrderCode(String orderCode);
}