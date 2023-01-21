const mongoose = require("mongoose");
const { Schema } = mongoose;

const orderSchema = new mongoose.Schema({
    timeStamp: {
      type: String
    },
    status: {
      type: String
    },
    buyer: {
      type: Schema.Types.ObjectId,
      ref: "Customer"
    },
    seller: {
      type: Schema.Types.ObjectId,
      ref: "Customer"
    },
    bookImage: {
      type: String
    },
    bookName: {
      type: String
    },
    bookPrice: {
      type: Number
    },
    quantity: {
      type: Number
    },
    hasReview: {
      type: Boolean
    }
});

const Order = mongoose.model("Order", orderSchema);

module.exports = Order;