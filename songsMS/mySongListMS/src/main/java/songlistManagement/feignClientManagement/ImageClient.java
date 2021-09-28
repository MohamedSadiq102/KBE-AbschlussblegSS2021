package songlistManagement.feignClientManagement;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("ImageMS")
public interface ImageClient {

    @RequestMapping("/image/{id}")
    byte[] getImageofSong(@RequestParam int id);
}
