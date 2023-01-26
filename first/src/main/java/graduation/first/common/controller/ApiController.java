package graduation.first.common.controller;

import graduation.first.common.response.StringResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ApiController {

    @GetMapping("/api/health")
    public StringResponse healthCheck() {
        return new StringResponse("Health check");
    }
}
