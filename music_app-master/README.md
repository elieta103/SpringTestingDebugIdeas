## Testing Debuggeando ideas 02


### Importar dependencias para Spring Boot
  - spring-boot-starter-test
### Crear carpetas para los test
  - A nivel del src, crear test/java 
### Crear packages para los test, desde la carpeta test/java
  - package com.debuggeando_ideas.music_app
### Configuracion para Spring Boot
  - @SpringBootTest
  - @ActiveProfiles("test")   Configuracion de test, no la de src
### Importar las assertions de manera estatica
  - import static org.junit.jupiter.api.Assertions.*;
### Importar los metodos de Mockito
  - import static org.mockito.Mockito.*;

### Configuraciones equivalentes
  - @SpringBootTest   <=>  @ExtendWith(SpringExtension.class)
  - @ActiveProfiles("test")

###
    - SPRING
      - @MockBean -> @Autowired, 
      - Propia de Spring
      - Se utiliza la interface
    - MOCKITO
      - @Mock -> @InjectMocks  
      - Propia de Mockito
      - Se utiliza la Implementacion

### Reset Mocks
```
@AfterEach
  void resetMocks(){
    reset(this.albumRepositoryMock);
  }
````

### Configuracion de los test para Repository
  - @ActiveProfiles("test")
  - @DataJpaTest
    - Automaticamente funciona con H2 
    - Ya esta incluida en @SpringBootTest
    - Es un Mock permitido por Spring
  - @AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
    - Para configurar los test con otro base de datos SQL

### WebMVC
  - Es una especificacion de Tomcat
  - @WebMvcTest(value = AlbumController.class)   Mockea un servidor, para una sola classe
  - @WebMvcTest(value = {AlbumController.class, TrackController.class})   Mockea un servidor, para varias classe
  - ObjectMapper, se utiliza para serializar datos, SE DEBE CONFIGURAR 