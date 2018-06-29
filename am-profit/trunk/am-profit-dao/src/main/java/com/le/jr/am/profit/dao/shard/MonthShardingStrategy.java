package com.le.jr.am.profit.dao.shard;

import com.lejr.platform.mybatis.spring.sharding.ShardingStrategy;

/**
 * @author: haolin
 * @date: 2016/4/21
 * @mail: linhao@le.com
 */
public class MonthShardingStrategy implements ShardingStrategy {

    @Override
    public <V> ShardingStrategy.NumberPair sharding(V shardingValue, int shardingTableCount, int shardingDBCount) {
        return NumberPair.pair(Integer.valueOf(shardingValue.toString()), 0);
    }
}
