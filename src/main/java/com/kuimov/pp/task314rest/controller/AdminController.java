package com.kuimov.pp.task314rest.controller;

import com.kuimov.pp.task314rest.service.RoleService;
import com.kuimov.pp.task314rest.service.RoleServiceImp;
import com.kuimov.pp.task314rest.service.UserServiceImp;
import com.kuimov.pp.task314rest.models.Role;
import com.kuimov.pp.task314rest.models.User;
import com.kuimov.pp.task314rest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserServiceImp userService, RoleServiceImp roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("")
    public String showAllUsers(ModelMap modelMap, @AuthenticationPrincipal User user) {
        modelMap.addAttribute("list", userService.getAllUsers());
        modelMap.addAttribute("roles", roleService.getAllRoles());
        modelMap.addAttribute("user", user);
        return "/adminPage";
    }

    @GetMapping(path = {"/user", "/api/incomingUser"})
    public String showLoggedInUserInformaion(@AuthenticationPrincipal User user, ModelMap model) {
        model.addAttribute("user", user);
        model.addAttribute("roles", user.getRoles());
        return "/adminUserPage";
    }

    @PostMapping("/save")
    public String processUserRegistration(@ModelAttribute User user,
                                  @RequestParam(value = "roless",
                                          required = false,
                                          defaultValue = "USER") Set<String> roles) {
        Set<Role> setRoles = roleService.getSetRoles(roles);
        user.setRoles(setRoles);
        userService.save(user);
        return "redirect:/admin";
    }

    @PostMapping(value = "/users/delete/{id}")
    public String deleteUser(@PathVariable("id") long id) {
        User user = userService.getUserById(id);
        userService.delete(user);
        return "redirect:/admin";
    }
}
