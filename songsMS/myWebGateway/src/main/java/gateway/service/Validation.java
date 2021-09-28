package gateway.service;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
@Scope("singleton")
public class Validation {

    private HashMap<String, String> session;

    public Validation() {
        this.session = new HashMap<>();
    }

    public void addSession(String token, String userid) {
        String s = this.session.put(token, userid);
    }

    public String getUserId(String token) {
        return this.session.get(token);
    }

    public boolean validSession(String token) {
        if (token == null) {
            return false;
        }
        System.out.println("logged in Users:" + session.size());
        return session.get(token) != null;
    }
}
