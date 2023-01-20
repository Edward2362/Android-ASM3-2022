const express = require("express");
const router = express.Router();
const authMiddleware = require("../middleware/auth");
const reviewController = require("../controllers/reviewController");

router.post("/uploadReview", authMiddleware.verifyToken, reviewController.uploadReview);
router.get("/getReviews", authMiddleware.verifyToken, reviewController.getReviews);
router.get("/getAllReviews", reviewController.getAllReviews);
router.get("/getAllCustomerReviews", authMiddleware.verifyToken, reviewController.getAllCustomerReviews);




module.exports = router;