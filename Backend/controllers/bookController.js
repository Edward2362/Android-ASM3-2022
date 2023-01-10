const Book = require("../models/Book");

const Customer = require("../models/Customer");
const Constants = require("../constants/Constants");

const uploadBook = async (req, response) => {
    const bookInput = {
      name: req.body.name,
      author: req.body.author,
      description: req.body.description,
      price: req.body.price,
      quantity: req.body.quantity,
      publishedAt: req.body.publishedAt,
      category: req.body.category,
      subCategory: req.body.subCategory,
      customer: req.customer.customerId
    };

    const book = await Book.create(bookInput);

    return response.json({
      message: "",
      error: false,
      data: [book]
    });
};

const updateBook = async (req, response) => {
    const books = await Book.find({
      _id: req.body._id
    });

    if (books.length == 0) {
      return response.json({
        message: "Error",
        error: true,
        data: []
      });
    }

    let bookInput = req.body;

    delete bookInput._id;

    const book = await Book.findOneAndUpdate({_id: books[0]._id}, {$set: bookInput}, {new: true});

    return response.json({
      message: "",
      error: false,
      data: [book]
    });
};

const deleteBook = (req, response) => {
    Book.deleteOne({_id: req.params.id}, (error, result) => {
        if (error) {
          return response.json({
            message: "Error",
            error: true,
            data: []
          });
        }

        return response.json({
          message: "",
          error: false,
          data: []
        });
    });
    
};


const getProducts = async (req, response) => {
  const input = req.query;

  let products = await Book.find({}).populate("subCategory").populate("category");

  if (input.subCategory === undefined && input.category === undefined) {
    return response.json({
      message: "",
      error: false,
      data: products
    });
  }

  let filteredProducts = [];

  
  
  let categories = [];
  if (input.category !== undefined) {
    if (typeof(input.category) === "string") {
      categories.push(input.category.replace(" ","+"));
    } else if (Array.isArray(input.category)) {
      for (let i=0;i<input.category.length;i++){
        categories.push(input.category[i].replace(" ","+"));
      }
    }
  }

  let subCategories = [];
  if (input.subCategory !== undefined) {
    if (typeof(input.subCategory) === "string") {
      subCategories.push(input.subCategory.replace(" ","+"));
    } else if (Array.isArray(input.subCategory)) {
      for (let i=0;i<input.subCategory.length;i++){
        subCategories.push(input.subCategory[i].replace(" ","+"));
      }
    }
  }

  if (categories.length !== 0 && subCategories.length !== 0) {
    for (let i = 0; i < products.length; ++i) {
      let product = products[i];

      if (categories.includes(product.category.name)) {
        if (subCategories.includes(product.subCategory.name)) {
          filteredProducts.push(product);
        }
      }
    }
  } else if (categories.length !== 0) {
    for (let i = 0; i < products.length; ++i) {
      let product = products[i];

      if (categories.includes(product.category.name)) {
        filteredProducts.push(product);
      }
    }
  } else if (subCategories.length !== 0) {
    for (let i = 0; i < products.length; ++i) {
      let product = products[i];

      if (subCategories.includes(product.subCategory.name)) {
        filteredProducts.push(product);
      }
    }
  }

  return response.json({
    message: "",
    error: false,
    data: filteredProducts
  });
};


const getUploadedProducts = async (req, response) => {
  try {
    const customerId = req.customer.customerId;

    const products = await Book.find({customer: customerId});

    return response.json({
      message: "",
      error: false,
      data: products
    });
  } catch(error) {
    console.log(error);
    process.exit(1);
  }
};

const getProduct = (req, response) => {
  const productId = req.params.productId;
  Book.find({_id: productId}, (error, books) => {
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
      data: books
    });
  });
};


const saveProduct = async (req, response) => {
  try {
    const customerId = req.customer.customerId;
    const input = req.body;

    const customers = await Customer.find({_id: customerId});
    customers[0].cart.push({
      product: input.product,
      quantity: input.quantity
    });

    return response.json({
      message: "",
      error: false,
      data: []
    });

  } catch(error) {
    console.log(error);
    process.exit(1);
  }
};



module.exports = {
  uploadBook,
  updateBook,
  deleteBook,
  getProducts,
  getProduct,
  getUploadedProducts,
  saveProduct
};