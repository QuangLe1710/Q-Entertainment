package com.app.Q_Entertainment.Service.ServiceImpl;

import com.app.Q_Entertainment.Model.Entity.User;
import com.app.Q_Entertainment.Repository.UsersRepository;
import com.app.Q_Entertainment.Security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsersRepository usersRepository;

    /**
     * Build a {@link CustomUserDetails} directly from an already-loaded {@link User} entity.
     * Use this inside services that already have the User object to avoid an extra DB query.
     */
    public CustomUserDetails fromUser(User user) {
        return CustomUserDetails.from(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = usersRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return CustomUserDetails.from(user);
    }
}
