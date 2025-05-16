package com.rays.pro4.Model;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import com.rays.pro4.Bean.IssueBean;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Exception.DatabaseException;
import com.rays.pro4.Exception.DuplicateRecordException;

import com.rays.pro4.Util.JDBCDataSource;


public class IssueModel {
	
	
	private static Logger log = Logger.getLogger(IssueModel.class);

	/**
	 * Find next PK of issue
	 *
	 * @throws DatabaseException
	 */

	public int nextPK() throws DatabaseException {

		log.debug("Model nextPK Started");

		String sql = "select max(id) from st_issue";
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

	public long add(IssueBean bean) throws ApplicationException, DuplicateRecordException {
		log.debug("Model add Started");

		String sql = "insert into st_issue values(?,?,?,?,?,?)";

		Connection conn = null;
		int pk = 0;

		try {
			conn = JDBCDataSource.getConnection();
			pk = nextPK();

			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement(sql);

			pstmt.setLong(1, pk);
			pstmt.setDate(2, new java.sql.Date(bean.getOpenDate().getTime()));
			pstmt.setString(3, bean.getTitle());
			pstmt.setString(4, bean.getDiscription());
			pstmt.setInt(5, bean.getAssignTo());
			pstmt.setInt(6, bean.getStatus());

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
	 * Delete a issue
	 *
	 * @param bean
	 * @throws DatabaseException
	 */
	public void delete(IssueBean bean) throws ApplicationException {
		log.debug("Model delete start");
		String sql = "delete from st_issue where id=?";
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
	 * Find issue by PK
	 *
	 * @param pk : get parameter
	 * @return bean
	 * @throws DatabaseException
	 */

	public IssueBean findByPK(long pk) throws ApplicationException {
		log.debug("Model findBy PK start");
		String sql = "select * from st_issue where id=?";
		IssueBean bean = null;
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, pk);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new IssueBean();
				bean.setId(rs.getLong(1));
				bean.setOpenDate(rs.getDate(2));;
				bean.setTitle(rs.getString(3));
				bean.setDiscription(rs.getString(4));
				bean.setAssignTo(rs.getInt(5));
				bean.setStatus(rs.getInt(6));

			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
			log.error("DataBase Exception ", e);
			throw new ApplicationException("Exception : Exception in getting issue by pk");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Method Find By PK end");
		return bean;
	}

	/**
	 * Update a stock
	 *
	 * @param bean
	 * @throws DatabaseException
	 */

	public void update(IssueBean bean) throws ApplicationException, DuplicateRecordException {
		log.debug("Model Update Start");
		String sql = "update st_issue set openDate=?, title=?, discription=?, assignTo=?, status=? where id=? ";
		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement(sql);

			pstmt.setDate(1, new java.sql.Date(bean.getOpenDate().getTime()));
			pstmt.setString(2, bean.getTitle());
			pstmt.setString(3, bean.getDiscription());
			pstmt.setInt(4, bean.getAssignTo());
			pstmt.setInt(5, bean.getStatus());
			pstmt.setLong(6, bean.getId()); // ID (where condition)

			int i = pstmt.executeUpdate();
			System.out.println("update issue>> " + i);
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
	 * Search issue
	 *
	 * @param bean : Search Parameters
	 * @throws DatabaseException
	 */

	public List search(IssueBean bean) throws ApplicationException {
		return search(bean, 0, 0);
	}

	/**
	 * Search issue with pagination
	 *
	 * @return list : List of issue
	 * @param bean     : Search Parameters
	 * @param pageNo   : Current Page No.
	 * @param pageSize : Size of Page
	 *
	 * @throws DatabaseException
	 */

	public List search(IssueBean bean, int pageNo, int pageSize) throws ApplicationException {
		log.debug("Model Search Start");
		StringBuffer sql = new StringBuffer("select * from st_issue where 1=1");
		
		
		if (bean != null) {
			

			if (bean.getOpenDate() != null && bean.getOpenDate().getTime() > 0) {
				sql.append(" AND OPENDATE like  '" + new java.sql.Date(bean.getOpenDate().getTime()) + "%'");
			}
			
			if (bean.getTitle() != null && bean.getTitle().length() > 0) {
				sql.append(" AND TITLE like '" + bean.getTitle() + "%'");
			}
			
			if (bean.getDiscription() != null && bean.getDiscription().length() > 0) {
				sql.append(" AND DISCRIPTION like '" + bean.getDiscription() + "%'");
			}
			
			if (bean.getAssignTo() > 0) {
				sql.append(" AND ASSIGNTO = " + bean.getAssignTo());
			}
			
			if (bean.getStatus() > 0) {
				sql.append(" AND STATUS = " + bean.getStatus());
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
				bean = new IssueBean();
				bean.setId(rs.getLong(1));
				bean.setOpenDate(rs.getDate(2));
				bean.setTitle(rs.getString(3));
				bean.setDiscription(rs.getString(4));
				bean.setAssignTo(rs.getInt(5));
				bean.setStatus(rs.getInt(6));


				list.add(bean);

			}
			rs.close();
		} catch (Exception e) {
			log.error("Database Exception", e);
			throw new ApplicationException("Exception: Exception in Search Stock");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Model Search end");
		return list;

	}

	/**
	 * Get List of Stock
	 *
	 * @return list : List of Stock
	 * @throws DatabaseException
	 */

	public List list() throws ApplicationException {
		return list(0, 0);
	}

	/**
	 * Get List of Stock with pagination
	 *
	 * @return list : List of issue
	 * @param pageNo   : Current Page No.
	 * @param pageSize : Size of Page
	 * @throws DatabaseException
	 */

	public List list(int pageNo, int pageSize) throws ApplicationException {
		log.debug("Model list Started");
		ArrayList list = new ArrayList();
		StringBuffer sql = new StringBuffer("select * from st_issue");

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
				IssueBean bean = new IssueBean();

				bean.setId(rs.getLong(1));
				bean.setOpenDate(rs.getDate(2));
				bean.setTitle(rs.getString(3));
				bean.setDiscription(rs.getString(4));
				bean.setAssignTo(rs.getInt(5));
				bean.setStatus(rs.getInt(6));



				list.add(bean);

			}
			rs.close();
		} catch (Exception e) {
			log.error("Database Exception...", e);
			throw new ApplicationException("Exception : Exception in getting list of Issue");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Model list End");
		return list;
	}

}



