package pl.kedziorek.mpkoperator.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.kedziorek.mpkoperator.service.FaultService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FaultController {
    private final FaultService faultService;


}
