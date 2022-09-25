package com.bside.pjt.zerobackend.ncp;

import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.IOException;
import java.util.UUID;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
public class ObjectStorageService {
    @Value("${ncp.object-storage.endpoint}")
    private String endPoint;
    @Value("${ncp.object-storage.region}")
    private String region;
    @Value("${ncp.object-storage.access-key}")
    private String accessKey;
    @Value("${ncp.object-storage.secret-key}")
    private String secretKey;
    @Value("${ncp.object-storage.bucket}")
    private String bucket;

    private AmazonS3 s3;

    @PostConstruct
    public void initialize() {
        this.s3 = AmazonS3ClientBuilder.standard()
            .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endPoint, region))
            .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
            .build();
    }

    public String upload(final long userId, final MultipartFile image) {
        final String imageName = UUID.randomUUID().toString();
        final String key = String.format("content/mission-progress/%d/%s", userId, imageName);

        try {
           final ObjectMetadata metadata = new ObjectMetadata();
           metadata.setContentType(image.getContentType());
           metadata.setContentLength(image.getSize());

            s3.putObject(new PutObjectRequest(bucket, key, image.getInputStream(), metadata)
                .withCannedAcl(CannedAccessControlList.PublicRead));
            log.info("Object %s has been created.\n", imageName);

            return endPoint + "/" + bucket + "/" + key;
        } catch (AmazonS3Exception e) {
            log.error(e.getMessage());
            throw e;
        } catch (SdkClientException e) {
            log.error(e.getMessage());
            throw e;
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException();
        }
    }
}
