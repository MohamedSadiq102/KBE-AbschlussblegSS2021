package userManagement.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;
import javax.persistence.*;


@Entity
@Table(name = "myuser")
public class MyUser {

    @Id
    @Column(name = "\"userid\"")
    private String userid;

    //    @Column(name = "password")
    private String password;

    //    @Column(name = "firstName")
    private String firstName;

    //    @Column(name = "lastName")
    private String lastName;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userId) {
        this.userid = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

}