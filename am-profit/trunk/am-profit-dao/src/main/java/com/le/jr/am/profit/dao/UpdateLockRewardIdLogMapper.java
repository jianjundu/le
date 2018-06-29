package com.le.jr.am.profit.dao;

import com.le.jr.am.profit.domain.UpdateLockRewardIdLog;

public interface UpdateLockRewardIdLogMapper {
    int deleteByPrimaryKey(String oid);

    int insert(UpdateLockRewardIdLog record);

    int insertSelective(UpdateLockRewardIdLog record);

    UpdateLockRewardIdLog selectByPrimaryKey(String oid);

    int updateByPrimaryKeySelective(UpdateLockRewardIdLog record);

    int updateByPrimaryKey(UpdateLockRewardIdLog record);
}