<%@ page import="db.*, bo.*, ui.models.UserModel"%>

<html>
<head>
<title>Logout Header</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">

<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/css/bootstrap.min.css">

<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">

</head>

	<%
	/* Checking the user credentials */
        
       UserModel user = (UserModel) session.getAttribute("user");
       if(user == null) response.sendRedirect("/login"); 
    %>


<body style="background-color: #E6F9E6;">
	<!--Company Header Starting  -->
	<div class="container-fluid text-center"
		style="margin-top: 45px; background-color: #3d9f3d; color: white; padding: 5px;">
		<h2>Web Store</h2>
	</div>
	<!-- Company Header Ending -->

    <%
	if(user.getType() == UserModel.Type.ADMIN){ //ADMIN HEADER
	%>
	<nav class="navbar navbar-default navbar-fixed-top" style="background-color: #000;">
		<div class="container-fluid">
			<div class="navbar-header">
				<button type="button" class="navbar-toggle" data-toggle="collapse"
					data-target="#myNavbar">
					<span class="icon-bar"></span> <span class="icon-bar"></span> <span
						class="icon-bar"></span>
				</button>
				<a class="navbar-brand" href="/products">
                    <span class="glyphicon glyphicon-home">&nbsp;</span>
                   <%=user.getType().label%> | <%=user.getName()%>
                </a>
			</div>
			<div class="collapse navbar-collapse" id="myNavbar">
				<ul class="nav navbar-nav navbar-right">
					<li><a href="/products">Products</a></li>
					<li><a href="/orders">Orders</a></li>
					<li><a href="/users">Users</a></li>
					<li><a href="/products-form">Add Products</a></li>
					<li><a href="/logout">Logout</a></li>

				</ul>
			</div>
		</div>
	</nav>
	<%
    } else  { 
	%>
	<nav class="navbar navbar-default navbar-fixed-top" style="background-color: #000;">

		<div class="container-fluid">
			<div class="navbar-header">
				<button type="button" class="navbar-toggle" data-toggle="collapse"
					data-target="#myNavbar">
					<span class="icon-bar"></span> <span class="icon-bar"></span> <span
						class="icon-bar"></span>
				</button>
				<a class="navbar-brand" href="/products"><span
					class="glyphicon glyphicon-home">&nbsp;</span>
                   <%=user.getType().label%> | <%=user.getName()%>
                </a>
			</div>

			<div class="collapse navbar-collapse" id="myNavbar">
				<ul class="nav navbar-nav navbar-right">
					<li><a href="/products">
                            <span >Products</span>
                        </a>
                    </li>
					<li><a href="/cart"> Cart</a></li>

	                <%
	                   if(user.getType() == UserModel.Type.WAREHOUSE_STAFF){ 
	                %>
					    <li><a href="/orders/package">Package Orders</a></li>
                    <% }else  { %>
                        <li><a href="/orders">Orders</a></li>
                    <% } %>
					<li><a href="/logout">Logout</a></li>
				</ul>
			</div>
		</div>
	</nav>
    <% } %>
</body>
</html>
