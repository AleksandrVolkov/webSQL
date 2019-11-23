package com.java.sql;


import com.java.sql.domain.Users;
import com.java.sql.repos.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class GreetingController {
    @Autowired
    private UsersRepo usersRepo;

    @GetMapping("/greeting")
    public String greeting(
            @RequestParam(name = "name", required = false, defaultValue = "World") String name,
            Map<String, Object> model
    ) {
        model.put("name", name);
        return "greeting";
    }

    @GetMapping
    public String main(Map<String, Object> model) {
        Iterable<Users> users = usersRepo.findAll();

        model.put("users", users);

        return "main";
    }

    @PostMapping("/add")
    public String add(@RequestParam String firstName, @RequestParam String lastName, @RequestParam String login, Map<String, Object> model) {
        Iterable<Users> users;
        if (firstName != null && !firstName.isEmpty() &&
                lastName != null && !lastName.isEmpty() &&
                login != null && !login.isEmpty()) {

            Users user = new Users(firstName, lastName, login);
            usersRepo.save(user);
            users = usersRepo.findAll();
        } else {
            users = usersRepo.findAll();
        }
        model.put("users", users);
        return "main";
    }

    @PostMapping("/remove")
    public String remove(@RequestParam String id, Map<String, Object> model) {
        usersRepo.deleteById(Long.parseLong(id));
        Iterable<Users> users = usersRepo.findAll();

        model.put("users", users);
        return "main";
    }

    @PostMapping("/edit")
    public String edit(@RequestParam String id, @RequestParam String firstName, @RequestParam String lastName, @RequestParam String login, Map<String, Object> model) {
        usersRepo.findById(Long.parseLong(id)).get().setFirstName(firstName);
        usersRepo.findById(Long.parseLong(id)).get().setLastName(lastName);
        usersRepo.findById(Long.parseLong(id)).get().setLogin(login);
        Iterable<Users> users = usersRepo.findAll();

        model.put("users", users);
        return "main";
    }

}
