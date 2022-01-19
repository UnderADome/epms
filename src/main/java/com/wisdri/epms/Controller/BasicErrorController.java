package com.wisdri.epms.Controller;

import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

//@RestController
//@RequestMapping({"${server.error.path:${error.path:/error}}"})
//public class BasicErrorController extends AbstractErrorController {
//
//    public BasicErrorController(ErrorAttributes errorAttributes) {
//        super(errorAttributes);
//    }
//
//    public BasicErrorController(ErrorAttributes errorAttributes, List<ErrorViewResolver> errorViewResolvers) {
//        super(errorAttributes, errorViewResolvers);
//    }
//}
