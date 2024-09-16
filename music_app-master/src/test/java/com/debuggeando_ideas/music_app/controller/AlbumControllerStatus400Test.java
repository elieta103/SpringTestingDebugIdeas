package com.debuggeando_ideas.music_app.controller;

import com.debuggeando_ideas.music_app.DataDummy;
import com.debuggeando_ideas.music_app.service.IAlbumService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.NoSuchElementException;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class AlbumControllerStatus400Test extends SpecAlbumController {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    IAlbumService albumServiceMock;

    ObjectMapper objectMapper;

    private static final Long INVALID_ID = 2L;
    private static final String RESOURCE_PATH = "/v1/album";

    @BeforeEach
    void init() {
        this.objectMapper = new ObjectMapper();
    }

    @BeforeEach
    void setupMocks() {
        doThrow(NoSuchElementException.class)
                .when(this.albumServiceMock).findById(eq(INVALID_ID));

        when(this.albumServiceMock.save(eq(DataDummy.ALBUM_DTO_INVALID)))
                .thenReturn(DataDummy.ALBUM_DTO_INVALID);

    }

    @Test
    @Order(1)
    @DisplayName("01. call findById should response 404")
    void findById() throws Exception {
        final var uri = RESOURCE_PATH + "/" + INVALID_ID;
        this.mockMvc.perform(
                        get(uri).contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound());
        verify(this.albumServiceMock).findById(eq(INVALID_ID));
    }

    @Test
    @Order(2)
    @DisplayName("02. call save should response 400")
    void save() throws Exception {
        this.mockMvc.perform(
                        post(RESOURCE_PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(this.objectMapper.writeValueAsString(DataDummy.ALBUM_DTO_INVALID)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.errors").isMap())
                .andExpect(jsonPath("$.errors.name").value("Must start with Upper"))
                .andExpect(jsonPath("$.errors.autor").value("Must start with Upper"));

        verify(this.albumServiceMock, times(0)).save(eq(DataDummy.ALBUM_DTO_INVALID));
    }

    @Test
    @Order(3)
    @DisplayName("03. call update should response 404")
    void update() throws Exception {
        final var uri = RESOURCE_PATH + "/" + INVALID_ID;
        final var albumToUpdate = DataDummy.ALBUM_DTO;
        albumToUpdate.setAutor("Autor updated");

        doThrow(NoSuchElementException.class)
                .when(this.albumServiceMock).update(eq(albumToUpdate), eq(INVALID_ID));

        mockMvc.perform(
                        put(uri)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(this.objectMapper.writeValueAsString(albumToUpdate)))
                .andExpect(status().isNotFound());
        verify(this.albumServiceMock).update(eq(albumToUpdate), eq(INVALID_ID));
    }

    @Test
    @Order(4)
    @DisplayName("04. call delete should response 404")
    void delete() throws Exception {
        final var uri = RESOURCE_PATH + "/" + INVALID_ID;

        doThrow(NoSuchElementException.class)
                .when(this.albumServiceMock).delete(eq(INVALID_ID));

        this.mockMvc.perform(MockMvcRequestBuilders.delete(uri))
                .andExpect(status().isNotFound());

        verify(this.albumServiceMock).delete(eq(INVALID_ID));
    }

}
