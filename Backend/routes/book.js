const express = require("express");
const router = express.Router();
const authMiddleware = require("../middleware/auth");
const bookController = require("../controllers/bookController");

router.post("/uploadBook", authMiddleware.verifyToken, bookController.uploadBook);

router.post("/updateBook", authMiddleware.verifyToken, bookController.updateBook);

router.delete("/deleteBook/:id", authMiddleware.verifyToken, bookController.deleteBook);




module.exports = router;