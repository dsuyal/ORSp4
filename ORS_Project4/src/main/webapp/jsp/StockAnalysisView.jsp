\<%@page import="com.rays.pro4.controller.StockAnalysisCtl"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="com.rays.pro4.Util.HTMLUtility"%>
<%@page import="com.rays.pro4.Util.DataUtility"%>
<%@page import="com.rays.pro4.Util.ServletUtility"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<html>
<head>
<link rel="icon" type="image/png"
	href="<%=ORSView.APP_CONTEXT%>/img/logo.png" sizes="16*16" />
<title>Stock Analysis Page</title>

<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet"
	href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
<link rel="stylesheet" href="/resources/demos/style.css">
<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>

<script src="<%=ORSView.APP_CONTEXT%>/js/Validation.js"></script>
<script src="<%=ORSView.APP_CONTEXT%>/js/Utilities.js"></script>





<script>
	$(function() {
		$("#udate").datepicker({
			changeMonth : true,
			changeYear : true,
			yearRange : '1900:2024',
		//dateFormat:'yy-mm-dd'
		});
	});

	$(function() {
		$("#udate1").datepicker({
			changeMonth : true,
			changeYear : true,
			yearRange : '1900:2024',
		//dateFormat:'yy-mm-dd'
		});
	});
</script>
<body>
	<jsp:useBean id="bean" class="com.rays.pro4.Bean.StockAnalysisBean"
		scope="request"></jsp:useBean>
	<%@ include file="Header.jsp"%>

	<center>

		<form action="<%=ORSView.STOCKANALYSIS_CTL%>" method="post">

			<%-- <%
				List list = (List) request.getAttribute("bList");
			%> --%>


			<div align="center">
				<h1>

					<%
						if (bean != null && bean.getId() > 0) {
					%>
					<tr>
						<th><font size="5px"> Update Stock </font></th>
					</tr>
					<%
						} else {
					%>
					<tr>
						<th><font size="5px"> Add Stock</font></th>
					</tr>
					<%
						}
					%>
				</h1>

				<h3>
					<font color="red"> <%=ServletUtility.getErrorMessage(request)%></font>
					<font color="green"> <%=ServletUtility.getSuccessMessage(request)%></font>
				</h3>

			</div>
			<input type="hidden" name="id" value="<%=bean.getId()%>"> 

			<table>
				<tr>
				<th align="left">Stock Symbol: <span style="color: red">*</span> :</th>

				
			<td> <input 
            type="text" 
            name="stockSymbol"  
            placeholder="Enter stock symbol" size="25"
            oninput="handleIntegerInput(this, 'stockSymbolError', 8)" 
            onblur="validateAlphanumericInput1(this, 'stockSymbolError', 8)" 
            value="<%=DataUtility.getStringData(bean.getStockSymbol())%>"
        >					
				<td style="position: fixed"><font color="red">
				
				 <%=ServletUtility.getErrorMessage("stockSymbol", request)%></font></td>

				</tr>

				<tr>
					<th style="padding: 3px"></th>
				</tr>


				<tr>
					<th align="left">Analysis Type <span style="color: red">*</span> :
					</th>
					<td>
						<%
						HashMap map = (HashMap) request.getAttribute("map");

							String hlist = HTMLUtility.getList("analysisType", String.valueOf(bean.getAnalysisType()), map);
						%> <%=hlist%>
					</td>
					<td style="position: fixed"><font color="red"> <%=ServletUtility.getErrorMessage("analysisType", request)%></font></td>
				</tr>
				<tr>
					<th style="padding: 3px"></th>
				</tr>

				<tr>
					<th align="left">startDate<span style="color: red">*</span>
						:
					</th>
					<td><input type="text" style="width: 210px" name="startDate"
						placeholder="Start date" readonly="readonly" id="udate"
						value="<%=DataUtility.getDateString(bean.getStartDate())%>"></td>
					<td style="position: fixed;"><font color="red"> <%=ServletUtility.getErrorMessage("startDate", request)%>
					</font></td>
				</tr>


				<tr>
					<th style="padding: 3px"></th>
				</tr>
				<tr>
					<th align="left">EndDate<span style="color: red">*</span>
						:
					</th>
					<td><input type="text" style="width: 210px" name="endDate"
						placeholder="End date" readonly="readonly" id="udate1"
						value="<%=DataUtility.getDateString(bean.getEndDate())%>"></td>
					<td style="position: fixed;"><font color="red"> <%=ServletUtility.getErrorMessage("endDate", request)%>
					</font></td>
				</tr>

				<tr>
					<th style="padding: 3px"></th>
				</tr>

				<tr>
					<th></th>
					<%
						if (bean.getId() > 0) {
					%>
					<td>&emsp;
					<td><input type="submit" name="operation"
						value="<%=StockAnalysisCtl.OP_UPDATE%>"> &nbsp; &nbsp; <input
						type="submit" name="operation" value="<%=StockAnalysisCtl.OP_CANCEL%>"></td>

					<%
						} else {
					%>

					<td colspan="2">&nbsp; &emsp; <input type="submit"
						name="operation" value="<%=StockAnalysisCtl.OP_SAVE%>"> &nbsp;
						&nbsp; <input type="submit" name="operation"
						value="<%=StockAnalysisCtl.OP_RESET%>"></td>

					<%
						}
					%>
				</tr>
			</table>
		</form>
	</center>

	<%@ include file="Footer.jsp"%>
</body>
</html>