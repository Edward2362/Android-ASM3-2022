const Book = require("../models/Book");

const Customer = require("../models/Customer");
const Constants = require("../constants/Constants");

const {Types} = require("mongoose");

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
		customer: req.customer.customerId,
		isNewProduct: req.body.isNewProduct,
		image: req.body.image,
	};

	const book = await Book(bookInput);

	await book.save();

	return response.json({
		message: "",
		error: false,
		data: [book],
	});
};

const updateBook = async (req, response) => {
	const books = await Book.find({
		_id: req.body._id,
	});

	if (books.length === 0) {
		return response.json({
			message: "Error",
			error: true,
			data: [],
		});
	}

	let bookInput = req.body;

	delete bookInput._id;

	const book = await Book.findOneAndUpdate(
		{ _id: books[0]._id },
		{ $set: bookInput },
		{ new: true }
	);

	return response.json({
		message: "",
		error: false,
		data: [book],
	});
};

const deleteBook = async (req, response) => {
	if (req.params.id === undefined || req.params.id === "") {
		return response.json({
			message: "Error",
			error: true,
			data: [],
		});
	}
	let books = await Book.find({
		_id: req.params.id,
	});
	if (books.length === 0) {
		return response.json({
			message: "Error",
			error: true,
			data: [],
		});
	}
	let customers = await Customer.find({
		cart: { $elemMatch: { product: req.params.id } },
	});
	for (let i = 0; i < customers.length; i++) {
		let cart = [];
		let productIndex = -1;
		cart = cart.concat(customers[i].cart);
		for (let j = 0; j < cart.length; j++) {
			if (cart[j].product.toString() === req.params.id) {
				productIndex = j;
			}
		}
		customers[i].cart.splice(productIndex, 1);
		await customers[i].save();
	}
	Book.deleteOne({ _id: req.params.id }, (error, result) => {
		if (error) {
			return response.json({
				message: "Error",
				error: true,
				data: [],
			});
		}

		return response.json({
			message: "",
			error: false,
			data: [],
		});
	});
};

