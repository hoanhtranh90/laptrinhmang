package com.business.Controller;


import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/sse")
public class LiveStreamController {
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    @GetMapping("/time")
    @CrossOrigin
    public SseEmitter streamDateTime() {


        SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);

        sseEmitter.onCompletion(() -> System.out.println("SseEmitter is completed"));

        sseEmitter.onTimeout(() -> System.out.println("SseEmitter is timed out"));

        sseEmitter.onError((ex) -> System.out.println("SseEmitter got error:"));

//        executor.execute(() -> {
//            for (int i = 0; i < 15; i++) {
//                try {
//                    sseEmitter.send(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss")));
//                    sleep(1, sseEmitter);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    sseEmitter.completeWithError(e);
//                }
//            }
//            sseEmitter.complete();
//        });
//
//        System.out.println("Controller exits");
//        String dir = "template/y2mate.com - Nhạc Chill TikTok  Những Bản Nhạc Lofi Chill Nhẹ Nhàng  Nhạc Lofi Buồn Hot Nhất Hiện Nay.mp3";
//        File musicFile = new File(dir);

        return sseEmitter;
    }
    private void sleep(int seconds, SseEmitter sseEmitter) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            sseEmitter.completeWithError(e);
        }
    }
}
