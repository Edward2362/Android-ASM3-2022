const Notification = require("../models/Notification");

const getNotification = (req, response) => {
    const notificationId = req.params.notificationId;
    Notification.find({_id: notificationId}, (error, notifications) => {
        if (error) {
            return response.json({
                message: "Error",
                error: true,
                data: []
            });
        }

        response.json({
            message: "",
            error: false,
            data: notifications
        });
    });
};


const uploadNotification = async (req, response) => {
    const input = req.body;
    const notification = new Notification(input);

    await notification.save();

    return response.json({
        message: "",
        error: false,
        data: [notification]
    });
};

const updateNotification = (req, response) => {
    const notificationId = req.params.notificationId;
    Notification.findOneAndUpdate({_id: notificationId}, {$set: {isRead: true}}, {new: true}, (error, notification) => {
        if (error) {
            return response.json({
                message: "Error",
                error: true,
                data: []
            });
        }

        response.json({
            message: "",
            error: false,
            data: [notification]
        });
    });
};





const deleteNotification = (req, response) => {
    const notificationId = req.params.notificationId;
    Notification.deleteOne({_id: notificationId}, (error, notification) => {
        if (error) {
            return response.json({
                message: "Error",
                error: true,
                data: []
            });
        }

        response.json({
            message: "",
            error: false,
            data: [notification]
        });
    });
};

const getNotifications = (req, response) => {
    const customerId = req.customer.customerId;
    Notification.find({customer: customerId}, (error, notifications) => {
        if (error) {
            return response.json({
                message: "Error",
                error: true,
                data: []
            });
        }

        response.json({
            message: "",
            error: false,
            data: notifications
        });
    });
};


module.exports = {
    getNotification,
    uploadNotification,
    updateNotification,
    deleteNotification,
    getNotifications
};