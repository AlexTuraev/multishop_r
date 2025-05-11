package org.tasks.myshop.controller;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.tasks.myshop.exception.LoadItemException;
import org.tasks.myshop.exception.SortException;

import java.io.IOException;

@ControllerAdvice
public class HandlerExceptionController {

    @ExceptionHandler(LoadItemException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleIOException(Model model, IOException ex) {
        model.addAttribute("reason", ex.getMessage());
        return "load-error";
    }

    @ExceptionHandler(SortException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleSortException(Model model, SortException ex) {
        model.addAttribute("reason", ex.getMessage());
        return "load-error";
    }

}
