package com.rays.pro4.Model;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.rays.pro4.Bean.StockAnalysisBean;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Exception.DatabaseException;
import com.rays.pro4.Exception.DuplicateRecordException;
import com.rays.pro4.Util.JDBCDataSource;

public class StockAnalysisModel {

	public int nextPK() throws DatabaseException {
		String sql = "SELECT MAX(ID) FROM st_stockanalysis";
		Connection conn = null;
		int pk = 0;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				pk = rs.getInt(1);
			}
			rs.close();
		} catch (Exception e) {
			throw new DatabaseException("Exception: Exception in getting PK");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return pk + 1;
	}

	public long add(StockAnalysisBean bean) throws ApplicationException, DuplicateRecordException {
		String sql = "INSERT INTO st_stockanalysis (id, stockSymbol, analysisType, startDate, endDate) VALUES (?, ?, ?, ?, ?)";
		Connection conn = null;
		int pk = 0;

		try {
			conn = JDBCDataSource.getConnection();
			pk = nextPK();
			conn.setAutoCommit(false);

			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, pk);
			pstmt.setString(2, bean.getStockSymbol());
			pstmt.setString(3, bean.getAnalysisType());
			pstmt.setDate(4, new java.sql.Date(bean.getStartDate().getTime()));
			pstmt.setDate(5, new java.sql.Date(bean.getEndDate().getTime()));

			pstmt.executeUpdate();
			conn.commit();
			pstmt.close();
		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (Exception ex) {
				throw new ApplicationException("Exception: add rollback exception " + ex.getMessage());
			}
			throw new ApplicationException("Exception: Exception in adding stock record");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return pk;
	}

	public void delete(StockAnalysisBean bean) throws ApplicationException {
		String sql = "DELETE FROM st_stockanalysis WHERE id=?";
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
			try {
				conn.rollback();
			} catch (Exception ex) {
				throw new ApplicationException("Exception: delete rollback exception " + ex.getMessage());
			}
			throw new ApplicationException("Exception: Exception in deleting stock record");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
	}

	public void update(StockAnalysisBean bean) throws ApplicationException, DuplicateRecordException {
		String sql = "UPDATE st_stockanalysis SET stockSymbol=?, analysisType=?, startDate=?, endDate=? WHERE ID=?";
		Connection conn = null;
		int pk = 0;
		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);

			PreparedStatement pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, bean.getStockSymbol());
			pstmt.setString(2, bean.getAnalysisType());
			pstmt.setDate(3, new java.sql.Date(bean.getStartDate().getTime()));

			pstmt.setDate(4, new java.sql.Date(bean.getEndDate().getTime()));
			pstmt.setLong(5, bean.getId());

			pstmt.executeUpdate();

			System.out.println("update method model===" + bean.getAnalysisType());
			conn.commit();
			pstmt.close();
		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (Exception ex) {
				throw new ApplicationException("Exception: update rollback exception " + ex.getMessage());
			}
			throw new ApplicationException("Exception: Exception in updating stock record");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
	}

	public List<StockAnalysisBean> search(StockAnalysisBean bean, int pageNo, int pageSize)
			throws ApplicationException {
		StringBuilder sql = new StringBuilder("SELECT * FROM st_stockanalysis WHERE 1=1");
		List<StockAnalysisBean> list = new ArrayList<>();
		Connection conn = null;

		if (bean != null) {

			if (bean.getAnalysisType() != null && bean.getAnalysisType().length() > 0) {
				sql.append(" AND AnalysisType like '" + bean.getAnalysisType() + "%'");
			}
			if (bean.getStartDate() != null && bean.getStartDate().getTime() > 0) {
				Date d = new Date(bean.getStartDate().getDate());
				sql.append(" AND startDate like '" + new java.sql.Date(bean.getStartDate().getTime()) + "%'");

			}
			if (bean.getEndDate() != null && bean.getEndDate().getTime() > 0) {
				Date d = new Date(bean.getEndDate().getDate());
				sql.append(" AND endDate like '" + new java.sql.Date(bean.getEndDate().getTime()) + "%'");
			}

			if (bean.getStockSymbol() != null && bean.getStockSymbol().length() > 0) {
				sql.append(" AND StockSymbol like '" + bean.getStockSymbol() + "%'");
			}

		}
		if (pageSize > 0) {
			// Calculate start record index
			pageNo = (pageNo - 1) * pageSize;

			sql.append(" Limit " + pageNo + ", " + pageSize);
			// sql.append(" limit " + pageNo + "," + pageSize);
		}

		System.out.println(sql);

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new StockAnalysisBean();
				bean.setId(rs.getLong(1));
				bean.setStockSymbol(rs.getString(2));
				bean.setAnalysisType(rs.getString(3));
				bean.setStartDate(rs.getDate(4));
				bean.setEndDate(rs.getDate(5));

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

	public StockAnalysisBean findByPK(long pk) throws ApplicationException {
		System.out.println("in findbypk =========== >>>>>>");
		String sql = "SELECT * FROM st_stockanalysis WHERE id=?";
		StockAnalysisBean bean = null;
		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, pk);
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				System.out.println("in rs.next() method...........");
				bean = new StockAnalysisBean();
				bean.setId(rs.getLong("id"));
				bean.setStockSymbol(rs.getString("stockSymbol"));
				bean.setAnalysisType(rs.getString("analysisType"));
				bean.setStartDate(rs.getDate("startDate"));
				bean.setEndDate(rs.getDate("endDate"));
			}
			rs.close();
		} catch (Exception e) {
			throw new ApplicationException("Exception: Exception in finding StockAnalysis record by PK");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return bean;
	}

	public List list() throws ApplicationException {
		return list(0, 0);
	}

	public List list(int pageNo, int pageSize) throws ApplicationException {
		String sql = "SELECT * FROM st_stockanalysis";
		List<StockAnalysisBean> list = new ArrayList<>();
		Connection conn = null;

		try {
			if (pageSize > 0) {
				pageNo = (pageNo - 1) * pageSize;
				sql += " LIMIT " + pageNo + ", " + pageSize;
			}

			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				StockAnalysisBean bean = new StockAnalysisBean();
				bean.setId(rs.getLong("id"));
				bean.setStockSymbol(rs.getString("stockSymbol"));
				bean.setAnalysisType(rs.getString("analysisType"));
				bean.setStartDate(rs.getDate("startDate"));
				bean.setEndDate(rs.getDate("endDate"));
				list.add(bean);
			}
			rs.close();
		} catch (Exception e) {
			throw new ApplicationException("Exception: Exception in listing portfolio records");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return list;
	}

}
