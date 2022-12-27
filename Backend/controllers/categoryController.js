const Category = require("../models/Category");

const initCategory = async (req, response) => {
  const categories = ["Foreign+Book", "Domestic+Book", "Text+Book"];

  for (let i = 0; i < categories.length; ++i) {
    let category = new Category({
      name: categories[i]
    });

    await category.save();
  }

  return response.json({
    message: "",
    error: false,
    data: []
  });
};



const getCategories = (req, response) => {
  Category.find({}, (error, categories) => {
    if (error) {
      return response.json({
        message: "Error",
        error: true,
        data: []
      });
    }
    
    response.json({
      message: "",
      error: false,
      data: categories
    });
  });
};

module.exports = {
  initCategory,
  getCategories
};