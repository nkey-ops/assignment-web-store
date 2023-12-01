<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import = "ui.models.*, db.*, java.util.*"%>

<!DOCTYPE html>
<html>
<head>

<title>Orders</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">

<link rel="stylesheet"
      href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/css/bootstrap.min.css">
</head>

<body style="background-color: #E6F9E6;">

	<%@ include file = "header.jsp" %>

	<div class="text-center"
		style="color: green; font-size: 24px; font-weight: bold;">
         Orders</div>
	<div class="container-fluid">
		<div class="table-responsive ">
			<table class="table table-hover table-sm">
				<thead
					style="background-color: #2c6c4b; color: white; font-size: 18px;">
					<tr>
                        <th style="text-align: center">ID</th>
                        <th colspan="2" style="text-align: center">Price</th>
						<th colspan="2" style="text-align: center">Status</th>

                        <%if(user.getType() == UserModel.Type.ADMIN 
                               || user.getType() == UserModel.Type.WAREHOUSE_STAFF) { %>
						    <th colspan="2" style="text-align: center">Buyer</th>
                        <% } %>

                        <%if(user.getType() == UserModel.Type.ADMIN) { %>
                            <th colspan="2" style="text-align: center">Packager</th>
                        <% } %>

                        <%if(user.getType() == UserModel.Type.WAREHOUSE_STAFF) { %>
                            <th colspan="2" style="text-align: center">Package</th>
                        <% } %>

					</tr>
		 		</thead>
				<tbody style="background-color: white; font-size: 16px;">


					<% 
                        List<OrderModel> orders = (List<OrderModel>) request.getAttribute("orders");

                        for (OrderModel order : orders)  {
                            double orderPrice = 0;
                            for (ProductModel product : order.getProducts()) {
                                orderPrice += product.getPrice();
                            } 
                    %>
					    <tr>
					    	<td style="text-align: center"> <%= order.getId() %></td>
					    	<td colspan="2" style="text-align: center"> $<%= orderPrice %></td>
					    	<td colspan="2" style="text-align: center"> <%= order.getStatus().label %></td>

                            <%if(user.getType() == UserModel.Type.ADMIN 
                                 || user.getType() == UserModel.Type.WAREHOUSE_STAFF) { %>
					    	    <td colspan="2" style="text-align: center"> <%= order.getBuyerName() %></td>
                            <% } %>

                            <%if(user.getType() == UserModel.Type.ADMIN) { %> 
					    	    <td colspan="2" style="text-align: center"> 
                                    <%= order.getPackagerName() == null ? "None" : order.getPackagerName()%></td>
                            <% } %>

                            <%if(user.getType() == UserModel.Type.WAREHOUSE_STAFF) { %>
                                <td colspan="2" style="text-align: center"> 
                                    <%if(order.getStatus() ==  OrderStatus.PENDING) { %> 
                                        <form method="post" action="/orders?id=<%=order.getId()%>">
                                            <input type="hidden" name="_method" value="PUT" />
                                            <button type="submit" class="fa fa-plus"></button>
                                        </form>
                                    <% } %>
                                </td>
                           <% } %>
					    </tr>

					<%      orderPrice = 0; 
                       }   
                    %>
				</tbody>
			</table>
		</div>
	</div>
</body>
