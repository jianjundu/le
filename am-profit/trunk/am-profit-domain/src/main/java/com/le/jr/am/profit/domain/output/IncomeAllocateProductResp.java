package com.le.jr.am.profit.domain.output;

import java.math.BigDecimal;

import com.le.jr.am.system.domain.BaseResp;


public class IncomeAllocateProductResp extends BaseResp {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String PROMPT_CODE_FIRST = "FIRST";// 首次分配收益
	public static final String PROMPT_CODE_Create = "CREATE";//待审核
	public static final String PROMPT_CODE_Allocating = "ALLOCATING";//发放中
	public static final String PROMPT_CODE_AllocateFail = "ALLOCATEFAIL";//发放失败
	public static final String PROMPT_CODE_Allocated = "ALLOCATED";//发放完成
	public static final String PROMPT_CODE_Apply = "APPLY";//可申请
	public static final String PROMPT_CODE_Nonapply = "NONAPPLY";//不可申请
	
	public static final String PROMPT_MESSAGE_FIRST = "该产品首次分配收益，请先选择该产品的收益首分配日，然后进行收益录入...";// 首次分配收益
	public static final String PROMPT_MESSAGE_Create = "请先审核该日的收益分配!";//待审核
	public static final String PROMPT_MESSAGE_Allocating = "请先等待该日的收益分配完成!";//发放中
	public static final String PROMPT_MESSAGE_AllocateFail = "该日收益派发异常，请重新手动派发!";//发放失败
	public static final String PROMPT_MESSAGE_Allocated = "今日收益已录入，待次日凌晨1点自动执行派息任务!";//发放完成
	public static final String PROMPT_MESSAGE_Apply = "今日未录入该日收益，请录入...";//可申请
	public static final String PROMPT_MESSAGE_Nonapply = "只能申请今日以及今日之前的收益分配!";//不可申请
	
	
	private String assetPoolOid;
	private String productOid;
	private String lastIncomeDate;//上一收益分配日
	private String incomeDate;//收益分配日
	private BigDecimal feeValue;//计提费用
	private BigDecimal productTotalScale;//产品总规模
	private BigDecimal productRewardBenefit;//奖励收益
	private String incomeCalcBasis;//收益计算基础
	private BigDecimal letvServiceFee;//某一天的乐视服务费
	private String promptCode;//对应收益分配情况代码
	private String promptMessage;//提示信息
	private String subType;//产品的子类型

	public String getSubType() {
		return subType;
	}
	public void setSubType(String subType) {
		this.subType = subType;
	}
	public String getAssetPoolOid() {
		return assetPoolOid;
	}
	public void setAssetPoolOid(String assetPoolOid) {
		this.assetPoolOid = assetPoolOid;
	}
	public String getProductOid() {
		return productOid;
	}
	public void setProductOid(String productOid) {
		this.productOid = productOid;
	}
	public String getLastIncomeDate() {
		return lastIncomeDate;
	}
	public void setLastIncomeDate(String lastIncomeDate) {
		this.lastIncomeDate = lastIncomeDate;
	}
	public String getIncomeDate() {
		return incomeDate;
	}
	public void setIncomeDate(String incomeDate) {
		this.incomeDate = incomeDate;
	}
	public BigDecimal getFeeValue() {
		return feeValue;
	}
	public void setFeeValue(BigDecimal feeValue) {
		this.feeValue = feeValue;
	}
	public BigDecimal getProductTotalScale() {
		return productTotalScale;
	}
	public void setProductTotalScale(BigDecimal productTotalScale) {
		this.productTotalScale = productTotalScale;
	}
	public BigDecimal getProductRewardBenefit() {
		return productRewardBenefit;
	}
	public void setProductRewardBenefit(BigDecimal productRewardBenefit) {
		this.productRewardBenefit = productRewardBenefit;
	}
	public String getIncomeCalcBasis() {
		return incomeCalcBasis;
	}
	public void setIncomeCalcBasis(String incomeCalcBasis) {
		this.incomeCalcBasis = incomeCalcBasis;
	}
	public BigDecimal getLetvServiceFee() {
		return letvServiceFee;
	}
	public void setLetvServiceFee(BigDecimal letvServiceFee) {
		this.letvServiceFee = letvServiceFee;
	}
	public String getPromptCode() {
		return promptCode;
	}
	public void setPromptCode(String promptCode) {
		this.promptCode = promptCode;
	}
	public String getPromptMessage() {
		return promptMessage;
	}
	public void setPromptMessage(String promptMessage) {
		this.promptMessage = promptMessage;
	}
	
	
	

}
