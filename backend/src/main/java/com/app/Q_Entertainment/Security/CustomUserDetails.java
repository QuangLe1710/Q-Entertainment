package com.app.Q_Entertainment.Security;
import com.app.Q_Entertainment.Model.Entity.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
/**
 * Custom implementation of {@link UserDetails} that carries additional
 * domain-specific fields (e.g. {@code userId}) beyond what the standard
 * Spring Security {@code User} builder supports.
 *
 * <p>Built directly from the {@link User} entity — no extra DB query needed.</p>
 */
@Getter
public class CustomUserDetails implements UserDetails {
    private final int userId;
    private final String username;
    private final String password;
    private final boolean enabled;
    private final boolean accountNonLocked;
    private final Collection<? extends GrantedAuthority> authorities;

    private CustomUserDetails(User user) {
        this.userId          = user.getId();
        this.username        = user.getUsername();
        this.password        = user.getPasswordHash();
        this.enabled         = user.isActive();
        this.accountNonLocked = !user.isLocked();
        this.authorities     = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getCode()))
                .collect(Collectors.toSet());
    }
    /**
     * Factory method — preferred over public constructor to keep the API clean.
     */
    public static CustomUserDetails from(User user) {
        return new CustomUserDetails(user);
    }
    // ── UserDetails interface ────────────────────────────────────────────────
    @Override public boolean isAccountNonExpired()  { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
}