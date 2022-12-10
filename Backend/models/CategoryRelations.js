const mongoose = require("mongoose");
const { Schema } = mongoose;

const categoryRelationsSchema = new mongoose.Schema({
    category: {
      type: Schema.Types.ObjectId,
      ref: "Category"
    },
    subCategory: {
      type: Schema.Types.ObjectId,
      ref: "SubCategory"
    }
});

const CategoryRelations = mongoose.model("CategoryRelations", categoryRelationsSchema);

module.exports = CategoryRelations;