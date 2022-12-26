const express = require("express");
const router = express.Router();
const notificationController = require("../controllers/notificationController");
const authMiddleware = require("../middleware/auth");

router.get("/getNotification/:notificationId", authMiddleware.verifyToken, notificationController.getNotification);
router.post("/uploadNotification", authMiddleware.verifyToken, notificationController.uploadNotification);
router.post("/updateNotification/:notificationId", authMiddleware.verifyToken, notificationController.updateNotification);
router.delete("/deleteNotification/:notificationId", authMiddleware.verifyToken, notificationController.deleteNotification);
router.get("/getNotifications/:customerId", authMiddleware.verifyToken, notificationController.getNotifications);




module.exports = router;