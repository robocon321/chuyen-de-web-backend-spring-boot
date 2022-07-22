package com.robocon321.demo.service.common;

import com.robocon321.demo.domain.EmailDetails;

public interface EmailService {
    boolean sendSimpleMail(EmailDetails details);

}
