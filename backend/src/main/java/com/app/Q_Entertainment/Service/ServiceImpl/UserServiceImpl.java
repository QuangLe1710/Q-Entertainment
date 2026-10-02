package com.app.Q_Entertainment.Service.ServiceImpl;

import com.app.Q_Entertainment.Model.Entity.User;
import com.app.Q_Entertainment.Repository.UsersRepository;
import com.app.Q_Entertainment.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UsersRepository usersRepository;

    @Override
    public List<User> getListUser() {
        return usersRepository.findAll();
    }
}
