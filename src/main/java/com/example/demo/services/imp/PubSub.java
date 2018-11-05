package com.example.demo.services.imp;

import com.example.demo.controller.MessageController;
import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutures;
import com.google.cloud.ServiceOptions;
import com.google.cloud.pubsub.v1.AckReplyConsumer;
import com.google.cloud.pubsub.v1.MessageReceiver;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.cloud.pubsub.v1.Subscriber;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.ProjectSubscriptionName;
import com.google.pubsub.v1.ProjectTopicName;
import com.google.pubsub.v1.PubsubMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

@Component
public class PubSub {

    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    // use the default project id
    private static final String PROJECT_ID = ServiceOptions.getDefaultProjectId();


    @Value("${application.pubsub.subscription.upload}")
    private String subscriptionName;

    @Value("${application.pubsub.topic}")
    private String topic;

    private static final BlockingQueue<PubsubMessage> messages = new LinkedBlockingDeque<>();

    private static class MessageReceiverExample implements MessageReceiver {

        @Override
        public void receiveMessage(PubsubMessage message, AckReplyConsumer consumer) {
            messages.offer(message);
            consumer.ack();
        }
    }


    public void publish(String message) {
        ProjectTopicName topicName = ProjectTopicName.of(PROJECT_ID, topic);
        Publisher publisher = null;
        List<ApiFuture<String>> futures = new ArrayList<>();

        try {
            // Create a publisher instance with default settings bound to the topic
            publisher = Publisher.newBuilder(topicName).build();


            // convert message to bytes
            ByteString data = ByteString.copyFromUtf8(message);
            PubsubMessage pubsubMessage = PubsubMessage.newBuilder()
                    .setData(data)
                    .build();

            // Schedule a message to be published. Messages are automatically batched.
            ApiFuture<String> future = publisher.publish(pubsubMessage);
            futures.add(future);

            // Wait on any pending requests
            List<String> messageIds = ApiFutures.allAsList(futures).get();

            for (String messageId : messageIds) {
                logger.info("pushed the message with id:" + messageId);
            }

            if (publisher != null) {
                // When finished with the publisher, shutdown to free up resources.
                publisher.shutdown();
            }

        } catch (Exception ex) {
            logger.error("Error publishing the message");
            ex.printStackTrace();
        }
    }

    public String receive() {
        ProjectSubscriptionName subName = ProjectSubscriptionName.of(
                PROJECT_ID, subscriptionName);
        String data = null;
        try {
            // create a subscriber bound to the asynchronous message receiver
            Subscriber subscriber =
                    Subscriber.newBuilder(subName, new PubSub.MessageReceiverExample()).build();
            subscriber.startAsync().awaitRunning();
            // Continue to listen to messages
            PubsubMessage message = messages.take();
            logger.info("Message Id: " + message.getMessageId());
            data = message.getData().toStringUtf8();
            if (subscriber != null) {
                subscriber.stopAsync();
            }
        } catch (Exception ex) {
            logger.error("Error reading the message");
            ex.printStackTrace();
        }
        return data;
    }

}
