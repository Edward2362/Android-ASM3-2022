const Review = require("../models/Review");

const Constants = require("../constants/Constants");

const uploadReview = async (req, response) => {
    try {
        const input = req.body;
        const customerId = req.customer.customerId;
        const review = new Review({
            content: input.content,
            date: input.content,
            rating: input.content,
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

module.exports = {
    uploadReview,
    getReviews
}