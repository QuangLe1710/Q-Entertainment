package com.app.Q_Entertainment.Service.ServiceImpl;

import com.app.Q_Entertainment.Model.Entity.User;
import com.app.Q_Entertainment.Repository.UsersRepository;
import com.app.Q_Entertainment.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService, UserDetails {

    private final UsersRepository usersRepository;

    @Override
    public List<User> getListUser() {
        return usersRepository.findAll();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public @Nullable String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return "";
    }
}
