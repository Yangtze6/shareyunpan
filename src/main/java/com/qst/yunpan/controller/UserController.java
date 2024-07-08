package com.qst.yunpan.controller;

import com.qst.yunpan.pojo.User;
import com.qst.yunpan.service.FileService;
import com.qst.yunpan.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService UserService;//实例化
    @Autowired
    private FileService FileService;//实例化

    @RequestMapping("/regist")
    public String regist(HttpServletRequest request, HttpServletResponse response, User user) throws Exception{
        System.out.println(user.getUsername()+"-------"+user.getPassword());
        if(user.getUsername() == null || user.getPassword() == null||user.getUsername().equals("")||user.getPassword().equals("")){
            request.setAttribute("msg", "请输入用户名和密码");
            return "regist";
        }else{
            boolean isSuccess = UserService.addUser(user);
            if(isSuccess){
                FileService.addNewNameSpace(request, user.getUsername());
                return "login";
            }else{
                request.setAttribute("msg", "注册失败");
                return "regist";
            }
        }
    }

    @RequestMapping("/login")
    public String login(HttpServletRequest request, User user){
        if(user.getUsername()==null||user.getUsername().equals("")||user.getPassword()==null||user.getPassword().equals("")){
            request.setAttribute("msg", "请输入用户名或密码");
            return "login";
        }
        User exsitUser = UserService.findUser(user);
        if(exsitUser != null){
            HttpSession session = request.getSession();
            session.setAttribute(User.NAMESPACE, exsitUser.getUsername());
            session.setAttribute("totalSize", exsitUser.getTotalSize());
            return "redirect:/index.action";
        }else{
            request.setAttribute("msg", "用户名或密码错误");
            return "login";
        }
    }

    @RequestMapping("/logout")
    public String logout(HttpServletRequest request){
        request.getSession().invalidate();
        return "redirect:/user/login.action";
    }

}