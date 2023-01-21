const mongoose = require("mongoose");
const { Schema } = mongoose;

const categorySchema = new mongoose.Schema({
    name: {
      type: String
    },
    subCategories: [
      {
        type: Schema.Types.ObjectId,
        ref: "SubCategory"
      }
    ]
});

const Category = mongoose.model("Category", categorySchema);






module.exports = Category;