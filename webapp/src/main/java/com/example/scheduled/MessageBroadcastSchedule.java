package com.example.scheduled;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

import static com.example.config.WebSocketConfig.TOPIC_PREFIX;
import static com.example.config.WebSocketConfig.TOPIC_SESSION;

@EnableScheduling
@Component
@Slf4j
public class MessageBroadcastSchedule {

    public final static List<String> messages = Arrays.asList("These","words","are","being","sent","over","a","socket");

    private int index = 0;

    @Autowired
    private SimpMessagingTemplate template;

    @Scheduled(cron = "0/5 * * * * *")
    public void sendMessage(){
        String topic = TOPIC_PREFIX + TOPIC_SESSION ;
        log.debug("Broadcasting message {} for session state on topic {}",messages.get(index), topic);
        template.convertAndSend(topic, messages.get(index++));
        if( index == messages.size() ) {
            index=0;
        }
    }
}
