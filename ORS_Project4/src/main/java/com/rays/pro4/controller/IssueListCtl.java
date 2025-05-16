package com.rays.pro4.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.rays.pro4.Bean.BaseBean;
import com.rays.pro4.Bean.IssueBean;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Model.IssueModel;
import com.rays.pro4.Util.DataUtility;
import com.rays.pro4.Util.PropertyReader;
import com.rays.pro4.Util.ServletUtility;

@WebServlet(name = "IssueListCtl", urlPatterns = { "/ctl/IssueListCtl" })

public class IssueListCtl extends BaseCtl {
	
	
	private static Logger log = Logger.getLogger(IssueListCtl.class);


	@Override
	protected void preload(HttpServletRequest request) {
		
		
		IssueModel smodel = new IssueModel();

		try {

			List slist = smodel.list(0, 0);

			request.setAttribute("isuue", slist);

		} catch (ApplicationException e) {
			e.printStackTrace();
		}


		Map<Integer, String> map = new HashMap();

		map.put(1, "Arush");
		map.put(2, "bhushan");
		map.put(3, "Chandan");
		//map.put(4, "Stop Order");

		request.setAttribute("issue", map);
		
		Map<Integer, String> map1 = new HashMap();

		map1.put(1, "Solved");
		map1.put(2, "Unsolved");
		map1.put(3, "In review");
		//map.put(4, "Stop Order");

		request.setAttribute("issue1", map1);

	

	}
	
	@Override
	protected BaseBean populateBean(HttpServletRequest request) {
		IssueBean bean = new IssueBean();



		bean.setId(DataUtility.getLong(request.getParameter("id")));
		
		bean.setOpenDate(DataUtility.getDate(request.getParameter("openDate")));
		bean.setTitle(DataUtility.getString(request.getParameter("title")));
		bean.setDiscription(DataUtility.getString(request.getParameter("discription")));
		bean.setAssignTo(DataUtility.getInt(request.getParameter("assignTo")));
		bean.setStatus(DataUtility.getInt(request.getParameter("status")));


        
		return bean;
	}

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.debug("IssueListCtl doGet Start");
		List list = null;
		List nextList = null;

		int pageNo = 1;
		int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));

		IssueBean bean = (IssueBean) populateBean(request);
		String op = DataUtility.getString(request.getParameter("operation"));

//	        get the selected checkbox ids array for delete list

		// String[] ids = request.getParameterValues("ids");
		IssueModel model = new IssueModel();

		try {
			list = model.search(bean, pageNo, pageSize);
			System.out.println("list" + list);

			nextList = model.search(bean, pageNo + 1, pageSize);

			request.setAttribute("nextlist", nextList.size());

			if (list == null || list.size() == 0) {
				ServletUtility.setErrorMessage("No record found ", request);
			}

			ServletUtility.setList(list, request);
			ServletUtility.setPageNo(pageNo, request);
			ServletUtility.setPageSize(pageSize, request);
			ServletUtility.forward(getView(), request, response);

		} catch (ApplicationException e) {
			log.error(e);
			ServletUtility.handleException(e, request, response);
			return;
		}
		log.debug("IssueListCtl doGet End");
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.debug("IssueListCtl doPost Start");

		List list;
		List nextList = null;

		int pageNo = DataUtility.getInt(request.getParameter("pageNo"));
		int pageSize = DataUtility.getInt(request.getParameter("pageSize"));
		pageNo = (pageNo == 0) ? 1 : pageNo;
		pageSize = (pageSize == 0) ? DataUtility.getInt(PropertyReader.getValue("page.size")) : pageSize;

		String op = DataUtility.getString(request.getParameter("operation"));
		IssueBean bean = (IssueBean) populateBean(request);
		// get the selected checkbox ids array for delete list
		String[] ids = request.getParameterValues("ids");

		IssueModel model = new IssueModel();

		if (OP_SEARCH.equalsIgnoreCase(op)) {
			pageNo = 1;
		} else if (OP_NEXT.equalsIgnoreCase(op)) {
			pageNo++;
		} else if (OP_PREVIOUS.equalsIgnoreCase(op) && pageNo > 1) {
			pageNo--;
		} else if (OP_NEW.equalsIgnoreCase(op)) {
			ServletUtility.redirect(ORSView.ISSUE_CTL, request, response);
			return;
		} else if (OP_RESET.equalsIgnoreCase(op)) {
			ServletUtility.redirect(ORSView.ISSUE_LIST_CTL, request, response);
			return;
		} else if (OP_DELETE.equalsIgnoreCase(op)) {
			pageNo = 1;
			if (ids != null && ids.length > 0) {
				IssueBean deletebean = new IssueBean();
				for (String id : ids) {
					deletebean.setId(DataUtility.getInt(id));
					try {
						model.delete(deletebean);
					} catch (ApplicationException e) {
						log.error(e);
						ServletUtility.handleException(e, request, response);
						return;
					}

					ServletUtility.setSuccessMessage("issue is Deleted Successfully", request);
				}
			} else {
				ServletUtility.setErrorMessage("Select at least one record", request);
			}
		}
		try {

			list = model.search(bean , pageNo, pageSize);

			nextList = model.search(bean, pageNo + 1, pageSize);

			request.setAttribute("nextlist", nextList.size());
			

		} catch (ApplicationException e) {
			log.error(e);
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
		log.debug("IssueListCtl doGet End");

	}

	@Override
	protected String getView() {
		return ORSView.ISSUE_LIST_VIEW;
	}

}
	
	
	
	


