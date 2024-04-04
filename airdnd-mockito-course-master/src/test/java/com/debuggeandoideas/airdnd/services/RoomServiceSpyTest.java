package com.debuggeandoideas.airdnd.services;


import com.debuggeandoideas.airdnd.Services.RoomService;
import com.debuggeandoideas.airdnd.repositories.RoomRepository;
import com.debuggeandoideas.airdnd.utils.DataDummy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RoomServiceSpyTest {

    @Spy
    private RoomRepository roomRepository;

    @InjectMocks
    private RoomService roomService;

    @BeforeEach
    void init(){
        //roomRepository = mock(RoomRepository.class);
        //roomService = new RoomService(roomRepository);
    }

    @Test
    @DisplayName("Should get all rooms available in roomRepository, @Spy")
    void findAllAvailableRooms(){
        //when(roomRepository.findAll()).thenReturn(DataDummy.defaultsRooms);
        var expected = 4;  //Avalilable = true
        var result = roomService.findAllAvailableRooms();

        assertEquals(expected, result.size());
    }

}
