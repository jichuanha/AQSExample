package com.chuan;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author jc
 * @Description:
 * @date 2019/5/21
 */
public class Collect {


    public static void main(String[] args) {

        List<User> users = new ArrayList<>();
        User u1 = new User();
        u1.setAddress("����");
        u1.setName("����");
        u1.setAge(10);

        User u2 = new User();
        u2.setAddress("����");
        u2.setName("����");
        u2.setAge(11);

        users.add(u1);
        users.add(u2);

        Stream<User> stream = users.stream();

        List<String> names = users.stream().map(user ->
            user.getName()
        ).collect(Collectors.toList());

        System.out.println(u1);

    }
}
