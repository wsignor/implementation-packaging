package com.mobiquityinc.controller;

import com.mobiquityinc.exception.APIException;
import com.mobiquityinc.packer.Packer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/v1/package")
public class PackageController {

    @GetMapping("/fileName")
    public String getResult(@RequestParam String filePath) throws APIException {
        return Packer.pack(filePath);
    }
}
