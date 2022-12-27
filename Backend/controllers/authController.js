const Customer = require("../models/Customer");

const bcryptjs = require("bcryptjs");
const jwt = require("jsonwebtoken");

const Constants = require("../constants/Constants");

const register = async (req, response) => {
    try {
      if (req.body.email === undefined || req.body.password === undefined) {
        return response.json({
          message: "Error",
          error: true,
          data: []
        });
      }

      
      if (req.body.email === "" || req.body.password === "") {
        return response.json({
          message: "Error",
          error: true,
          data: []
        });
      }
      
      const customerInput = {
        email: req.body.email,
        password: req.body.password,
        username: req.body.username,
        address: req.body.address,
        role: Constants.CUSTOMER_ROLE,
        ratings: 0
      };

      const customers = await Customer.find({
        email: req.body.email
      });

      if (customers.length != 0) {
        return response.json({
          message: "Error",
          error: true,
          data: []
        });
      }

      let encryptedPassword = await bcryptjs.hash(req.body.password, 10);
      customerInput.password = encryptedPassword;

      const customer = await Customer.create(customerInput);

      return response.json({
        message: "",
        error: false,
        data: [customer]
      });
    } catch(error) {
      console.log(error);
      process.exit(1);
    }
};

const login = async (req, response) => {
    try {
      const customerInput = req.body;
      if (customerInput.email === undefined || customerInput.password === undefined) {
        return response.json({
          message: "Error",
          error: true,
          data: []
        });
      }

      const customers = await Customer.find({email: customerInput.email});

      if (customers.length == 0) {
        return response.json({
          message: "Error",
          error: true,
          data: []
        });
      }

      const customer = customers[0];

      if (!(await bcryptjs.compare(customerInput.password, customer.password))) {
        return response.json({
          message: "Error",
          error: true,
          data: []
        });
      }

      const token = jwt.sign({
        customerId: customer._id,
        email: customer.email
      },
      process.env.TOKEN_KEY,
      {
        expiresIn: "2h"
      });
      
      customer.token = token;
      return response.json({
        message: "",
        error: false,
        data: [customer]
      });
    } catch(error) {
      console.log(error);
      process.exit(1);
    }
};

const getCustomerData = async (req, response) => {
    try {
      const customers = await Customer.find({_id: req.customer.customerId});

      if (customers.length == 0) {
        return response.json({
          message: "Error",
          error: true,
          data: []
        });
      }

      return response.json({
        message: "",
        error: false,
        data: customers
      });
    } catch(error) {
      console.log(error);
      process.exit(1);
    }
};





const setCustomerData = async (req, response) => {
  try {
    const input = req.body;
    const customerId = req.customer.customerId;
    const customerData = {
      username: input.username,
      address: input.address,
      ratings: input.ratings
    };

    const customer = await Customer.findOneAndUpdate({_id: customerId}, {$set: customerData}, {new: true});

    if (customer === undefined || customer === null) {
      return response.json({
        message: "Error",
        error: true,
        data: []
      });
    }

    return response.json({
      message: "",
      error: false,
      data: [customer]
    });
  } catch(error) {
    console.log(error);
    process.exit(1);
  }
};

module.exports = {
  register,
  login,
  getCustomerData,
  setCustomerData
};