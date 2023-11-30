package br.com.kamehouse.sqs.entrypoint;

import io.quarkus.vertx.ConsumeEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import jakarta.annotation.PostConstruct;

@ApplicationScoped
public class SQSConsumer {

    @Inject
    private SqsClient sqsClient;

//    @PostConstruct
//    public void startConsumer(){
//        ReceiveMessageRequest receiveMessageRequest = ReceiveMessageRequest.builder()
//                .queueUrl("")
//                .waitTimeSeconds(20)
//                .maxNumberOfMessages(1)
//                .build();
//
//        Message sqsMessage = sqsClient.receiveMessage(receiveMessageRequest).messages().getFirst();
//    }

//    @ConsumeEvent(value = "emitter-message")
    @Incoming(value = "emitter-message")
    public void sqsConsumer(MessageRecord message){
        System.out.println(message);
    }
}
