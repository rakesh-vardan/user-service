package com.epam.jpop.userservice.controller;

import com.epam.jpop.userservice.domain.Result;
import com.epam.jpop.userservice.domain.User;
import com.epam.jpop.userservice.service.UserService;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Api(value = "user-service")
public class UserController {

    private static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @GetMapping
    @ApiOperation(value = "View list of available users", response = List.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved users list"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    public ResponseEntity<Object> getAllUsers() {
        logger.info("Getting all the available users present in the library application");
        return new ResponseEntity<>(userService.list(), HttpStatus.OK);
    }

    @PostMapping
    @ApiOperation(value = "Registers a new user to the library")
    public ResponseEntity<Result> save(
            @ApiParam(value = "New User details to save data to the library", required = true) @RequestBody User user) {
        Result apiResult = userService.add(user);
        logger.info("Successfully added a new user to system: {}", apiResult.getId());
        return new ResponseEntity<>(apiResult, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Get a user registered with library by an Id")
    public User get(@ApiParam(value = "User Id to fetch the details from", required = true) @PathVariable Long id) {
        logger.info("Fetching the user details with id: {}", id);
        return userService.get(id);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Update an existing user in the library")
    public ResponseEntity<Result> update(
            @ApiParam(value = "User Id to Update the details to", required = true) @PathVariable Long id,
            @ApiParam(value = "Updated User details", required = true) @RequestBody User user) {
        logger.info("Updating the user details for user ID: {}", id);
        return new ResponseEntity<>(userService.update(id, user), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete an existing user from the library")
    public ResponseEntity<Result> delete(@ApiParam(value = "User Id to delete the data", required = true) @PathVariable Long id) {
        logger.info("Deleting the user from the library: {}", id);
        return new ResponseEntity<>(userService.delete(id), HttpStatus.NO_CONTENT);
    }
}
