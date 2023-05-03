package v1.amachon.domain.mail.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import v1.amachon.domain.base.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
public class EmailVerification extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "email_verification_id")
    private Long id;
    private String email;
    private String code;
    private LocalDateTime expiredDate;

    public EmailVerification(String email, String code) {
        this.code = code;
        this.email = email;
        this.expiredDate = LocalDateTime.now().plusMinutes(3);
    }
}
