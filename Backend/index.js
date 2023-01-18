const express = require("express");
const bodyParser = require("body-parser");
const cors = require("cors");
require("dotenv").config();
const connectDB = require("./config/db");
const authRouter = require("./routes/auth");
const bookRouter = require("./routes/book");
const orderRouter = require("./routes/order");
const categoryRouter = require("./routes/category");
const subCategoryRouter = require("./routes/subCategory");
const notificationRouter = require("./routes/notification");
const reviewRouter = require("./routes/review");





const app = express();

app.use(bodyParser.json({limit: "50mb"}));
app.use(bodyParser.urlencoded({
    limit: '50mb',
    extended: true
  }));
app.use(cors());







connectDB();

app.use("/api/auth", authRouter);
app.use("/api/book", bookRouter);
app.use("/api/order", orderRouter);
app.use("/api/category", categoryRouter);
app.use("/api/subCategory", subCategoryRouter);
app.use("/api/notification", notificationRouter);
app.use("/api/review", reviewRouter);


app.listen(9000);