const SubCategory = require("../models/SubCategory");
const CategoryRelations = require("../models/CategoryRelations");
const Category = require("../models/Category");

const initSubCategories = async (req, response) => {
  const subCategories = ["Novel", "Children+Book", "Comics"];

  const categories = await Category.find({$or: [{name: "Foreign+Book"}, {name: "Domestic+Book"}]});

  for (let i = 0; i < subCategories.length; ++i) {
    let subCategory = new SubCategory({
      name: subCategories[i]
    });

    await subCategory.save();

    for (let index = 0; index < categories.length; ++index) {
      let categoryRelation = new CategoryRelations({
        category: categories[index]._id,
        subCategory: subCategory._id
      });

      await categoryRelation.save();
    }
  }

  return response.json({
    message: "",
    error: false,
    data: []
  });
};

const getSubCategories = async (req, response) => {
  const input = req.query;
  let subCategories = [];

  if (input.category === undefined) {
    subCategories = await SubCategory.find({});
  } else {
    let categoryRelations = await CategoryRelations.find({}).populate("category").populate("subCategory");
    let categoryName = input.category.replace(" ","+");

    for (let i = 0; i < categoryRelations.length; ++i) {
      let categoryRelation = categoryRelations[i];

      if (categoryRelation.category.name === categoryName) {
        subCategories.push(categoryRelation.subCategory);
      }
    }
  }

  
  
  return response.json({
    message: "",
    error: false,
    data: subCategories
  });
};

module.exports = {
  initSubCategories,
  getSubCategories
};