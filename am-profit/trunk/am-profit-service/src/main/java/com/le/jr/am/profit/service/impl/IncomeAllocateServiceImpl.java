package com.le.jr.am.profit.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.le.jr.am.product.common.util.PageUtil;
import com.le.jr.am.product.common.util.UUIDUtil;
import com.le.jr.am.profit.dao.IncomeAllocateMapper;
import com.le.jr.am.profit.domain.IncomeAllocate;
import com.le.jr.am.profit.domain.IncomeEvent;
import com.le.jr.am.profit.domain.input.SearchAllocateVO;
import com.le.jr.am.profit.domain.input.SearchEventVo;
import com.le.jr.am.profit.service.IncomeAllocateService;
import com.le.jr.am.profit.service.IncomeEventService;
import com.le.jr.am.task.common.util.DateUtil;
import com.le.jr.trade.publictools.exception.BizException;
import com.le.jr.trade.publictools.page.Page;
import com.le.jr.trade.publictools.util.JsonUtils;
import com.le.jr.trade.publictools.util.StringUtil;



@Service("incomeAllocateService")
public class IncomeAllocateServiceImpl implements IncomeAllocateService{
	
	Logger logger = LoggerFactory.getLogger(IncomeAllocateServiceImpl.class);
	
	@Resource
	private IncomeAllocateMapper incomeAllocateMapper;
	
	@Resource
	private IncomeEventService incomeEventService;

	
	
	@Override
	public Date getLatestIncomeDate(String productOid) throws BizException{
		Date incomeDate = this.incomeAllocateMapper.getLatestIncomeDate(productOid);
		return incomeDate;
	}



	@Override
	public List<IncomeAllocate> selectIncomeAllocates(SearchAllocateVO vo) throws BizException{
		return null;
	}



	@Override
	public Page<IncomeAllocate> selectIncomeAllocates4Api(SearchAllocateVO vo) throws BizException{
		  Map<String, Object> map = new HashMap<>();

	        List<IncomeAllocate> result = new ArrayList<IncomeAllocate>();

	        if (vo.getCurrentPageNo() <= 0) {
	        	vo.setCurrentPageNo(0);
	        }
	        if (vo.getPageSize() <= 0) {
	        	vo.setPageSize(10);
	        }

	        PageUtil.setPageParam(vo.getCurrentPageNo(), vo.getPageSize(), map);

	        if (!StringUtil.isEmpty(vo.getProductOid())) {
	            map.put("productOid", vo.getProductOid());
	        }
	        
	        if (null != vo.getBaseDate()) {
	            map.put("baseDate", DateUtil.format(vo.getBaseDate()));
	        }
	        
	        
	        if (StringUtil.isNotBlank(vo.getAssetPoolOid())) {
	        	
	        	SearchEventVo s= new SearchEventVo();
	        	s.setAssetPoolOid(vo.getAssetPoolOid());
	        	
	        	List<String> productOids = incomeEventService.searchDistincProductEvents(s);
	        	
	        	logger.info("selectIncomeAllocates4Api:IncomeEventlist size:{}",productOids==null?"0":productOids.size());
	        	
	        	/*List<String> productOids = new ArrayList<String>();
	        	
	        	for(String e:list){
	        		if(!productOids.contains(e.getProductOid())){
	        			productOids.add(e.getProductOid());
	        		}
	        		
	        	}*/
	        	
	        	if(productOids.size()>0){
	        		map.put("productOids", productOids);
	        	}else{
	        		Page<IncomeAllocate> page = new Page<IncomeAllocate>();
	    	        page.setDataList(new ArrayList<IncomeAllocate>());
	    	        page.setTotalCount(0);
	    	        return page;
	        	}
	        	
	            
	        }

	        logger.info("selectIncomeAllocates4Api:map:{}",JsonUtils.writeValue(map));

	        int count = this.incomeAllocateMapper.selectCountIncomeAllocates4Api(map);
	        if (count > 0) {
	            result = this.incomeAllocateMapper.selectIncomeAllocates4Api(map);
	        }

	        Page<IncomeAllocate> page = new Page<IncomeAllocate>();
	        page.setDataList(result);
	        page.setTotalCount(count);

	        return page;
	}



	@Override
	public IncomeAllocate selectByEventOid(String eventOid) throws BizException{
		return incomeAllocateMapper.selectByEventOid(eventOid);
	}



	@Override
	public Boolean isIncomeAllocated(String productOid, Date baseDate)throws BizException {
		
		
		int i = incomeAllocateMapper.isIncomeAllocated(productOid, baseDate);
		
		return i>0?true:false;
		
		
	}



	@Override
	public IncomeAllocate selectByOid(String oid)throws BizException {
		return incomeAllocateMapper.selectByPrimaryKey(oid);
	}



	@Override
	public Boolean insert(IncomeAllocate ia) throws BizException{
		ia.setOid(UUIDUtil.creatUUID());
		int i= incomeAllocateMapper.insert(ia);
		return i>0?true:false;
	}



	/**
	 * 获取产品列表下所有最后分配收益的记录
	 */
	@Override
	public List<IncomeAllocate> findLatestIncomeAllocates(List<String> productOids) throws BizException{
		
		return incomeAllocateMapper.findLatestIncomeAllocates(productOids);
	}



	@Override
	public IncomeAllocate findLastValidIncomeAllocate(String productOid)throws BizException {
		
		List<IncomeAllocate> list = incomeAllocateMapper.findLastValidIncomeAllocate(productOid);
		
		if(list.size()>0){
			return list.get(0);
		}
		
		return null;
		
	}



	@Override
	public Boolean updateIncomeAllocate(IncomeAllocate vo) throws BizException {
		int i= incomeAllocateMapper.updateByPrimaryKeySelective(vo);
		
		return i>0?true:false;
	}
}
