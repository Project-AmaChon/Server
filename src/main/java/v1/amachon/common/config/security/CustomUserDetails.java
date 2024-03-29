package v1.amachon.common.config.security;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import v1.amachon.member.entity.Member;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"enabled","accountNonExpired", "accountNonLocked", "credentialsNonExpired", "authorities"})
public class CustomUserDetails implements UserDetails {

    private String username;
    private String password;

    @Builder.Default
    private List<String> roles = new ArrayList<>();

    public static UserDetails of(Member users) {
        return CustomUserDetails.builder()
                .username(users.getEmail())
                .password(users.getPassword())
                .roles(users.getRoles())
                .build();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    @JsonIgnoreProperties(ignoreUnknown = true)
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    @JsonIgnoreProperties(ignoreUnknown = true)
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    @JsonIgnoreProperties(ignoreUnknown = true)
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    @JsonIgnoreProperties(ignoreUnknown = true)
    public boolean isEnabled() {
        return false;
    }
}
