## Testing Debuggeando ideas 02

### URLs
  - localhost:8080/v1/album
### Importar resources para test
  - application.properties
### Importar dependencias para Spring Boot
  - spring-boot-starter-test
### Crear carpetas para los test
  - A nivel del src, crear test/java 
### Crear packages para los test, desde la carpeta test/java
  - package com.debuggeando_ideas.music_app


### Configuracion para Spring Boot
  - NO SE RECOMIENDA USAR !! :  @ExtendWith(MockitoExtension.class). 
    - Requeririamos usar nuestra configuración, ya que Mockito estaría desconfigurado.
  - RECOMENDABLE USAR !! : @SpringBootTest
  - @ActiveProfiles("test")   
    - Toma la Configuracion de test, no la de src. application.properties

### Importar las assertions de manera estatica
  - import static org.junit.jupiter.api.Assertions.*;
### Importar los metodos de Mockito
  - import static org.mockito.Mockito.*;
### Configuraciones equivalentes
  - @SpringBootTest   <=>  @ExtendWith(SpringExtension.class)
  - @ActiveProfiles("test")

### Mockito para TrackService
  - Se utilizó Mockito para estos Tests.

### MockBeans para AlbumService
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

### WebMVC Testing controllers, endpoints
  - Es una simulación de Tomcat
  - @WebMvcTest(value = AlbumController.class)   
    - Mockea un servidor, para una sola classe
  - @WebMvcTest(value = {AlbumController.class, TrackController.class})   
    - Mockea un servidor, para varias classe
  - ObjectMapper, se utiliza para serializar datos, SE DEBE CONFIGURAR 