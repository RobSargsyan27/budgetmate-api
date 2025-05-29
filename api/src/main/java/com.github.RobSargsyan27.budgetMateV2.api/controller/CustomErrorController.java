package com.github.RobSargsyan27.budgetMateV2.api.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
public class CustomErrorController implements ErrorController {
    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        Object statusCode = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object errorMsg = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
        Object errorUri = request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);
        Object errorType = request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);

        log.error("Status code: {}", statusCode);
        log.error("Error message: {}", errorMsg);
        log.error("Error URI: {}", errorUri);
        log.error("Error Type: {}", errorType);

        if(statusCode != null){
            int _statusCode = Integer.parseInt(statusCode.toString());

            if(_statusCode == HttpStatus.NOT_FOUND.value()){
                return "redirect:/login";
            }

            if(_statusCode == HttpStatus.FORBIDDEN.value()){
                return "redirect:/dashboard";
            }

            if(_statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()){
                return "redirect:/500-error";
            }

        }

        return "error";
    }
}
