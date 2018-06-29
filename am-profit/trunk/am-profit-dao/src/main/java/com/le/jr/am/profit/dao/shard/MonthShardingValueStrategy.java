package com.le.jr.am.profit.dao.shard;

import com.lejr.platform.mybatis.spring.sharding.ShardingValueStrategy;
import org.apache.commons.httpclient.util.DateUtil;
import java.util.Date;

/**
 * 根据月份shard
 * @author: haolin
 * @date: 2016/4/21
 * @mail: linhao@le.com
 */
public class MonthShardingValueStrategy implements ShardingValueStrategy {

    private static final String MONTH_SHARD_FMT = "yyyyMMdd";

    @Override
    public Object get(Object ctime) {
    	if(ctime.getClass().getName().contains("String")){
    		
    		ctime = ctime.toString().replace("_", "");
    		
    		return ctime;
    	}
        return DateUtil.formatDate((Date) ctime, MONTH_SHARD_FMT);
    }
}
