const express = require("express");
const bodyParser = require("body-parser");
const cors = require("cors");
require("dotenv").config();
const connectDB = require("./config/db");
const authRouter = require("./routes/auth");
const bookRouter = require("./routes/book");
const orderRouter = require("./routes/order");

const app = express();

app.use(bodyParser.json());
app.use(cors());



connectDB();

app.use("/api/auth", authRouter);
app.use("/api/book", bookRouter);
app.use("/api/order", orderRouter);

app.listen(9000);