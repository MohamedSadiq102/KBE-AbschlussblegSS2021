package pictureManagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class SongPictureApp {

    public static void main(String[] args) {
        SpringApplication.run(SongPictureApp.class, args);
    }
}
