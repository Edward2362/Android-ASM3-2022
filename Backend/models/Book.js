const mongoose = require("mongoose");

const { Schema } = mongoose;

const bookSchema = new mongoose.Schema({
    name: {
      type: String
    },
    author: {
      type: String
    },
    description: {
      type: String
    },
    price: {
      type: Number
    },
    quantity: {
      type: Number,
      default: 0
    },
    publishedAt: {
      type: String,
    },
    category: {
      type: Schema.Types.ObjectId,
      ref: "Category"
    },
    subCategory: [{
      type: Schema.Types.ObjectId,
      ref: "SubCategory"
    }],
    customer: {
      type: Schema.Types.ObjectId,
      ref: "Customer"
    },
    isNewProduct: {
      type: Boolean,
    },
    image: {
      type: String
    }
});


const Book = mongoose.model("Book", bookSchema);




module.exports = Book;