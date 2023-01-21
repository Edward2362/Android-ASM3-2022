const mongoose = require("mongoose");
const { Schema } = mongoose;

const notificationSchema = new mongoose.Schema({
    customer: {
        type: Schema.Types.ObjectId,
        ref: "Customer"
    },
    content: {
        type: String
    },
    isRead: {
        type: Boolean,
        default: false
    },
    timestamp: {
        type: String
    }
});


const Notification = mongoose.model("Notification", notificationSchema);


module.exports = Notification;