const getProducts = async (req, response) => {
	const input = req.query;

	let products = await Book.find({}).populate("category").populate("subCategory").populate("customer");

	if (input.subCategory === undefined && input.category === undefined) {
		return response.json({
			message: "",
			error: false,
			data: products,
		});
	}

	let filteredProducts = [];

	let categories = [];
	if (input.category !== undefined) {
		if (typeof input.category === "string") {
			categories.push(input.category.replace(" ", "+"));
		} else if (Array.isArray(input.category)) {
			for (let i = 0; i < input.category.length; i++) {
				categories.push(input.category[i].replace(" ", "+"));
			}
		}
	}

	let subCategories = [];
	if (input.subCategory !== undefined) {
		if (typeof input.subCategory === "string") {
			subCategories.push(input.subCategory.replace(" ", "+"));
		} else if (Array.isArray(input.subCategory)) {
			for (let i = 0; i < input.subCategory.length; i++) {
				subCategories.push(input.subCategory[i].replace(" ", "+"));
			}
		}
	}

	if (categories.length !== 0 && subCategories.length !== 0) {
		for (let i = 0; i < products.length; ++i) {
			let product = products[i];

			if (categories.includes(product.category.name)) {
				for (let j=0;j<products[i].subCategory.length;j++) {
					if (subCategories.includes(products[i].subCategory[j].name)) {
						filteredProducts.push(product);
						break;
					}
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
			for (let j=0;j<products[i].subCategory.length;j++) {
				if (subCategories.includes(products[i].subCategory[j].name)) {
					filteredProducts.push(product);
					break;
				}
			}
		}
	}

	return response.json({
		message: "",
		error: false,
		data: filteredProducts,
	});
};

const getUploadedProducts = async (req, response) => {
	try {
		const customerId = req.customer.customerId;

		const products = await Book.find({ customer: customerId });

		return response.json({
			message: "",
			error: false,
			data: products,
		});
	} catch (error) {
		console.log(error);
		process.exit(1);
	}
};

const getPublicCustomerProducts = async (req, response) => {
	try {
		const customerId = req.params.customerId;

		const products = await Book.find({ customer: customerId });

		return response.json({
			message: "",
			error: false,
			data: products
		});
	} catch (error) {
		console.log(error);
		process.exit(1);
	}
};

const getProduct = async (req, response) => {
	try {
		const productId = req.params.productId;
		const products = await Book.find({ _id: productId }).populate("customer");

		if (products.length === 0) {
			return response.json({
				message: "Error",
				error: true,
				data: products,
			});
		}

		return response.json({
			message: "",
			error: false,
			data: products,
		});
	} catch (error) {
		console.log(error);
		process.exit(1);
	}
};

const saveProduct = async (req, response) => {
	try {
		const customerId = req.customer.customerId;
		const input = req.body;

		const customers = await Customer.find({ _id: customerId });
		if (
			customers[0].cart.findIndex((element) => {
				return input.product == element.product.toString();
			}) === -1
		) {
			customers[0].cart.push({
				product: Types.ObjectId(input.product),
				quantity: input.quantity,
			});
			await customers[0].save();
		} else {
			return response.json({
				message: "",
				error: false,
				data: [],
			});
		}
		return response.json({
			message: "",
			error: false,
			data: [],
		});
	} catch (error) {
		console.log(error);
		process.exit(1);
	}
};

const suggestProduct = async (req, response) => {
	try {
		const input = req.query;
		if (input.queryInput === undefined || input.queryInput === "") {
			return response.json({
				message: "Error",
				error: true,
				data: [],
			});
		}
		let products = await Book.find({ name: { $regex: new RegExp(input.queryInput.toLowerCase(), "i")} });
		products = removeDuplicate(products);
		return response.json({
			message: "",
			error: false,
			data: products,
		});
	} catch (error) {
		console.log(error);
		process.exit(1);
	}
};

const searchProduct = async (req, response) => {
	try {
		const input = req.query;
		if (input.queryInput === undefined || input.queryInput === "") {
			return response.json({
				message: "Error",
				error: true,
				data: [],
			});
		}
		let products = await Book.find({ name: { $regex: new RegExp(input.queryInput.toLowerCase(), "i") } }).populate("customer");
		return response.json({
			message: "",
			error: false,
			data: products,
		});
	} catch (error) {
		console.log(error);
		process.exit(1);
	}
};

const getCustomerCart = async (req, response) => {
	try {
		const customerId = req.customer.customerId;
		const customers = await Customer.find({
			_id: customerId,
		}).populate("cart.product");
		return response.json({
			message: "",
			error: false,
			data: customers[0].cart,
		});
	} catch (error) {
		console.log(error);
		process.exit(1);
	}
};

const removeCustomerCart = async (req, response) => {
	try {
		const customerId = req.customer.customerId;
		const productId = req.params.productId;
		if (productId === undefined || productId === "") {
			return response.json({
				message: "Error",
				error: true,
				data: [],
			});
		}
		const customers = await Customer.find({
			_id: customerId,
		}).populate("cart.product");
		let index = customers[0].cart.findIndex((element) => {
			return productId == element.product._id.toString();
		});
		if (index === -1) {
			return response.json({
				message: "Error",
				error: true,
				data: [],
			});
		}
		customers[0].cart.splice(index, 1);
		await customers[0].save();

		return response.json({
			message: "",
			error: false,
			data: customers[0].cart,
		});
	} catch (error) {
		console.log(error);
		process.exit(1);
	}
};

const removeDuplicate = (array) => {
	let products = [];
	for (let i = 0; i < array.length; i++) {
		if (
			products.findIndex((element) => {
				return element.name === array[i].name;
			}) === -1
		) {
			products.push(array[i]);
		}
	}
	return products;
};


module.exports = {
	uploadBook,
	updateBook,
	deleteBook,
	getProducts,
	getProduct,
	getUploadedProducts,
	saveProduct,
	suggestProduct,
	searchProduct,
	getCustomerCart,
	removeCustomerCart,
	getPublicCustomerProducts
};
