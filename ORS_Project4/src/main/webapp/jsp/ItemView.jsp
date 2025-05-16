<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="com.rays.pro4.Util.HTMLUtility"%>
<%@page import="com.rays.pro4.Util.DataUtility"%>
<%@page import="com.rays.pro4.Util.ServletUtility"%>
<%@page import="com.rays.pro4.controller.ItemCtl"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<html>
<head>
<link rel="icon" type="image/png"
	href="<%=ORSView.APP_CONTEXT%>/img/logo.png" sizes="16*16" />
<title>Item Page</title>

<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet"
	href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
<link rel="stylesheet" href="/resources/demos/style.css">
<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>

<script src="<%=request.getContextPath()%>/js/Utilities.js"></script>

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
	<%@ include file="Header.jsp"%>


	<center>

		<form action="<%=ORSView.ITEM_CTL%>" method="post">



			<div align="center">
				<h1>

					<%
						if (bean != null && bean.getId() > 0) {
					%>
					<tr>
						<th><font size="5px"> Update Item </font></th>
					</tr>
					<%
						} else {
					%>
					<tr>
						<th><font size="5px"> Add Item </font></th>
					</tr>
					<%
						}
					%>
				</h1>

				<h3>
					<font color="red"> <%=ServletUtility.getErrorMessage(request)%></font>
					<font color="limegreen"> <%=ServletUtility.getSuccessMessage(request)%></font>
				</h3>

				<%
					Map map = (Map) request.getAttribute("cate");
				%>

			</div>


			<table>
				<input type="hidden" name="id" value="<%=bean.getId()%>">

				<tr>
					<th align="left">Title <span style="color: red">*</span> :
					</th>
					<td><input type="text" name="title" placeholder="Enter title"
						size="26" id="titleInput"
						oninput="handleAlphabetInput('titleInput', 'titleError', 10)"
						onblur="handleAlphabetInput('titleInput', 'titleError', 10)"
						value="<%=DataUtility.getStringData(bean.getTitle()).equals('0') ? "" : DataUtility.getStringData(bean.getTitle())%>">
					

					</td>
					<td style="position: fixed"><span style="color: red;"
						id="titleError"><%=ServletUtility.getErrorMessage("title", request)%></span>
					</td>
				</tr>
				
				<tr>
					<th style="padding: 3px"></th>
				</tr>
				<tr>

					<th align="left">OverView<span style="color: red">*</span> :
					</th>
					<td><textarea name="overView" id="overViewInput"
							placeholder="Enter overView"
							style="height: 34px; width: 219px;"
							oninput="handleAlphabetInput('overViewInput', 'overViewError', 30)"
							onblur="handleAlphabetInput('overViewInput', 'overViewError', 30)">
							<%= DataUtility.getStringData(bean.getOverView()) %></textarea>
					</td>					
					<td style="position: fixed"><span style="color: red;"
						id="overViewError"><font color="red"><%=ServletUtility.getErrorMessage("overView", request)%></font></td>

				</tr>

				

				<tr>
					<th style="padding: 3px"></th>
				</tr>

				<tr>
					<th align="left">Cost<span style="color: red">*</span> :
					</th>
					<td><input type="text" name="cost"
						placeholder="Enter cost" size="26"
						oninput=" handleIntegerInput(this, 'costError', 8)"
						onblur=" validateIntegerInput(this, 'costError', 8)"
						value="<%=(DataUtility.getStringData(bean.getCost()).equals("0") ? "" : bean.getCost())%>"></td>
					<td style="position: fixed"><font color="red"id="quantityError"><%=ServletUtility.getErrorMessage("cost", request)%></font></td>

				</tr>
				
				<tr>
					<th style="padding: 3px"></th>
				</tr>
				
				
				<tr>
					<th align="left">Purchase Date <span style="color: red">*</span> :
					</th>
					<td><input type="text" name="purchaseDate" placeholder="Enter Purchase Date"
						size="26" readonly="readonly" id="udatee"
						value="<%=DataUtility.getDateString(bean.getPurchaseDate())%>"></td>
					<td style="position: fixed;"><font color="red"> <%=ServletUtility.getErrorMessage("purchaseDate", request)%></font></td>
				</tr>

				<tr>
					<th style="padding: 3px"></th>
				</tr>


				<tr>
					<th align="left">Category<span style="color: red">*</span> :
					</th>
					<td>
						<%
							String hlist = HTMLUtility.getList2("category", DataUtility.getStringData(bean.getCategory()), map);
						%> <%=hlist%>
					</td>
					<td style="position: fixed"><font color="red"> <%=ServletUtility.getErrorMessage("category", request)%></font></td>
				</tr>
				
				<tr>
					<th style="padding: 3px"></th>
				</tr>




				<tr>
					<th></th>
					<%
						if (bean.getId() > 0) {
					%>
					<td colspan="2">&nbsp; &emsp; <input type="submit"
						name="operation" value="<%=ItemCtl.OP_UPDATE%>"> &nbsp;
						&nbsp; <input type="submit" name="operation"
						value="<%=ItemCtl.OP_CANCEL%>"></td>

					<%
						} else {
					%>

					<td colspan="2">&nbsp; &emsp; <input type="submit"
						name="operation" value="<%=ItemCtl.OP_SAVE%>"> &nbsp;
						&nbsp; <input type="submit" name="operation"
						value="<%=ItemCtl.OP_RESET%>"></td>

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