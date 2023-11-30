package br.com.kamehouse.sqs.config;

import br.com.kamehouse.sqs.entrypoint.MessageRecord;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.scheduler.Scheduled;
import io.vertx.core.eventbus.EventBus;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;

import java.util.List;

@ApplicationScoped
public class SqsConfig {

    @Inject
    private SqsClient sqsClient;
//    @Inject
//    private EventBus eventBus;
    @Inject
    @Channel("emitter-message")
    private Emitter<MessageRecord> emitter;

    @PostConstruct
    public void postInit() {
        sqsClient.createQueue(m -> {
            m.queueName("quarkus-poc");
            m.build();
        });
    }

    @Scheduled(every = "10s")
    public void sqsReader(){
        ReceiveMessageRequest receiveMessageRequest = ReceiveMessageRequest.builder()
                .queueUrl("quarkus-poc")
                .waitTimeSeconds(20)
                .maxNumberOfMessages(1)
                .build();
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        List<Message> sqsMessage = sqsClient.receiveMessage(receiveMessageRequest).messages();
        sqsMessage.forEach(m -> {
            try{
                MessageRecord message = mapper.readValue(m.body(), MessageRecord.class);
//            eventBus.send("eventbus-message", message);
                this.emitter.send(message);
            }catch (Exception e){
                e.printStackTrace();
            }

        });

    }


}
