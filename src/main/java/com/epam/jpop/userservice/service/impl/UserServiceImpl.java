package com.epam.jpop.userservice.service.impl;

import com.epam.jpop.userservice.domain.Result;
import com.epam.jpop.userservice.domain.User;
import com.epam.jpop.userservice.exception.UserIdMismatchException;
import com.epam.jpop.userservice.exception.UserNotFoundException;
import com.epam.jpop.userservice.repository.UserRepository;
import com.epam.jpop.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<User> list() {
        List<com.epam.jpop.userservice.entity.User> usersFromEntity = userRepository.findAll();
        return usersFromEntity.stream().map(u -> new User(u.getId(), u.getName(), u.getRole(),
                u.getEmail(), u.getPhoneNumber())).collect(Collectors.toList());
    }

    @Override
    public Result add(User user) {
        com.epam.jpop.userservice.entity.User userEntity = userRepository.save(prepareUserEntity(user));
        return new Result(userEntity.getId());
    }

    @Override
    public User get(Long id) {
        com.epam.jpop.userservice.entity.User userEntity = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
        return getUserFromEntity(userEntity);
    }

    @Override
    public Result update(Long id, User user) {
        if (!user.getId().equals(id))
            throw new UserIdMismatchException();

        verifyAndThrowExceptionIfUserNotPresent(id);
        userRepository.save(prepareUserEntity(user));
        return new Result(id);
    }

    @Override
    public Result delete(Long id) {
        verifyAndThrowExceptionIfUserNotPresent(id);
        userRepository.deleteById(id);
        return new Result(id);
    }

    private com.epam.jpop.userservice.entity.User prepareUserEntity(User user) {
        com.epam.jpop.userservice.entity.User userEntity = new com.epam.jpop.userservice.entity.User();
        userEntity.setId(user.getId());
        userEntity.setName(user.getName());
        userEntity.setRole(user.getRole());
        userEntity.setEmail(user.getEmail());
        userEntity.setPhoneNumber(user.getPhoneNumber());

        return userEntity;
    }

    private void verifyAndThrowExceptionIfUserNotPresent(Long id) {
        userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }

    private User getUserFromEntity(com.epam.jpop.userservice.entity.User userEntity) {
        User user = new User();
        user.setId(userEntity.getId());
        user.setName(userEntity.getName());
        user.setRole(userEntity.getRole());

        Optional<String> email = Optional.ofNullable(userEntity.getEmail());
        email.ifPresent(user::setEmail);

        Optional<String> phone = Optional.ofNullable(userEntity.getPhoneNumber());
        phone.ifPresent(user::setPhoneNumber);
        return user;
    }
}
