const express = require("express");
const router = express.Router();
const authMiddleware = require("../middleware/auth");
const bookController = require("../controllers/bookController");

router.post("/uploadBook", authMiddleware.verifyToken, bookController.uploadBook);
router.post("/updateBook", authMiddleware.verifyToken, bookController.updateBook);
router.post("/saveProduct", authMiddleware.verifyToken, bookController.saveProduct);

router.delete("/deleteBook/:id", authMiddleware.verifyToken, bookController.deleteBook);


router.get("/getProducts", bookController.getProducts);
router.get("/getProduct/:productId", bookController.getProduct);
router.get("/getUploadedProducts", authMiddleware.verifyToken, bookController.getUploadedProducts);

module.exports = router;