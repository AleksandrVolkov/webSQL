package com.java.sql.controller;


import com.java.sql.domain.Post;
import com.java.sql.domain.Users;
import com.java.sql.repos.PostRepo;
import com.java.sql.repos.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Controller
public class MainController {
    @Autowired
    private UsersRepo usersRepo;

    @GetMapping("/")
    public String greeting(Map<String, Object> model) {
        return "greeting";
    }

    @GetMapping("/main")
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


    @Autowired
    private PostRepo postRepo;


    @PostMapping("/add_api")
    public String add_api(Map<String, Object> model) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<List<Post>> rateResponse =
                restTemplate.exchange("http://jsonplaceholder.typicode.com/posts?_limit=10",
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<Post>>() {
                        });
        List<Post> postsList = rateResponse.getBody();

        for (Post p : postsList) {
            Post post = new Post( p.getTitle(), p.getBody(), p.getUserId());
            postRepo.save(post);
        }
        Iterable<Post> posts = postRepo.findAll();

        model.put("posts", posts);

        return "main";
    }
    @PostMapping("/remove_all_api")
    public String remove_api(Map<String, Object> model) {
        try {
            postRepo.deleteAll();
        }catch (Exception e){}
        return "main";
    }
}
