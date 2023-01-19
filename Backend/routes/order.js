const express = require("express");
const router = express.Router();
const authMiddleware = require("../middleware/auth");
const orderController = require("../controllers/orderController");

router.post("/orderProducts", authMiddleware.verifyToken, orderController.orderProducts);
router.get("/generateOrders", authMiddleware.verifyToken, orderController.generateOrders);


module.exports = router;