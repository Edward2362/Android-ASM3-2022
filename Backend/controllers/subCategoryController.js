const SubCategory = require("../models/SubCategory");
const Category = require("../models/Category");

const initSubCategories = async (req, response) => {
  const subCategories = ["Children", "Young+adult", "Fiction","Nonfiction","Economic","Dictionary","Skill","Poetry","Literature","Math","Science","Physics","Chemistry","Biology","Ethics","Geography","History","Foreign+Languages","Computing"];

  for (let i = 0; i < subCategories.length; ++i) {
    let subCategory = new SubCategory({
      name: subCategories[i]
    });

    await subCategory.save();
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
    let categoryName = input.category.replace(" ","+");

    let categories = await Category.find({name: categoryName}).populate("subCategories");

    if (categories.length !== 0) {
      subCategories = categories[0].subCategories;
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