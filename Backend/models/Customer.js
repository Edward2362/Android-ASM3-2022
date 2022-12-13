const mongoose = require("mongoose");

const Constants = require("../constants/Constants");

const customerSchema = new mongoose.Schema({
    username: {
      type: String,
      unique: true
    },
    password: {
      type: String
    },
    first_name: {
      type: String,
      default: ""
    },
    last_name: {
      type: String,
      default: ""
    },
    address: {
      type: String,
      default: ""
    },
    role: {
      type: String,
      default: Constants.CUSTOMER_ROLE
    },
    ratings: {
      type: Number,
      default: null
    },
    token: {
      type: String
    }
});

const Customer = mongoose.model("Customer", customerSchema);

module.exports = Customer;