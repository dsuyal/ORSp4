package com.rays.pro4.controller;

import java.io.IOException;
import java.sql.Date;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.rays.pro4.Bean.BaseBean;
import com.rays.pro4.Bean.StockAnalysisBean;
import com.rays.pro4.Bean.VehicleBean;
import com.rays.pro4.Model.StockAnalysisModel;
import com.rays.pro4.Model.VehicleModel;
import com.rays.pro4.Util.DataUtility;
import com.rays.pro4.Util.DataValidator;
import com.rays.pro4.Util.PropertyReader;
import com.rays.pro4.Util.ServletUtility;


@WebServlet(name = "StockAnalysisCtl", urlPatterns = { "/ctl/StockAnalysisCtl" })


	public class StockAnalysisCtl extends BaseCtl {
		
		@Override
		protected void preload(HttpServletRequest request) {

			HashMap map = new HashMap();
			map.put("Fundamental", "Fundamental");
			map.put("Technical", "Technical");
			
			request.setAttribute("map", map);
		}
		
		@Override
		protected boolean validate(HttpServletRequest request) {
			System.out.println("uctl Validate");

			boolean pass = true;

			if (DataValidator.isNull(request.getParameter("stockSymbol"))) {
				request.setAttribute("stockSymbol", PropertyReader.getValue("error.require", "stockSymbol"));
				pass = false;
			}
			
			if (DataValidator.isLong(request.getParameter("stockSymbol"))) {
				request.setAttribute("stockSymbol", PropertyReader.getValue("error.require", "stockSymbol"));
				pass = false;
			}
			else if (DataValidator.isTooLong(request.getParameter("stockSymbol"), 45)) {
			    request.setAttribute("stockSymbol", "stockSymbol too long");
			    pass = false;
			}
			
			if (DataValidator.isNull(request.getParameter("analysisType"))) {
				request.setAttribute("analysisType", PropertyReader.getValue("error.require", "analysisType"));
				pass = false;
			
			}
			if (DataValidator.isNull(request.getParameter("startDate"))) {
				request.setAttribute("startDate", PropertyReader.getValue("error.require", "startDate"));
				pass = false;
			}
			if (DataValidator.isNull(request.getParameter("endDate"))) {
				request.setAttribute("endDate", PropertyReader.getValue("error.require", "endDate"));
				pass = false;
			}
			
			System.out.println("Validate ended and value of pass is = "+pass);

			return pass;

		}
		@Override
		protected BaseBean populateBean(HttpServletRequest request) {
			
			StockAnalysisBean bean = new StockAnalysisBean();
			
			bean.setId(DataUtility.getInt(request.getParameter("id")));
			bean.setStockSymbol(DataUtility.getString(request.getParameter("stockSymbol")));
			bean.setAnalysisType(DataUtility.getString(request.getParameter("analysisType")));
			bean.setStartDate( DataUtility.getDate(request.getParameter("startDate")));
			bean.setEndDate( DataUtility.getDate(request.getParameter("endDate")));
			System.out.println("analysis Type >= " + (request.getParameter("analysisType")));
			
			return bean;
		}
		
		@Override
		protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
			
			String op = DataUtility.getString(req.getParameter("operation"));

			StockAnalysisModel model = new StockAnalysisModel();

			long id = DataUtility.getLong(req.getParameter("id"));
			System.out.println("id = "+id);
			System.out.println("stockanalysis Edit Id >= " + id);

			if (id != 0 && id > 0) {

				System.out.println("in id > 0  condition " + id);
				StockAnalysisBean bean;

				try {
					bean = model.findByPK(id);
					ServletUtility.setBean(bean, req);
				
				} catch (Exception e) {

					e.printStackTrace();
				}
			}

			ServletUtility.forward(getView(), req, resp);

		}
			//super.doGet(req, resp);
		
		@Override
		protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
			
			System.out.println("vctl Do Post");

			String op = DataUtility.getString(req.getParameter("operation"));

			long id = DataUtility.getLong(req.getParameter("id"));

			System.out.println(">>>><<<<>><<><<><<><>**********" + id   +  op);

			StockAnalysisModel model = new StockAnalysisModel();

			if (OP_CANCEL.equalsIgnoreCase(op)) {

				ServletUtility.redirect(ORSView.STOCKANALYSIS_LIST_CTL, req, resp);

			}
			if (OP_RESET.equalsIgnoreCase(op)) {

				ServletUtility.redirect(ORSView.STOCKANALYSIS_CTL, req, resp);

			}
			if (OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op)) {

				StockAnalysisBean bean = (StockAnalysisBean) populateBean(req);

				if (id > 0) {

					try {
						model.update(bean);
						ServletUtility.setBean(bean, req);
						ServletUtility.setSuccessMessage("Stock is successfully Updated", req);
			               System.out.println("update chal rhi h");
			      

						ServletUtility.forward(getView(), req, resp);
					} catch (Exception e) {
						System.out.println("stock not update");
						e.printStackTrace();
					}

				} else {

					try {
						long pk = model.add(bean);
						ServletUtility.setSuccessMessage("stock is successfully Added", req);

						bean.setId(pk);
						ServletUtility.forward(getView(), req, resp);
					} catch (Exception e) {
						System.out.println("stock not added");
						e.printStackTrace();
					}

				}

			}
		}
			//super.doPost(req, resp);
			
		
		

		@Override
		protected String getView() {
			// TODO Auto-generated method stub
			return ORSView.STOCKANALYSIS_VIEW;
		}
		
		

	}

