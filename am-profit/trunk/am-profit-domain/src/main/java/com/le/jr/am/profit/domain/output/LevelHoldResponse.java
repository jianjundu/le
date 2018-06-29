package com.le.jr.am.profit.domain.output;

import java.util.List;

public class LevelHoldResponse extends ApiResp{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	List<LevelHold> levelHolds;
	public List<LevelHold> getLevelHolds() {
		return levelHolds;
	}
	public void setLevelHolds(List<LevelHold> levelHolds) {
		this.levelHolds = levelHolds;
	}
	
	
}
