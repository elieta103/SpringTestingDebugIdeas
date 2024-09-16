package com.debuggeando_ideas.music_app.controller;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

@WebMvcTest(value = AlbumController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SpecAlbumController {

}