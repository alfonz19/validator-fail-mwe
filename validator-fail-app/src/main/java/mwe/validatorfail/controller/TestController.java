package mwe.validatorfail.controller;

import jakarta.validation.Valid;
import mwe.validatorfail.dto.AnnotatedSampleDTO;
import mwe.validatorfail.dto.SampleDTO;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = {"/test"})
@Validated
public class TestController {


    @PostMapping(path = "annotated")
    public String annotated(@RequestBody @Valid AnnotatedSampleDTO request) {
        return "OK";
    }

    @PostMapping(path = "xml")
    public String xml(@RequestBody @Valid SampleDTO request) {
        return "OK";
    }


}
