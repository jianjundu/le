package com.le.jr.am.profit.service.work.dts;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.le.dts.client.executor.job.processor.SimpleJobProcessor;
import com.le.dts.client.executor.simple.processor.SimpleJobContext;
import com.le.dts.common.domain.result.ProcessResult;
import com.le.jr.am.profit.service.HoldApartService;
import com.le.jr.am.profit.service.HoldService;
import com.le.jr.am.profit.service.PublisherProductAgreementService;

public class LoadHoldData2CacheDTS implements SimpleJobProcessor {


    private static final Logger logger = LoggerFactory.getLogger(LoadHoldData2CacheDTS.class);


    @Resource
    private HoldService holdService;

    @Resource
    private HoldApartService holdApartService;


    @Resource
    private PublisherProductAgreementService publisherProductAgreementService;


    @Override
    public ProcessResult process(SimpleJobContext context) {

    	Boolean flag1 = false;
    	Boolean flag2 = false;
    

        logger.info("process 执行开始增量加载总仓数据到缓存-----");
        try {
            holdService.loadHoldData2Cache();
            flag1 = true;
        } catch (Exception e) {
            logger.error("process 执行增量加载总仓数据到缓存失败:", e.getMessage());
            
        }
        logger.info("process 执行完成增量加载总仓数据到缓存-----");

        logger.info("process 执行开始增量加载分仓到缓存-----");
        try {
            holdApartService.loadHoldApartData2Cache();
            flag2 = true;
        } catch (Exception e) {
            logger.error("process 执行增量加载分仓到缓存失败:", e.getMessage());
           
        }
        logger.info("process 执行结束增量加载分仓到缓存-----");

        

        if(flag1 && flag2){
        	 return new ProcessResult( true); 
        }else{
        	 return new ProcessResult( false); 
        }
       

    }

}



/*-- 总持仓, 产品列表
select concat('hset lecurrent:H:', investorOid, ' ', productOid, ' \'',
'{"investorOid":"', investorOid, '",',
'"productOid":"', productOid, '",',
'"value":', floor(COALESCE(value, 0)*100), ',',
'"totalHoldVolume":',floor(COALESCE(totalHoldVolume, 0)*100), ',',
'"redeemableHoldVolume":', floor(COALESCE(redeemableHoldVolume, 0)*100), ',',
'"investTotalVolume":', floor(COALESCE(investTotalVolume, 0)*100), ',',
'"investTotalIncome":', floor(COALESCE(holdTotalIncome, 0)*100), ',',
'"holdYesterdayIncome":', floor(COALESCE(holdYesterdayIncome, 0)*100), ',',
'"confirmDate":', UNIX_TIMESTAMP(COALESCE(lastConfirmDate, current_date()))*1000, ',',
'"updateTime":', UNIX_TIMESTAMP(updateTime)*1000, '}\''
)
INTO outfile '#d/lecurrent_hold_#b_#p.log'
FROM T_MONEY_PUBLISHER_HOLD #x
where investorOid is not null and updateTime >= '#start'
;





-- 订单持仓hash
select concat('hmset lecurrent:O:', orderOid,
' holdVolume ', floor(COALESCE(investVolume, 0)*100),
' value ', floor(COALESCE(value, 0)*100),
' holdTotalIncome ', floor(COALESCE(totalIncome, 0)*100),
' holdYesterdayIncome ', floor(COALESCE(yesterdayIncome, 0)*100),
' redeemableIncome ', floor(COALESCE(redeemableIncome, 0)*100),
' holdStatus ', COALESCE(holdStatus, '0'),
' redeemStatus ', COALESCE(redeemStatus, '0'),
' updateTime ', UNIX_TIMESTAMP(updateTime)*1000,
' beginRedeemDate ', UNIX_TIMESTAMP(beginRedeemDate)*1000,
' beginAccuralDate ', UNIX_TIMESTAMP(beginAccuralDate)*1000, ''
)
into outfile '#d/lecurrent_hold_order_#b_#p.log'
from T_MONEY_PUBLISHER_HOLDAPART #x
where updateTime >= '#start';




-- 协议PDF文件信息, 分订单级别和产品级别两类文件
-- select concat('hset lecurrent:A:', COALESCE(orderOid, productOid), ' ', agreementType,
select concat('hset lecurrent:A:', agreementCode, ' ', agreementType,
' \'{',
'"productOid":"', productOid, '",',
'"orderOid":"', COALESCE(orderOid, ''), '",',
'"agreementCode":"', COALESCE(agreementCode,''), '",',
'"agreementName":"', COALESCE(agreementName,''), '",',
'"agreementUrl":"', COALESCE(agreementUrl,''), '",',
'"agreementType":"', COALESCE(agreementType,''), '",',
'"updateTime":"', UNIX_TIMESTAMP(COALESCE(updateTime,now()))*1000, '",',
'"createTime":"', UNIX_TIMESTAMP(COALESCE(createTime,now()))*1000, '"'
'}\''
)
into outfile '#d/lecurrent_pdf_#b_#p.log'
from T_MONEY_PUBLISHER_PRODUCT_AGREEMENT
where agreementUrl is not null and updateTime >= '#start';*/