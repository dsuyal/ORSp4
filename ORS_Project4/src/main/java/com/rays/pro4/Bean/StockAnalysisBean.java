package com.rays.pro4.Bean;

import java.util.Date;

public class StockAnalysisBean extends BaseBean {
	
	
	
	private String stockSymbol;
	private String analysisType;
	private Date startDate;
	private Date endDate;
	
	
	
	public String getStockSymbol() {
		return stockSymbol;
	}
	public void setStockSymbol(String stockSymbol) {
		this.stockSymbol = stockSymbol;
	}
	public String getAnalysisType() {
		return analysisType;
	}
	public void setAnalysisType(String analysisType) {
		this.analysisType = analysisType;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	@Override
	public String getkey() {
		// TODO Auto-generated method stub
		return analysisType;
	}
	@Override
	public String getValue() {
		// TODO Auto-generated method stub
		return analysisType;
	}
	
	
	
	



	

}
