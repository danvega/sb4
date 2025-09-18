package dev.danvega.sb4.bean_registration;

public class EmailMessageService implements MessageService {

    @Override
    public String getMessage() {
        return "📧 Email message sent at " + java.time.LocalDateTime.now();
    }

    @Override
    public String getServiceType() {
        return "EMAIL";
    }
}