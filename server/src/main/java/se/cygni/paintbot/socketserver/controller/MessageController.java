package se.cygni.paintbot.socketserver.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import se.cygni.paintbot.socketserver.wshandler.EventSocketHandler;
import se.cygni.paintbot.socketserver.model.Message;

@CrossOrigin(origins = "*")
@Slf4j
@RequiredArgsConstructor
public class MessageController {

    private final EventSocketHandler socketHandler;

    @MessageMapping("/events-controller")
    @SendTo("/topic/game-events")
    public Message handleEvents(String what) {
        log.info("Received event: {}", what);
        return new Message("no");
    }

//    @MessageMapping("/chat")
//    @SendTo("/topic/messages")
//    public Message getMessages(Message dto){
//        return dto;
//    }
//
//    @SubscribeMapping("/subscribe")
//    public String oneTime() {
//        return "oneTime()";
//    }
//
//    @MessageMapping("/request")
//    public void handleMessage(String message) {
//        System.out.println("Received in handleMessage() " +  message);
//    }

//    @MessageMapping("/request")
//    @SendTo("/queue/responses")
//    public String handleMessageWithResponse(String message) {
//        System.out.println("Received in handleMessageWithResponse " + message);
//        return  "The response";
//    }

//    @MessageExceptionHandler
//    @SendTo("/queue/errors")
//    public String handleException(Throwable exception) {
//        return "server exception: " + exception.getMessage();
//    }
}
