package com.debuggeandoideas.airdnd.services;


import com.debuggeandoideas.airdnd.Services.BookingService;
import com.debuggeandoideas.airdnd.Services.PaymentService;
import com.debuggeandoideas.airdnd.Services.RoomService;
import com.debuggeandoideas.airdnd.dto.BookingDto;
import com.debuggeandoideas.airdnd.dto.RoomDto;
import com.debuggeandoideas.airdnd.helpers.MailHelper;
import com.debuggeandoideas.airdnd.repositories.BookingRepository;
import com.debuggeandoideas.airdnd.repositories.PaymentRepository;
import com.debuggeandoideas.airdnd.utils.CurrencyConverter;
import com.debuggeandoideas.airdnd.utils.DataDummy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {

    @Mock
    BookingRepository bookingRepository;
    @Mock
    PaymentRepository paymentRepository;
    @Mock
    MailHelper mailHelper;
    @Mock
    PaymentService paymentService;
    @Mock
    RoomService roomService;

    @Captor
    ArgumentCaptor<String> stringArgumentCaptor;

    @InjectMocks
    BookingService bookingService;

    @Test
    @DisplayName("0. getAvailablePlaceCount() should works")
    void getAvailablePlaceCountTest() {
        when(roomService.findAllAvailableRooms()).thenReturn(DataDummy.defaultsRoomsList);

        var expected = 14;
        var result = bookingService.getAvailablePlaceCount();

        assertEquals(expected, result);
    }

    @Test
    @DisplayName("1. booking(final BookingDto booking)  should works, Argument Matchers")
    void bookingTest_1() {
        var roomId = UUID.randomUUID().toString();
        //when(roomService.findAvailableRoom(DataDummy.bookingRequest1)).thenReturn(DataDummy.defaultsRoomsList.get(0));
        when(roomService.findAvailableRoom(any(BookingDto.class))).thenReturn(DataDummy.defaultsRoomsList.get(0));

        //when(bookingRepository.save(DataDummy.bookingRequest1)).thenReturn(roomId);
        when(bookingRepository.save(any(BookingDto.class))).thenReturn(roomId);

        var result = bookingService.booking(DataDummy.bookingRequest1);

        assertEquals(roomId, result);
    }

    @Test
    @DisplayName("2. booking(final BookingDto booking)  should works, doReturn...when  VS when...thenReturn")
    void bookingTest_2() {
        var roomId = UUID.randomUUID().toString();
        //when(roomService.findAvailableRoom(DataDummy.bookingRequest1)).thenReturn(DataDummy.defaultsRoomsList.get(0));
        doReturn(DataDummy.defaultsRoomsList.get(0)).when(roomService).findAvailableRoom(DataDummy.bookingRequest1);

        //when(bookingRepository.save(DataDummy.bookingRequest1)).thenReturn(roomId);
        doReturn(roomId).when(bookingRepository).save(DataDummy.bookingRequest1);

        var result = bookingService.booking(DataDummy.bookingRequest1);

        assertEquals(roomId, result);
    }


    @Test
    @DisplayName("3. booking(final BookingDto booking)  should works, verify")
    void bookingTest_3() {
        var roomId = UUID.randomUUID().toString();
        //when(roomService.findAvailableRoom(DataDummy.bookingRequest1)).thenReturn(DataDummy.defaultsRoomsList.get(0));
        doReturn(DataDummy.defaultsRoomsList.get(0)).when(roomService).findAvailableRoom(DataDummy.bookingRequest1);

        //when(bookingRepository.save(DataDummy.bookingRequest1)).thenReturn(roomId);
        doReturn(roomId).when(bookingRepository).save(DataDummy.bookingRequest1);

        var result = bookingService.booking(DataDummy.bookingRequest1);

        assertEquals(roomId, result);
        verify(roomService, times(1)).findAvailableRoom(any(BookingDto.class));
        verify(bookingRepository, times(1)).save(any(BookingDto.class));
    }


    @Test
    @DisplayName("4. booking(final BookingDto booking)  should works, metodo void & verify")
    void bookingTest_4() {
        var roomId = UUID.randomUUID().toString();
        //when(roomService.findAvailableRoom(DataDummy.bookingRequest1)).thenReturn(DataDummy.defaultsRoomsList.get(0));
        doReturn(DataDummy.defaultsRoomsList.get(0)).when(roomService).findAvailableRoom(DataDummy.bookingRequest1);

        //when(bookingRepository.save(DataDummy.bookingRequest1)).thenReturn(roomId);
        doReturn(roomId).when(bookingRepository).save(DataDummy.bookingRequest1);

        // Mocking metodo void
        doNothing().when(roomService).bookRoom(anyString());
        doNothing().when(mailHelper).sendMail(anyString(), anyString());

        var result = bookingService.booking(DataDummy.bookingRequest1);

        assertEquals(roomId, result);
        verify(roomService, times(1)).findAvailableRoom(any(BookingDto.class));
        verify(bookingRepository, times(1)).save(any(BookingDto.class));
        verify(roomService, times(1)).bookRoom(anyString());
        verify(mailHelper, times(1)).sendMail(anyString(), anyString());
    }

    @Test
    @DisplayName("5. booking(final BookingDto booking)  should works, with exceptions. Unhappy path")
    void bookingExceptionTest() {
        doReturn(DataDummy.defaultsRoomsList.get(0))
                .when(roomService)
                .findAvailableRoom(DataDummy.bookingRequestException);

        // No se mockean los metodos : bookingRepository.save(booking);  roomService.bookRoom(roomId);
        // En el metodo : this.paymentService.pay(booking, price); Lanza excepcion y ya no llega a esa parte
        // Recordar que cuando lanza una Exception en la linea 50, ya no se ejecuta nada de las lineas 53-57

        doThrow(new IllegalArgumentException("Max 3 guest"))
                .when(paymentService)
                .pay(any(BookingDto.class), anyDouble());

        // Guarda la exception lanzada en el metodo, para su verificacion
        Executable executable = () -> bookingService.booking(DataDummy.bookingRequestException);

        assertThrows(IllegalArgumentException.class, executable);

    }


    @Test
    @DisplayName("6. booking(final BookingDto booking)  should works, with exceptions. Unhappy path. Conditions")
    void bookingException2Test() {
        doReturn(DataDummy.defaultsRoomsList.get(0))
                .when(roomService)
                .findAvailableRoom(DataDummy.bookingRequestException);

        // No se mockean los metodos : bookingRepository.save(booking);  roomService.bookRoom(roomId);
        // En el metodo : this.paymentService.pay(booking, price); Lanza excepcion y ya no llega a esa parte
        // Recordar que cuando lanza una Exception en la linea 50, ya no se ejecuta nada de las lineas 53-57

        // Valuando parametros
        // Es 120, porque : PRICE_ROOM * totalNights; 20 * 6 dias, del bookingRequestException 20 al 26
        doThrow(new IllegalArgumentException("Max 3 guest"))
                .when(paymentService)
                .pay(eq(DataDummy.bookingRequestException), eq(120.00));


        Executable executable = () -> bookingService.booking(DataDummy.bookingRequestException);

        assertThrows(IllegalArgumentException.class, executable);

    }


    @Test
    @DisplayName("7. getAvailablePlaceCount() should works, multiple returns")
    void getAvailablePlaceCountMultipleReturnTest() {
        when(roomService.findAllAvailableRooms())
                .thenReturn(DataDummy.defaultsRoomsList)
                .thenReturn(Collections.emptyList())
                .thenReturn(DataDummy.singleRoomsList);

        var expected1 = 14;
        var expected2 = 0;
        var expected3 = 5;
        var result1 = bookingService.getAvailablePlaceCount();
        var result2 = bookingService.getAvailablePlaceCount();
        var result3 = bookingService.getAvailablePlaceCount();

        assertAll(
                () -> assertEquals(expected1, result1),
                () -> assertEquals(expected2, result2),
                () -> assertEquals(expected3, result3)
        );
    }


    @Test
    @DisplayName("8. unbook() should works, Testing methods void")
    void getUnBookTest() {
        var roomId1 = UUID.randomUUID().toString();
        var roomId2 = UUID.randomUUID().toString();

        //Los response requrieren tener seteado un Room
        var bookingResponse1 = DataDummy.bookingRequest1;
        bookingResponse1.setRoom(DataDummy.defaultsRoomsList.get(3));

        var bookingResponse2 = DataDummy.bookingRequest2;
        bookingResponse2.setRoom(DataDummy.defaultsRoomsList.get(4));

        when(bookingRepository.findById(anyString()))
                .thenReturn(bookingResponse1)
                .thenReturn(bookingResponse2);

        // unbookRoom y deleteById , son void
        doNothing().when(roomService).unbookRoom(anyString());
        doNothing().when(bookingRepository).deleteById(anyString());

        bookingService.unbook(roomId1);
        bookingService.unbook(roomId2);

        verify(roomService, times(2)).unbookRoom(anyString());
        verify(bookingRepository, times(2)).deleteById(anyString());
        verify(bookingRepository, times(2)).findById(stringArgumentCaptor.capture());

        System.out.println("Argument Capture : " + stringArgumentCaptor.getAllValues());

        assertEquals(List.of(roomId1, roomId2), stringArgumentCaptor.getAllValues());
    }


    @Test
    @DisplayName("10 calculateInMxn() should works, Testing methods static")
    void getCalculateInMxnTest() {
        try (MockedStatic<CurrencyConverter> mockedStatic = mockStatic(CurrencyConverter.class)) {
            final var expected = 900.0;
            mockedStatic.when(() -> CurrencyConverter.toMx(anyDouble())).thenReturn(expected);

            var response = bookingService.calculateInMxn(DataDummy.bookingRequest1);

            assertEquals(expected, response);
        }
    }


    @Test
    @DisplayName("11 shouldCountAvailablePlaces() given...when...then")
    void getShouldAvailablePlacesGivenWhenThenTest() {
        //Given
        BDDMockito.given(roomService.findAllAvailableRooms())
                .willReturn(Collections.singletonList(new RoomDto("A1", 2)));

        //When
        var expected = 2;
        int response = bookingService.getAvailablePlaceCount();

        //Then
        BDDMockito.then(roomService).should(times(1)).findAllAvailableRooms();
        assertEquals(expected, response);
    }

}
