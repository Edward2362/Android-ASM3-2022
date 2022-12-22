const Customer = require("../models/Customer");

const bcryptjs = require("bcryptjs");
const jwt = require("jsonwebtoken");

const Constants = require("../constants/Constants");

const register = async (req, response) => {
    try {
      if (req.body.username === undefined || req.body.password === undefined) {
        return response.json({
          message: "Error",
          error: true,
          data: []
        });
      }

      
      if (req.body.username === "" || req.body.password === "") {
        return response.json({
          message: "Error",
          error: true,
          data: []
        });
      }
      
      const customerInput = {
        username: req.body.username,
        password: req.body.password,
        first_name: req.body.first_name,
        last_name: req.body.last_name,
        address: req.body.address,
        role: Constants.CUSTOMER_ROLE,
        ratings: 0
      };

      const customers = await Customer.find({
        username: req.body.username
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
      if (customerInput.username === undefined || customerInput.password === undefined) {
        return response.json({
          message: "Error",
          error: true,
          data: []
        });
      }

      const customers = await Customer.find({username: customerInput.username});

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
        username: customer.username
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

module.exports = {
  register,
  login,
  getCustomerData
};