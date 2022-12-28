const mongoose = require("mongoose");
const { Schema } = mongoose;

const reviewSchema = new mongoose.Schema({
    content: {
        type: String,
    },
    date: {
        type: String,
    },
    rating: {
        type: Number,
    },
    customer: {
        type: Schema.Types.ObjectId,
        ref: "Customer"
    },
    order: {
        type: Schema.Types.ObjectId,
        ref: "Order"
    }
});


const Review = mongoose.model("Review", reviewSchema);


module.exports = Review;