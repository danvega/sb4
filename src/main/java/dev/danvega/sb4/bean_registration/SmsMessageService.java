package dev.danvega.sb4.bean_registration;

public class SmsMessageService implements MessageService {

    @Override
    public String getMessage() {
        return "📱 SMS message sent at " + java.time.LocalDateTime.now();
    }

    @Override
    public String getServiceType() {
        return "SMS";
    }
}