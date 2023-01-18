const mongoose = require("mongoose");
const { Schema } = mongoose;

const orderSchema = new mongoose.Schema({
    timeStamp: {
      type: String
    },
    status: {
      type: String
    },
    customer: {
      type: Schema.Types.ObjectId,
      ref: "Customer"
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