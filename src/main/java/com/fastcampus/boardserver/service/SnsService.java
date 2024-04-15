package com.fastcampus.boardserver.service;

import com.fastcampus.boardserver.config.AWSConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;

@Service
@RequiredArgsConstructor
public class SnsService {

    private final AWSConfig awsConfig;

    public AwsCredentialsProvider getAWSCredentials(String accessKeyId, String searchAccessKey) {
        AwsBasicCredentials awsBasicCredentials = AwsBasicCredentials.create(accessKeyId, searchAccessKey);
        return () -> awsBasicCredentials;
    }

    public SnsClient getSsnsClient() {
        return SnsClient.builder()
                .credentialsProvider(getAWSCredentials(awsConfig.getSnsTopicARN(), awsConfig.getSnsTopicAccessKey()))
                .region(Region.of(awsConfig.getSnsTopicRegion()))
                .build();
    }
}
