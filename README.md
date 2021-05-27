# Intuit Order Management System Interview Test


- API version: 1.0.0
    - Build date: 26-May-2021
    
## Retail API Categories:
### Product Management APIs :
    1. Add Product
    2. Get All Products
    3. Get Single Product Configuration
    4. Update Single Product Configuration
### Order Management APIs :
    1. Add Order
    2. Get All Orders
    3. Get Orders For Customer ID
### Customer Management APIs :
    1. Add Customer
    2. Update Customer
    3. Get All Cusomers
    3. Get Single Cusomer Information


## How To Use :

Now the project is running at `http://localhost:8080` and your routes is:


| Route                                                      | HTTP Method       | Json Body                           	                                                        |
|:-----------------------------------------------------------|:------------------|:---------------------------------------------------------------------------------------------|
| /addProduct     	                                         | POST       	     | {"quantity":20,"productId":4,"price":75.0,"productName":"ProConnect 2"                       |
| /getAllProducts  	                                         | GET      	     |                                                                                              |
| /rpc/{productId}  	                                     | GET      	     |                                                                                              |
| /upc/{productId}                                           | POST        	     |{"quantity":20,"productId":4,"price":75.0,"productName":"ProConnect 2"                        |
| /addOrder                                                  | POST        	     |{"productQuantity":20,"productId":2,"orderId":9991,"customerId":12345,"productPrice":75.0}    |
| /getAllOrders                                              | GET        	     |                                                                                	            |
| /getAllOrders/{customerId}                                 | GET        	     |                                                                                 	            |


## Requirements

Building the API client library requires:
1. Java 1.7+
2. Maven/Gradle

## Installation

To install the API client library to your local Maven repository, simply execute:

```shell
mvn clean install
```

