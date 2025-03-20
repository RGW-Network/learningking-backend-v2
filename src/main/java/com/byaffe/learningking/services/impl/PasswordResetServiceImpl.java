package com.byaffe.learningking.services.impl;

import com.byaffe.learningking.models.PasswordResetToken;
import com.byaffe.learningking.services.PasswordResetService;
import com.byaffe.learningking.services.UserService;
import com.byaffe.learningking.shared.exceptions.OperationFailedException;
import com.byaffe.learningking.shared.exceptions.ValidationFailedException;
import com.byaffe.learningking.shared.models.MessageTemplate;
import com.byaffe.learningking.shared.models.MessageTemplateChannel;
import com.byaffe.learningking.shared.models.MessageTemplateType;
import com.byaffe.learningking.shared.models.User;
import com.byaffe.learningking.shared.services.MessageTemplateService;
import com.byaffe.learningking.shared.services.MessageTemplateServiceImpl;
import com.byaffe.learningking.shared.utils.ApplicationContextProvider;
import com.byaffe.learningking.shared.utils.MailService;
import com.byaffe.learningking.shared.utils.PassEncTech4;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
public class PasswordResetServiceImpl extends GenericServiceImpl<PasswordResetToken> implements PasswordResetService {

    @Autowired
    private UserService userRepository;
    @Autowired
    MailService mailService;

    public void initiatePasswordReset(String email) {
        User user = userRepository.getUserByUsername(email);
        if (user == null) {
            throw new ValidationFailedException("No user found with this email");
        }

        // Generate reset token
        String token = PassEncTech4.generateOTP(6);

        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setExpiryDate(LocalDateTime.now().plusHours(1)); // Token expires in 1 hour
        resetToken.setUser(user);
        save(resetToken);

        MessageTemplate emailTemplate = ApplicationContextProvider.getBean(MessageTemplateService.class).getActiveTemplate(MessageTemplateChannel.EMAIL, MessageTemplateType.RESET_PASSWORD_OTP);
        if (emailTemplate == null) {
            throw new ValidationFailedException("No email template");
        }
        String subject = MessageTemplateServiceImpl.format(emailTemplate.getSubject(), user, token);
        String body = MessageTemplateServiceImpl.format(emailTemplate.getBody(), user, token);

        mailService.sendEmail(user.getEmailAddress(), subject, body);

    }


    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = searchUniqueByPropertyEqual("token", token);
        if (StringUtils.isEmpty(newPassword)) {
            throw new ValidationFailedException("Password cannot be empty");
        }

        if (resetToken == null || resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new ValidationFailedException("Missing or expired token");
        }
        User user = resetToken.getUser();
        user.setPassword(PassEncTech4.generateSecurePassword(newPassword)); // Set the new password
        userRepository.saveUser(user);
    }

    @Override
    public boolean isDeletable(PasswordResetToken entity) throws OperationFailedException {
        return false;
    }

    @Override
    public PasswordResetToken saveInstance(PasswordResetToken instance) throws ValidationFailedException, OperationFailedException {
        return null;
    }
}
