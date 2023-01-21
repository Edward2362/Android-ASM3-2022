const express = require("express");
const router = express.Router();
const authMiddleware = require("../middleware/auth");
const orderController = require("../controllers/orderController");

router.post("/orderProducts", authMiddleware.verifyToken, orderController.orderProducts);
router.post("/updateStatusOrder", authMiddleware.verifyToken, orderController.updateStatusOrder);

router.get("/generateOrders", authMiddleware.verifyToken, orderController.generateOrders);
router.get("/getCustomerOrders", authMiddleware.verifyToken, orderController.getCustomerOrders);
router.get("/getSellingOrders", authMiddleware.verifyToken, orderController.getSellingOrders);






module.exports = router;