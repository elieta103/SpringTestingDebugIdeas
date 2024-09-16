package com.debuggeando_ideas.music_app.service;


import com.debuggeando_ideas.music_app.DataDummy;
import com.debuggeando_ideas.music_app.dto.TrackDTO;
import com.debuggeando_ideas.music_app.entity.AlbumEntity;
import com.debuggeando_ideas.music_app.repository.AlbumRepository;
import com.debuggeando_ideas.music_app.repository.RecordCompanyRepository;
import com.debuggeando_ideas.music_app.repository.TrackRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AlbumServiceTest extends SpecServiceTest {

    @MockBean
    AlbumRepository albumRepositoryMock;

    @MockBean
    TrackRepository trackRepositoryMock;

    @MockBean
    RecordCompanyRepository recordCompanyRepositoryMock;


    @Autowired
    IAlbumService albumService;

    static final Long VALID_ID = 1L;
    static final Long INVALID_ID = 2L;

    @BeforeEach
    void setMocks() {
    }

    @Test
    @Order(1)
    @DisplayName("01. findById should works! Happy path & Exception")
    void findByIdTest() {
        when(albumRepositoryMock.findById(VALID_ID)).thenReturn(Optional.of(DataDummy.ALBUM));
        when(albumRepositoryMock.findById(INVALID_ID)).thenReturn(Optional.empty());

        var result = albumService.findById(VALID_ID);
        assertEquals(DataDummy.ALBUM_DTO, result);
        verify(albumRepositoryMock, times(1)).findById(eq(VALID_ID));

        // Cuando no encuentra lanza una exception
        assertThrows(NoSuchElementException.class, () -> {
            albumService.findById(INVALID_ID);
            verify(albumRepositoryMock, times(1)).findById(eq(INVALID_ID));
        });

    }


    @Test
    @Order(2)
    @DisplayName("02. getAll should works!, Exception ")
    void getAllExceptionTest() {
        when(albumRepositoryMock.findAll()).thenReturn(Collections.emptyList());

        // Cuando no encuentra lanza una exception
        assertThrows(NoSuchElementException.class, () -> {
            albumService.getAll();
            verify(albumRepositoryMock, times(1)).findAll();
        });
    }

    @Test
    @Order(3)
    @DisplayName("03. getAll should works!, OK Data ")
    void getAllDataTest() {
        var expected = Set.of(DataDummy.ALBUM_DTO);
        when(albumRepositoryMock.findAll()).thenReturn(List.of(DataDummy.ALBUM));

        var result = albumService.getAll();

        assertEquals(expected, result);
        assertFalse(result.isEmpty());
        assertEquals(expected.size(), result.size());
        verify(albumRepositoryMock, times(1)).findAll();
    }

    @Test
    @Order(4)
    @DisplayName("04. save should works!")
    void getSaveTest() {
        when(recordCompanyRepositoryMock.findById(anyString()))
                .thenReturn(Optional.of(DataDummy.RECORD_COMPANY));
        when(albumRepositoryMock.save(any(AlbumEntity.class)))
                .thenReturn(DataDummy.ALBUM);

        var result = albumService.save(DataDummy.ALBUM_DTO);

        assertEquals(DataDummy.ALBUM_DTO, result);
        verify(recordCompanyRepositoryMock, times(1)).findById(anyString());
        verify(albumRepositoryMock, times(1)).save(any(AlbumEntity.class));
    }


    @Test
    @Order(5)
    @DisplayName("05. delete should works! Exception")
    void deleteExpressTest() {
        when(albumRepositoryMock.findById(INVALID_ID)).thenReturn(Optional.empty());

        // Cuando no encuentra lanza una exception
        assertThrows(NoSuchElementException.class, () -> {
            albumService.delete(INVALID_ID);
            verify(albumRepositoryMock, times(1)).findById(eq(INVALID_ID));
        });
    }

    @Test
    @Order(6)
    @DisplayName("06. delete should works! Happy path OK")
    void deleteOKTest() {
        when(albumRepositoryMock.findById(VALID_ID)).thenReturn(Optional.of(DataDummy.ALBUM));
        doNothing().when(albumRepositoryMock).deleteById(VALID_ID);

        albumService.delete(VALID_ID);

        verify(albumRepositoryMock, times(1)).findById(eq(VALID_ID));
        verify(albumRepositoryMock, times(1)).deleteById(eq(VALID_ID));

    }

    @Test
    @Order(7)
    @DisplayName("07. update should works! Exception")
    void updateExceptionTest() {
        when(albumRepositoryMock.findById(VALID_ID)).thenReturn(Optional.empty());

        // Cuando no encuentra lanza una exception
        assertThrows(NoSuchElementException.class, () -> {
            albumService.delete(INVALID_ID);
            verify(albumRepositoryMock, times(1)).findById(eq(INVALID_ID));
        });
    }

    @Test
    @Order(8)
    @DisplayName("08. update should works! OK")
    void updateOkTest() {
        when(albumRepositoryMock.findById(VALID_ID)).thenReturn(Optional.of(DataDummy.ALBUM));
        when(albumRepositoryMock.save(any(AlbumEntity.class))).thenReturn(DataDummy.ALBUM);

        var result = albumService.update(DataDummy.ALBUM_DTO, VALID_ID);

        assertTrue(Objects.nonNull(result));
        verify(albumRepositoryMock, times(2)).findById(eq(VALID_ID));
        verify(albumRepositoryMock, times(1)).save(any(AlbumEntity.class));

    }

    @Test
    @Order(9)
    @DisplayName("09. findBetweenPrice, Exception")
    void findBetweenPriceExceptionTest() {
        when(albumRepositoryMock.findByPriceBetween(anyDouble(), anyDouble())).thenReturn(Collections.emptySet());
        assertThrows(NoSuchElementException.class,
                () -> {
                    albumService.findBetweenPrice(anyDouble(), anyDouble());
                    verify(albumRepositoryMock, times(1)).findByPriceBetween(anyDouble(), anyDouble());
                });
    }

    @Test
    @Order(10)
    @DisplayName("10. findBetweenPrice, OK")
    void findBetweenPriceOKTest() {
        when(albumRepositoryMock.findByPriceBetween(anyDouble(), anyDouble())).thenReturn(Set.of(DataDummy.ALBUM));
        var result = albumService.findBetweenPrice(anyDouble(), anyDouble());
        assertEquals(Set.of(DataDummy.ALBUM).size(), result.size());
        verify(albumRepositoryMock, times(1)).findByPriceBetween(anyDouble(), anyDouble());
    }

    @Test
    @Order(11)
    @DisplayName("11. addTrack, Exception ")
    void addTrackExceptionTest() {
        when(albumRepositoryMock.findById(INVALID_ID)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class,
                () -> {
                    albumService.addTrack(any(TrackDTO.class), INVALID_ID);
                    verify(albumRepositoryMock, times(1)).findById(INVALID_ID);
                });
    }

    @Test
    @Order(12)
    @DisplayName("12. addTrack, OK")
    void addTrackOkTest() {
        when(albumRepositoryMock.findById(VALID_ID)).thenReturn(Optional.of(DataDummy.ALBUM));
        when(albumRepositoryMock.save(any(AlbumEntity.class)))
                .thenReturn(DataDummy.ALBUM);

        var result = albumService.addTrack(DataDummy.TRACK_1_DTO, VALID_ID);

        assertEquals(DataDummy.ALBUM_DTO, result);
        verify(albumRepositoryMock, times(2)).findById(anyLong());
        verify(albumRepositoryMock, times(1)).save(any(AlbumEntity.class));
    }

    @Test
    @Order(13)
    @DisplayName("13. removeTrack, Exception ")
    void removeTrackExceptionTest() {
        when(albumRepositoryMock.existsById(INVALID_ID)).thenReturn(false);
        assertThrows(NoSuchElementException.class,
                () -> {
                    albumService.removeTrack(any(TrackDTO.class), INVALID_ID);
                    verify(albumRepositoryMock, times(1)).existsById(INVALID_ID);
                });
    }

    @Test
    @Order(14)
    @DisplayName("14. removeTrack, OK")
    void removeTrackOkTest() {
        when(albumRepositoryMock.findById(VALID_ID)).thenReturn(Optional.of(DataDummy.ALBUM));
        when(trackRepositoryMock.existsById(VALID_ID)).thenReturn(true);
        when(albumRepositoryMock.save(any(AlbumEntity.class)))
                .thenReturn(DataDummy.ALBUM);

        var result = albumService.removeTrack(DataDummy.TRACK_1_DTO, VALID_ID);

        assertEquals(DataDummy.ALBUM_DTO, result);
        verify(albumRepositoryMock, times(1)).findById(anyLong());
        verify(trackRepositoryMock, times(1)).existsById(anyLong());
        verify(albumRepositoryMock, times(2)).save(any(AlbumEntity.class));

    }
}
