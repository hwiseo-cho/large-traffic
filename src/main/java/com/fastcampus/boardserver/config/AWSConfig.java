package com.fastcampus.boardserver.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class AWSConfig {

    @Value("${sns.topic.arn}")
    private String snsTopicARN;
    @Value("${sns.accesskey}")
    private String snsTopicAccessKey;
    @Value("${sns.region}")
    private String snsTopicRegion;
    @Value("${cloud.aws.region.static}")
    private String snsTopicRegionStatic;
    @Value("${cloud.aws.static.auto}")
    private String snsTopicStaticAuto;

}
