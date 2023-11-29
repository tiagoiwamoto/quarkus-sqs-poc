package br.com.kamehouse.sqs.config;

import br.com.kamehouse.sqs.entrypoint.MessageRecord;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.scheduler.Scheduled;
import io.vertx.core.eventbus.EventBus;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;

@ApplicationScoped
public class SqsConfig {

    @Inject
    private SqsClient sqsClient;
    @Inject
    private EventBus eventBus;

    @Scheduled(every = "10s")
    public void sqsReader(){
        ReceiveMessageRequest receiveMessageRequest = ReceiveMessageRequest.builder()
                .queueUrl("quarkus-poc")
                .waitTimeSeconds(20)
                .maxNumberOfMessages(1)
                .build();
        ObjectMapper mapper = new ObjectMapper();
        Message sqsMessage = sqsClient.receiveMessage(receiveMessageRequest).messages().getFirst();
        try{
            MessageRecord message = mapper.readValue(sqsMessage.body(), MessageRecord.class);
            eventBus.send("eventbus-message", message);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
