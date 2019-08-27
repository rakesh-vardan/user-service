package com.epam.jpop.userservice.service;

import com.epam.jpop.userservice.domain.Result;
import com.epam.jpop.userservice.domain.User;
import com.epam.jpop.userservice.domain.UserRole;
import com.epam.jpop.userservice.exception.UserIdMismatchException;
import com.epam.jpop.userservice.exception.UserNotFoundException;
import com.epam.jpop.userservice.repository.UserRepository;
import com.epam.jpop.userservice.service.impl.UserServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

@RunWith(PowerMockRunner.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private List<com.epam.jpop.userservice.entity.User> userEntityList;
    private com.epam.jpop.userservice.entity.User userRecord1;
    private com.epam.jpop.userservice.entity.User userRecord2;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        userEntityList = new ArrayList<>();

        userRecord1 = new com.epam.jpop.userservice.entity.User();
        userRecord2 = new com.epam.jpop.userservice.entity.User();

        userRecord1.setId(1L);
        userRecord1.setName("Rakesh");
        userRecord1.setRole(UserRole.ADMIN.name());
        userRecord1.setPhoneNumber("9885448921");

        userRecord2.setId(2L);
        userRecord2.setName("Vardan");
        userRecord2.setRole(UserRole.GENERAL.name());

        userEntityList.add(userRecord1);
        userEntityList.add(userRecord2);
    }

    @Test
    public void testList() {
        Mockito.when(userRepository.findAll()).thenReturn(userEntityList);

        List<User> users = userService.list();
        assertEquals("Rakesh", users.get(0).getName());
        assertEquals("ADMIN", users.get(0).getRole());
        assertEquals("9885448921", users.get(0).getPhoneNumber());

        assertEquals("Vardan", users.get(1).getName());
        assertEquals("GENERAL", users.get(1).getRole());

        Mockito.verify(userRepository, Mockito.times(1)).findAll();
    }

    @Test
    public void testAdd() {
        Mockito.when(userRepository.save(Mockito.any(com.epam.jpop.userservice.entity.User.class)))
                .thenReturn(userRecord1);
        User user = new User();
        user.builder().id(1L).name("Rakesh").role("ADMIN").build();

        Result result = userService.add(user);
        assertEquals(Optional.of(1L).get(), result.getId());
        Mockito.verify(userRepository, Mockito.times(1))
                .save(Mockito.any(com.epam.jpop.userservice.entity.User.class));
    }

    @Test
    public void testGetWithExistingUserId() {
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(userRecord2));
        User user = userService.get(1L);

        assertEquals("Vardan", user.getName());
        assertEquals("GENERAL", user.getRole());
        Mockito.verify(userRepository, Mockito.times(1)).findById(Mockito.anyLong());
    }

    @Test(expected = UserNotFoundException.class)
    public void testGetWithInvalidUserId() {
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenThrow(UserNotFoundException.class);
        User user = userService.get(1L);
    }

    @Test
    public void testUpdateWithValidUserId() {
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(userRecord1));
        Mockito.when(userRepository.save(Mockito.any(com.epam.jpop.userservice.entity.User.class)))
                .thenReturn(userRecord1);
        User user = User.builder().id(1L).name("Rakesh").role("ADMIN").build();
        Result result = userService.update(1L, user);

        assertEquals(Optional.of(1L).get(), result.getId());
        Mockito.verify(userRepository, Mockito.times(1)).findById(Mockito.anyLong());
        Mockito.verify(userRepository, Mockito.times(1))
                .save(Mockito.any(com.epam.jpop.userservice.entity.User.class));
    }

    @Test(expected = UserIdMismatchException.class)
    public void testUpdateWithUnmatchedUserId() {
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(userRecord1));
        Mockito.when(userRepository.save(Mockito.any(com.epam.jpop.userservice.entity.User.class)))
                .thenThrow(UserIdMismatchException.class);
        User user = User.builder().id(1L).name("Rakesh").role("ADMIN").build();
        userService.update(1L, user);

        Mockito.verify(userRepository, Mockito.times(0))
                .save(Mockito.any(com.epam.jpop.userservice.entity.User.class));
        Mockito.verify(userRepository, Mockito.times(1))
                .findById(Mockito.anyLong());
    }

    @Test(expected = UserNotFoundException.class)
    public void testUpdateWithUserIdNotFound() {
        Mockito.when(userRepository.save(Mockito.any(com.epam.jpop.userservice.entity.User.class)))
                .thenThrow(UserNotFoundException.class);
        User user = User.builder().id(1L).name("Rakesh").role("ADMIN").build();
        userService.update(1L, user);
        Mockito.verify(userRepository, Mockito.times(0))
                .save(Mockito.any(com.epam.jpop.userservice.entity.User.class));
        Mockito.verify(userRepository, Mockito.times(1))
                .findById(Mockito.anyLong());
    }

    @Test
    public void testDelete() {
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(userRecord1));
        Mockito.doNothing().when(userRepository).deleteById(Mockito.anyLong());
        Result result = userService.delete(1L);

        assertEquals(Optional.of(1L).get(), result.getId());
        Mockito.verify(userRepository, Mockito.times(1)).findById(Mockito.anyLong());
        Mockito.verify(userRepository, Mockito.times(1)).deleteById(Mockito.anyLong());
    }

    @Test(expected = UserNotFoundException.class)
    public void testDeleteWithUserNotFound() {
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenThrow(UserNotFoundException.class);
        Mockito.doThrow(UserNotFoundException.class).when(userRepository).deleteById(Mockito.anyLong());
        userService.delete(1L);
        Mockito.verify(userRepository, Mockito.times(1)).findById(Mockito.anyLong());
        Mockito.verify(userRepository, Mockito.times(0)).deleteById(Mockito.anyLong());
    }
}
