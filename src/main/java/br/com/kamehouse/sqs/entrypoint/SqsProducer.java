package br.com.kamehouse.sqs.entrypoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;

import java.time.LocalDate;
import java.util.UUID;

@Path("/producer")
public class SqsProducer {

    @Inject
    private SqsClient sqsClient;

    @GET
    public Boolean enviarMensagem(){

        MessageRecord message = new MessageRecord(1L, UUID.randomUUID(), "teste", LocalDate.now());
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        try{
            var sqsMessage = mapper.writeValueAsString(message);

            SendMessageResponse response =
                    sqsClient.sendMessage(m -> m.queueUrl("quarkus-poc").messageBody(sqsMessage));
        }catch (Exception e){
            e.printStackTrace();
        }
        return Boolean.TRUE;
    }

}
