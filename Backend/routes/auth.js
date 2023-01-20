const express = require("express");
const router = express.Router();
const authController = require("../controllers/authController");
const authMiddleware = require("../middleware/auth");

router.get("/getData", authMiddleware.verifyToken, authController.getCustomerData);
router.get("/getProfileCustomer/:customerId", authController.getProfileCustomer);


router.post("/register", authController.register);

router.post("/login", authController.login);



router.post("/setData", authMiddleware.verifyToken, authController.setCustomerData);
router.post("/changePassword", authMiddleware.verifyToken, authController.changePassword);
router.post("/changeAvatar", authMiddleware.verifyToken,authController.changeAvatar);
router.post("/increaseCartQuantity", authMiddleware.verifyToken,authController.increaseCartQuantity);
router.post("/decreaseCartQuantity", authMiddleware.verifyToken,authController.decreaseCartQuantity);




module.exports = router;