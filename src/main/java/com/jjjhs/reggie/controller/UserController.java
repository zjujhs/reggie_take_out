package com.jjjhs.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jjjhs.reggie.common.R;
import com.jjjhs.reggie.common.ValidateCodeUtils;
import com.jjjhs.reggie.entity.User;
import com.jjjhs.reggie.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession httpSession) {
        log.info("消息发送请求，手机：{}", user.getPhone());
        String code = ValidateCodeUtils.generateValidateCode4String(6);
        log.info("验证码: {}", code);
        httpSession.setAttribute(user.getPhone(), code);
        return R.success("");
    }

    @PostMapping("/login")
    public R<String> login(HttpServletRequest request, @RequestBody Map user, HttpSession session) {
        String phone = (String) user.get("phone");
        String code = (String) session.getAttribute(phone);
        String input = (String) user.get("code");
        User u = new User();
        u.setPhone(phone);
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getPhone, phone);
        User maybe = userService.getOne(queryWrapper);
        if(maybe == null)
            userService.save(u);
        else
            u = maybe;
        if(code != null && input != null && code.equals(input)) {
            request.getSession().setAttribute("user", u.getId());
            return R.success("");
        }
        return R.error("验证码错误");
    }

    @PostMapping("/loginout")
    public R<String> logout(HttpServletRequest request) {
        request.getSession().removeAttribute("user");
        return R.success("退出成功");
    }

}
