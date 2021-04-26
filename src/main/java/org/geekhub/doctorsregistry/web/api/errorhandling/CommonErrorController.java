package org.geekhub.doctorsregistry.web.api.errorhandling;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class CommonErrorController implements ErrorController {

    @RequestMapping("/error")
    @ResponseBody
    public ErrorWithStatusDTO handleError(Model model, HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            if (statusCode == 400) {
                return new ErrorWithStatusDTO(statusCode, "Bad request");
            } else if (statusCode == 403) {
                return new ErrorWithStatusDTO(statusCode, "Sorry, you are not allowed to to that");
            } else if (statusCode == 404) {
                return new ErrorWithStatusDTO(statusCode, "Page you are looking for does not exist");
            } else if (statusCode < 500 && statusCode >= 400) {
                return new ErrorWithStatusDTO(statusCode, "Some unexpected error happened");
            }
        }
        return new ErrorWithStatusDTO(500, "Some unexpected error happened");
    }

    @Override
    public String getErrorPath() {
        return null;
    }
}

