# Android-ASM3-2022
# Go-Goat

> An Android App use for buy/sell used/new books
![goat_logo](https://user-images.githubusercontent.com/57244454/213659108-cc25226a-3dfa-43f5-af75-347e007b2668.svg)


### Team Members
| Student Name            | Student ID  | Roles           | Contributions                                                   |
| ----------------------- | ----------  | --------------- |-----------------------------------------------------------------|
| Nguyen Tan Song Hao     | s3817884    | Back-end        | Account validation & register, Shopping Cart & Checkout page, CRUD for books (products), Filtering/Searching Products, Update (for seller) / check (for buyer) order status,Comment/Feedback for sellers,Hosting web api |
| Nguyen Vinh Quang       | s3817788    | Front-end       | Account validation & register, CRUD for books (products),Comment/Feedback for sellers,Design, theming, fonts|
| Ngo Van Dat             | s3817813    | Back-end        | Account setting, Shopping Cart & Checkout page, CRUD for books (products),Hosting web api      |
| Hoang Truc Hai          | s3818558    | Back-end        | Shopping Cart & Checkout page, Filtering/Searching Products, Update (for seller) / check (for buyer) order status,Comment/Feedback for sellers      |
| Tran Minh Anh           | s3931980    | Front-end       | Filtering/Searching Products,Update (for seller) / check (for buyer) order status,Comment/Feedback for sellers, Design, theming, fonts,Visuals|


### Functions
|Function Name            | Function Description                                                                |
|-------------------------|-------------------------------------------------------------------------------------|
|Login                    | Validate account with a username, and password.                                     |
|Register                 | Register with a username, email, and password.                                      |
|Search Book              |Filtering/Search products by authors, sellers, categories, and subcategories         |
|Post Book                | Add data using self-made web API                                                    |
|Update Book              | Update data using self-made web API                                                 |
|Delete Book              | Delete data using self-made web API                                                 | 
|Account Setting          | Allow user to change their account information and profile picture                  |
|Review Seller            | omment/Feedback for sellers: Allow users to comment and give feedback for the seller|
|Order Status             | Update (for seller) / check (for buyer) order status                                |
|Shopping Cart & Checkout |Display image and minimal product information (name, prices, quantity, sum of price, user delivery information), coupon, and purchased button|
### Technologies
|Front-End                | Back-End             | Database      | IDE            |Hosting services|
|-------------------------|----------------------|---------------|----------------|----------------|
|Java, XML                |Nodejs, Render        |MongoDB, Mongo Atlas|Android Studio, Visual Studio Code|Heroku|

### Web API
> We have create a Heroku hosting domain (https://go-goat-api-v1.herokuapp.com/) to connect to the server
> you can click on the sample below to perform a get all products GET method
[Sample](https://go-goat-api-v1.herokuapp.com/api/book/getProducts)
### Fail to delivery function
> Reset Password, Notifications
## ChangeLog

[CHANGELOG](CHANGELOG.md)

## License

Copyright (c) 2022-present
