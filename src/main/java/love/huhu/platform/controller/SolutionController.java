package love.huhu.platform.controller;

import lombok.RequiredArgsConstructor;
import love.huhu.platform.authorization.AuthorizationRequired;
import love.huhu.platform.client.ManagerClient;
import love.huhu.platform.domain.Solution;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @Description
 * @Author nwl
 * @Create 2023-02-25 下午10:59
 */
@RestController
@RequestMapping("/api/solution")
@RequiredArgsConstructor
public class SolutionController {
    private final ManagerClient managerClient;
    @AuthorizationRequired
    @GetMapping
    public ResponseEntity<Object> getSolutions(Long problemId, Long solutionId) {
        return new ResponseEntity<>(managerClient.getSolution(problemId,solutionId),HttpStatus.OK);
    }

    @AuthorizationRequired
    @PostMapping
    public ResponseEntity<Object> saveSolution(@RequestBody Solution solution) {
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @AuthorizationRequired
    @PutMapping
    public ResponseEntity<Object> updateSolution(@RequestBody Solution solution) {
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @AuthorizationRequired
    @DeleteMapping
    public ResponseEntity<Object> deleteSolution(Long solutionId) {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
