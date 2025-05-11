package org.tasks.myshop.controller;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.tasks.myshop.exception.LoadItemException;
import org.tasks.myshop.service.MyshopService;

@Controller
@RequestMapping("/upload")
public class UploadController {
    private final MyshopService myshopService;

    public UploadController(MyshopService myshopService) {
        this.myshopService = myshopService;
    }

    @GetMapping
    public String getUploadPage() {
        return "loaditems";
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String loadItemsFromCsv(
            @RequestPart("uploadcsvfile") MultipartFile file,
            @RequestPart("images") MultipartFile[] images) throws LoadItemException {
        myshopService.loadItemsFromCsv(file, images).block();
        return "load-success";
    }

}
