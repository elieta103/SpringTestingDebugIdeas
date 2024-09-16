package com.debuggeando_ideas.music_app.service;


import com.debuggeando_ideas.music_app.DataDummy;
import com.debuggeando_ideas.music_app.entity.TrackEntity;
import com.debuggeando_ideas.music_app.repository.TrackRepository;
import com.debuggeando_ideas.music_app.service.impl.TrackServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;


import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class TrackServiceTest extends SpecServiceTest {

    @Mock
    TrackRepository trackRepositoryMock;

    @InjectMocks
    TrackServiceImpl trackService;

    static final Long VALID_ID = 1L;
    static final Long INVALID_ID = 2L;


    @Test
    @Order(1)
    @DisplayName("01. findById should works")
    void findByIdTest(){
        when(trackRepositoryMock.findById(eq(VALID_ID))).thenReturn(Optional.of(DataDummy.TRACK_1));
        when(trackRepositoryMock.findById(eq(INVALID_ID))).thenReturn(Optional.empty());

        var result = trackService.findById(VALID_ID);
        // Opcion 1
        //assertEquals(DataDummy.TRACK_1, result);
        // Cuando no encuentra lanza una exception
        //assertThrows(NoSuchElementException.class, ()->trackService.findById(INVALID_ID));

        // Opcion 2
        assertAll(
                () -> assertEquals(DataDummy.TRACK_1, result),
                () -> assertThrows(NoSuchElementException.class, ()->trackService.findById(INVALID_ID))
        );

    }

    @Test
    @Order(2)
    @DisplayName("02. getAll should works")
    void getAllTest(){
        Set<TrackEntity> expected = Set.of(DataDummy.TRACK_4, DataDummy.TRACK_2);
        when(trackRepositoryMock.findAll()).thenReturn(expected);
        var result = trackService.getAll();
        assertEquals(expected.size(), result.size());
        assertEquals(expected, result);

        Set<TrackEntity> expectedEmpty = Collections.EMPTY_SET;
        when(trackRepositoryMock.findAll()).thenReturn(expectedEmpty);
        var resultEmpty = trackService.getAll();
        assertEquals(expectedEmpty.size(), resultEmpty.size());
        assertEquals(expectedEmpty, resultEmpty);
    }

    @Test
    @Order(3)
    @DisplayName("03. save should works")
    void saveTest(){
        var expected = DataDummy.TRACK_2;

        when(trackRepositoryMock.save(any(TrackEntity.class)))
                .thenReturn(expected);

        var result = trackService.save(expected);

        assertNotNull(result.getTrackId());
        assertEquals(expected, result);
        verify(trackRepositoryMock, times(1)).save(any(TrackEntity.class));
    }

    @Test
    @Order(4)
    @DisplayName("04. delete should works")
    void deleteTest(){
        trackService.delete(VALID_ID);
        verify(trackRepositoryMock, times(1))
                .deleteById(eq(VALID_ID));
    }


    @Test
    @Order(5)
    @DisplayName("05. update should works")
    void updateTest(){
        var expected = DataDummy.TRACK_1;
        when(trackRepositoryMock.existsById(VALID_ID)).thenReturn(true);
        when(trackRepositoryMock.findById(VALID_ID)).thenReturn(Optional.of(expected));
        when(trackRepositoryMock.save(any(TrackEntity.class))).thenReturn(expected);

        var result = trackService.update(DataDummy.TRACK_1, VALID_ID);

        System.out.println(result);
        assertEquals(expected, result);
        verify(trackRepositoryMock, times(1)).existsById(eq(VALID_ID));
    }

    @Test
    @Order(6)
    @DisplayName("06. update should works, exception")
    void updateExceptionTest(){
        when(trackRepositoryMock.existsById(INVALID_ID)).thenReturn(false);

        assertThrows(NoSuchElementException.class,
                ()-> trackService.update(DataDummy.TRACK_1, INVALID_ID));

        verify(trackRepositoryMock, times(1)).existsById(eq(INVALID_ID));
        verify(trackRepositoryMock, times(0)).findById(eq(INVALID_ID));
        verify(trackRepositoryMock, times(0)).save(any(TrackEntity.class));


    }
}
