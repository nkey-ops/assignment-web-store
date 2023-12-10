# Requirements 
Lab in Java enterprise
In this lab, you will make a first webshop, 
using a Java application that via JDBC has access
to a database and has JSP as an interface to the users. 
This, of course, in a three-layer architecture.

## Grade 3
● Shopping basket  
● User identification  
● Ability to put items in the basket and look in it.  
● The solution must have a 3-layer architecture.  

## Grade 4
● You must be able to send orders that must be within a transaction.  
● User management must be developed so that you can administer them. Different
authorization classes must also be introduced. (customer, admin, warehouse staff)  
● A good 3 layer structure must be clear.  

## Grade 5
● Here we will write part of the administrative part of the system,
which can be implemented both as HTML or a regular graphical interface.  
● You must be able to add and edit goods and goods categories.  
● Warehouse staff must be able to look at orders and 'pack' them.    
● Of course, for these things you have to have authorization.  
● You must also implement an MVC structure in combination with
the 3-layer architecture.  

# Login page
Where the user can be logged in as a client, warehouse staff, or admin
![Login Page](https://github.com/nkey-ops/assignment-web-store/assets/81760194/2983ff8c-72dd-4082-a790-2a58a8fa2768)

# Client: Products List
A client can see available stock in the store its quantity and price per unit. 
A Client also can add the product to the cart.
![Client Products List](https://github.com/nkey-ops/assignment-web-store/assets/81760194/db5b4225-f4f1-4e85-9c7b-5d9a845449ce)

# Client: Cart of products
This page allows the client to see the products added to the cart, their amount and 
the total price of certain products as well as the price for the whole cart.
After paying for the cart the order will be created for these products.
![Client Cart Products List](https://github.com/nkey-ops/assignment-web-store/assets/81760194/6f455319-5f95-4c2f-9fcf-8b3aa19a72cb)

# Client: Orders List.
Here a client can see the status of the created orders and their price.
![Client Orders List](https://github.com/nkey-ops/assignment-web-store/assets/81760194/89b0f98a-e0a3-465f-b07d-ca0124282aef)

# Warehouse Staff: Orders
If the user is logged in as a warehouse staff he/she can prepare the package for sending.
After that, the order status will be changed from "Pending" to "Sent".
![Warehouse Staff Orders List](https://github.com/nkey-ops/assignment-web-store/assets/81760194/351ca8aa-417f-4679-94d1-60de163925e1)

# Admin: Products List
When the user is logged in as an admin he/she will have more abilities over the client or
warehouse staff.  
An admin can add new products and edit old ones. The status of the product is
also visible, if the product is available it will have an "ACTIVE" status if it was bought "SOLD".
![Admin Products List](https://github.com/nkey-ops/assignment-web-store/assets/81760194/aeba0a33-e876-454f-8930-5c6b370d3c0e)

# Admin: Orders List
Here an admin can see the status of all the orders whether it was "Sent" or it is still "Pending",
the buyer's account name, and if the order was sent, the name of the warehouse staff who packaged it.
![Admin Orders List](https://github.com/nkey-ops/assignment-web-store/assets/81760194/5752d717-58b9-4eba-94f5-d941eac7e9dc)

# Admin: Users List
Admin can see all the users that are logged in, their ID, name, and type of account, and edit it. 
![Admin Users List](https://github.com/nkey-ops/assignment-web-store/assets/81760194/cb0327a5-543e-4794-94de-903406645cc2)

# Admin: Product Form
The ability to create new products and categories is also provided to an admin.
![Admin Product Form](https://github.com/nkey-ops/assignment-web-store/assets/81760194/6e9c11c9-dc4a-448b-a9b0-52fed5dd8193)

# Admin: Edit Product Form
Already created products can be edited by an admin. 
![Admin Edit Product Form](https://github.com/nkey-ops/assignment-web-store/assets/81760194/fa52d3cb-3952-4239-aba7-6ba388d5d21a)
