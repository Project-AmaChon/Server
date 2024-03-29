package v1.amachon.common.config.security.util;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@RequiredArgsConstructor
public class SecurityUtils {

    public static String getLoggedUserEmail() {
        Authentication principal = SecurityContextHolder.getContext().getAuthentication();
        return principal.getName();
    }
}