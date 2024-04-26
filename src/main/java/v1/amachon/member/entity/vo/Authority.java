package v1.amachon.member.entity.vo;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import v1.amachon.member.entity.Member;

import javax.persistence.*;

import static lombok.AccessLevel.PROTECTED;

@Embeddable
@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
@Builder
public class Authority implements GrantedAuthority {

    private String role;

    public static Authority ofMember() {
        return Authority.builder()
                .role("ROLE_USER")
                .build();
    }

    public static Authority ofManager() {
        return Authority.builder()
                .role("ROLE_MANAGER")
                .build();
    }

    @Override
    public String getAuthority() {
        return role;
    }
}