package com.epam.jpop.userservice.controller;

import com.epam.jpop.userservice.domain.Result;
import com.epam.jpop.userservice.domain.User;
import com.epam.jpop.userservice.domain.UserRole;
import com.epam.jpop.userservice.exception.UserIdMismatchException;
import com.epam.jpop.userservice.exception.UserNotFoundException;
import com.epam.jpop.userservice.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

@RunWith(PowerMockRunner.class)
public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private List<User> users;
    private User user1;
    private User user2;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        user1 = User.builder()
                .id(1L)
                .name("Rakesh Vardan")
                .role(UserRole.ADMIN.name())
                .email("rakeshvardan@yopmail.com")
                .phoneNumber("9885448921")
                .build();

        user2 = User.builder()
                .id(2L)
                .name("Rakesh Budugu")
                .role(UserRole.GENERAL.name())
                .build();

        users = new ArrayList<>();
        users.add(user1);
        users.add(user2);
    }

    @Test
    public void testUserList() {
        Mockito.when(userService.list()).thenReturn(users);

        ResponseEntity<Object> response = userController.list();
        List<User> userListFromResponse = (List<User>) response.getBody();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Rakesh Vardan", userListFromResponse.get(0).getName());
        assertEquals("ADMIN", userListFromResponse.get(0).getRole());
        assertEquals("rakeshvardan@yopmail.com", userListFromResponse.get(0).getEmail());
        assertEquals("9885448921", userListFromResponse.get(0).getPhoneNumber());

        assertEquals("Rakesh Budugu", userListFromResponse.get(1).getName());
        assertEquals("GENERAL", userListFromResponse.get(1).getRole());
        Mockito.verify(userService).list();
    }

    @Test
    public void testGetUser() {
        Mockito.when(userService.get(Mockito.anyLong())).thenReturn(user1);

        User user = userController.get(1L);
        assertEquals("Rakesh Vardan", user.getName());
        assertEquals("ADMIN", user.getRole());
        assertEquals("rakeshvardan@yopmail.com", user.getEmail());
        assertEquals("9885448921", user.getPhoneNumber());
        Mockito.verify(userService).get(Mockito.anyLong());
    }

    @Test
    public void testSave() {
        Mockito.when(userService.add(Mockito.any(User.class))).thenReturn(new Result(1L));
        ResponseEntity<Result> apiResponse = userController.save(user2);

        verifyResponse(apiResponse);
        Mockito.verify(userService, Mockito.times(1)).add(Mockito.any(User.class));
    }

    @Test
    public void testUpdateWithExistingUser() {
        Mockito.when(userService.update(Mockito.anyLong(), Mockito.any(User.class))).thenReturn(new Result(1L));
        ResponseEntity<Result> apiResponse = userController.update(1L, user1);

        verifyResponse(apiResponse);
        Mockito.verify(userService, Mockito.times(1)).update(Mockito.anyLong(),
                Mockito.any(User.class));
    }

    @Test(expected = UserNotFoundException.class)
    public void testUpdateWithNonExistingUser() {
        Mockito.when(userService.update(Mockito.anyLong(), Mockito.any(User.class)))
                .thenThrow(UserNotFoundException.class);
        userController.update(1L, user2);
    }

    @Test(expected = UserIdMismatchException.class)
    public void testUpdateWithNotMatchingUser(){
        Mockito.when(userService.update(Mockito.anyLong(), Mockito.any(User.class)))
                .thenThrow(UserIdMismatchException.class);
        userController.update(1L, user2);
    }

    @Test
    public void testDeleteWithExistingUser() {
        Mockito.when(userService.delete(Mockito.anyLong())).thenReturn(new Result(1L));
        ResponseEntity<Result> apiResponse = userController.delete(1L);

        verifyResponse(apiResponse);
        Mockito.verify(userService, Mockito.times(1)).delete(Mockito.anyLong());
    }

    @Test(expected = UserNotFoundException.class)
    public void testDeleteWithNonExistingUser() {
        Mockito.when(userService.delete(Mockito.anyLong())).thenThrow(UserNotFoundException.class);
        userController.delete(1L);
    }

    private void verifyResponse(ResponseEntity<Result> apiResponse) {
        assertEquals(Optional.of(1L).get(), ((Result) apiResponse.getBody()).getId());
    }
}
