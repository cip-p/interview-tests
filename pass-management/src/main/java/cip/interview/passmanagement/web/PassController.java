package cip.interview.passmanagement.web;

import cip.interview.passmanagement.domain.Pass;
import cip.interview.passmanagement.exceptions.InvalidPassException;
import cip.interview.passmanagement.exceptions.PassAlreadyExistsException;
import cip.interview.passmanagement.exceptions.PassNotFoundException;
import cip.interview.passmanagement.service.PassService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/vendors/{vendorId}/passes/{passId}")
public class PassController {

    private final PassService passService;

    public PassController(PassService passService) {
        this.passService = passService;
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody PassRequest passRequest, @PathVariable String vendorId, @PathVariable String passId) {

        Pass pass = createPassFromRequest(passRequest, vendorId, passId);

        passService.create(pass);

        return status(CREATED).build();
    }

    @DeleteMapping
    public ResponseEntity<Void> cancel(@PathVariable String vendorId, @PathVariable String passId) {

        passService.cancel(vendorId, passId);

        return status(NO_CONTENT).build();
    }

    @PutMapping("/renew")
    public ResponseEntity<Void> renew(@RequestBody Long passLength, @PathVariable String vendorId, @PathVariable String passId) {

        passService.renew(vendorId, passId, passLength);

        return ok().build();
    }

    @GetMapping("/is-active")
    public ResponseEntity<Boolean> isActive(@PathVariable String vendorId, @PathVariable String passId) {

        boolean active = passService.isActive(vendorId, passId);

        return ok(active);
    }

    private Pass createPassFromRequest(PassRequest passRequest, String vendorId, String passId) {
        Pass pass = new Pass();
        pass.setVendorId(vendorId);
        pass.setPassId(passId);
        pass.setCustomerName(passRequest.getCustomerName());
        pass.setHomeCity(passRequest.getHomeCity());
        pass.setPassCity(passRequest.getPassCity());
        pass.setPassLength(passRequest.getPassLength());
        return pass;
    }

    @ExceptionHandler
    @ResponseStatus(BAD_REQUEST)
    private void invalidPassExceptionHandler(InvalidPassException ex) {
    }

    @ExceptionHandler
    @ResponseStatus(CONFLICT)
    private void passAlreadyExistsExceptionHandler(PassAlreadyExistsException ex) {
    }

    @ExceptionHandler
    @ResponseStatus(NOT_FOUND)
    private void passNotFoundExceptionHandler(PassNotFoundException ex) {
    }
}
