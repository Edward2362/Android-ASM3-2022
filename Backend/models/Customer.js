const mongoose = require("mongoose");
const { Schema } = mongoose;

const Constants = require("../constants/Constants");

const customerSchema = new mongoose.Schema({
    email: {
      type: String,
      unique: true
    },
    password: {
      type: String
    },
    username: {
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
    cart: [{
      product: {
        type: Schema.Types.ObjectId,
        ref: "Book"
      },
      quantity: Number
    }],
    avatar: {
      type: String
    },
    token: {
      type: String
    }
});

const Customer = mongoose.model("Customer", customerSchema);

module.exports = Customer;