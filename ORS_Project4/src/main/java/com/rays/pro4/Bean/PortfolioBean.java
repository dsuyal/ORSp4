package com.rays.pro4.Bean;

public class PortfolioBean extends BaseBean{
	
	
	private String portfolioName;
	private int initialInvestmentAmount;
	private String riskToleranceLevel;
	private String investmentStrategy;
	
	

	public String getPortfolioName() {
		return portfolioName;
	}

	public void setPortfolioName(String portfolioName) {
		this.portfolioName = portfolioName;
	}

	public int getInitialInvestmentAmount() {
		return initialInvestmentAmount;
	}

	public void setInitialInvestmentAmount(int initialInvestmentAmount) {
		this.initialInvestmentAmount = initialInvestmentAmount;
	}

	public String getRiskToleranceLevel() {
		return riskToleranceLevel;
	}

	public void setRiskToleranceLevel(String riskToleranceLevel) {
		this.riskToleranceLevel = riskToleranceLevel;
	}

	public String getInvestmentStrategy() {
		return investmentStrategy;
	}

	public void setInvestmentStrategy(String investmentStrategy) {
		this.investmentStrategy = investmentStrategy;
	}

	@Override
	public String getkey() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getValue() {
		// TODO Auto-generated method stub
		return null;
	}

}
