package org.geekhub.doctorsregistry.web.common;

import org.geekhub.doctorsregistry.web.api.errorhandling.ErrorWithStatusDTO;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
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
    public ErrorWithStatusDTO handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        HttpStatus httpStatus = resolveStatus(status);
        return new ErrorWithStatusDTO(httpStatus, httpStatus.getReasonPhrase());
    }

    private HttpStatus resolveStatus(Object status) {
        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            return HttpStatus.valueOf(statusCode);
        }
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

    @Override
    public String getErrorPath() {
        return null;
    }
}

