import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

public class SweetShopApplication {
    @RestController
class TestController {
    @GetMapping("/api/health")
    public String health() {
        return "Backend is Active!";
    }
}
}
