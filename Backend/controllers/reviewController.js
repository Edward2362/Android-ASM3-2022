const Review = require("../models/Review");

const Constants = require("../constants/Constants");

const uploadReview = async (req, response) => {
    try {
        const input = req.body;
        const customerId = req.customer.customerId;
        const newDate = new Date();
        const review = new Review({
            content: input.content,
            date: newDate.toDateString(),
            rating: input.rating,
            customer: customerId,
            order: input.orderId
        });

        await review.save();

        return response.json({
            message: "",
            error: false,
            data: [review]
          });
    } catch (error) {
        console.log(error);
        process.exit(1);
    }
};

const getReviews = async (req, response) => {
    const customerId = req.customer.customerId;
    const reviews = await Review.find({});
    let reviewArray = [];
    for (let i=0; i< reviews.length;i++){
        let review = reviews[i];
        if (review.order.seller === customerId) {
            reviewArray.push(review);
        }
    }
    return response.json({
        message: "",
        error: false,
        data: reviewArray
      }); 
};

const getAllReviews = async (req, response) => {
    const reviews = await Review.find({});
    return response.json({
        message: "",
        error: false,
        data: reviews
      }); 
};

module.exports = {
    uploadReview,
    getReviews,
    getAllReviews
}