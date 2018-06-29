package com.le.jr.am.profit.domain.output;

import java.io.Serializable;

import com.le.jr.am.system.domain.BaseResp;



public class IncomeDistributionResp extends BaseResp implements Serializable{
	

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String oid;//收益分配表的oid
	private String assetpoolOid;
	private String assetPoolName;
	private String productOid;
	private String productName;
	private String baseDate; // 基准日
	private String totalAllocateIncome;//总分配收益
	private String capital;//产品总规模
	private String allocateIncome;//分配收益
	private String rewardIncome;//奖励收益
	private String ratio;//收益率
	private String creator;// 申请人
	private String createTime;  // 申请时间
	private String auditor;  // 审批人
	private String auditTime; // 审批时间
	private String status;// 状态 (待审核: CREATE;通过: PASS;驳回: FAIL;已删除: DELETE)
	private String successAllocateIncome;//成功分配收益	
	private String successAllocateRewardIncome;//成功分配奖励收益金额
	private String leftAllocateIncome;//剩余收益
	private String successAllocateInvestors;//成功分配投资者数
	private String failAllocateInvestors;//失败分配投资者数
	
	public String getOid() {
		return oid;
	}
	public void setOid(String oid) {
		this.oid = oid;
	}
	public String getAssetpoolOid() {
		return assetpoolOid;
	}
	public void setAssetpoolOid(String assetpoolOid) {
		this.assetpoolOid = assetpoolOid;
	}
	public String getAssetPoolName() {
		return assetPoolName;
	}
	public void setAssetPoolName(String assetPoolName) {
		this.assetPoolName = assetPoolName;
	}
	public String getProductOid() {
		return productOid;
	}
	public void setProductOid(String productOid) {
		this.productOid = productOid;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getBaseDate() {
		return baseDate;
	}
	public void setBaseDate(String baseDate) {
		this.baseDate = baseDate;
	}
	public String getTotalAllocateIncome() {
		return totalAllocateIncome;
	}
	public void setTotalAllocateIncome(String totalAllocateIncome) {
		this.totalAllocateIncome = totalAllocateIncome;
	}
	public String getCapital() {
		return capital;
	}
	public void setCapital(String capital) {
		this.capital = capital;
	}
	public String getAllocateIncome() {
		return allocateIncome;
	}
	public void setAllocateIncome(String allocateIncome) {
		this.allocateIncome = allocateIncome;
	}
	public String getRewardIncome() {
		return rewardIncome;
	}
	public void setRewardIncome(String rewardIncome) {
		this.rewardIncome = rewardIncome;
	}
	public String getRatio() {
		return ratio;
	}
	public void setRatio(String ratio) {
		this.ratio = ratio;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getAuditor() {
		return auditor;
	}
	public void setAuditor(String auditor) {
		this.auditor = auditor;
	}
	public String getAuditTime() {
		return auditTime;
	}
	public void setAuditTime(String auditTime) {
		this.auditTime = auditTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getSuccessAllocateIncome() {
		return successAllocateIncome;
	}
	public void setSuccessAllocateIncome(String successAllocateIncome) {
		this.successAllocateIncome = successAllocateIncome;
	}
	public String getSuccessAllocateRewardIncome() {
		return successAllocateRewardIncome;
	}
	public void setSuccessAllocateRewardIncome(String successAllocateRewardIncome) {
		this.successAllocateRewardIncome = successAllocateRewardIncome;
	}
	public String getLeftAllocateIncome() {
		return leftAllocateIncome;
	}
	public void setLeftAllocateIncome(String leftAllocateIncome) {
		this.leftAllocateIncome = leftAllocateIncome;
	}
	public String getSuccessAllocateInvestors() {
		return successAllocateInvestors;
	}
	public void setSuccessAllocateInvestors(String successAllocateInvestors) {
		this.successAllocateInvestors = successAllocateInvestors;
	}
	public String getFailAllocateInvestors() {
		return failAllocateInvestors;
	}
	public void setFailAllocateInvestors(String failAllocateInvestors) {
		this.failAllocateInvestors = failAllocateInvestors;
	}
	

	
	
	
}
