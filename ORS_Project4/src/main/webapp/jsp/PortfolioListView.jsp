<%@page import="java.util.Map"%>
<%@page import="com.rays.pro4.controller.PortfolioListCtl"%>
<%@page import="com.rays.pro4.Util.HTMLUtility"%>
<%@page import="com.rays.pro4.Bean.PortfolioBean"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="com.rays.pro4.Util.DataUtility"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.rays.pro4.Util.ServletUtility"%>
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Portfolio page</title>
<script src="<%=ORSView.APP_CONTEXT%>/js/jquery.min.js"></script>
<script src="<%=ORSView.APP_CONTEXT%>/js/Checkbox11.js"></script>


<link rel="stylesheet"
	href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
<link rel="stylesheet" href="/resources/demos/style.css">
<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<script src="<%=ORSView.APP_CONTEXT%>/js/utilities.js"></script>
<script>
	$(function() {
		$("#Udate").datepicker({
			changeMonth : true,
			changeYear : true,
			yearRange : '1980:2002',
		//dateFormat:'yy-mm-dd'
		});
	});

	function limitInputLength(input, maxLength) {
		if (input.value.length > maxLength) {
			input.value = input.value.slice(0, maxLength);
		}
	}
</script>
</head>
<body>
	<jsp:useBean id="bean" class="com.rays.pro4.Bean.PortfolioBean"
		scope="request"></jsp:useBean>
	<%@include file="Header.jsp"%>
	<form action="<%=ORSView.PORTFOLIO_LIST_CTL%>" method="post">
		<center>

			<div align="center">
				<h1>Portfolio List</h1>
				<h3>
					<font color="red"><%=ServletUtility.getErrorMessage(request)%></font>
					<font color="green"><%=ServletUtility.getSuccessMessage(request)%></font>
				</h3>

			</div>
			<%
				Map map = (Map) request.getAttribute("prolist");

				int next = DataUtility.getInt(request.getAttribute("nextlist").toString());
			%>

			<%
				int pageNo = ServletUtility.getPageNo(request);
				int pageSize = ServletUtility.getPageSize(request);
				int index = ((pageNo - 1) * pageSize) + 1;

				List list = ServletUtility.getList(request);
				Iterator<PortfolioBean> it = list.iterator();
				if (list.size() != 0) {
			%>
			<table width="100%" align="center">
				<div>

					<td align="center"><label>Portfolio Name</font> :
					</label> <input type="text" name="portfolioName" 
						placeholder="Enter portfolioName"
						oninput="handleLetterInput(this,'portfolioNameError', 200)"
						onblur="validateLetterInput(this,'portfolioNameError', 200)"
						value="<%=ServletUtility.getParameter("portfolioName", request)%>">
						<br> <font color="red" id="portfolioNameError"> <%=ServletUtility.getErrorMessage("portfolioName", request)%></font>

					</td>
				</div>
				
				
				<div>

						<td align="center"><label>InitialInvestment Amount</font>:
						</label><input type="text" name="initialInvestmentAmount" 
							placeholder="Enter initialInvestmentAmount"
							oninput="handleIntegerInput(this, 'initialInvestmentAmountError', 10)"
							onblur="validateIntegerInput(this, 'initialInvestmentAmountError', 10)"
							value="<%=ServletUtility.getParameter("initialInvestmentAmount", request)%>">
							<br> <font color="red" id="initialInvestmentAmountError"> 
							<%=ServletUtility.getErrorMessage("initialInvestmentAmount", request)%></font>
						</td>

					</div>
					
					<div>
						<td><label>RiskTolerance Level</font> :
						</label> <%=HTMLUtility.getList1("riskToleranceLevel", String.valueOf(bean.getRiskToleranceLevel()), map)%>
						</td>

					</div> 	

				<td align="center"><label>Investment Startegy</font> :
				</label> <input type="text" name="investmentStrategy" 
					placeholder="Enter investmentStrategy" 
					oninput="handleLetterInput(this, 'investmentStrategyError', 20)"
					onblur="validateLetterInput(this,'investmentStrategyError', 20)"
					value="<%=ServletUtility.getParameter("investmentStrategy", request)%>">

				
					<td><input type="submit" name="operation"
						value="<%=PortfolioListCtl.OP_SEARCH%>"> 
					<input type="submit" name="operation"
						value="<%=PortfolioListCtl.OP_RESET%>"></td>
				

			</table>
			<br>

			<table border="1" width="100%" align="center" cellpadding=6px
				cellspacing=".2">


				<tr style= "background-color: lightblue" >
					<th><input type="checkbox" id="select_all" name="select">Select
						All</th>

					<th>S.No.</th>
					<th>Portfolio Name</th>
					<th> InitialInvestmentAmount</th>
					<th>RiskToleranceLevel</th>
					<th>InvestmentStartegy</th>
					<th>Edit</th>
				</tr>
				<%
					while (it.hasNext()) {
							bean = it.next();
				%>
				<tr align="center">
					<td><input type="checkbox" class="checkbox" name="ids"
						value="<%=bean.getId()%>"></td>

					<td><%=index++%></td>
					<td><%=bean.getPortfolioName()%></td>
					<td><%=bean.getInitialInvestmentAmount()%></td>
					<td><%=map.get(Integer.parseInt(bean.getRiskToleranceLevel()))%></td>
					<td><%=bean.getInvestmentStrategy()%></td>
					<td><a href="PortfolioCtl?id=<%=bean.getId()%>">Edit</td>
				</tr>
				<%
					}
				%>

				<table width="100%">

					<tr>
						<th></th>
						<%
							if (pageNo == 1) {
						%>
						<td><input type="submit" name="operation" disabled="disabled"
							value="<%=PortfolioListCtl.OP_PREVIOUS%>"></td>
						<%
							} else {
						%>
						<td><input type="submit" name="operation"
							value="<%=PortfolioListCtl.OP_PREVIOUS%>"></td>
						<%
							}
						%>

						<td><input type="submit" name="operation"
							value="<%=PortfolioListCtl.OP_DELETE%>"></td>
							
						<td align="center"><input type="submit" name="operation"
							value="<%=PortfolioListCtl.OP_NEW%>"></td>

						<td align="right"><input type="submit" name="operation"
							value="<%=PortfolioListCtl.OP_NEXT%>"
							<%=(list.size() < pageSize || next == 0) ? "disabled" : ""%>></td>



					</tr>
				</table>
				<%
					}
					if (list.size() == 0) {
				%>
				<td align="center"><input type="submit" name="operation"
					value="<%=PortfolioListCtl.OP_BACK%>"></td>


				<%
					}
				%>

				<input type="hidden" name="pageNo" value="<%=pageNo%>">
				<input type="hidden" name="pageSize" value="<%=pageSize%>">

				</form>
				</br>

				</br>
				</br>
				</br>
				</br>
				</br>
				</br>

				</center>
				<%@include file="Footer.jsp"%>
</body>
</html>