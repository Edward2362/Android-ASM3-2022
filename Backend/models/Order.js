const mongoose = require("mongoose");
const { Schema } = mongoose;

const orderSchema = new mongoose.Schema({
    timeStamp: {
      type: Date,
      default: Date.now()
    },
    seller: {
      type: Schema.Types.ObjectId,
      ref: "Customer"
    },
    status: {
      type: String
    },
    customer: {
      type: Schema.Types.ObjectId,
      ref: "Customer"
    }
});

const Order = mongoose.model("Order", orderSchema);

module.exports = Order;