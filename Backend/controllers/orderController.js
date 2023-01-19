const Order = require("../models/Order");
const Customer = require("../models/Customer");

const orderProducts = async (req, response) => {

  const customerId = req.customer.customerId;
  const customers = await Customer.find({
    _id: customerId
  }).populate("cart.product");
  let orders = [];
  for (let i=0; i<customers[0].cart.length; i++){
    const product = customers[0].cart[i].product;
    product.quantity = product.quantity - customers[0].cart[i].quantity;
    await product.save();
    let timeStamp = new Date();
    const order = new Order({
      timeStamp: timeStamp.toString(),
      status: "Packaging",
      buyer: customerId,
      seller: product.customer,
      bookImage: product.image,
      bookName: product.name,
      bookPrice: product.price,
      quantity: customers[0].cart[i].quantity,
      hasReview: false
    });
    await order.save();
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

  const generateOrders = async (req, response) => {

    const customerId = req.customer.customerId;
    const customers = await Customer.find({
      _id: customerId
    }).populate("cart.product");
    let orders = [];
    for (let i=0; i<customers[0].cart.length; i++){
      const product = customers[0].cart[i].product;
      let timeStamp = new Date();
      const order = new Order({
        timeStamp: timeStamp.toString(),
        status: "Packaging",
        buyer: customerId,
        seller: product.customer,
        bookImage: product.image,
        bookName: product.name,
        bookPrice: product.price,
        quantity: customers[0].cart[i].quantity,
        hasReview: false
      });
      orders.push(order);
    }

      return response.json({
        message: "",
        error: false,
        data: orders
      });
    };
module.exports = {
  orderProducts,
  generateOrders
};