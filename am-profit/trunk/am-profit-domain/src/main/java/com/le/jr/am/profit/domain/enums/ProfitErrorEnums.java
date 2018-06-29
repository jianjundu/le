package com.le.jr.am.profit.domain.enums;

public enum ProfitErrorEnums {

	/**
	 * 更新失败
	 */
	UPDATEERROR(50000, "更新失败"),

	/**
	 * 插入失败
	 */
	INSERTERROR(50001, "插入失败");

	public String value;

	public int code;

	ProfitErrorEnums(int code, String value) {
		this.value = value;
		this.code = code;
	}

	public static ProfitErrorEnums getInstance(String projectType) {
		ProfitErrorEnums[] allStatus = ProfitErrorEnums.values();
		for (ProfitErrorEnums ws : allStatus) {
			if (ws.value.equals(projectType)) {
				return ws;
			}
		}
		return null;
	}

}
