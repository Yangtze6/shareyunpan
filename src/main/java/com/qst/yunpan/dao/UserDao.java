package com.qst.yunpan.dao;

import com.qst.yunpan.pojo.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface UserDao {

    User findUser(User user) throws Exception;
    User checkUser(User user) throws Exception;
    void addUser(User user) throws Exception;

    String getCountSize(String username);

    void reSize(@Param("username") String username, @Param("formatSize") String formatSize) throws Exception;

}
