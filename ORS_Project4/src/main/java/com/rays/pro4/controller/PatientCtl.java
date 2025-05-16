package com.rays.pro4.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.rays.pro4.Bean.BaseBean;
import com.rays.pro4.Bean.PatientBean;
import com.rays.pro4.Model.PatientModel;
import com.rays.pro4.Util.DataUtility;
import com.rays.pro4.Util.DataValidator;
import com.rays.pro4.Util.PropertyReader;
import com.rays.pro4.Util.ServletUtility;

@WebServlet(name = "PatientCtl", urlPatterns = { "/ctl/PatientCtl" })


public class PatientCtl extends BaseCtl {
	
	@Override
	protected void preload(HttpServletRequest request) {
		// TODO Auto-generated method stub
		PatientModel model = new PatientModel();

		try {

			List pList = model.list();
			request.setAttribute("pList", pList);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected boolean validate(HttpServletRequest request) {
		System.out.println("uctl Validate");

		boolean pass = true;

		if (DataValidator.isNull(request.getParameter("name"))) {
			request.setAttribute("name", PropertyReader.getValue("error.require", "name"));
			pass = false;
		} else if (!DataValidator.isName(request.getParameter("name"))) {
			request.setAttribute("name", " name must contains alphabet only");
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("dateOfVisit"))) {
			request.setAttribute("dateOfVisit", PropertyReader.getValue("error.require", "dateofvisit"));
			pass = false;
		} else if (!DataValidator.isDate(request.getParameter("dateOfVisit"))) {
			request.setAttribute("dateOfVisit", "dateofvisit name must contains Number only");
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("mobile"))) {
			request.setAttribute("mobile", PropertyReader.getValue("error.require", "mobile"));
			pass = false;
		} else if (!DataValidator.isMobileNo(request.getParameter("mobile"))) {
			request.setAttribute("mobile", "mobile name must contains Number only");
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("decease"))) {
			request.setAttribute("decease", PropertyReader.getValue("error.require", "decease"));
			pass = false;
		} else if (!DataValidator.isName(request.getParameter("decease"))) {
			request.setAttribute("decease", "decease name must contains alphabet only");
			pass = false;
		}
		return pass;

	}

	protected BaseBean populateBean(HttpServletRequest request) {

		PatientBean bean = new PatientBean();

		System.out.println("populateBean id>>>>> " + request.getParameter("id"));

		bean.setId(DataUtility.getLong(request.getParameter("id")));

		System.out.println("populateBean bean id get " + bean.getId());

		bean.setName(DataUtility.getString(request.getParameter("name")));

		bean.setDateOfVisit(DataUtility.getDate(request.getParameter("dateOfVisit")));

		bean.setMobile(DataUtility.getString(request.getParameter("mobile")));

		bean.setDecease(DataUtility.getString(request.getParameter("decease")));

		bean.setId(DataUtility.getLong(request.getParameter("pList")));
		populateDTO(bean, request);

		return bean;

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		PatientModel model = new PatientModel();

		long id = DataUtility.getLong(request.getParameter("id"));

		System.out.println("product Edit Id >= " + id);

		if (id != 0 && id > 0) {

			System.out.println("in id > 0  condition " + id);
			PatientBean bean;

			try {
				bean = model.findByPK(id);
				ServletUtility.setBean(bean, request);
				/*
				 * System.out.println("productCatagory name =====> " +
				 * bean.getProductCatagory()); System.out.println("product name ===== > " +
				 * bean.getProductName());
				 */

			} catch (Exception e) {

				e.printStackTrace();
			}
		}

		ServletUtility.forward(getView(), request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("uctl Do Post");

		String op = DataUtility.getString(request.getParameter("operation"));

		long id = DataUtility.getLong(request.getParameter("id"));

		PatientModel model = new PatientModel();

		if (OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op)) {

			System.out.println("operation = " + op);

			PatientBean bean = (PatientBean) populateBean(request);

//			bean.setId(id);

			System.out.println("update id >>>>==== " + bean.getId());

			System.out.println("update name >>>>==== " + bean.getName());

			if (id > 0) {

				try {
					model.update(bean);
					ServletUtility.setBean(bean, request);
					ServletUtility.setSuccessMessage("patient is successfully Updated", request);
				} catch (Exception e) {
					System.out.println("patient not update");
					e.printStackTrace();
				}

			} else {

				try {
					long pk = model.add(bean);
					ServletUtility.setBean(bean, request);
					ServletUtility.setSuccessMessage("patient is successfully Added", request);
					bean.setId(pk);
				} catch (Exception e) {
					System.out.println("patient not added");
					e.printStackTrace();
				}

			}

		}
		ServletUtility.forward(getView(), request, response);

	}

	@Override
	protected String getView() {

		return ORSView.PATIENT_VIEW;
	}


}
