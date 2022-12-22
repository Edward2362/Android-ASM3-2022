const express = require("express");
const router = express.Router();
const subCategoryController = require("../controllers/subCategoryController");

router.post("/uploadSubCategories", subCategoryController.initSubCategories);

router.get("/getSubCategories", subCategoryController.getSubCategories);







module.exports = router;