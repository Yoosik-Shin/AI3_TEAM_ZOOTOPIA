package com.aloha.zootopia.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/map")
public class MapController {

    @GetMapping("/map")
    public String zooPage() {
        return "map/map"; // templates/map.html
    }
}
