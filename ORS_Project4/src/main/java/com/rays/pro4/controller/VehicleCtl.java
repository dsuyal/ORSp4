package com.rays.pro4.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.rays.pro4.Bean.BaseBean;
import com.rays.pro4.Bean.VehicleBean;
import com.rays.pro4.Model.VehicleModel;
import com.rays.pro4.Util.DataUtility;
import com.rays.pro4.Util.DataValidator;
import com.rays.pro4.Util.PropertyReader;
import com.rays.pro4.Util.ServletUtility;

@WebServlet(name = "VehicleCtl", urlPatterns = { "/ctl/VehicleCtl" })

public class VehicleCtl extends BaseCtl {
	
	@Override
	protected void preload(HttpServletRequest request) {
		// TODO Auto-generated method stub
		//super.preload(request);
	}
	
	@Override
	protected boolean validate(HttpServletRequest request) {
		System.out.println("uctl Validate");

		boolean pass = true;

		if (DataValidator.isNull(request.getParameter("number"))) {
			request.setAttribute("number", PropertyReader.getValue("error.require", "number"));
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("purchaseDate"))) {
			request.setAttribute("purchaseDate", PropertyReader.getValue("error.require", "PurchaseDate"));
			pass = false;
		
		}
		if (DataValidator.isNull(request.getParameter("insuranceAmount"))) {
			request.setAttribute("insuranceAmount", PropertyReader.getValue("error.require", "insuranceAmount"));
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("colour"))) {
			request.setAttribute("colour", PropertyReader.getValue("error.require", "colour"));
			pass = false;
		}
		
		System.out.println("Validate ended and value of pass is = "+pass);

		return pass;

	}
	@Override
	protected BaseBean populateBean(HttpServletRequest request) {
		
		VehicleBean bean = new VehicleBean();
		
		bean.setId(DataUtility.getInt(request.getParameter("id")));
		bean.setNumber(DataUtility.getString(request.getParameter("number")));
		bean.setPurchaseDate(DataUtility.getDate(request.getParameter("purchaseDate")));
		bean.setInsuranceAmount(DataUtility.getString(request.getParameter("insuranceAmount")));
		bean.setColour(DataUtility.getString(request.getParameter("colour")));
		
		return bean;
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String op = DataUtility.getString(req.getParameter("operation"));

		VehicleModel model = new VehicleModel();

		long id = DataUtility.getLong(req.getParameter("id"));

		System.out.println("vehicle Edit Id >= " + id);

		if (id != 0 && id > 0) {

			System.out.println("in id > 0  condition " + id);
			VehicleBean bean;

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

		System.out.println(">>>><<<<>><<><<><<><>**********" + id + op);

		VehicleModel model = new VehicleModel();

		if (OP_CANCEL.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.VEHICLE_LIST_CTL, req, resp);

		}
		if (OP_RESET.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.VEHICLE_CTL, req, resp);

		}

		if (OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op)) {

			VehicleBean bean = (VehicleBean) populateBean(req);

			if (id > 0) {

				try {
					model.update(bean);
					ServletUtility.setBean(bean, req);
					ServletUtility.setSuccessMessage("vehicle is successfully Updated", req);
					ServletUtility.forward(getView(), req, resp);
				} catch (Exception e) {
					System.out.println("vehicle not update");
					e.printStackTrace();
				}

			} else {

				try {
					long pk = model.add(bean);
					ServletUtility.setSuccessMessage("vehicle is successfully Added", req);

					bean.setId(pk);
					ServletUtility.forward(getView(), req, resp);
				} catch (Exception e) {
					System.out.println("vehicle not added");
					e.printStackTrace();
				}

			}

		}
	}
		//super.doPost(req, resp);
		
	
	

	@Override
	protected String getView() {
		// TODO Auto-generated method stub
		return ORSView.VEHICLE_VIEW;
	}
	
	

}
