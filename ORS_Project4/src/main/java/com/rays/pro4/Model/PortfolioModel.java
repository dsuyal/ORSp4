package com.rays.pro4.Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.rays.pro4.Bean.PortfolioBean;

import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Exception.DatabaseException;
import com.rays.pro4.Exception.DuplicateRecordException;
import com.rays.pro4.Util.JDBCDataSource;



public class PortfolioModel {
	
	  public int nextPK() throws DatabaseException {
	        String sql = "SELECT MAX(ID) FROM st_portfolio";
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

	    public long add(PortfolioBean bean) throws ApplicationException, DuplicateRecordException {
	        String sql = "INSERT INTO st_portfolio (id, portfolioName, initialInvestmentAmount, riskToleranceLevel, investmentStrategy) VALUES (?, ?, ?, ?, ?)";
	        Connection conn = null;
	        int pk = 0;

	        try {
	            conn = JDBCDataSource.getConnection();
	            pk = nextPK();
	            conn.setAutoCommit(false);

	            PreparedStatement pstmt = conn.prepareStatement(sql);
	            pstmt.setInt(1, pk);
	            pstmt.setString(2, bean.getPortfolioName());
	            pstmt.setInt(3, bean.getInitialInvestmentAmount());
	            pstmt.setString(4, bean.getRiskToleranceLevel());
	            pstmt.setString(5, bean.getInvestmentStrategy());

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

	    public void delete(PortfolioBean bean) throws ApplicationException {
	        String sql = "DELETE FROM st_portfolio WHERE id=?";
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

	    public void update(PortfolioBean bean) throws ApplicationException, DuplicateRecordException {
	        String sql = "UPDATE st_portfolio SET portfolioName=?, initialInvestmentAmount=?, riskToleranceLevel=?, investmentStrategy=? WHERE ID=?";
	        Connection conn = null;
	        int pk = 0;
	        try {
	            conn = JDBCDataSource.getConnection();
	            conn.setAutoCommit(false);

	            PreparedStatement pstmt = conn.prepareStatement(sql);
	           
	            pstmt.setString(1, bean.getPortfolioName());
	            pstmt.setInt(2, bean.getInitialInvestmentAmount());
	            pstmt.setString(3, bean.getRiskToleranceLevel());

	            pstmt.setString(4, bean.getInvestmentStrategy());
	            pstmt.setInt(5, pk);

	            pstmt.executeUpdate();
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

	    public List<PortfolioBean> search(PortfolioBean bean, int pageNo, int pageSize) throws ApplicationException {
	        StringBuilder sql = new StringBuilder("SELECT * FROM st_portfolio WHERE 1=1");
	        List<PortfolioBean> list = new ArrayList<>();
	        Connection conn = null;

	        if (bean != null) {
	            if (bean.getId() > 0) {
	                sql.append(" AND ID = ?");
	            }
	            if (bean.getPortfolioName() != null && !bean.getPortfolioName().isEmpty()) {
	                sql.append(" AND portfolioName = ?");
	            }
	            if (bean.getInitialInvestmentAmount() > 0) {
	                sql.append(" AND initialInvestmentAmount = ?");
	            }
	            if (bean.getRiskToleranceLevel() != null && !bean.getRiskToleranceLevel().isEmpty()) {
	                sql.append(" AND riskToleranceLevel = ?");
	            }
	            if (bean.getInvestmentStrategy() != null && !bean.getInvestmentStrategy().isEmpty()) {
	                sql.append(" AND investmentStrategy = ?");
	            }
	        }

	        if (pageSize > 0) {
	            int offset = Math.max(0, (pageNo - 1) * pageSize);
	            sql.append(" LIMIT ?, ?");
	        }

	        try {
	            conn = JDBCDataSource.getConnection();
	            PreparedStatement pstmt = conn.prepareStatement(sql.toString());

	            int index = 1;
	            if (bean != null) {
	                if (bean.getId() > 0) pstmt.setLong(index++, bean.getId());
	                if (bean.getPortfolioName() != null && !bean.getPortfolioName().isEmpty()) pstmt.setString(index++, bean.getPortfolioName());
	                if (bean.getInitialInvestmentAmount() > 0) pstmt.setInt(index++, bean.getInitialInvestmentAmount());
	                if (bean.getRiskToleranceLevel() != null && !bean.getRiskToleranceLevel().isEmpty()) pstmt.setString(index++, bean.getRiskToleranceLevel());
	                if (bean.getInvestmentStrategy() != null && !bean.getInvestmentStrategy().isEmpty()) pstmt.setString(index++, bean.getInvestmentStrategy());
	            }

	            if (pageSize > 0) {
	                pstmt.setInt(index++, Math.max(0, (pageNo - 1) * pageSize));
	                pstmt.setInt(index++, pageSize);
	            }

	            ResultSet rs = pstmt.executeQuery();
	            while (rs.next()) {
	                PortfolioBean portfolioBean = new PortfolioBean();
	                portfolioBean.setId(rs.getLong("id"));
	                portfolioBean.setPortfolioName(rs.getString("portfolioName"));
	                portfolioBean.setInitialInvestmentAmount(rs.getInt("initialInvestmentAmount"));
	                portfolioBean.setRiskToleranceLevel(rs.getString("riskToleranceLevel"));
	                portfolioBean.setInvestmentStrategy(rs.getString("investmentStrategy"));
	                list.add(portfolioBean);
	            }
	            rs.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	            throw new ApplicationException("Exception: Exception in search Portfolio records");
	        } finally {
	            JDBCDataSource.closeConnection(conn);
	        }

	        return list;
	    }


	    
	    
	    public PortfolioBean findByPK(long pk) throws ApplicationException {
	        String sql = "SELECT * FROM st_portfolio WHERE id=?";
	        PortfolioBean bean = null;
	        Connection conn = null;

	        try {
	            conn = JDBCDataSource.getConnection();
	            PreparedStatement pstmt = conn.prepareStatement(sql);
	            pstmt.setLong(1, pk);
	            ResultSet rs = pstmt.executeQuery();

	            if (rs.next()) {
	                bean = new PortfolioBean();
	                bean.setId(rs.getLong("id"));
	                bean.setPortfolioName(rs.getString("portfolioName"));
	                bean.setInitialInvestmentAmount(rs.getInt("initialInvestmentAmount"));
	                bean.setRiskToleranceLevel(rs.getString("riskToleranceLevel"));
	                bean.setInvestmentStrategy(rs.getString("investmentStrategy"));
	            }
	            rs.close();
	        } catch (Exception e) {
	            throw new ApplicationException("Exception: Exception in finding portfolio record by PK");
	        } finally {
	            JDBCDataSource.closeConnection(conn);
	        }
	        return bean;
	    }

	    public List list() throws ApplicationException {
	        return list(0, 0);
	    }

	    public List list(int pageNo, int pageSize) throws ApplicationException {
	        String sql = "SELECT * FROM st_portfolio";
	        List<PortfolioBean> list = new ArrayList<>();
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
	                PortfolioBean bean = new PortfolioBean();
	                bean.setId(rs.getLong("id"));
	                bean.setPortfolioName(rs.getString("portfolioName"));
	                bean.setInitialInvestmentAmount(rs.getInt("initialInvestmentAmount"));
	                bean.setRiskToleranceLevel(rs.getString("riskToleranceLevel"));
	                bean.setInvestmentStrategy(rs.getString("investmentStrategy"));
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
