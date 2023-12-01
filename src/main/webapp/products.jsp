<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@ page import = "ui.models.*, java.util.*"%>

<html>
<head>

<title>Products List</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">

<link rel="stylesheet"
      href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/css/bootstrap.min.css">
<link rel="stylesheet"
      href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/js/bootstrap.min.js"></script>


</head>

<body style="background-color: #E6F9E6;">

	<%@ include file = "header.jsp" %>

	<div class="text-center"
		style="color: green; font-size: 24px; font-weight: bold;">
        Products List</div>
	<div class="container-fluid">
		<div class="table-responsive ">
			<table class="table table-hover table-sm">
				<thead
					style="background-color: #2c6c4b; color: white; font-size: 18px;">
					<tr>
						<th>Name</th>
                        <th colspan="2" style="text-align: center">Price</th>
                        <th colspan="2" style="text-align: center">Quantity</th>
                        <th colspan="2" style="text-align: center">Category</th>

                        <%if(user.getType() == UserModel.Type.ADMIN) { %>
						    <th colspan="2" style="text-align: center">Status</th>
						    <th colspan="2" style="text-align: center">Edit</th>
                        <% }else { %>
                            <th colspan="2" style="text-align: center">Add</th>
                        <% } %>


					</tr>
		 		</thead>
				<tbody style="background-color: white; font-size: 16px;">


					<% 
                        List<ProductModel> products = (List<ProductModel>) request.getAttribute("products");
                        products.sort((a, b) -> a.getName().compareTo(b.getName()));

                        int qty = 1;
                        for (int i = 0; i < products.size(); i++) { 
                            ProductModel product = products.get(i);
                            
                            if(i != products.size() - 1 
                                && product.getName().equals(products.get(i + 1).getName())) { 
                                qty++;
                                continue;
                            }else { 
                      %> 
					    <tr style="height: 65px;">
					    	<td> <%=product.getName()%></td>
					    	<td colspan="2" style="text-align: center"> $<%=product.getPrice()%> </td>
					    	<td colspan="2" style="text-align: center"> <%=qty%></td>
					    	<td colspan="2" style="text-align: center"> <%=product.getCategory()%></td>

                            <% if(user.getType() == UserModel.Type.ADMIN) { %>
                                <td colspan="2" style="text-align: center"> <%=product.getStatus()%></td>

                                <td colspan="2" style="text-align: center">
                                 <%if(product.getStatus() == ProductStatus.ACTIVE) { %>
                                         <form method="get" action="/products-edit-form">
                                             <input type="hidden" name="id" value="<%=product.getId()%>" /> 
                                             <button type="submit" class="fa fa-edit"></button>
                                         </form>
                                 <% } %>
                                </td>
                            <% } else { %>
                                <td colspan="2" style="text-align: center">
                                    <form method="post" action="/cart?id=<%=product.getId()%>">
                                        <button type="submit" class="fa fa-plus"></button>
                                    </form>
                            <% } %>  
                            </td>
					    </tr>

					<%      qty = 1; 
                          }
                       }   
                    %>
				</tbody>
			</table>
		</div>
	</div>
</body>
