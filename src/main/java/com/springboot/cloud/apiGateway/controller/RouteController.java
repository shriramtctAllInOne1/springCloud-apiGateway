package com.springboot.cloud.apiGateway.controller;

import com.springboot.cloud.apiGateway.config.RefreshableRoutesLocator;
import com.springboot.cloud.apiGateway.domain.RouteDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
public class RouteController {

    @Autowired
    RefreshableRoutesLocator refreshableRoutesLocator;

    @RequestMapping("/")
    public @ResponseBody
    String showRoute() {
        List<String> routePaths = new ArrayList<>();
        refreshableRoutesLocator.getRoutes().subscribe(route ->
                routePaths.add("<tr>" + "<td>" + route.getPredicate().toString() + "</td>"
                        + "<td>" + route.getUri().toString() + "</td>" + "</tr>"));
        return "<html>\n" +
                "<body style=\"text-align : center \">\n" +
                "<h1> Api Gateway</h1>\n" +
                "<h3> Routing rules </h3>\n" +
                "<table>" +
                String.join(" ", routePaths) +
                "</table>" +
                "</body>\n" +
                "</html>";
    }
}
