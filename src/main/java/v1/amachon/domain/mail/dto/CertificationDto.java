package v1.amachon.domain.mail.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CertificationDto {
    private String email;
    private String code;
}
