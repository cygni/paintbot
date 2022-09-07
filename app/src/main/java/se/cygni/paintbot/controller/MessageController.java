package se.cygni.paintbot.controller;

import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

//@Controller
@CrossOrigin(origins = "*")
public class MessageController {

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
