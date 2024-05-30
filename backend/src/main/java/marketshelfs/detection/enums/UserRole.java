package marketshelfs.detection.enums;

import org.springframework.security.core.GrantedAuthority;

public enum UserRole implements GrantedAuthority {
    INDIVIDUAL,
    CORPORATE;

    @Override
    public String getAuthority() {
        return "ROLE_" + name();
    }
}
