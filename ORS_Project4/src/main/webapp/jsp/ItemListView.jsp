<%@page import="com.rays.pro4.Model.ItemModel"%>
<%@page import="java.util.Map"%>
<%@page import="com.rays.pro4.Bean.ItemBean"%>
<%@page import="com.rays.pro4.Bean.BaseBean"%>
<%@page import="com.rays.pro4.Util.HTMLUtility"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="com.rays.pro4.Util.DataUtility"%>
<%@page import="com.rays.pro4.Util.ServletUtility"%>
<%@page import="com.rays.pro4.controller.ItemListCtl"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<html>
<head>
<link rel="icon" type="image/png"
	href="<%=ORSView.APP_CONTEXT%>/img/logo.png" sizes="16*16" />

<title>Item List</title>

<script src="<%=ORSView.APP_CONTEXT%>/js/jquery.min.js"></script>
<script src="<%=ORSView.APP_CONTEXT%>/js/Checkbox11.js"></script>
<link rel="stylesheet"
	href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
<link rel="stylesheet" href="/resources/demos/style.css">
<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>

<link rel="stylesheet" href="/resources/demos/style.css">
<script>
	$(function() {
		$("#udatee").datepicker({
			changeMonth : true,
			changeYear : true,
			yearRange : '2024:2026',
		});
	});
</script>

</head>
<body>
	<jsp:useBean id="bean" class="com.rays.pro4.Bean.ItemBean"
		scope="request"></jsp:useBean>
	<%@include file="Header.jsp"%>


	<form action="<%=ORSView.ITEM_LIST_CTL%>" method="post">

		<center>

			<div align="center">
				<h1>Item List</h1>
				<h3>
					<font color="red"><%=ServletUtility.getErrorMessage(request)%></font>
					<font color="limegreen"><%=ServletUtility.getSuccessMessage(request)%></font>
				</h3>

			</div>

			<%
				List ilist = (List) request.getAttribute("Title");

				int next = DataUtility.getInt(request.getAttribute("nextlist").toString());
			%>

			<%
				Map map = (Map) request.getAttribute("cate");
			%>


			<%
				int pageNo = ServletUtility.getPageNo(request);
				int pageSize = ServletUtility.getPageSize(request);
				int index = ((pageNo - 1) * pageSize) + 1;

				List list = ServletUtility.getList(request);
				Iterator<ItemBean> it = list.iterator();

				if (list.size() != 0) {
			%>
			<table width="100%" align="center">
				<input type="hidden" name="id" value="<%=bean.getId()%>">
				<tr>
					<th></th>
					<td align="center">&emsp; <label>Title</font> :
					</label> <%=HTMLUtility.getList("ids", DataUtility.getStringData(bean.getTitle()), ilist)%>


						<label>Cost</font> :
					</label> <input type="text" name="cost" placeholder="Enter Cost"
						value="<%=ServletUtility.getParameter("cost", request)%>">
						
						
						 <label>Purchase Date:</label>
                        <input type="text" name="purchaseDate" placeholder="Select Date" readonly="readonly" id="udatee"
                               value="<%=ServletUtility.getParameter("purchaseDate", request)%>">


						&nbsp; <label>Category</font> :
					</label><%=HTMLUtility.getList2("category", DataUtility.getStringData(bean.getCategory()), map)%>




						&nbsp; <input type="submit" name="operation"
						value="<%=ItemListCtl.OP_SEARCH%>"> &nbsp; <input
						type="submit" name="operation" value="<%=ItemListCtl.OP_RESET%>">
					</td>
				</tr>
			</table>

			<table border="1" width="100%" align="center" cellpadding=6px
				cellspacing=".2">
				<tr style="background: orange">
					<th><input type="checkbox" id="select_all" name="select">Select
						All</th>

					<th>S.No.</th>
					<th>Title</th>
					<th>OverView</th>
					<th>Cost</th>
					<th>Purchase Date</th>
					<th>Category</th>
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
					<td><%=bean.getTitle()%></td>
					<td><%=bean.getOverView()%></td>
					<td><%=bean.getCost()%></td>
					<td><%=bean.getPurchaseDate()%></td>
					<td><%=map.get(bean.getCategory())%></td>
					<td><a href="ItemCtl?id=<%=bean.getId()%>">Edit</a></td>
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
						value="<%=ItemListCtl.OP_PREVIOUS%>"></td>
					<%
						} else {
					%>
					<td><input type="submit" name="operation"
						value="<%=ItemListCtl.OP_PREVIOUS%>"></td>
					<%
						}
					%>

					<td><input type="submit" name="operation"
						value="<%=ItemListCtl.OP_DELETE%>"></td>
					<td><input type="submit" name="operation"
						value="<%=ItemListCtl.OP_NEW%>"></td>
					<td align="right"><input type="submit" name="operation"
						value="<%=ItemListCtl.OP_NEXT%>"
						<%=(list.size() < pageSize || next == 0) ? "disabled" : ""%>></td>



				</tr>
			</table>
			<%
				}
				if (list.size() == 0) {
			%>
			<td align="center"><input type="submit" name="operation"
				value="<%=ItemListCtl.OP_BACK%>"></td>
			<%
				}
			%>

			<input type="hidden" name="pageNo" value="<%=pageNo%>"> <input
				type="hidden" name="pageSize" value="<%=pageSize%>"></form>
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