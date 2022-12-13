const Customer = require("../models/Customer");

const bcryptjs = require("bcryptjs");
const jwt = require("jsonwebtoken");

const Constants = require("../constants/Constants");

const register = async (req, response) => {
    try {
      if (req.body.username === undefined || req.body.password === undefined) {
        response.json({
          message: "Error"
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
        response.json({
          message: "Error"
        });
      }

      let encryptedPassword = await bcryptjs.hash(req.body.password, 10);
      customerInput.password = encryptedPassword;

      const customer = await Customer.create(customerInput);

      response.json(customer);
    } catch(error) {
      console.log(error);
      process.exit(1);
    }
};

const login = async (req, response) => {
    try {
      const customerInput = req.body;
      if (customerInput.username === undefined || customerInput.password === undefined) {
        response.json({
          message: "Error"
        });
      }

      const customers = await Customer.find({username: customerInput.username});

      if (customers.length == 0) {
        response.json({
          message: "Error"
        });
      }

      const customer = customers[0];

      if (!(await bcryptjs.compare(customerInput.password, customer.password))) {
        response.json({
          message: "Error"
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

      response.json(customer);
    } catch(error) {
      console.log(error);
      process.exit(1);
    }
};

module.exports = {
  register,
  login
};