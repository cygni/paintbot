package se.cygni.paintbot.socketserver.controller;

import javax.servlet.http.HttpServletResponse;

//@RestController
// TODO: move to FE
public class StartController {

//    @GetMapping("/")
    public void index(HttpServletResponse response) throws Exception {
        response.sendRedirect("http://game.paintbot.cygni.se");
    }

//    @GetMapping("/mtd2017")
    public void mtd2017(HttpServletResponse response) throws Exception {
        response.sendRedirect("https://docs.google.com/a/cygni.se/forms/d/e/1FAIpQLSe0tZDPLPGvj05wiC57LzieVOv3Y_s26fHtJ6S0KVViLbpUBw/viewform");
    }
}
