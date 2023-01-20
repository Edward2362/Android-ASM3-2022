const Review = require("../models/Review");
const Order = require("../models/Order");
const Constants = require("../constants/Constants");
const Customer = require("../models/Customer");

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
        const orders = await Order.find({_id: input.orderId});
        const sellerId = orders[0].seller;
        const customers = await Customer.find({_id: sellerId});
        const reviews = await Review.find({}).populate("order");
        let count = 0;
        let totalReviewRating = 0;
        for (let i=0; i < reviews.length; i++){
            if (reviews[0].order.seller.toString() === sellerId.toString()) { 
                count = count + 1;
                totalReviewRating = totalReviewRating + reviews[0].rating;
            }
        }
    
        customers[0].ratings = totalReviewRating/count;
        await customers[0].save();
        orders[0].hasReview = true;
        await orders[0].save();
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

const getAllCustomerReviews = async (req, response) => {
    const customerId = req.customer.customerId;
    const reviews = await Review.find({}).populate("order");
    let customerReviews = [];
    for (let i=0;i<reviews.length;i++){
        if (reviews[i].order.seller == customerId) {
            customerReviews.push(reviews[i]);
        }
    }
    return response.json({
        message: "",
        error: false,
        data: customerReviews
      }); 
};

module.exports = {
    uploadReview,
    getReviews,
    getAllReviews,
    getAllCustomerReviews
}