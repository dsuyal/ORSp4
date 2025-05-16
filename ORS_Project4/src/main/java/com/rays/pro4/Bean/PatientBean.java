package com.rays.pro4.Bean;

import java.util.Date;

public class PatientBean extends BaseBean {
	
	private String name;
	private Date dateOfVisit;
	private String mobile;
	private String decease;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getDateOfVisit() {
		return dateOfVisit;
	}

	public void setDateOfVisit(Date dateOfVisit) {
		this.dateOfVisit = dateOfVisit;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getDecease() {
		return decease;
	}

	public void setDecease(String decease) {
		this.decease = decease;
	}

	@Override
	public String getkey() {
		// TODO Auto-generated method stub
		return decease;
	}

	@Override
	public String getValue() {
		// TODO Auto-generated method stub
		return decease;
	}


}
