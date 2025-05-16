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
import com.rays.pro4.Bean.StockBean;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Exception.DuplicateRecordException;
import com.rays.pro4.Model.StockModel;
import com.rays.pro4.Model.IssueModel;
import com.rays.pro4.Util.DataUtility;
import com.rays.pro4.Util.DataValidator;
import com.rays.pro4.Util.PropertyReader;
import com.rays.pro4.Util.ServletUtility;


@WebServlet(name = "IssueCtl", urlPatterns = { "/ctl/IssueCtl" })

public class IssueCtl extends BaseCtl{
	
	
	private static final long serialVersionUID = 1L;

	/** The log. */
	private static Logger log = Logger.getLogger(IssueCtl.class);

	@Override
	protected boolean validate(HttpServletRequest request) {
	    log.debug("IssueCtl Method validate Started");

	    boolean pass = true;

	    String openDate = request.getParameter("openDate");
	    String title = request.getParameter("title");
	    String discription = request.getParameter("discription");
	    String assignTo = request.getParameter("assignTo");
	    String status = request.getParameter("status");
	    String operation = request.getParameter("operation");

	    // Logging to check values
	    log.debug("openDate: " + openDate);
	    log.debug("title: " + title);
	    log.debug("discription: " + discription);
	    log.debug("assignTo: " + assignTo);
	    log.debug("status: " + status);
	    log.debug("operation: " + operation);

		if (DataValidator.isNull(request.getParameter("openDate"))) {
			request.setAttribute("openDate", PropertyReader.getValue("error.require", "OpenDate"));
			pass = false;
		} else if (!DataValidator.isDate(request.getParameter("openDate"))) {
			request.setAttribute("openDate", PropertyReader.getValue("error.date", "OpenDate"));
			pass = false;
		}


	    if (DataValidator.isNull(title)) {
	        request.setAttribute("title", PropertyReader.getValue("error.require", "Title"));
	        pass = false;
	    }

	    if (DataValidator.isNull(discription)) {
	        request.setAttribute("discription", PropertyReader.getValue("error.require", "Description"));
	        pass = false;
	    }

	    if (DataValidator.isNull(assignTo)) {
	        request.setAttribute("assignTo", PropertyReader.getValue("error.require", "Assign To"));
	        pass = false;
	    }

	    if (DataValidator.isNull(status)) {
	        request.setAttribute("status", PropertyReader.getValue("error.require", "Status"));
	        pass = false;
	    }

	    log.debug("IssueCtl Method validate Ended");
	    return pass;
	}

	@Override
	protected void preload(HttpServletRequest request) {

	    Map<Integer, String> map = new HashMap<>();

	    map.put(1, "Arush");
	    map.put(2, "Bhushan");
	    map.put(3, "Chandan");

	    request.setAttribute("issue", map);

	    Map<Integer, String> map1 = new HashMap<>();

	    map1.put(1, "Solved");
	    map1.put(2, "Unsolved");
	    map1.put(3, "In review");

	    request.setAttribute("issue1", map1);
	}


	
	@Override
	protected BaseBean populateBean(HttpServletRequest request) {
	    log.debug("IssueCtl Method populateBean Started");

	    IssueBean bean = new IssueBean();
	    bean.setId(DataUtility.getLong(request.getParameter("id")));

	    // Log the ID to debug
	    log.debug("Issue ID: " + bean.getId());

	    bean.setOpenDate(DataUtility.getDate(request.getParameter("openDate")));
	    bean.setTitle(DataUtility.getString(request.getParameter("title")));
	    bean.setDiscription(DataUtility.getString(request.getParameter("discription")));
	    
	    System.out.println("In populateBean: discription = " + request.getParameter("discription"));

	    bean.setAssignTo(DataUtility.getInt(request.getParameter("assignTo")));
	    bean.setStatus(DataUtility.getInt(request.getParameter("status")));
	    
	    System.out.println("From JSP -> request.getParameter(\"discription\") = " + request.getParameter("discription"));
	    System.out.println("Set in bean -> bean.getDiscription() = " + bean.getDiscription());


	    log.debug("IssueCtl Method populateBean Ended");
	    return bean;
	    
	}


	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.debug("IssueCtl Method doGet Started");

		String op = DataUtility.getString(request.getParameter("operation"));
		// get model
		IssueModel model = new IssueModel();
		long id = DataUtility.getLong(request.getParameter("id"));
		System.out.println("Issue Edit Id >= " + id);
		if (id != 0 && id > 0) {
			System.out.println("in id > 0  condition");
			IssueBean bean;
			try {
				bean = model.findByPK(id);
				System.out.println(bean);
				ServletUtility.setBean(bean, request);
			} catch (ApplicationException e) {
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			}
		}

		ServletUtility.forward(getView(), request, response);
		log.debug("IssueCtl Method doGet Ended");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("uctl Do Post");

		log.debug("IssueCtl Method doPost Started");

		String op = DataUtility.getString(request.getParameter("operation"));
		long id = DataUtility.getLong(request.getParameter("id"));

		IssueModel model = new IssueModel();
		if (OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op)) {
			IssueBean bean = (IssueBean) populateBean(request);
			System.out.println(" U ctl DoPost 11111111");

			try {
				if (id > 0) {

					// System.out.println(" in dopost update");

					model.update(bean);
					ServletUtility.setBean(bean, request);
					System.out.println(" U ctl DoPost 222222");
					ServletUtility.setSuccessMessage("Issue is successfully Updated", request);

				} else {
					System.out.println(" U ctl DoPost 33333");
					long pk = model.add(bean);

					bean = model.findByPK(pk);

					ServletUtility.setBean(bean, request);

					ServletUtility.setSuccessMessage("Issue is successfully Added", request);
					/*
					 * ServletUtility.forward(getView(), request, response);
					 */ }
				/*
				 * ServletUtility.setBean(bean, request);
				 * ServletUtility.setSuccessMessage("Stock is successfully saved", request);
				 */

			} catch (ApplicationException e) {
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			} catch (DuplicateRecordException e) {
				System.out.println(" U ctl D post 4444444");
				ServletUtility.setBean(bean, request);
				ServletUtility.setErrorMessage("Login id already exists", request);
			}
		} else if (OP_DELETE.equalsIgnoreCase(op)) {
			System.out.println(" U ctl D p 5555555");

			IssueBean bean = (IssueBean) populateBean(request);
			try {
				model.delete(bean);
				System.out.println(" u ctl D Post  6666666");
				ServletUtility.redirect(ORSView.ISSUE_CTL, request, response);
				return;
			} catch (ApplicationException e) {
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			}

		} else if (OP_CANCEL.equalsIgnoreCase(op)) {
			System.out.println(" U  ctl Do post 77777");

			ServletUtility.redirect(ORSView.ISSUE_LIST_CTL, request, response);
			return;
		}
		ServletUtility.forward(getView(), request, response);

		log.debug("IssueCtl Method doPostEnded");
	}

	@Override
	protected String getView() {
		return ORSView.ISSUE_VIEW;
	}

	
}
