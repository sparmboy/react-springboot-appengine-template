package com.example.scheduled;

import static com.example.config.WebSocketConfig.TOPIC_MY_EVENT;
import static com.example.config.WebSocketConfig.TOPIC_PREFIX;

import com.example.domain.dto.WebSocketEventDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@EnableScheduling
@Component
@Slf4j
public class MessageBroadcastSchedule {

    private boolean status = false;

    private final SimpMessagingTemplate template;

    public MessageBroadcastSchedule(SimpMessagingTemplate template) {
        this.template = template;
    }

    @Scheduled(cron = "0/2 * * * * *")
    public void sendMessage(){
        String topic = TOPIC_PREFIX + TOPIC_MY_EVENT ;
        log.debug("Broadcasting message for session state on topic {}", topic);
        template.convertAndSend(topic, new WebSocketEventDTO(status));
        status = !status;
    }
}
