const Order = require("../models/Order");

const orderProducts = async (req, response) => {
    
    
    return response.json({
      message: "",
      error: false,
      data: []
    });
  };

module.exports = {
  orderProducts
};