package com.debuggeandoideas.airdnd.utils;

import com.debuggeandoideas.airdnd.dto.BookingDto;
import com.debuggeandoideas.airdnd.dto.RoomDto;

import java.time.LocalDate;
import java.util.*;

public class DataDummy {
    private DataDummy(){
    }

    public static  final Map<RoomDto, Boolean> defaultsRooms = new HashMap<>() {{
        put(new RoomDto("A", 2), true);
        put(new RoomDto("B", 2), true);
        put(new RoomDto("C", 3), true);
        put(new RoomDto("D", 2), false);
        put(new RoomDto("E", 2), false);
        put(new RoomDto("F", 3), false);
    }};

    public static  final List<RoomDto> defaultsRoomsList = new ArrayList<>(){{
        add(new RoomDto("A", 2));
        add(new RoomDto("B", 2));
        add(new RoomDto("C", 3));
        add(new RoomDto("D", 2));
        add(new RoomDto("E", 2));
        add(new RoomDto("F", 3));
    }};

    public static  final List<RoomDto> singleRoomsList = new ArrayList<>(){{
        add(new RoomDto("A", 5));
    }};


    public static final BookingDto bookingRequest1 = new BookingDto(
            "14697",
            LocalDate.of(2025,06, 10),
            LocalDate.of(2025, 06, 20),
            2,
            false);

    public static final BookingDto bookingRequest2 = new BookingDto(
            "546879",
            LocalDate.of(2025,06, 15),
            LocalDate.of(2025, 06, 20),
            2,
            false);

    public static final BookingDto bookingRequest3 = new BookingDto(
            "878789",
            LocalDate.of(2025,06, 16),
            LocalDate.of(2025, 06, 20),
            2,
            false);

    public static final BookingDto bookingRequestException = new BookingDto(
            "878789",
            LocalDate.of(2025,06, 20),
            LocalDate.of(2025, 06, 26),
            2,
            true);

}
