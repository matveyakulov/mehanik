/*
 * Copyright MinuSoft (c) 2022.
 */
package ru.neirodev.mehanik.sms;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.neirodev.mehanik.dto.SmsDTO;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;


@RestController
@RequestMapping("/sms")
@Slf4j
public class SmsController {

    private static final Queue<Message> queue = new ConcurrentLinkedQueue<>();

    private Timer timer = new Timer();

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
    public void init() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Message msg = queue.poll();
                if (msg != null)
                    try {
                        SmsSender.sendMessage(msg.getPhone(), msg.getMessage());
                    } catch (Exception e) {
                        log.error("Возникла ошибка при отправке СМС", e);
                    }
            }
        }, 0, 500);
    }

    @PreDestroy
    public void destroy() {
        timer.cancel();
        SmsSender.stop();
    }

    @PostMapping
    protected ResponseEntity<?> send(@RequestBody final SmsDTO smsDTO) {
        String phone = smsDTO.getPhone();
        String message = smsDTO.getMessage();
        String secret = smsDTO.getSecret();
        try {
            if ((phone == null) || (message == null) || message.isEmpty() || (secret == null)) {
                return ResponseEntity.badRequest().body("Неверные данные");
            }
            if ((PropUtil.getApiSecret() == null) || PropUtil.getApiSecret().isEmpty()) {
                return ResponseEntity.internalServerError().body("API ключ не определен");
            }
            if (!secret.equals(PropUtil.getApiSecret())) {
                return new ResponseEntity<>("Неверный API ключ", UNAUTHORIZED);
            }
            phone = formatPhone(phone);
            if (phone == null) {
                return ResponseEntity.badRequest().body("Неверный номер телефона");
            }
            queue.add(new Message(phone, message));
            return ResponseEntity.ok().build();
        } catch (Exception ex){
            return ResponseEntity.internalServerError().body(ex.getMessage());
        }
    }

}