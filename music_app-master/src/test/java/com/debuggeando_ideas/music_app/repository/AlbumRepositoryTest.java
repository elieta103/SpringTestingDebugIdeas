package com.debuggeando_ideas.music_app.repository;

import com.debuggeando_ideas.music_app.DataDummy;
import com.debuggeando_ideas.music_app.entity.AlbumEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class AlbumRepositoryTest extends SpecRepositoryTest{

    @Autowired
    AlbumRepository albumRepository;

    // Datos válidos de la BD data.sql
    private static final Long VALID_ID = 100L;
    private static final Long INVALID_ID = 900L;

    @Test
    @Order(1)
    @DisplayName("01. findById Should works. Happy & Unhappy Path")
    void findByIdTest (){
        var result = albumRepository.findById(VALID_ID);
        var albumResult = result.get();

        assertTrue(result.isPresent());
        assertAll(
                () -> assertEquals("fear of the dark", albumResult.getName()),
                () -> assertEquals("iron maiden", albumResult.getAutor()),
                () -> assertEquals(280.50, albumResult.getPrice())
                );

        result = albumRepository.findById(INVALID_ID);
        assertTrue(result.isEmpty());
    }

    @Test
    @Order(2)
    @DisplayName("02. findAll Should works.")
    void findAllTest (){
        var result = (List<AlbumEntity>)albumRepository.findAll();

        assertFalse(result.isEmpty());
        assertEquals(2, result.size());
    }

    @Test
    @Order(3)
    @DisplayName("03. save Should works.")
    void saveTest () {
        var result = albumRepository.save(DataDummy.ALBUM);

        assertFalse(Objects.isNull(result));
        assertEquals(DataDummy.ALBUM, result);
    }

    @Test
    @Order(4)
    @DisplayName("04. findBetweenPrice Should works.")
    void findBetweenPriceTest () {
        var result = albumRepository.findByPriceBetween(270.00D, 271.00D);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    @Order(5)
    @DisplayName("05. deleteById Should works. void method")
    void deleteByIdTest () {
        var records = (List<AlbumEntity>)albumRepository.findAll();
        var totalRecords = records.size();
        assertEquals(2, totalRecords);

        var albumToDelete = albumRepository.findById(VALID_ID);
        albumRepository.deleteById(albumToDelete.get().getAlbumId());

        records = (List<AlbumEntity>)albumRepository.findAll();
        totalRecords = records.size();
        assertEquals(1, totalRecords);

    }
}
