package com.rays.pro4.Model;

import java.sql.Connection; 
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

//import org.apache.log4j.Logger;

import com.rays.pro4.Bean.ItemBean;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Exception.DatabaseException;
import com.rays.pro4.Exception.DuplicateRecordException;
import com.rays.pro4.Exception.RecordNotFoundException;
import com.rays.pro4.Util.DataUtility;
import com.rays.pro4.Util.EmailBuilder;
import com.rays.pro4.Util.EmailMessage;
import com.rays.pro4.Util.EmailUtility;
import com.rays.pro4.Util.JDBCDataSource;

/**
 * JDBC Implementation of ItemModel.
 * 
 * @author Dinesh Suyal
 *
 */

public class ItemModel {
	private static Logger log = Logger.getLogger(ItemModel.class);

	/**
	 * Find next PK of Item
	 *
	 * @throws DatabaseException
	 */

	public int nextPK() throws DatabaseException {

		log.debug("Model nextPK Started");

		String sql = "select max(id) from st_item";
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
		log.debug("Model nextPK Started");
		return pk + 1;

	}

	/**
	 * Item Add
	 *
	 * @param bean
	 * @throws DatabaseException
	 *
	 */

	/**
	 * @param bean
	 * @return
	 * @throws ApplicationException
	 * @throws DuplicateRecordException
	 */
	public long add(ItemBean bean) throws ApplicationException, DuplicateRecordException {
		log.debug("Model add Started");

		String sql = "insert into st_item values(?,?,?,?,?,?)";

		Connection conn = null;
		int pk = 0;

		try {
			conn = JDBCDataSource.getConnection();
			pk = nextPK();

			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement(sql);

			pstmt.setLong(1, pk);
			pstmt.setString(2, bean.getTitle());
			pstmt.setString(3, bean.getOverView());
			pstmt.setLong(4, bean.getCost());
			pstmt.setDate(5, new java.sql.Date(bean.getPurchaseDate().getTime()));
			pstmt.setInt(6, bean.getCategory());

			int i = pstmt.executeUpdate();
			System.out.println(i);
			conn.commit();
			pstmt.close();

		} catch (Exception e) {
			log.error("Database Exception ...", e);
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
		log.debug("Model Add End");
		return pk;

	}

	/**
	 * Delete a Item
	 *
	 * @param bean
	 * @throws DatabaseException
	 */
	public void delete(ItemBean bean) throws ApplicationException {
		log.debug("Model delete start");
		String sql = "delete from st_item where id=?";
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, bean.getId());
			pstmt.executeUpdate();
			conn.commit();
			pstmt.close();
		} catch (Exception e) {
			log.error("DataBase Exception", e);
			try {
				conn.rollback();
			} catch (Exception e2) {
				throw new ApplicationException("Exception: Delete rollback Exception" + e2.getMessage());
			}
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Model Delete End");
	}

	/**
	 * Find Item by PK
	 *
	 * @param pk : get parameter
	 * @return bean
	 * @throws DatabaseException
	 */

