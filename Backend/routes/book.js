const express = require("express");
const router = express.Router();
const authMiddleware = require("../middleware/auth");
const bookController = require("../controllers/bookController");

router.post("/uploadBook", authMiddleware.verifyToken, bookController.uploadBook);
router.post("/updateBook", authMiddleware.verifyToken, bookController.updateBook);
router.post("/saveProduct", authMiddleware.verifyToken, bookController.saveProduct);



router.delete("/removeCart/:productId", authMiddleware.verifyToken, bookController.removeCustomerCart);
router.delete("/deleteBook/:id", authMiddleware.verifyToken, bookController.deleteBook);


router.get("/getProducts", bookController.getProducts);
router.get("/getPublicCustomerProducts/:customerId", bookController.getPublicCustomerProducts);
router.get("/getProduct/:productId", bookController.getProduct);
router.get("/getUploadedProducts", authMiddleware.verifyToken, bookController.getUploadedProducts);
router.get("/suggestProduct", bookController.suggestProduct);
router.get("/searchProduct", bookController.searchProduct);
router.get("/getCart", authMiddleware.verifyToken, bookController.getCustomerCart);


module.exports = router;