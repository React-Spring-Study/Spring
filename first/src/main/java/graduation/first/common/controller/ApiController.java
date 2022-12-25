package graduation.first.common.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ApiController {

    private final Environment environment;

    @GetMapping("/api/health")
    public String healthCheck() {
        return "Health Checking";
    }
}
