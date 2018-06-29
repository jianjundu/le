package com.le.jr.am.profit.interfaces.impl;

import javax.annotation.Resource;

import com.le.jr.am.profit.domain.input.SearchEventVo;
import com.le.jr.trade.publictools.util.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.le.jr.am.profit.domain.IncomeEvent;
import com.le.jr.am.profit.interfaces.IncomeEventInterface;
import com.le.jr.am.profit.service.IncomeEventService;
import com.le.jr.trade.publictools.Messages;
import com.le.jr.trade.publictools.data.Code;
import com.le.jr.trade.publictools.data.Message;
import com.le.jr.trade.publictools.exception.BizException;

import java.util.List;


@Service("incomeEventInterface")
public class IncomeEventInterfaceImpl implements IncomeEventInterface {


    Logger logger = LoggerFactory.getLogger(IncomeEventInterfaceImpl.class);

    @Resource
    private IncomeEventService incomeEventService;


    @Override
    public Message<IncomeEvent> getLastIncomeEventByAssetPoolId(String assetPoolOid, String productOid) {
        logger.info(" getLastIncomeEventByAssetPoolId,assetPoolOid{}", assetPoolOid);
        IncomeEvent result = null;
        try {

            result = incomeEventService.getLastIncomeEventByAssetPoolId(assetPoolOid, productOid);

        } catch (BizException e) {
            logger.error("getLastIncomeEventByAssetPoolId error...", e);
            return Messages.failed(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("getLastIncomeEventByAssetPoolId error...", e);
            return Messages.failed(Code.SYSTEMEXCEPTION.getValue());
        }
        return Messages.success(result);
    }

    @Override
    public Message<List<IncomeEvent>> selectEvents(SearchEventVo searchEventVo) {
        logger.info("selectEvents,searchEventVo{}", JsonUtils.writeValue(searchEventVo));
        List<IncomeEvent> result;
        try {
            result = incomeEventService.selectEvents(searchEventVo);
        } catch (BizException e) {
            logger.error("selectEvents error...", e);
            return Messages.failed(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("selectEvents error...", e);
            return Messages.failed(Code.SYSTEMEXCEPTION.getValue());
        }
        return Messages.success(result);
    }


}
