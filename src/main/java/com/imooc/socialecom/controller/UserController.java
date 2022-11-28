package com.imooc.socialecom.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.imooc.socialecom.base.JsonReturnType;
import com.imooc.socialecom.mapper.UserMapper;
import com.imooc.socialecom.pojo.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/get")
    public JsonReturnType get(@RequestParam Integer id) {
        User user = userMapper.selectById(id);
        if (user != null) {
            return JsonReturnType.createType(user);
        }
        return JsonReturnType.createErrorType("用户不存在");
    }


    @GetMapping("/create")
    public JsonReturnType create(@RequestParam(value = "userName", required = false) String userName,
                                 @RequestParam(value = "password", required = false) String password,
                                 @RequestParam(value = "userTel", required = false) String userTel) {
        User user = new User();
        user.setUserName(userName);
        user.setUserTel(userTel);
        user.setUserPassword(password);
        userMapper.insert(user);
        return JsonReturnType.createType(user);
    }


    @GetMapping("/update")
    public JsonReturnType update(@RequestParam(value = "userName", required = false) String userName,
                                 @RequestParam(value = "password", required = false) String password,
                                 @RequestParam(value = "userTel", required = false) String userTel,
                                 @RequestParam(value = "id", required = false) Integer id) {
        User user = userMapper.selectById(id);
        user.setUserName(userName);
        user.setUserTel(userTel);
        user.setUserPassword(password);
        userMapper.updateById(user);
        return JsonReturnType.createType(user);
    }


    @GetMapping("/getForUpdate")
    public JsonReturnType getForUpdate(@RequestParam(value = "id", defaultValue = "0") Long id) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("id", id);
        wrapper.last("for update");
        User user = userMapper.selectOne(wrapper);
        return JsonReturnType.createType(user);
    }


    @GetMapping("/updateByVersion")
    public JsonReturnType updateByVersion(@RequestParam(value = "userName", required = false) String userName,
                                          @RequestParam(value = "password", required = false) String password,
                                          @RequestParam(value = "userTel", required = false) String userTel,
                                          @RequestParam(value = "id", required = false) Integer id) {
        User user = userMapper.selectById(id);
        user.setUserName(userName);
        user.setUserTel(userTel);
        user.setUserPassword(password);
        int count = userMapper.updateById(user);
        if (count < 1) {
            return JsonReturnType.createType(user);
        }
        return JsonReturnType.createErrorType("更新失败");
    }


    @GetMapping("/batchGet")
    public JsonReturnType batchGet(@RequestParam(name = "ids", defaultValue = "") List<Integer> ids) {
        List<User> users = userMapper.selectBatchIds(ids);
        return JsonReturnType.createType(users);
    }


    @GetMapping("/getByCondition")
    public JsonReturnType getByCondition(@RequestParam(value = "userName", required = false) String userName,
                                         @RequestParam(value = "password", required = false) String password,
                                         @RequestParam(value = "userTel", required = false) String userTel,
                                         @RequestParam(value = "id", required = false) Integer id,
                                         @RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
                                         @RequestParam(value = "pageNo", defaultValue = "30", required = false) Integer pageNo) {
        Page<User> userPage = new Page<>(page, pageNo);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (id != null) {
            queryWrapper.eq("id", id);
        }
        if (userName != null) {
            queryWrapper.eq("user_name", userName);
        }
        if (password != null) {
            queryWrapper.eq("user_password", password);
        }
        if (userTel != null) {
            queryWrapper.eq("user_tel", userTel);
        }
        IPage<User> result = userMapper.selectPage(userPage, queryWrapper);
        return JsonReturnType.createType(result.getRecords());
    }



}
