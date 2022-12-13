const mongoose = require("mongoose");
const { Schema } = mongoose;

const orderDetailSchema = new mongoose.Schema({
    bookName: {
      type: String
    },
    bookPrice: {
      type: Number
    },
    quantity: {
      type: Number
    },
    order: {
      type: Schema.Types.ObjectId,
      ref: "Order"
    }
});

const OrderDetail = mongoose.model("OrderDetail", orderDetailSchema);


module.exports = OrderDetail;