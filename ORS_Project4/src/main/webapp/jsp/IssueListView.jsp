<%@page import="com.rays.pro4.Model.IssueModel"%>
<%@page import="java.util.Map"%>
<%@page import="com.rays.pro4.Bean.IssueBean"%>
<%@page import="com.rays.pro4.Bean.BaseBean"%>
<%@page import="com.rays.pro4.Util.HTMLUtility"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="com.rays.pro4.Util.DataUtility"%>
<%@page import="com.rays.pro4.Util.ServletUtility"%>
<%@page import="com.rays.pro4.controller.IssueListCtl"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<html>
<head>
<link rel="icon" type="image/png"
	href="<%=ORSView.APP_CONTEXT%>/img/logo.png" sizes="16*16" />

<title>Issue List</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet"
	href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
<!-- <link rel="stylesheet" href="/resources/demos/style.css"> -->
<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<script src="<%=request.getContextPath()%>/js/Utilities.js"></script>
<script>
	$(function() {
		$("#udate").datepicker({
			changeMonth : true,
			changeYear : true,
			yearRange : '2000:2025',
		});
	});
</script>

<!-- <script>
function handleAlphabetInput(inputElementId, errorElementId, maxLength) {
    const inputElement = document.getElementById(inputElementId);
    const errorMessage = document.getElementById(errorElementId);
    const currentValue = inputElement.value;

    if (/[^a-zA-Z]/.test(currentValue)) {
        errorMessage.textContent = 'Only alphabets are allowed.';
        inputElement.value = currentValue.replace(/[^a-zA-Z]/g, '');
    } else {
        if (currentValue.length > maxLength) {
            errorMessage.textContent = `Only ${maxLength} characters are allowed.`;
            inputElement.value = currentValue.slice(0, maxLength);
        } else {
            errorMessage.textContent = '';
        }
    }

    // Enforce max length again
    inputElement.value = inputElement.value.slice(0, maxLength);
}
</script>
 -->


</head>
<body>
	<jsp:useBean id="bean" class="com.rays.pro4.Bean.IssueBean"
		scope="request"></jsp:useBean>
	<%@include file="Header.jsp"%>


	<form action="<%=ORSView.ISSUE_LIST_CTL%>" method="post">

		<center>

			<div align="center">
			
				<h1>Issue List</h1>
				
				<h3>
					<font color="red"><%=ServletUtility.getErrorMessage(request)%></font>
					<font color="limegreen"><%=ServletUtility.getSuccessMessage(request)%></font>
				</h3>

			</div>

			<%
				List tlist = (List) request.getAttribute("isuue");

				int next = DataUtility.getInt(request.getAttribute("nextlist").toString());
			%>

			<%
				Map map = (Map) request.getAttribute("issue");
				Map map1 = (Map) request.getAttribute("issue1");
			%>


			<%
				int pageNo = ServletUtility.getPageNo(request);
				int pageSize = ServletUtility.getPageSize(request);
				int index = ((pageNo - 1) * pageSize) + 1;

				List list = ServletUtility.getList(request);
				Iterator<IssueBean> it = list.iterator();

				if (list.size() != 0) {
			%>
			<table width="100%" align="center">
				<tr> 
					<th></th>
					
					 <label>OpenDate:</label>
                     <input type="text" 
                        	name="openDate" 
                        	placeholder="Select Date" 
                        	readonly="readonly" id="udate"
                            value="<%=ServletUtility.getParameter("openDate", request)%>">
                               
                      &nbsp;         
                               
					<label>Title<span style="color: red"></span> :</label>
					<input 
   					 type="text" 
   					 name="title"
  				     id="titleInput"
   					 placeholder="Enter Title"
  				     value="<%=ServletUtility.getParameter("title", request)%>"
    				 oninput="handleAlphabetInput('titleInput', 'titleError', 10)" 
    				 onblur="handleAlphabetInput('titleInput', 'titleError', 10)"
					 >
					<span style="color: red;" id="titleError"></span>
					
					 &nbsp;
					<label>Discription<span style="color: red"></span> :</label>
					<input type="text" name="discription" id="discriptionInput"
						placeholder="Enter Discription"
						value="<%=ServletUtility.getParameter("discription", request)%>"
						oninput="handleAlphabetInput('discriptionInput', 'discriptionError', 30)"
						onblur="handleAlphabetInput('discriptionInput', 'discriptionError', 30)">
					<span style="color: red; margin-left: 10px;" id="discriptionError"></span>


					&nbsp;
					<label>AssignTo</font> :
					</label><%=HTMLUtility.getList2("assignTo", String.valueOf(bean.getAssignTo()), map)%>


					&nbsp;
					<label>Status</font> :
					</label><%=HTMLUtility.getList1("status", String.valueOf(bean.getStatus()), map1)%>


					&nbsp;
					<input type="submit" name="operation"
						value="<%=IssueListCtl.OP_SEARCH%>"> &nbsp;
					<input type="submit" name="operation"
						value="<%=IssueListCtl.OP_RESET%>">
					</td>
				</tr>
			</table>

			<table border="1" width="100%" align="center" cellpadding=6px
				cellspacing=".2">
				<tr style="background: skyblue">
					<th><input type="checkbox" id="select_all"
					 name="select">Select All</th>
					<th>S.No.</th>
					<th>OpenDate</th>
					<th>Title</th>
					<th>Discription</th>
					<th>AssignTo</th>
					<th>Status</th>
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
					<td><%=bean.getOpenDate()%></td>
					<td><%=bean.getTitle()%></td>
					<td><%=bean.getDiscription()%></td>
					<td><%=map.get(bean.getAssignTo())%></td>
					<td><%=map1.get(bean.getStatus())%></td>

					<td><a href="IssueCtl?id=<%=bean.getId()%>">Edit</a></td>
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
						value="<%=IssueListCtl.OP_PREVIOUS%>"></td>
					<%
						} else {
					%>
					<td><input type="submit" name="operation"
						value="<%=IssueListCtl.OP_PREVIOUS%>"></td>
					<%
						}
					%>

					<td><input type="submit" name="operation"
						value="<%=IssueListCtl.OP_DELETE%>"></td>
					<td><input type="submit" name="operation"
						value="<%=IssueListCtl.OP_NEW%>"></td>
					<td align="right"><input type="submit" name="operation"
						value="<%=IssueListCtl.OP_NEXT%>"
						<%=(list.size() < pageSize || next == 0) ? "disabled" : ""%>></td>



				</tr>
			</table>
			<%
				}
				if (list.size() == 0) {
			%>
			<td align="center"><input type="submit" name="operation"
				value="<%=IssueListCtl.OP_BACK%>"></td>
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
	

	</center>

	<%@include file="Footer.jsp"%>
</body>
</html>