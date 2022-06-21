package com.corbandalas.shelterbot.telegram;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.context.env.Environment;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.StringWriter;
import java.util.HashMap;

@Slf4j
@Singleton
public final class ShelterBotStateStorage {

    @Inject
    private S3Client s3Client;
    @Inject
    private Environment environment;
    @Inject
    private ObjectMapper objectMapper;

    public ShelterBotState getBotState(Long chatID) {

        try {
            ShelterBotState shelterBotState = objectMapper.readValue(s3Client.getObject(GetObjectRequest.builder()
                    .bucket(environment.getProperty("aws.s3.botstate-bucket", String.class).orElseThrow())
                    .key(String.valueOf(chatID)).build()), ShelterBotState.class);

            if (shelterBotState != null && shelterBotState.getData() == null) {
                shelterBotState.setData(new HashMap<>());
            }

            return shelterBotState;

        } catch (Exception e) {
            log.error("Error while parsing bot state for chat id: " + chatID, e);
            throw new IllegalStateException(e);
        }
    }

    public void deleteBotState(Long chatID) {
        try {
            s3Client.deleteObject(DeleteObjectRequest.builder()
                    .key(String.valueOf(chatID))
                    .bucket(environment.getProperty("aws.s3.botstate-bucket", String.class).orElseThrow())
                    .build());
        } catch (Exception e) {
            log.error("Error while deleting bot state for chat id: " + chatID, e);
        }
    }

    public void saveBotState(Long chatID, ShelterBotState shelterBotState) {

        try {

            StringWriter stringWriter = new StringWriter();

            objectMapper.writeValue(stringWriter, shelterBotState);

            s3Client.putObject(PutObjectRequest.builder()
                    .key(String.valueOf(chatID))
                    .bucket(environment.getProperty("aws.s3.botstate-bucket", String.class).orElseThrow())
                    .build(), RequestBody.fromString(stringWriter.getBuffer().toString()));


        } catch (Exception e) {
            log.error("Error while saving bot state for chat id: " + chatID, e);
            throw new IllegalStateException(e);
        }

    }

}
