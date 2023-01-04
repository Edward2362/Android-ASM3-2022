const Order = require("../models/Order");
const Book = require("../models/Book");
const OrderDetail = require("../models/OrderDetail");
const Customer = require("../models/Customer");

const orderProducts = async (req, response) => {
    const customers = await Customer.find({_id: req.customer.customerId});

    let productIdArray = [];
    let bookArrayInput = [];
    let customerIdArray = [];
    let orders = [];
    
    
    bookArrayInput = bookArrayInput.concat(customers[0].cart);
    
    
    
    
    
    
    
    for (let i = 0; i < bookArrayInput.length; ++i) {
      let productId = {};
      productId._id = bookArrayInput[i]._id;

      productIdArray.push(productId);
    }

    const books = await Book.find({$or: productIdArray});

    
    
    for (let i = 0; i < books.length; ++i) {
      if (customerIdArray.findIndex((element) => {return element._id === books[i].customer;}) === -1) {
        let customerId = {};
        customerId._id = books[i].customer;
        customerIdArray.push(customerId);
      }
    }

    for (let i = 0; i < customerIdArray.length; ++i) {
      const order = new Order({
        seller: customerIdArray[i]._id,
        customer: req.customer.customerId,
        status: ""
      });

      await order.save();

      for (let index = 0; index < books.length; ++index) {
        if (customerIdArray[i]._id === books[index].customer) {
          const orderDetail = new OrderDetail({
            bookName: books[index].name,
            bookPrice: books[index].price,
            quantity: bookArrayInput[bookArrayInput.findIndex((element) => {return element._id === books[index]._id;})].quantity,
            order: order._id
          });

          await orderDetail.save();

          books[index].quantity = books[index].quantity - orderDetail.quantity;

          await books[index].save();
        }
      }
    
      orders.push(order);
    }

    customers[0].cart = [];
    await customers[0].save();
    
    return response.json({
      message: "",
      error: false,
      data: orders
    });
  };

module.exports = {
  orderProducts
};