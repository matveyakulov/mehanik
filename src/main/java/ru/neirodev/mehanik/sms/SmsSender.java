/*
 * Copyright MinuSoft (c) 2014.
 */
package ru.neirodev.mehanik.sms;

import lombok.extern.slf4j.Slf4j;
import org.jsmpp.InvalidResponseException;
import org.jsmpp.PDUException;
import org.jsmpp.bean.*;
import org.jsmpp.bean.OptionalParameter.OctetString;
import org.jsmpp.bean.OptionalParameter.Tag;
import org.jsmpp.extra.NegativeResponseException;
import org.jsmpp.extra.ProcessRequestException;
import org.jsmpp.extra.ResponseTimeoutException;
import org.jsmpp.extra.SessionState;
import org.jsmpp.session.*;
import org.jsmpp.util.InvalidDeliveryReceiptException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;

/**
 * @author Minu <<a href="minu-moto@mail.ru">minu-moto@mail.ru</a>>
 * @since 25.07.2014 22:30:20
 */
@Component
@Slf4j
public class SmsSender {

    private static final TypeOfNumber SENDER_TON = TypeOfNumber.ALPHANUMERIC;
    private static final NumberingPlanIndicator SENDER_NPI = NumberingPlanIndicator.UNKNOWN;

    private static final CharsetEncoder asciiEncoder = StandardCharsets.US_ASCII.newEncoder();

    @Value("${smpp.host.name}")
    private String smppHost;

    @Value("${smpp.host.port}")
    private Integer smppPort;

    @Value("${smpp.auth.user}")
    private String smppUser;

    @Value("${smpp.auth.password}")
    private String smppPassword;

    @Value("${smpp.addr.from}")
    private String smsFromAddr;

    private static boolean isPureAscii(String text) {
        return asciiEncoder.canEncode(text);
    }

    private static SMPPSession session;
    /**
     * Слушатель для СМС подтверждения
     */
    private static final MessageReceiverListener listener = new MessageReceiverListener() {
        @Override
        public DataSmResult onAcceptDataSm(DataSm dataSm, Session source) {
            log.info("+++++++++ onAcceptDataSm " + dataSm);
            return null;
        }

        @Override
        public void onAcceptDeliverSm(DeliverSm deliverSm) throws ProcessRequestException {
            if (MessageType.SMSC_DEL_RECEIPT.containedIn(deliverSm.getEsmClass())) {
                try {
                    DeliveryReceipt delReceipt = deliverSm.getShortMessageAsDeliveryReceipt();
                    log.info("Receiving delivery receipt for message '{}' : {}", delReceipt.getId(), delReceipt);
                } catch (InvalidDeliveryReceiptException e) {
                    log.error("Failed getting delivery receipt", e);
                }
            } else {
                // regular short message
                log.info("Receiving message : {}", new String(deliverSm.getShortMessage()));
            }
        }

        @Override
        public void onAcceptAlertNotification(AlertNotification alertNotification) {
            log.info("+++++++++ onAcceptAlertNotification " + alertNotification);
        }
    };

    public synchronized void sendMessage(String phone, String message) throws Exception {
        sendMessage(phone, message, isPureAscii(message));
    }

    /**
     * Отправить смс
     *
     * @param recipient - получатель, телефонный номер в формате (7xxxnnnnnnn)
     * @param message   - тело сообщения
     * @param langType  - true если латиница, false если не латиница
     * @return идентификатор сообщения
     * @throws Exception
     */
    private String sendMessage(String recipient, String message, Boolean langType) throws Exception {
        if ((recipient == null) || recipient.isEmpty() || (message == null))
            return null;
        if ((session == null) || (session.getSessionState() == SessionState.CLOSED))
            connectAndBind();
        if ((session == null) || (session.getSessionState() == SessionState.CLOSED))
            return null;

        try {
            String id = session.submitShortMessage("cpa",
                    SENDER_TON,
                    SENDER_NPI,
                    smsFromAddr,
                    TypeOfNumber.INTERNATIONAL,
                    NumberingPlanIndicator.ISDN,
                    recipient,
                    new ESMClass(),
                    (byte) 0,
                    (byte) 1,
                    null,
                    null,
                    new RegisteredDelivery(SMSCDeliveryReceipt.SUCCESS_FAILURE),
                    ReplaceIfPresentFlag.DEFAULT.value(),
                    new GeneralDataCoding(langType ? Alphabet.ALPHA_DEFAULT : Alphabet.ALPHA_UCS2),
                    (byte) 0,
                    new byte[0],
                    new OctetString(Tag.MESSAGE_PAYLOAD.code(),
                            message,
                            langType ? StandardCharsets.UTF_8.name() : StandardCharsets.UTF_16.name())).getMessageId();
            log.info("Message for " + recipient + " submitted, message_id is " + id);
            return id;
        } catch (PDUException e) {
            log.error("Invalid PDU parameter", e);
            throw new Exception("Invalid PDU parameter");
        } catch (ResponseTimeoutException e) {
            log.error("Response timeout", e);
            throw new Exception("Response timeout");
        } catch (InvalidResponseException e) {
            log.error("Receive invalid respose", e);
            throw new Exception("Receive invalid respose");
        } catch (NegativeResponseException e) {
            switch (e.getCommandStatus()) {
                case 0x505:
                    log.error("Сообщение не отправлено в связи с финансовой блокировкой. Необходимо пополнить баланс.", e);
//				String emails = "minu-moto@mail.ru";
//				if ((emails != null) && !emails.isEmpty())
//					MailUtil.sendMessage(emails, "Ошибка смс-оповещения",
//							"Сообщение для " + recipient
//							+ " не отправлено. Проверьте баланс в личном кабинете <a href='https://office.sms-agent.ru/'>https://office.sms-agent.ru/</a>."
//							+ "<br><br>========================<br><br>" + message);
                    throw new Exception("Необходимо пополнить баланс");
                case 0x0d:
                    log.error("Ошибка при подключении к шлюзу", e);
                    throw new Exception("Ошибка при подключении к шлюзу");
                case 0x0e:
                    log.error("Неправильный пароль", e);
                    throw new Exception("Неправильный пароль");
                case 0x0f:
                    log.error("Неправильный логин", e);
                    throw new Exception("Неправильный логин");
                default:
                    log.error("Receive negative response", e);
                    throw new Exception(e.getMessage());
            }
        } catch (IOException e) {
            log.error("IO error occur", e);
            throw new Exception("IO error occur");
        } catch (Exception e) {
            log.error("Something error occur", e);
            throw e;
        }
    }

    private void connectAndBind() throws Exception {
        session = new SMPPSession();
        session.setTransactionTimer(20000);
        session.setMessageReceiverListener(listener);
        try {
            session.connectAndBind(smppHost, smppPort, new BindParameter(BindType.BIND_TRX,
                    smppUser, smppPassword, "cpa", SENDER_TON, SENDER_NPI, null));
        } catch (IOException e) {
            throw new Exception("Ошибка при подключении к шлюзу", e);
        }
    }

    public synchronized void stop() {
        if ((session != null) && (session.getSessionState() != SessionState.CLOSED))
            session.unbindAndClose();
        session = null;
    }
}