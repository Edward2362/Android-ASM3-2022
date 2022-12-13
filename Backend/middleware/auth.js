const jwt = require("jsonwebtoken");

const verifyToken = (req, response, next) => {
  const token = req.body.token || req.query.token || req.headers["x-access-token"];

  if (token === undefined) {
    return response.json({
      message: "Error"
    });
  }

  try {
    const decoded = jwt.verify(token, process.env.TOKEN_KEY);
    req.customer = decoded;
    return next();
  } catch(error) {
    return response.json({
      message: "Wrong token"
    });
  }
};

module.exports = {
  verifyToken
};