	public ItemBean findByPK(long pk) throws ApplicationException {
		log.debug("Model findBy PK start");
		String sql = "select * from st_item where id=?";
		ItemBean bean = null;
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, pk);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new ItemBean();
				bean.setId(rs.getLong(1));
				bean.setTitle(rs.getString(2));
				bean.setOverView(rs.getString(3));
				bean.setCost(rs.getLong(4));
				bean.setPurchaseDate(rs.getDate(5));
				bean.setCategory(rs.getInt(6));

			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
			log.error("DataBase Exception ", e);
			throw new ApplicationException("Exception : Exception in getting Item by pk");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Method Find By PK end");
		return bean;
	}

	/**
	 * Update a item
	 *
	 * @param bean
	 * @throws DatabaseException
	 */

	public void update(ItemBean bean) throws ApplicationException, DuplicateRecordException {
		log.debug("Model Update Start");
		String sql = "update st_item set title=?, over_view=?, cost=?, purchase_date=?, category=? where id=? ";
		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, bean.getTitle());
			pstmt.setString(2, bean.getOverView());
			pstmt.setLong(3, bean.getCost());
			pstmt.setDate(4, new java.sql.Date(bean.getPurchaseDate().getTime()));
			pstmt.setInt(5, bean.getCategory());
			pstmt.setLong(6, bean.getId());

			int i = pstmt.executeUpdate();
			System.out.println("update item>> " + i);
			conn.commit();
			pstmt.close();
		} catch (Exception e) {
			e.printStackTrace();
			log.error("DataBase Exception", e);
			try {
				conn.rollback();
			} catch (Exception e2) {
				e2.printStackTrace();
				throw new ApplicationException("Exception : Update Rollback Exception " + e2.getMessage());
			}
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Model Update End ");
	}

	/**
	 * Search Item
	 *
	 * @param bean : Search Parameters
	 * @throws DatabaseException
	 */

	public List search(ItemBean bean) throws ApplicationException {
		return search(bean, 0, 0);
	}

	/**
	 * Search Item with pagination
	 *
	 * @return list : List of Items 
	 * @param bean     : Search Parameters 
	 * @param pageNo   : Current Page No. 
	 * @param pageSize : Size of Page 
	 *
	 * @throws DatabaseException 
	 */

	public List search(ItemBean bean, int pageNo, int pageSize) throws ApplicationException {
		log.debug("Model Search Start");
		StringBuffer sql = new StringBuffer("select * from st_item where 1=1");
		if (bean != null) {

			if (bean.getTitle() != null && bean.getTitle().length() > 0) {
				sql.append(" AND TITLE like '" + bean.getTitle() + "%'");
			}
			

			if (bean.getOverView() != null && bean.getOverView().length() > 0) {
				sql.append(" AND OVER_VIEW like '" + bean.getOverView() + "%'");
			}
			
			if (bean.getCategory() > 0) {
				sql.append(" AND CATEGORY = " + bean.getCategory());
			}
			
			if (bean.getPurchaseDate() != null && bean.getPurchaseDate().getTime() > 0) {
				sql.append(" AND PURCHASE_DATE like  '" + new java.sql.Date(bean.getPurchaseDate().getTime()) + "%'");
			}
			if (bean.getId() > 0) {
				sql.append(" AND ID = " + bean.getId());
			}
			
			if (bean.getCost() > 0) {
				sql.append(" AND COST = " + bean.getCost());
			}

		}
		// if page size is greater than zero then apply pagination
		if (pageSize > 0) {
			// Calculate start record index
			pageNo = (pageNo - 1) * pageSize;

			sql.append(" Limit " + pageNo + ", " + pageSize);
			// sql.append(" limit " + pageNo + "," + pageSize);
		}
		System.out.println("sql query search >>= " + sql.toString());
		List list = new ArrayList();
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new ItemBean();
				bean.setId(rs.getLong(1));
				bean.setTitle(rs.getString(2));
				bean.setOverView(rs.getString(3));
				bean.setCost(rs.getLong(4));
				bean.setPurchaseDate(rs.getDate(5));
				bean.setCategory(rs.getInt(6));


				list.add(bean);

			}
			rs.close();
		} catch (Exception e) {
			log.error("Database Exception", e);
			throw new ApplicationException("Exception: Exception in Search Item");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Model Search end");
		return list;

	}

	/**
	 * Get List of Item
	 *
	 * @return list : List of Item
	 * @throws DatabaseException
	 */

	public List list() throws ApplicationException {
		return list(0, 0);
	}

	/**
	 * Get List of Item with pagination
	 *
	 * @return list : List of items
	 * @param pageNo   : Current Page No.
	 * @param pageSize : Size of Page
	 * @throws DatabaseException
	 */

	public List list(int pageNo, int pageSize) throws ApplicationException {
		log.debug("Model list Started");
		ArrayList list = new ArrayList();
		StringBuffer sql = new StringBuffer("select * from st_item");

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
				ItemBean bean = new ItemBean();

				bean.setId(rs.getLong(1));
				bean.setTitle(rs.getString(2));
				bean.setOverView(rs.getString(3));
				bean.setCost(rs.getLong(4));
				bean.setPurchaseDate(rs.getDate(5));
				bean.setCategory(rs.getInt(6));

				list.add(bean);

			}
			rs.close();
		} catch (Exception e) {
			log.error("Database Exception...", e);
			throw new ApplicationException("Exception : Exception in getting list of items");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Model list End");
		return list;
	}

}