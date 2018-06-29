package com.le.jr.am.profit.interfaces;

import java.util.List;

import com.le.jr.am.profit.domain.HoldApart;
import com.le.jr.am.profit.domain.input.ApartIncomeRequest;
import com.le.jr.am.profit.domain.input.HoldDetailRequest;
import com.le.jr.am.profit.domain.input.SearchHoldApartVo;
import com.le.jr.am.profit.domain.output.ApartIncomeResponse;
import com.le.jr.am.profit.domain.output.HoldDetailResponse;
import com.le.jr.trade.publictools.data.Message;
import com.le.jr.trade.publictools.exception.BizException;
import com.le.jr.trade.publictools.page.Page;


/**
 * 持有人分仓接口
 * xxx
 *
 * @author lining6
 * @date 2016年11月17日 下午5:15:02
 *
 */
public interface HoldApartInterface {
	
	/**
	 * 根据oid查询
	 * @param holdApartOid
	 * @return
	 */
	public Message<HoldApart> findHoldApartByOid(String holdApartOid);
	
	

	public Message<HoldApart> findHoldApartByOrderOid(String orderOid);


	/**
	 * 根据平台订单code查询分仓记录
	 * @param orderCode 平台订单code
	 * @return
	 */
	Message<HoldDetailResponse> findHoldApartByOrderCode(String orderCode)throws BizException ;

	
	public Message<Page<HoldApart>> selectHoldApart4ApiByVo(SearchHoldApartVo vo);
	
	
	public Message<List<HoldApart>> selectHoldApartByVo(SearchHoldApartVo vo);
	
	
	public Message<HoldDetailResponse> queryBalanceByLevel(HoldDetailRequest request) ;
	
	public Message<ApartIncomeResponse> queryApartIncome(ApartIncomeRequest request);


	/**
	 * 修改分仓锁定奖励Id ，起息日，可赎回日
	 * @param lockRewardId
	 * @return
	 * @throws BizException
	 */
	public Message<Boolean> updateHoldApartRewardInfo(String orderOid,String lockRewardId);
	
	
}
