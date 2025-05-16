<%@page import="com.rays.pro4.Model.StockModel"%>
<%@page import="java.util.Map"%>
<%@page import="com.rays.pro4.Bean.StockBean"%>
<%@page import="com.rays.pro4.Bean.BaseBean"%>
<%@page import="com.rays.pro4.Util.HTMLUtility"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="com.rays.pro4.Util.DataUtility"%>
<%@page import="com.rays.pro4.Util.ServletUtility"%>
<%@page import="com.rays.pro4.controller.StockListCtl"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<html>
<head>
<link rel="icon" type="image/png"
	href="<%=ORSView.APP_CONTEXT%>/img/logo.png" sizes="16*16" />

<title>Stock List</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet"
	href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
<link rel="stylesheet" href="/resources/demos/style.css">
<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<script src="<%=ORSView.APP_CONTEXT%>/js/Utilities.js"></script>
<script>
	$(function() {
		$("#udate").datepicker({
			changeMonth : true,
			changeYear : true,
			yearRange : '1990:2025',
		});
	});
</script>

</head>
<body>
	<jsp:useBean id="bean" class="com.rays.pro4.Bean.StockBean"
		scope="request"></jsp:useBean>
	<%@include file="Header.jsp"%>


	<form action="<%=ORSView.STOCK_LIST_CTL%>" method="post">

		<center>

			<div align="center">
				<h1>Stock Purchase List</h1>
				<h3>
					<font color="red"><%=ServletUtility.getErrorMessage(request)%></font>
					<font color="limegreen"><%=ServletUtility.getSuccessMessage(request)%></font>
				</h3>

			</div>

			<%
				List tlist = (List) request.getAttribute("Quantities");

				int next = DataUtility.getInt(request.getAttribute("nextlist").toString());
			%>

			<%
				Map map = (Map) request.getAttribute("stock");
			%>


			<%
				int pageNo = ServletUtility.getPageNo(request);
				int pageSize = ServletUtility.getPageSize(request);
				int index = ((pageNo - 1) * pageSize) + 1;

				List list = ServletUtility.getList(request);
				Iterator<StockBean> it = list.iterator();

				if (list.size() != 0) {
			%>
			<table width="100%" align="center">
				<tr> 
					<th></th>
					<td align="center">&emsp; <label>Quantity</font> :
					</label> <%=HTMLUtility.getList("id", ServletUtility.getParameter("quantity", request), tlist)%>

					&nbsp;
					<label>Purchase Price</font> :</label>
					 	<input type="text" name="purchasePrice"
						placeholder="Enter Purchase Price"
						oninput=" handleDoubleInput(this, 'purchasePriceError', 8)"
						id="purchasePriceError"
						
						value="<%=ServletUtility.getParameter("purchasePrice", request)%>">
						
					&nbsp;
					 <label>Purchase Date:</label>
                        <input type="text" name="purchaseDate" placeholder="Select Date" readonly="readonly" id="udate"
                               value="<%=ServletUtility.getParameter("purchaseDate", request)%>">

					&nbsp; 
					<label>OrderType</font> :
					</label><%=HTMLUtility.getList2("orderType", String.valueOf(bean.getOrderType()), map)%>




						&nbsp; <input type="submit" name="operation"
						value="<%=StockListCtl.OP_SEARCH%>"> &nbsp; <input
						type="submit" name="operation" value="<%=StockListCtl.OP_RESET%>">
					</td>
				</tr>
			</table>

			<table border="1" width="100%" align="center" cellpadding=6px
				cellspacing=".2">
				<tr style="background: skyblue">
					<th><input type="checkbox" id="select_all" name="select">
					SelectAll</th>

					<th>S.No.</th>
					<th>Quantity</th>
					<th>PurchasePrice</th>
					<th>PurchaseDate</th>
					<th>OrderType</th>
					<th>Edit</th>
				</tr>

				<%
					while (it.hasNext()) {
							bean = it.next();
				%>


				<tr align="center">
					<%--<td><%=map.get(Integer.parseInt(bean.getImportance()))%></td> --%>
					<td><input type="checkbox" class="checkbox" name="ids"
						value="<%=bean.getId()%>"></td>
					<td><%=index++%></td>
					<td><%=bean.getQuantity()%></td>
					<td><%=bean.getPurchasePrice()%></td>
					<td><%=bean.getPurchaseDate()%></td>
					<td><%=map.get(bean.getOrderType())%></td>

					<td><a href="StockCtl?id=<%=bean.getId()%>">Edit</a></td>
				</tr>
				<%
					}
				%>
			</table>

			<table width="100%">
				<tr>
					<th></th>
					<%
						if (pageNo == 1) {
					%>
					<td><input type="submit" name="operation" disabled="disabled"
						value="<%=StockListCtl.OP_PREVIOUS%>"></td>
					<%
						} else {
					%>
					<td><input type="submit" name="operation"
						value="<%=StockListCtl.OP_PREVIOUS%>"></td>
					<%
						}
					%>

					<td><input type="submit" name="operation"
						value="<%=StockListCtl.OP_DELETE%>"></td>
					<td><input type="submit" name="operation"
						value="<%=StockListCtl.OP_NEW%>"></td>
					<td align="right"><input type="submit" name="operation"
						value="<%=StockListCtl.OP_NEXT%>"
						<%=(list.size() < pageSize || next == 0) ? "disabled" : ""%>></td>



				</tr>
			</table>
			<%
				}
				if (list.size() == 0) {
			%>
			<td align="center"><input type="submit" name="operation"
				value="<%=StockListCtl.OP_BACK%>"></td>
			<%
				}
			%>

			<input type="hidden" name="pageNo" value="<%=pageNo%>"> <input
				type="hidden" name="pageSize" value="<%=pageSize%>">
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