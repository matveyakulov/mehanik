/*
 * Copyright MinuSoft (c) 2022.
 */
package ru.neirodev.mehanik.sms;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import ru.neirodev.mehanik.dto.SmsDTO;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;


@PropertySource("file:${catalina.base}/conf/sms.properties")
@Service
@Slf4j
public class SmsService {

    private final SmsSender smsSender;
    @Value("${api.secret}")
    private String secret;
    private static final Queue<Message> queue = new ConcurrentLinkedQueue<>();

    private final Timer timer = new Timer();

    @Autowired
    public SmsService(SmsSender smsSender) {
        this.smsSender = smsSender;
    }

    private static String formatPhone(String phone) {
        StringBuilder ret = new StringBuilder();
        for (char c : phone.toCharArray())
            if ((c >= '0') && (c <= '9'))
                ret.append(c);
        if (ret.length() == 10)
            ret.insert(0, "+7");
        if ((ret.length() == 11) && ((ret.charAt(0) == '7') || (ret.charAt(0) == '8'))) {
            ret.deleteCharAt(0);
            ret.insert(0, "+7");
        }
        if ((ret.length() == 12) && ret.toString().startsWith("+7"))
            return ret.toString();
        return null;
    }

    @PostConstruct
    private void init() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Message msg = queue.poll();
                if (msg != null)
                    try {
                        smsSender.sendMessage(msg.getPhone(), msg.getMessage());
                    } catch (Exception e) {
                        log.error("Возникла ошибка при отправке СМС на номер: " + msg.getPhone(), e);
                    }
            }
        }, 0, 500);
    }

    @PreDestroy
    private void destroy() {
        timer.cancel();
        smsSender.stop();
    }

    public boolean send(SmsDTO smsDTO) {
        String phone = smsDTO.getPhone();
        String message = smsDTO.getCode();
        try {
            if (!secret.equals(smsDTO.getSecret())) {
                return false;
            }
            phone = formatPhone(phone);
            if (phone == null) {
                return false;
            }
            queue.add(new Message(phone, "Код подтверждения:" + message));
            return true;
        } catch (Exception ex){
            return false;
        }
    }

}