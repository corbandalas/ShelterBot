package com.corbandalas.shelterbot;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.corbandalas.shelterbot.telegram.menu.ShelterBotMenuProcessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.context.ApplicationContextBuilder;
import io.micronaut.function.aws.MicronautRequestHandler;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Update;

import javax.annotation.Nonnull;


@Slf4j
public class FunctionRequestHandler extends MicronautRequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    @Inject
    private ObjectMapper objectMapper;

    @Inject
    private ShelterBotMenuProcessor shelterBotMenuProcessor;

    @Override
    public APIGatewayProxyResponseEvent execute(APIGatewayProxyRequestEvent input) {
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        try {

            Update update = objectMapper.readValue(input.getBody(), Update.class);

            log.info(update.toString());
            log.info("Update text: " + update.getMessage().getText());

            shelterBotMenuProcessor.process(update);

        } catch (Exception e) {
            log.error("Telegram webhook communication exception", e);
        } finally {
            response.setStatusCode(200);
            response.setBody("Take care!");
        }
        return response;
    }

    @Nonnull
    @Override
    protected ApplicationContextBuilder newApplicationContextBuilder() {
        ApplicationContextBuilder builder = super.newApplicationContextBuilder();
        builder.eagerInitSingletons(true);
        return builder;
    }
}