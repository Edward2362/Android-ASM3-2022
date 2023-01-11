const Category = require("../models/Category");
const SubCategory = require("../models/SubCategory");

const initCategory = async (req, response) => {
  const categories = [{
      categoryName: "Foreign+Book",
      subCategories: ["Children", "Young+adult", "Fiction","Nonfiction","Economic","Dictionary","Skill","Poetry"]
    },
    {
      categoryName: "Domestic+Book",
      subCategories: ["Children", "Young+adult", "Fiction","Nonfiction","Economic","Dictionary","Skill","Poetry"]
    },
    {
      categoryName: "Text+Book",
      subCategories: ["Literature","Math","Science","Physics","Chemistry","Biology","Ethics","Geography","History","Foreign+Languages","Computing"]
    }
  ];

  const subCategories = await SubCategory.find({});

  
  
  for (let i = 0; i < categories.length; ++i) {
    let subCategoryIds = [];

    for (let index = 0; index < categories[i].subCategories.length; ++index) {
      let subCategoryIndex = subCategories.findIndex((element) => {return element.name === categories[i].subCategories[index];});
      
      if (subCategoryIndex !== -1) {
        subCategoryIds.push(subCategories[subCategoryIndex]._id);
      }
    }
    
    let category = new Category({
      name: categories[i].categoryName,
      subCategories: subCategoryIds
    });

    await category.save();
  }

  return response.json({
    message: "",
    error: false,
    data: []
  });
};





const getCategories = async (req, response) => {
  try {
    const categories = await Category.find({}).populate("subCategories");

    return response.json({
      message: "",
      error: false,
      data: categories
    });
  } catch(error) {
    console.log(error);
    process.exit(1);
  }
};







module.exports = {
  initCategory,
  getCategories
};