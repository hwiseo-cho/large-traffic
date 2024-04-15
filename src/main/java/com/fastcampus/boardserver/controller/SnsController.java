package com.fastcampus.boardserver.controller;

import com.fastcampus.boardserver.config.AWSConfig;
import com.fastcampus.boardserver.service.SnsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.*;

import java.util.Map;

@RestController
@Log4j2
@RequiredArgsConstructor
public class SnsController {

    private final AWSConfig awsConfig;
    private final SnsService snsService;

    @PostMapping("/create-topic")
    public ResponseEntity<String> createTopic(@RequestParam final String topicName) {
        final CreateTopicRequest createTopicRequest = CreateTopicRequest.builder()
                .name(topicName)
                .build();

        SnsClient snsClient = snsService.getSsnsClient();
        final CreateTopicResponse createTopicResponse = snsClient.createTopic(createTopicRequest);

        if(!createTopicResponse.sdkHttpResponse().isSuccessful()) {
            throw getResponseStatusException(createTopicResponse);
        }

        log.info("Topic name: " + createTopicResponse.topicArn());
        snsClient.close();

        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    @PostMapping("/subscribe")
    public ResponseEntity<String> subscribe(@RequestParam final String endpoint, @RequestParam final String topicArn) {
        final SubscribeRequest subscribeRequest = SubscribeRequest.builder()
                .protocol("Https")
                .topicArn(topicArn)
                .endpoint(endpoint)
                .build();

        SnsClient snsClient = snsService.getSsnsClient();
        final SubscribeResponse subscribeResponse = snsClient.subscribe(subscribeRequest);

        if(!subscribeResponse.sdkHttpResponse().isSuccessful()) {
            throw getResponseStatusException(subscribeResponse);
        }

        log.info("subscribe name: " + subscribeResponse.subscriptionArn());
        snsClient.close();

        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    @PostMapping("/publish")
    public String pulish(@RequestParam String topicArn, @RequestBody Map<String, Object> message) {
        SnsClient snsClient = snsService.getSsnsClient();
        final PublishRequest publishRequest = PublishRequest.builder()
                .topicArn(topicArn)
                .subject("HTTP TEST")
                .message(message.toString())
                .build();

        PublishResponse publishResponse = snsClient.publish(publishRequest);
        snsClient.close();
        return publishResponse.messageId();
    }


    private ResponseStatusException getResponseStatusException(SnsResponse snsResponse) {
        return new ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR, snsResponse.sdkHttpResponse().statusText().get()
        );
    }
}
