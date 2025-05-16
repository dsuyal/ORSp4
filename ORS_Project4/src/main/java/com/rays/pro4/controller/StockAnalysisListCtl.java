package com.rays.pro4.controller;


import java.io.IOException;
import java.sql.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.rays.pro4.Bean.BaseBean;
import com.rays.pro4.Bean.StockAnalysisBean;
import com.rays.pro4.Model.StockAnalysisModel;

import com.rays.pro4.Util.DataUtility;
import com.rays.pro4.Util.PropertyReader;
import com.rays.pro4.Util.ServletUtility;

@WebServlet(name = "StockAnalysisListCtl", urlPatterns = { "/ctl/StockAnalysisListCtl" })

public class StockAnalysisListCtl extends BaseCtl {
	

		@Override
		protected void preload(HttpServletRequest request) {
			StockAnalysisModel model = new StockAnalysisModel();
			try {
				List vlist = model.list();
				request.setAttribute("vlist", vlist);

				List rlist = model.list();

				request.setAttribute("prolist", rlist);

			} catch (Exception e) {
				// TODO: handle exception
			}
		}

		@Override
		protected BaseBean populateBean(HttpServletRequest request) {
			
	        StockAnalysisBean bean = new StockAnalysisBean();
			
			bean.setId(DataUtility.getInt(request.getParameter("id")));
			bean.setStockSymbol(DataUtility.getString(request.getParameter("stockSymbol")));
			bean.setAnalysisType(DataUtility.getString(request.getParameter("analysisType")));
			bean.setStartDate( DataUtility.getDate(request.getParameter("startDate")));
			bean.setEndDate(DataUtility.getDate(request.getParameter("endDate")));
			System.out.println("vehicle amount >= " + (request.getParameter("startDate")));
			
			return bean;
		}

		@Override
		protected void doGet(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException {
			List list = null;
			List nextList = null;

			int pageNo = 1;
			int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));

			StockAnalysisBean bean = (StockAnalysisBean) populateBean(request);
	      System.out.println("stockAnalysis list ctl");
	      System.out.println();
	      System.out.println(bean.getStockSymbol());
	      System.out.println(bean.getAnalysisType());
	      System.out.println();
	      StockAnalysisModel model = new StockAnalysisModel();

			try {
				list = model.search(bean, pageNo, pageSize);
				nextList = model.search(bean, pageNo + 1, pageSize);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("list" + list);

			request.setAttribute("nextlist", nextList.size());

			if (list == null || list.size() == 0) {
				ServletUtility.setErrorMessage("No record found ", request);
			}

			ServletUtility.setList(list, request);
			ServletUtility.setPageNo(pageNo, request);
			ServletUtility.setPageSize(pageSize, request);
			ServletUtility.forward(getView(), request, response);

		}

		@Override
		protected void doPost(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException {
			List list;
			List nextList = null;

			int pageNo = DataUtility.getInt(request.getParameter("pageNo"));
			int pageSize = DataUtility.getInt(request.getParameter("pageSize"));

			pageNo = (pageNo == 0) ? 1 : pageNo;
			pageSize = (pageSize == 0) ? DataUtility.getInt(PropertyReader.getValue("page.size")) : pageSize;

			String op = DataUtility.getString(request.getParameter("operation"));
			StockAnalysisBean bean = (StockAnalysisBean) populateBean(request);

			String[] ids = request.getParameterValues("ids");

			StockAnalysisModel model = new StockAnalysisModel();

			if (OP_SEARCH.equalsIgnoreCase(op)) {
				pageNo = 1;
			} else if (OP_NEXT.equalsIgnoreCase(op)) {
				pageNo++;
			} else if (OP_PREVIOUS.equalsIgnoreCase(op) && pageNo > 1) {
				pageNo--;
			} else if (OP_NEW.equalsIgnoreCase(op)) {
				ServletUtility.redirect(ORSView.STOCKANALYSIS_CTL, request, response);
				return;
			} else if (OP_RESET.equalsIgnoreCase(op)) {
				ServletUtility.redirect(ORSView.STOCKANALYSIS_LIST_CTL, request, response);
				return;
			} else if (OP_DELETE.equalsIgnoreCase(op)) {
				pageNo = 1;
				if (ids != null && ids.length > 0) {
					StockAnalysisBean deletebean = new StockAnalysisBean();
					for (String id : ids) {
						deletebean.setId(DataUtility.getInt(id));

						try {
							model.delete(deletebean);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						ServletUtility.setSuccessMessage("Stock is Deleted Successfully", request);
					}
				} else {
					ServletUtility.setErrorMessage("Select at least one record", request);
				}
			}

			try {

				list = model.search(bean, pageNo, pageSize);
				System.out.println("model amount >= " + (request.getParameter("startDate")));
				nextList = model.search(bean, pageNo + 1, pageSize);

				request.setAttribute("nextlist", nextList.size());

			} catch (Exception e) {
				ServletUtility.handleException(e, request, response);
				return;
			}
			if (list == null || list.size() == 0 && !OP_DELETE.equalsIgnoreCase(op)) {
				ServletUtility.setErrorMessage("No record found ", request);
			}
			ServletUtility.setList(list, request);
			ServletUtility.setBean(bean, request);
			ServletUtility.setPageNo(pageNo, request);
			ServletUtility.setPageSize(pageSize, request);
			ServletUtility.forward(getView(), request, response);
		}
		@Override
		protected String getView() {
			// TODO Auto-generated method stub
			return ORSView.STOCKANALYSIS_LIST_VIEW;
		}

	}

		
		





