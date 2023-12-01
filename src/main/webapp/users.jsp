<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@ page import = "ui.models.*, java.util.*"%>

<html>
<head>

<title>Users Management</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">

  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>

</head>

<body style="background-color: #E6F9E6;">

	<%@ include file = "header.jsp" %>

    <%if(user.getType() != UserModel.Type.ADMIN) { 
        response.sendRedirect("forbidden.jsp");
      }
    %>

	<div class="text-center"
		style="color: green; font-weight: bold; font-size: 24px;">Users</div>

	<div class="container-fluid" >
		<div class="table-responsive " style="min-height: 100vh;">
			<table class="table table-hover table-sm" >
				<thead 
					style="background-color: #2c6c4b; color: white; font-size: 18px;">
					<tr class="dropdown">
						<th>ID</th>
                        <th colspan="2" style="text-align: center">Name</th>
                        <th colspan="2" style="text-align: center">Type</th>
					</tr>
		 		</thead>
				<tbody style="background-color: white; font-size: 16px;">

					<% 
                        List<UserModel> users = (List<UserModel>) 
                                                request.getAttribute("users");
 
                        for (int i = 0; i < users.size(); i++) { 
                            UserModel u = users.get(i);
                    %>
                      <tr align="center">
					    	<td> <%=u.getId()%></td>
					    	<td colspan="2" style="text-align: center"> <%=u.getName()%></td>
                            <td colspan="2" style="text-align: center" class=" dropdown" >
                                <a class="dropdown-toggle" data-toggle="dropdown" 
                                    href="#">
                                    <%=u.getType().label%> 
                                    <span class="caret"></span>
					            </a>

                                <ul class="dropdown-menu">
                                    <% for(UserModel.Type type : UserModel.Type.values()) { %>
                                    <li colspan="4" style="text-align: center" ></li>
                                    <li colspan="4" style="text-align: center" ></li>
                                    <li colspan="4" style="text-align: center" >
							            <form method="post" action="/users?name=<%= u.getName()%>&type=<%= type %>">
                                            <button type="submit" class="btn btn-primary"> <%= type.label %> </a>
							            </form>
                                        </a>
                                    </li>
                                    <% } %>
                                </ul>
                             </td>
					    </tr>
					<%     
                     }   
                    %>
				</tbody>
			</table>
		</div>
	</div>
</body>
