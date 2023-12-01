<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import = "ui.models.*, java.util.*"%>

<!DOCTYPE html>

<html>
<head>
<title>Edit Product</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">

<link rel="stylesheet"
      href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/css/bootstrap.min.css">
</head>

<body style="background-color: #E6F9E6;">

	<%@ include file = "header.jsp" %>

    <%if(user.getType() != UserModel.Type.ADMIN) { 
        response.setStatus(403);
        response.sendRedirect("forbidden.jsp");
      }
    %>

	<%
	String errorProduct =    request.getParameter("errorProduct");
	String errorCategory =   request.getParameter("errorCategory");
	String successProduct =  request.getParameter("successProduct");
	String successCategory = request.getParameter("successCategory");
    ProductModel product =  (ProductModel) request.getAttribute("product");
	%>
	<div class="container">
		<div class="row"
			style="margin-top: 5px; margin-left: 2px; margin-right: 2px;">

			<form action="/products" method="post" class="col-md-6 col-md-offset-3"
				style="border: 2px solid black; border-radius: 10px; background-color: #FFE5CC; padding: 10px;">
                <input type="hidden" name="_method" value="PUT" />
                <input type="hidden" name="id" value="<%=product.getId()%>" />

				<div style="font-weight: bold;" class="text-center">
					<h2 style="color: green;">Edit Product Form</h2>

					<% if (errorProduct != null) { %>
					      <p style="color: red;"> <%=errorProduct%> </p>
                    <% }else if(successProduct != null) { %>
					      <p style="color: green;"> The Product was updated successfully  </p>
                    <% } %> 
				</div>
				<div></div>
				<div class="row">
					<div class="col-md-6 form-group">
						<label for="last_name">Product Name</label> 
                        <input type="text" placeholder="Enter Product Name" name="name" class="form-control"
                               id="last_name" value="<%=product.getName()%>" required>
					</div>
					<div class="col-md-6 form-group">
						<label for="producttype">Product Category</label> 
                        <select name="category" id="producttype" class="form-control" required>
                            <% 
                                List<String> categories = (List<String>) request.getAttribute("categories");
                                categories.sort((c1, c2) -> c1.compareTo(c2)); 
                                for( String category : categories ) {
                             %> 
                             <option value="<%= category %>"> <%= category%> </option>
                            
                             <% } %>
						</select>
					</div>
				</div>
				<div class="row">
					<div class="col-md-6 form-group">
						<label for="last_name">Price</label> 
                        <input type="number" placeholder="Enter Unit Price" name="price" class="form-control"
							id="last_name" value="<%=product.getPrice()%>"required>
					</div>
				</div>
				<div class="row">
					<div class="col-md-6 text-center" style="margin-bottom: 2px;">
						<button type="reset" class="btn btn-danger">Reset</button>
					</div>
					<div class="col-md-6 text-center">
						<button type="submit" class="btn btn-success">Update</button>
					</div>
				</div>
			</form>
		</div>
	</div>
</body>
</html>
