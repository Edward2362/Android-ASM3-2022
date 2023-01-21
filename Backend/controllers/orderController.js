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

  const getCustomerOrders = async (req, response) => {
    try {
      const customerId = req.customer.customerId;

      const orders = await Order.find({buyer: customerId}).populate("seller");

      return response.json({
        message: "",
        error: false,
        data: orders
      });
    } catch(error) {
      console.log(error);
      process.exit(1);
    }
  };

  const getSellingOrders = async (req, response) => {
    try {
      const customerId = req.customer.customerId;

      const orders = await Order.find({seller: customerId}).populate("buyer");

      return response.json({
        message: "",
        error: false,
        data: orders
      });
    } catch(error) {
      console.log(error);
      process.exit(1);
    }
  };

  const updateStatusOrder = async (req, response) => {
    try {
      const customerId = req.customer.customerId;
      const input = req.body;
      const orderData = {
        status: input.status
      };
      const order = await Order.findOneAndUpdate({_id: input.orderId},{$set: orderData},{new: true});
      if (order === undefined || order === null) {
        return response.json({
          message: "Error",
          error: true,
          data: []
        });
      }

      return response.json({
        message: "",
        error: false,
        data: [order]
      });
    } catch(error) {
      console.log(error);
      process.exit(1);
    }
  };



module.exports = {
  orderProducts,
  generateOrders,
  getCustomerOrders,
  getSellingOrders,
  updateStatusOrder
};