const Book = require("../models/Book");
const Constants = require("../constants/Constants");

const uploadBook = async (req, response) => {
    const bookInput = {
      name: req.body.name,
      author: req.body.author,
      description: req.body.description,
      price: req.body.price,
      quantity: req.body.quantity,
      publishedAt: req.body.publishedAt,
      createdAt: Date.now(),
      category: req.body.category,
      customer: req.customer.customerId
    };

    const book = await Book.create(bookInput);

    response.json(book);
};


const updateBook = async (req, response) => {
    const books = await Book.find({
      _id: req.body._id
    });

    if (books.length == 0) {
      response.json({
        message: "Error"
      });
    }

    let bookInput = req.body;

    delete bookInput._id;

    const book = await Book.findOneAndUpdate({_id: books[0]._id}, {$set: bookInput}, {new: true});

    response.json(book);
};

const deleteBook = (req, response) => {
    Book.deleteOne({_id: req.params.id}, (error, result) => {
        if (error) {
          response.json({
            message: "Error"
          });
        }

        response.json(result);
    });
};

module.exports = {
  uploadBook,
  updateBook,
  deleteBook
};