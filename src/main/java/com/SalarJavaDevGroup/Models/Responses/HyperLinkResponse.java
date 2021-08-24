package com.SalarJavaDevGroup.Models.Responses;

import com.SalarJavaDevGroup.Models.HyperLinkType;

public class HyperLinkResponse {
    HyperLinkType hyperLinkType;
    TweetResponse tweetResponse;
    ConversationResponse conversationResponse;

    public HyperLinkResponse(TweetResponse tweetResponse) {
        this.tweetResponse = tweetResponse;
        hyperLinkType = HyperLinkType.Tweet;
    }

    public HyperLinkResponse(ConversationResponse conversationResponse) {
        this.conversationResponse = conversationResponse;
        hyperLinkType = HyperLinkType.Conversation;
    }

    public HyperLinkResponse(HyperLinkType hyperLinkType) {
        this.hyperLinkType = hyperLinkType;
    }
}
