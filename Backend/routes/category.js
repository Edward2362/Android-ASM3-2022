const express = require("express");
const router = express.Router();
const categoryController = require("../controllers/categoryController");

router.post("/uploadCategories", categoryController.initCategory);

router.get("/getAllCategories", categoryController.getCategories);







module.exports = router;