package com.otbs.model;

public class Feedback {

    private int feedbackId;
    private String feedbackText;
    private int rating;

    // Default constructor
    public Feedback() {}

    // Constructor with parameters
    public Feedback(int feedbackId, String feedbackText, int rating) {
        this.feedbackId = feedbackId;
        this.feedbackText = feedbackText;
        this.rating = rating;
    }

    // Getters and Setters
    public int getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(int feedbackId) {
        this.feedbackId = feedbackId;
    }

    public String getFeedbackText() {
        return feedbackText;
    }

    public void setFeedbackText(String feedbackText) {
        this.feedbackText = feedbackText;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
