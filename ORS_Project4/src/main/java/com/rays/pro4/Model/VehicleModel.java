package com.rays.pro4.Model;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.rays.pro4.Bean.VehicleBean;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Exception.DatabaseException;
import com.rays.pro4.Exception.DuplicateRecordException;
import com.rays.pro4.Util.JDBCDataSource;

public class VehicleModel {

	// private static Logger log = Logger.getLogger(VehicleBean.class);

	public int nextPK() throws DatabaseException {

		// log.debug("Model nextPK Started");

		String sql = "SELECT MAX(ID)FROM st_vehicle ";
		Connection conn = null;
		int pk = 0;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				pk = rs.getInt(1);
			}
			rs.close();
		} catch (Exception e) {

			throw new DatabaseException("Exception : Exception in getting PK");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		// log.debug("Model nextPK Started");
		return pk + 1;

	}

	public long add(VehicleBean bean) throws ApplicationException, DuplicateRecordException {
		// log.debug("Model add Started");
		System.out.println("add method started");
		String sql = "INSERT INTO st_vehicle VALUES(?,?,?,?,?)";

		Connection conn = null;
		int pk = 0;

//		BankBean existbean = findByLogin(bean.getLogin());                               
//		if (existbean != null) {
//			throw new DuplicateRecordException("login Id already exists");
//
//		}

		try {
			conn = JDBCDataSource.getConnection();
			pk = nextPK();

			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement(sql);

			pstmt.setLong(1, pk);
			pstmt.setString(2, bean.getNumber());
			pstmt.setDate(3, new java.sql.Date(bean.getPurchaseDate().getTime()));
			;
			pstmt.setString(4, bean.getInsuranceAmount());
			pstmt.setString(5, bean.getColour());

			int a = pstmt.executeUpdate();
			System.out.println(a + "data inserted.......");
			conn.commit();
			pstmt.close();

		} catch (Exception e) {
			// log.error("Database Exception ...", e);
			try {
				e.printStackTrace();
				conn.rollback();

			} catch (Exception e2) {
				e2.printStackTrace();
				// application exception
				throw new ApplicationException("Exception : add rollback exceptionn" + e2.getMessage());
			}
		}

		finally {
			JDBCDataSource.closeConnection(conn);
		}
		// log.debug("Model Add End");
		return pk;

	}

	public void delete(VehicleBean bean) throws ApplicationException {
		// log.debug("Model delete start");
		String sql = "DELETE FROM st_vehicle WHERE id=?";
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, bean.getId());
			int i = pstmt.executeUpdate();
			System.out.println(i + "data deleted");
			conn.commit();
			pstmt.close();

		} catch (Exception e) {
			// log.error("DataBase Exception", e);
			try {
				conn.rollback();
			} catch (Exception e2) {
				throw new ApplicationException("Exception: Delete rollback Exception" + e2.getMessage());
			}
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		// log.debug("Model Delete End");
	}

	public void update(VehicleBean bean) throws ApplicationException, DuplicateRecordException {
		// log.debug("Model Update Start");
		String sql = "UPDATE st_vehicle SET Number = ?, PurchaseDate = ?, InsuranceAmount = ?, Colour = ? where id = ?";
		Connection conn = null;
//		BankBean existBean = findByLogin(bean.getLogin());
//		if (existBean != null && !(existBean.getId() == bean.getId())) {
//			throw new DuplicateRecordException("LoginId is Already Exist");
//		}
		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, bean.getNumber());
			pstmt.setDate(2, new java.sql.Date(bean.getPurchaseDate().getTime()));
			pstmt.setString(3, bean.getInsuranceAmount());
			pstmt.setString(4, bean.getColour());
			pstmt.setLong(5, bean.getId());
			pstmt.executeUpdate();
			int i = pstmt.executeUpdate();
			conn.commit();
			pstmt.close();
		} catch (Exception e) {
			e.printStackTrace();
			// log.error("DataBase Exception", e);
			try {
				conn.rollback();
			} catch (Exception e2) {
				e2.printStackTrace();
				throw new ApplicationException("Exception : Update Rollback Exception " + e2.getMessage());
			}
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		// log.debug("Model Update End ");
	}

	public List search(VehicleBean bean) throws ApplicationException {
		return search(bean, 0, 0);
	}

	public List search(VehicleBean bean, int pageNo, int pageSize) throws ApplicationException {
		// log.debug("Model Search Start");
		System.out.println("model Search Start--------------");
		StringBuffer sql = new StringBuffer("SELECT * FROM st_vehicle WHERE 1=1");
		System.out.println(bean + "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
		if (bean != null) {
			
			System.out.println(bean.getNumber());
			
			if (bean.getNumber() != null && bean.getNumber().length() > 0) {
				sql.append(" AND Number like '" + bean.getNumber() + "%'");
			}
			
			if (bean.getPurchaseDate() != null && bean.getPurchaseDate().getTime() > 0) {
				sql.append(" AND PurchaseDate like  '" + new java.sql.Date(bean.getPurchaseDate().getTime()) + "%'");
			}

			if (bean.getInsuranceAmount() != null && bean.getInsuranceAmount().length() > 0) {
				sql.append(" AND InsuranceAmount like '" + bean.getInsuranceAmount() + "%'");
			}			
			
			if (bean.getColour() != null && bean.getColour().length() > 0) {
				sql.append(" AND Colour like '" + bean.getColour() + "%'");
			}

		}

		if (pageSize > 0) {
			// Calculate start record index
			pageNo = (pageNo - 1) * pageSize;

			sql.append(" Limit " + pageNo + ", " + pageSize);
			// sql.append(" limit " + pageNo + "," + pageSize);
		}

		System.out.println(sql);
		List list = new ArrayList();
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new VehicleBean();
				bean.setId(rs.getLong(1));
				bean.setNumber(rs.getString(2));
				bean.setPurchaseDate(rs.getDate(3));
				bean.setInsuranceAmount(rs.getString(4));
				bean.setColour(rs.getString(5));

				list.add(bean);

			}
			rs.close();
		} catch (Exception e) {
			// log.error("Database Exception", e);
			throw new ApplicationException("Exception: Exception in Search User");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		// log.debug("Model Search end");

		return list;

	}

	public VehicleBean findByPK(long pk) throws ApplicationException {
		// log.debug("Model findBy PK start");
		String sql = "SELECT * FROM st_vehicle WHERE id=?";
		VehicleBean bean = null;
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, pk);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new VehicleBean();
				bean.setId(rs.getLong(1));
				bean.setNumber(rs.getString(2));
				bean.setPurchaseDate(rs.getDate(3));
				bean.setInsuranceAmount(rs.getString(4));
				bean.setColour(rs.getString(5));

			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
			// log.error("DataBase Exception ", e);
			throw new ApplicationException("Exception : Exception in getting vehicle by pk");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		// log.debug("Method Find By PK end");
		return bean;
	}

	public List list() throws ApplicationException {
		return list(0, 0);
	}

	public List list(int pageNo, int pageSize) throws ApplicationException {
		// log.debug("Model list Started");
		ArrayList list = new ArrayList();
		StringBuffer sql = new StringBuffer("select * from st_vehicle");

		if (pageSize > 0) {
			pageNo = (pageNo - 1) * pageSize;
			sql.append(" limit " + pageNo + "," + pageSize);
		}

		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				VehicleBean bean = new VehicleBean();
				bean.setId(rs.getLong(1));
				bean.setNumber(rs.getString(2));
				bean.setPurchaseDate(rs.getDate(3));
				bean.setInsuranceAmount(rs.getString(4));
				bean.setColour(rs.getString(5));

				list.add(bean);

			}
			rs.close();
		} catch (Exception e) {
			// log.error("Database Exception...", e);
			throw new ApplicationException("Exception : Exception in getting list of vehicle");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		// log.debug("Model list End");
		return list;
	}

}
