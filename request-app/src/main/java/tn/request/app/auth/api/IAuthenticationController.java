package tn.request.app.auth.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import tn.request.app.auth.dto.LoginRequest;
import tn.request.app.auth.dto.LoginResponse;
import tn.request.app.auth.dto.UserRegistrationRequest;

public interface IAuthenticationController {
    @Operation(summary = "Create a new user and send a confirmation link to user email.")
    @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json")})
    @ApiResponse(responseCode = "400", description = "Invalid request", content = {@Content(mediaType = "application/json")})
    @ApiResponse(responseCode = "409", description = "A user already registered with the same email", content = {@Content(mediaType = "application/json")})
    ResponseEntity<UserRegistrationRequest> register(@RequestBody UserRegistrationRequest request);

    @Operation(summary = "Confirm user email")
    @ApiResponse(responseCode = "200", content = {@Content(mediaType = MimeTypeUtils.TEXT_HTML_VALUE)})
    @ApiResponse(responseCode = "400", description = "Token is either invalid or expired", content = {@Content(mediaType = MimeTypeUtils.APPLICATION_JSON_VALUE)})
    ResponseEntity<Object> activateAccount(@RequestParam("token") String token);

    ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request);
}
