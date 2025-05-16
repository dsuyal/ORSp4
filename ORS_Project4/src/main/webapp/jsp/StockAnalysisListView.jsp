
<%@page import="java.util.HashMap"%>
<%@page import="com.rays.pro4.controller.StockAnalysisListCtl"%>
<%@page import="com.rays.pro4.Bean.StockAnalysisBean"%>
<%@page import="com.rays.pro4.Util.HTMLUtility"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="com.rays.pro4.Util.DataUtility"%>
<%@page import="com.rays.pro4.Util.ServletUtility"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<html>
<head>
<link rel="icon" type="image/png"
	href="<%=ORSView.APP_CONTEXT%>/img/logo.png" sizes="16*16" />

<title>Stock Analysis List</title>
<link rel="stylesheet"
	href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">


<script src="<%=ORSView.APP_CONTEXT%>/js/jquery.min.js"></script>
<script src="<%=ORSView.APP_CONTEXT%>/js/Checkbox11.js"></script>
<script src="<%=ORSView.APP_CONTEXT%>/js/Utilities.js"></script>

<link rel="stylesheet" href="/resources/demos/style.css">
<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>

<script>
        /**
         * Function to validate input length (min 5, max 15 characters).
         * @param {HTMLInputElement} input - The input field to validate.
         * @param {string} errorElementId - The ID of the error message element.
         */
        function validateLength(input, errorElementId) {
            const minLength = 5;
            const maxLength = 15;
            const value = input.value.trim();
            const errorElement = document.getElementById(errorElementId);

            // Check length validation
            if (value.length === 0) {
                errorElement.textContent = "UserId not less then 5 characture.";
                errorElement.textContent = "UserId not more then 15 characture.";
                
            } else if (value.length < minLength) {
                errorElement.textContent = `UserId must be at least ${minLength} characters.`;
            } else if (value.length > maxLength) {
                errorElement.textContent = `UserId must not exceed ${maxLength} characters.`;
               
            } else {
                errorElement.textContent = ""; // Clear error if valid
            }
        }
    </script>


<script>
	$(function() {
		$("#udate").datepicker({
			changeMonth : true,
			changeYear : true,
			yearRange : '1980:2024',
		//  mindefaultDate : "01-01-1962"
		});
	});

	$(function() {
		$("#udate1").datepicker({
			changeMonth : true,
			changeYear : true,
			yearRange : '1980:2024',
		//  mindefaultDate : "01-01-1962"
		});
	});
</script>

</head>
<body>
	<jsp:useBean id="bean" class="com.rays.pro4.Bean.StockAnalysisBean"
		scope="request"></jsp:useBean>
	<%@include file="Header.jsp"%>


	<form action="<%=ORSView.STOCKANALYSIS_LIST_CTL%>" method="post">

		<center>

			<div align="center">
				<h1>Stock Analysis List</h1>
				<h3>
					<font color="red"><%=ServletUtility.getErrorMessage(request)%></font>
					<font color="green"><%=ServletUtility.getSuccessMessage(request)%></font>
				</h3>

			</div>
			

			<%
				int next = DataUtility.getInt(request.getAttribute("nextlist").toString());
			%>


			<%
				int pageNo = ServletUtility.getPageNo(request);
				int pageSize = ServletUtility.getPageSize(request);
				int index = ((pageNo - 1) * pageSize) + 1;

				List list = ServletUtility.getList(request);
				Iterator<StockAnalysisBean> it = list.iterator();

				if (list.size() != 0) {
			%>
			<table width="100%" align="center">
				 <tr>
					<th></th>
					
					
					 <td align="center"><label>Stock Symbol</font> :
					</label> <input type="text" name="stockSymbol"
						placeholder="Enter Stock Symbol"
						
								
						
						
						value="<%=ServletUtility.getParameter("stockSymbol", request)%>">
						
					 <td align="center"><label>Start Date</font> :
					</label> <input type="text" name="startDate"
						placeholder="Enter startDate" readonly="readonly" id="udate"
						value="<%=ServletUtility.getParameter("startDate", request)%>">

						 <td align="center"><label>End Date  </font> :
					</label> <input type="text" name="endDate"
						placeholder="Enter endtDate" readonly="readonly" id="udate1"
						value="<%=ServletUtility.getParameter("endDate", request)%>">
						
						 &emsp; <label>Analysis Type</font> : </label>
						 
						<%
						 	HashMap map = new HashMap();
						 	map.put("Fundamental", "Fundamental");
						 	map.put("Technical", "Technical");
							

							String hlist = HTMLUtility.getList("analysisType", String.valueOf(bean.getAnalysisType()), map);
						%> <%=hlist%>
					
						</td>
						&nbsp;
					
						
						 
						<td>
						 <input type="submit" name="operation"
						value="<%=StockAnalysisListCtl.OP_SEARCH%>"> &nbsp; <input
						type="submit" name="operation"
						value="<%=StockAnalysisListCtl.OP_RESET%>"></td>
				</tr>
			</table>
			<br>

			<table border="1" width="100%" align="center" cellpadding=6px
				cellspacing=".2">
				<tr style="background: skyblue">
					<th><input type="checkbox" id="select_all" name="select">Select
						All</th>

					<th>S.No.</th>
					<th>Stock Symbol</th>
					<th>Analysis Type</th>
					<th>Start Date</th>
					<th>End Date</th>
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
					<td><%=bean.getStockSymbol()%></td>
					<td><%=bean.getAnalysisType()%></td>
					<td><%=bean.getStartDate()%></td>
					<td><%=bean.getEndDate()%></td>
					<td><a href="StockAnalysisCtl?id=<%=bean.getId()%>">Edit</a></td>
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
						value="<%=StockAnalysisListCtl.OP_PREVIOUS%>"></td>
					<%
						} else {
					%>
					<td><input type="submit" name="operation"
						value="<%=StockAnalysisListCtl.OP_PREVIOUS%>"></td>
					<%
						}
					%>

					<td><input type="submit" name="operation"
						value="<%=StockAnalysisListCtl.OP_DELETE%>"></td>
					<td><input type="submit" name="operation"
						value="<%=StockAnalysisListCtl.OP_NEW%>"></td>
					<td align="right"><input type="submit" name="operation"
						value="<%=StockAnalysisListCtl.OP_NEXT%>"
						<%=(list.size() < pageSize || next == 0) ? "disabled" : ""%>></td>



				</tr>
			</table>
			<%
				}
				if (list.size() == 0) {
			%>
			<td align="center"><input type="submit" name="operation"
				value="<%=StockAnalysisListCtl.OP_BACK%>"></td>
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