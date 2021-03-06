package com.epam.jpop.userservice.service;

import com.epam.jpop.userservice.domain.Result;
import com.epam.jpop.userservice.domain.User;

import java.util.List;

public interface UserService {

    List<User> list();

    Result add(User user);

    User get(Long id);

    Result update(Long id, User user);

    Result delete(Long id);

}
