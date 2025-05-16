<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="com.rays.pro4.Util.HTMLUtility"%>
<%@page import="com.rays.pro4.Util.DataUtility"%>
<%@page import="com.rays.pro4.Util.ServletUtility"%>
<%@page import="com.rays.pro4.controller.IssueCtl"%>


<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<html>
<head>
<link rel="icon" type="image/png"
	href="<%=ORSView.APP_CONTEXT%>/img/logo.png" sizes="16*16" />
<title>Issue Page</title>

<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet"
	href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
<link rel="stylesheet" href="/resources/demos/style.css">
<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>

<script src="<%=request.getContextPath()%>/js/Utilities.js"></script>

<%-- <script src="<%=ORSView.APP_CONTEXT%>/js/Utilities.js"></script>
 --%>

<script>
	$(function() {
		$("#udate").datepicker({
			changeMonth : true,
			changeYear : true,
			yearRange : '1990:2025',
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
</script>  -->


</head>
<body>
	<jsp:useBean id="bean" class="com.rays.pro4.Bean.IssueBean"
		scope="request"></jsp:useBean>
	<%@ include file="Header.jsp"%>


	<div align="center">

		<form action="<%=ORSView.ISSUE_CTL%>" method="post"
			onsubmit="return validateForm()">


			<h1>

				<%
					if (bean != null && bean.getId() > 0) {
				%>
				<tr>
					<th><font size="5px"> Update Issue </font></th>
				</tr>
				<%
					} else {
				%>
				<tr>
					<th><font size="5px"> Add Issue </font></th>
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
   					 Map<Integer, String> map = (Map<Integer, String>) request.getAttribute("issue");
   					 Map<Integer, String> map1 = (Map<Integer, String>) request.getAttribute("issue1");
				%>
			<table>

				<tr>
					<input type="hidden" name="id" value="<%=bean.getId()%>">

					<th align="left">Open Date <span style="color: red">*</span> :
					</th>
					<td><input type="text" name="openDate"
						placeholder="Enter openDate" size="26" readonly="readonly"
						id="udate"
						value="<%=DataUtility.getDateString(bean.getOpenDate())%>"></td>
					<td style="position: fixed;"><font color="red"> <%=ServletUtility.getErrorMessage("openDate", request)%></font></td>
				</tr>

				<tr>
					<input type="hidden" name="id" value="<%=bean.getId()%>">
					
					<th align="left">Title<span style="color: red">*</span>:
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
					<th align="left">Discription<span style="color: red">*</span>
						:
					</th>
					<td><textarea name="discription" id="discriptionInput"
							placeholder="Enter discription"
							style="height: 34px; width: 219px;"
							oninput="handleAlphabetInput('discriptionInput', 'discriptionError', 30)"
							onblur="handleAlphabetInput('discriptionInput', 'discriptionError', 30)">
							<%= DataUtility.getStringData(bean.getDiscription()) %></textarea>
					</td>
					<td style="position: fixed"><span style="color: red;"
						id="discriptionError"> <%=ServletUtility.getErrorMessage("discription", request)%>
					</span></td>
				</tr>





				<tr>
					<th align="left">AssignTo<span style="color: red">*</span> :
					</th>
					<td>
						<%
							String hlist = HTMLUtility.getList2("assignTo", DataUtility.getStringData(bean.getAssignTo()), map);
						%> <%=hlist%>
					</td>
					<td style="position: fixed"><font color="red"> <%=ServletUtility.getErrorMessage("assignTo", request)%></font></td>
				</tr>

				<tr>
					<th style="padding: 3px"></th>
				</tr>

				<tr>
					<th align="left">Status<span style="color: red">*</span> :
					</th>
					<td>
						<%
							String hlist1 = HTMLUtility.getList1("status", DataUtility.getStringData(bean.getStatus()), map1);
						%> <%=hlist1%>
					</td>
					<td style="position: fixed"><font color="red"> <%=ServletUtility.getErrorMessage("status", request)%></font></td>
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
						name="operation" value="<%=IssueCtl.OP_UPDATE%>"> &nbsp;
						&nbsp; <input type="submit" name="operation"
						value="<%=IssueCtl.OP_CANCEL%>"></td>

					<%
						} else {
					%>

					<td colspan="2">&nbsp; &emsp; <input type="submit"
						name="operation" value="<%=IssueCtl.OP_SAVE%>"> &nbsp;
						&nbsp; <input type="submit" name="operation"
						value="<%=IssueCtl.OP_RESET%>"></td>

					<%
						}
					%>
				</tr>
			</table>
		</form>
	</div>

	<%@ include file="Footer.jsp"%>

</body>
</html>