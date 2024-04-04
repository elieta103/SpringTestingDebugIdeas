## Testing Debuggeando ideas 02


### Importar dependencias
  - mockito-junit-jupiter    5.7.0
  - mockito-core             5.7.0
### Crear carpetas para los test
  - A nivel del src, crear test/java
### Crear packages para los test, desde la carpeta test/java
  - package com.debuggeandoideas.airdnd
### Importar las assertions de manera estatica
  - import static org.junit.jupiter.api.Assertions.*;
### Importar los metodos de Mockito
  - import static org.mockito.Mockito.*;
### CUIDADO! Mala practica
  - roomRepository = new RoomRepository();        Sin Mock 
  - roomRepository = mock(RoomRepository.class);  Con Mock


### Los valores default de Mockito
  - Cuando solo se hace mock(RoomRepository.class);
  - Pero el test se omite : when(roomRepository.findAll()).thenReturn(DataDummy.defaultsRooms);
  - Collections : []
  - booleans    : false
  - String      : null
  - Numeric     : 0
  - Object      : null

### Annotations
- @Mock.- Mockea las dependencias
- @InjectMocks.- Inyecta la classe con sus Mocks
```
    @ExtendWith(MockitoExtension.class)
    
    @Mock
    private RoomRepository roomRepository;   =  //roomRepository = mock(RoomRepository.class);
    
    @InjectMocks
    private RoomService roomService;    =   //roomService = new RoomService(roomRepository);
```


### Argument Matchers
  - Permiten testear metodos con argumentos :
  - Se pueden pasar los argumentos estrictos o con any()
  - when(roomService.findAvailableRoom(any(BookingDto.class))).thenReturn(DataDummy.defaultsRoomsList.get(0)); 
  - when(bookingRepository.save(any(BookingDto.class))).thenReturn(roomId);
  - O también :
  - when(roomService.findAvailableRoom(DataDummy.bookingRequest1)).thenReturn(DataDummy.defaultsRoomsList.get(0));
  - when(bookingRepository.save(DataDummy.bookingRequest1)).thenReturn(roomId);


### doReturn
  - doReturn hace un  mock mas estricto
  - Es otra forma de hacer el when, 
    - when...thenReturn (llama al metodo real) 
    - doReturn...when (No llama al metodo real), es un Mock Total
  - when(roomService.findAvailableRoom(DataDummy.bookingRequest1)).thenReturn(DataDummy.defaultsRoomsList.get(0));
  - when(bookingRepository.save(DataDummy.bookingRequest1)).thenReturn(roomId);
  - O también :    
  - doReturn(DataDummy.defaultsRoomsList.get(0)).when(roomService).findAvailableRoom(DataDummy.bookingRequest1);
  - doReturn(roomId).when(bookingRepository).save(DataDummy.bookingRequest1);

### verify
  - Verificar el comportamiento de un metodo, puede llamarse con los Arguments Matchers any()...
  - Se agrega los verify despues del assert 
  - verify(roomService, times(1)).findAvailableRoom(any(BookingDto.class));
  - verify(bookingRepository, times(1)).save(any(BookingDto.class));

### Mockear un metodo void
  - doNothing().when(roomService).bookRoom(anyString());
  - verify(roomService, times(1)).bookRoom(anyString());

### Testing excepciones
  - Revisar metodos : bookingExceptionTest(), bookingExceptionOtherTest()
  - doThrow(new IllegalArgumentException("Max 3 guest"))
  -  .when(paymentService)
  -  .pay(any(BookingDto.class), anyDouble());
  - Executable executable = () -> bookingService.booking(DataDummy.bookingRequestException);
  - assertThrows(IllegalArgumentException.class, executable);
  - Las 2 opciones son equivalentes.
    ```
       doThrow(new IllegalArgumentException("Max 3 guest"))
                .when(paymentService)
                .pay(any(BookingDto.class), anyDouble());
        
       when(paymentService.pay(any(BookingDto.class), anyDouble()))
                .thenThrow(new IllegalArgumentException("Max 3 guest"));

    ```

  - when(paymentService.pay(any(BookingDto.class), anyDouble()))
  -    .thenThrow(new IllegalArgumentException("Max 3 guest")); 
  - Executable executable = () -> bookingService.booking(DataDummy.bookingRequestException);
  - assertThrows(IllegalArgumentException.class, executable);

  - Se puede condicionar el lanzamiento de la exception por medio de valores :
  -         doThrow(new IllegalArgumentException("Max 3 guest"))
  -              .when(paymentService)
  -              .pay(eq(DataDummy.bookingRequestException), eq(120.00));

### Spy VS Mock
  - Los Spy, son un tipo de Mock, 
  - Spy su comportamiento por defecto es llamar al metodo real
  - Spy cuando no tengo mockeado algo, con when...ThenReturn o doReturn...when
  - manda llamar al metodo real.
  - Si hay mucha restriccion con los Test, NUNCA USAR SPY
  - Podemos dejar metodos que no esten mockeados completamente.


### Mock con multiples return
  - Solo se puede hacer cuando tenemos when...thenReturn
  - when...thenReturn...thenReturn...thenThrows 

### Testing metodos void
  - Para validar que un metodo void mockeado se manda llamar el verify
  - Para validar un metodo void del service, se usa: Argument Capture
  - Permite guardar los argumentos con los que se llama un metodo
  - Permite hacer un assert para un metodo void con los  :
    - @Captor ArgumentCaptor<String> stringArgumentCaptor;


### Testing metodos final
  - Si se puede, y se utiliza la misma configuración para los metodos void

### Testing metodo static
  - Los testing para métodos estáticos estan disponibles desde la version 5.
  - debe ir dentro de un bloque try{...}catch
  - with resources, try(){}catch{}

### Given When Then
```
       //Given
        BDDMockito.given(roomService.findAllAvailableRooms())
                .willReturn(Collections.singletonList(new RoomDto("A1", 2)));

        //When
        var expected = 2;
        int response = bookingService.getAvailablePlaceCount();

        //Then
        BDDMockito.then(roomService).should(times(1)).findAllAvailableRooms();
        assertEquals(expected, response);
```


### Para agregar codigo en rama :
  - git checkout -b "02/mockito" (SE REALIZÓ AL INICIO)
  - git status
  - git add .
  - git commit -m "02/mockito"
  - git push -u origin 02/mockito
  