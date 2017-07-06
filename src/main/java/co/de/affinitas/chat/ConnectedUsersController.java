package co.de.affinitas.chat;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
public class ConnectedUsersController {

    @RequestMapping("/connect")
    public void connect(HttpSession session, String username) {

    }

 }
