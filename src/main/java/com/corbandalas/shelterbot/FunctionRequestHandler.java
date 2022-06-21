package com.corbandalas.shelterbot;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.corbandalas.shelterbot.telegram.menu.ShelterBotMenuProcessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.function.aws.MicronautRequestHandler;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Update;



@Slf4j
public class FunctionRequestHandler extends MicronautRequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    @Inject
    private ObjectMapper objectMapper;

    @Inject
    private ShelterBotMenuProcessor shelterBotMenuProcessor;



    //    "message":
//    {
//        "message_id": 24,
//            "from": {
//        "id": 151800231,
//                "is_bot": false,
//                "first_name": "corbandalas",
//                "username": "corbandalas",
//                "language_code": "ru"
//    },
//        "chat": {
//        "id": 151800231,
//                "first_name": "corbandalas",
//                "username": "corbandalas",
//                "type": "private"
//    },
//        "date": 1655034125,
//            "text": "test"
//    }
//}
//
    @Override
    public APIGatewayProxyResponseEvent execute(APIGatewayProxyRequestEvent input) {
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        try {

            Update update = objectMapper.readValue(input.getBody(), Update.class);

            log.info(update.toString());

            shelterBotMenuProcessor.process(update);

        } catch (Exception e) {
            log.error("Telegram webhook communication exception", e);
        } finally {
            response.setStatusCode(200);
            response.setBody("Take care!");
        }
        return response;
    }
}