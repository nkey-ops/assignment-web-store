<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import = "ui.models.*, java.util.*"%>

<html>
<head>

<title>Shopping Cart</title>
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
            Cart Items
        </div>
	<div class="container-fluid">
		<div class="table-responsive ">
			<table class="table table-hover table-sm">
				<thead
					style="background-color: #2c6c4b; color: white; font-size: 18px;">
					<tr>
						<th>Name</th>
                        <th colspan="2" style="text-align: center">Price</th>
                        <th colspan="2" style="text-align: center">Quantity</th>
						<th colspan="2" style="text-align: center">Remove</th>
						<th colspan="2" style="text-align: center">Total Price</th>
					</tr>
		 		</thead>
				<tbody style="background-color: white; font-size: 16px;">


					<% 
                        List<ProductModel> products = (List<ProductModel>) request.getAttribute("products");

                        int qty = 1;
                        int totalPerItem = 0;
                        int totalAmount = 0;
                        for (int i = 0; i < products.size(); i++) { 
                            ProductModel product = products.get(i);
                            totalAmount += product.getPrice();
                            totalPerItem += product.getPrice(); 

                            if(i < products.size() - 1
                                && product.getName().equals(products.get(i + 1).getName()))  {
                                qty++;
                                continue;
                            }else { 
                    %>
					    <tr>
					    	<td> <%=product.getName()%></td>
					    	<td colspan="2" style="text-align: center"> $<%=product.getPrice()%></td>
					    	<td colspan="2" style="text-align: center"> <%=qty%></td>
                            <td colspan="2" style="text-align: center">
							    <form method="post" action="/cart?method=DELETE&id=<%=product.getId()%>">
                                    <button type="submit" class="fa fa-minus"></button>
							    </form>
                            </td>
                            <td colspan="2" style="text-align: center"> $<%=totalPerItem%></td>

					    </tr>

					<%      totalPerItem = 0; 
                            qty = 1; 
                          }
                       }   
                    %>


                    <%
                    if (totalAmount != 0) {
                    %>
                        <tr style="background-color: grey; color: white;">
                            <td colspan="5" >
                                <td colspan="2" style="color: black; font-size: 24px; font-weight: bold; text-align: right">
                                    Total Amount to Pay 
                                </td>
                                <td colspan="2" 
                                    style="color: green; font-size: 24px; font-weight: bold; text-align: center;"> 
                                    $<%=totalAmount%>
                                </td>
                            </td>
                        </tr>
                        <tr style="background-color: grey; color: white;">
                            <td colspan="5" >
                                <td colspan="2" style="text-align: right;">
                                    <form method="post" >
                                        <button formaction="/cart?method=DELETE"
                                                style="background-color: black; color: white;">
                                                Clear Cart 
                                        </button>
                                    </form>
                                </td>
                                <td colspan="2" style="text-align: center ;">
                                    <form method="post" >
                                        <button formaction="/orders" 
                                               style="background-color: green; color: white;" >
                                            Pay Now
                                        </button>
                                    </form>
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
