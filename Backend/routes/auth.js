const express = require("express");
const router = express.Router();
const authController = require("../controllers/authController");
const authMiddleware = require("../middleware/auth");

router.get("/getData", authMiddleware.verifyToken, authController.getCustomerData);

router.post("/register", authController.register);

router.post("/login", authController.login);




router.post("/setData", authMiddleware.verifyToken, authController.setCustomerData);
router.post("/changePassword", authMiddleware.verifyToken, authController.changePassword);




module.exports = router;