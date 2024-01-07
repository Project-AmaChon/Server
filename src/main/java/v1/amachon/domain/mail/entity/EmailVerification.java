package v1.amachon.domain.mail.entity;

import lombok.*;
import v1.amachon.domain.mail.entity.exception.InvalidEmailFormatException;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
public class EmailVerification {

    private static final String EMAIL_FORMAT_PATTERN = "^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$";

    @Id @GeneratedValue
    @Column(name = "email_verification_id")
    private Long id;
    private String email;
    private String code;
    private LocalDateTime expiredDate;

    @Builder
    public EmailVerification(String email, String code) {
        this.code = code;
        validateEmailFormat(email);
        this.email = email;
        this.expiredDate = LocalDateTime.now().plusMinutes(3);
    }

    private void validateEmailFormat(String email) {
        if (!email.matches(EMAIL_FORMAT_PATTERN)) {
            throw new InvalidEmailFormatException();
        }
    }
}
