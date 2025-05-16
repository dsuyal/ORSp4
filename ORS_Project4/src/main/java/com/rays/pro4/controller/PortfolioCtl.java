package com.rays.pro4.controller;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.rays.pro4.Bean.BaseBean;
import com.rays.pro4.Bean.PortfolioBean;

import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Exception.DuplicateRecordException;
import com.rays.pro4.Model.PortfolioModel;
import com.rays.pro4.Util.DataUtility;
import com.rays.pro4.Util.DataValidator;
import com.rays.pro4.Util.PropertyReader;
import com.rays.pro4.Util.ServletUtility;


@WebServlet(name = "PortfolioCtl", urlPatterns = { "/ctl/PortfolioCtl" })
public class PortfolioCtl extends BaseCtl {

	
	@Override
	protected void preload(HttpServletRequest request) {
		
		PortfolioModel model = new PortfolioModel();
		Map<Integer, String> map = new HashMap();
		
		map.put(1, "Low");
		map.put(2, "Medium");
		map.put(3, "High");
		
		

		request.setAttribute("prolist", map);
	}
	    


	@Override
	protected boolean validate(HttpServletRequest request) {
		System.out.println("uctl Validate");

		boolean pass = true;

		if (DataValidator.isNull(request.getParameter("portfolioName"))) {
			request.setAttribute("portfolioName", PropertyReader.getValue("error.require", "portfolioName"));
			pass = false;
		}

		System.out.println("portfolioName ====> " + request.getParameter("portfolioName"));

		if (DataValidator.isNull(request.getParameter("initialInvestmentAmount"))) {
			request.setAttribute("initialInvestmentAmount", PropertyReader.getValue("error.require", "initialInvestmentAmount"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("riskToleranceLevel"))) {
			request.setAttribute("riskToleranceLevel", PropertyReader.getValue("error.require", "riskToleranceLevel"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("investmentStrategy"))) {
			request.setAttribute("investmentStrategy", PropertyReader.getValue("error.require", "investmentStrategy"));
			pass = false;
		}
		return pass;

	}

	@Override
	protected BaseBean populateBean(HttpServletRequest request) {
		PortfolioBean bean = new PortfolioBean();

		bean.setId(DataUtility.getLong(request.getParameter("id")));
		bean.setPortfolioName(DataUtility.getString(request.getParameter("portfolioName")));
		bean.setInitialInvestmentAmount(DataUtility.getInt(request.getParameter("initialInvestmentAmount")));
		bean.setRiskToleranceLevel(DataUtility.getString(request.getParameter("riskToleranceLevel")));
		bean.setInvestmentStrategy(DataUtility.getString(request.getParameter("investmentStrategy")));

		return bean;
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String op = DataUtility.getString(request.getParameter("operation"));

		PortfolioModel model = new PortfolioModel();

		long id = DataUtility.getLong(request.getParameter("id"));

		//System.out.println("Portfolio Edit Id >= " + id);

		if (id != 0 && id > 0) {

			System.out.println("in id > 0  condition " + id);
			PortfolioBean bean;

			try {
				bean = model.findByPK(id);
				ServletUtility.setBean(bean, request);

			} catch (Exception e) {

				e.printStackTrace();
			}
		}

		ServletUtility.forward(getView(), request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String op = DataUtility.getString(request.getParameter("operation"));

		long id = DataUtility.getLong(request.getParameter("id"));

		System.out.println(">>>><<<<>><<><<><<><>" + id + op);

		PortfolioModel model = new PortfolioModel();

		if (OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op)) {

			PortfolioBean bean = (PortfolioBean) populateBean(request);

			try {
				if (id > 0) {

					model.update(bean);
					ServletUtility.setBean(bean, request);

					ServletUtility.setSuccessMessage("Portfoilio  is successfully Updated", request);
				} else {
					System.out.println(" U ctl DoPost 33333");
					long pk = model.add(bean);
					// ServletUtility.setBean(bean, request);
					ServletUtility.setBean(bean, request);

					ServletUtility.setSuccessMessage("Portfolio is successfully Added", request);

					bean.setId(pk);
				}

			} catch (ApplicationException e) {
				ServletUtility.handleException(e, request, response);
				return;
			} catch (DuplicateRecordException e) {

				ServletUtility.setBean(bean, request);
				ServletUtility.setErrorMessage("Login id already exists", request);
			}
		} else if (OP_DELETE.equalsIgnoreCase(op)) {

			PortfolioBean bean = (PortfolioBean) populateBean(request);
			try {
				model.delete(bean);

				ServletUtility.redirect(ORSView.PORTFOLIO_CTL, request, response);
				return;
			} catch (ApplicationException e) {
				ServletUtility.handleException(e, request, response);
				return;
			}
		} else if (OP_CANCEL.equalsIgnoreCase(op)) {
			System.out.println(" U  ctl Do post 77777");

			ServletUtility.redirect(ORSView.PORTFOLIO_LIST_CTL, request, response);
			return;
		}
		ServletUtility.forward(getView(), request, response);
	}

	@Override
	protected String getView() {
		
		return ORSView.PORTFOLIO_VIEW;
	}

}
