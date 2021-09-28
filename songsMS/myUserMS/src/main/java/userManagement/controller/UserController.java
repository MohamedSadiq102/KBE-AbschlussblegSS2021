package userManagement.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import userManagement.model.AccessAgent;
import userManagement.model.MyUser;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import userManagement.repository.UserRepository;

import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping(value = "/auth")
public class UserController {


    private final UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping(headers = "Accept=application/json", produces = {MediaType.TEXT_PLAIN_VALUE})
    public ResponseEntity<String> authorize(@RequestBody AccessAgent accessAgent) {
        List<MyUser> list = userRepository.findUser(accessAgent.getUserid());
        if (list != null && list.size() == 1 && list.get(0).getPassword().equals(accessAgent.getPassword()) && list.get(0).getUserid().equals(accessAgent.getUserid())) {
            String generationtoken = list.get(0).getFirstName() + list.get(0).getLastName() + list.get(0).getUserid() + System.currentTimeMillis();
            byte[] bs = generationtoken.getBytes(StandardCharsets.UTF_8);
            for (int i = 0; i < bs.length; i++) {
                bs[i] = (byte) (~bs[i] * (-1));
                bs[i] = (byte) ((bs[i]) % 94 + 32);

                if (bs[i] < 0) {
                    bs[i] = (byte) (bs[i] * (-1));
                }
                if (32 < bs[i] && bs[i] < 48) {
                    bs[i] += 15;
                }
                if (57 < bs[i] && bs[i] < 65) {
                    bs[i] += 7;
                }
                if (90 < bs[i] && bs[i] < 97) {
                    bs[i] += 6;
                }
                System.out.println(bs[i]);
            }
            String token = new String(bs, StandardCharsets.UTF_8);
            HttpHeaders header = new HttpHeaders();
            header.setContentType(MediaType.TEXT_PLAIN);
            header.set("USERID", list.get(0).getUserid());
            header.set("TOKEN", token);
            return ResponseEntity.ok().headers(header).body(token);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}
