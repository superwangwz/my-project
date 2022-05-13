package com.bishe.controller;

import com.bishe.pojo.User;
import com.bishe.service.IUserService;
import com.bishe.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 用户控制层
 */
@RestController
@CrossOrigin
@RequestMapping("/user")
@Api(tags = "用户控制器")
public class UserController {

    @Resource
    private IUserService userService;

    @ApiOperation("用户登录")
    @PostMapping("/login")
    public Result login(@RequestBody User user){
        User login = userService.login(user);
        return Result.ok(login);
    }
}
