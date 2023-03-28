package fr.univlyon1.m1if.m1if13.controller.ui;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import fr.univlyon1.m1if.m1if13.dto.model.user.UserDto;
import fr.univlyon1.m1if.m1if13.service.UserServiceInterface;

@Controller
public class AdminDashboardController {

  @Autowired
  private UserServiceInterface userService;

  @GetMapping(path = "/home")
  public ModelAndView listAllUsers() {
    ModelAndView modelAndView = new ModelAndView("home");
    Set<String> listOfUsersLogin = userService.getUsers();
    modelAndView.addObject("userLogin", listOfUsersLogin);
    return modelAndView;
  }

  @GetMapping("/manage/{login}")
  public String getUserInformation(@PathVariable("login") final String login, final Model model) {
    UserDto userInfo = userService.getUserByLogin(login);
    model.addAttribute("userInfo", userInfo);
    return "user-info";
  }

}
