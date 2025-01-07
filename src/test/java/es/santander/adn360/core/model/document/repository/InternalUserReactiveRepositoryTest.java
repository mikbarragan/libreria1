package es.santander.adn360.core.model.document.repository;

import com.santander.darwin.core.cache.DarwinCacheMono;
import es.santander.adn360.core.config.MongoProperties;
import es.santander.adn360.core.model.document.InternalUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.actuate.observability.AutoConfigureObservability;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.test.context.TestPropertySource;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@SpringBootTest
@TestPropertySource(properties = {
        "spring.main.web-application-type=reactive",
        "spring.main.allow-bean-definition-overriding=true"})
@AutoConfigureObservability
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class InternalUserReactiveRepositoryTest {

    @Autowired
    private InternalUserReactiveRepositoryImpl internalUserRepository;

    @Autowired
    private ReactiveMongoTemplate mongoTemplate;

    @Autowired
    private MongoProperties mongoCollectionsProperties;

    @Autowired
    CacheManager cacheManager;

    @Autowired
    DarwinCacheMono darwinCacheMono;

    @BeforeEach
    void init() {
        internalUserRepository = new InternalUserReactiveRepositoryImpl(mongoTemplate, mongoCollectionsProperties, darwinCacheMono);
        cacheManager.getCacheNames().forEach(cacheName -> cacheManager.getCache(cacheName).clear());
        mongoTemplate.dropCollection("usuarios_internos")
                .as(StepVerifier::create)
                .verifyComplete();
    }

    /**
     * Test que valida que cuando se solicita un dato existente se devuelve un Mono que emite el valor solicitado
     */
    @Test
    void findOneByIdUsuario_hasResult() {
        InternalUser internalUser = InternalUser.builder()
                .idUsuario("testUser")
                .usuarioInterno("internalUserTest")
                .fechaBajaLogica(Date.from(LocalDate.of(9999,12,31).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()))
                .build();
        // Save an internal user
        mongoTemplate
                .save(internalUser, mongoCollectionsProperties.getInternalUserCollection())
                .block();

        //BeforeEach save internal user in mongoembeded
        var result = this.internalUserRepository.findOneByIdUsuario("testUser");
        StepVerifier.create(result)
                .expectNextMatches(user -> user.getUsuarioInterno().equals("internalUserTest"))
                .expectComplete()
                .verify();
    }

    /**
     * Test que valida que cuando se solicite un  dato NO existente se emite un Mono.empty
     */
    @Test
    void findOneByIdUsuario_empty() {
        StepVerifier.create(this.internalUserRepository.findOneByIdUsuario("testUser2"))
                .expectComplete()
                .verify();
    }


    /**
     * Test que valida si que el repositorio está cacheando correctamente.
     * 1-> Inserta el dato en la bbdd
     * 2-> Valida el dato insertado
     * 3-> Modifica el dato insertado
     * 4-> Vuelve a recuperar el dato, comprobando que el repositorio trae el dato incial insertado/cacheado y no
     * el modificado
     */
    @Test
    void findOneByIdUsuario_cached() {
        InternalUser internalUser = InternalUser.builder()
                .idUsuario("testUser")
                .usuarioInterno("internalUserTest")
                .fechaBajaLogica(Date.from(LocalDate.of(9999,12,31).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()))
                .build();
        // Save an internal user
        mongoTemplate.save(internalUser, mongoCollectionsProperties.getInternalUserCollection()).block();
        var result = this.internalUserRepository.findOneByIdUsuario("testUser");
        StepVerifier.create(result)
                .expectNextMatches(user -> user.getUsuarioInterno().equals("internalUserTest"))
                .expectComplete()
                .verify();

        // Even if we change the internal user in the database, because the response is cached, we get same response as before
        InternalUser internalUser2 = InternalUser.builder().build();
        BeanUtils.copyProperties(internalUser, internalUser2);
        internalUser2.setUsuarioInterno("internalUserTest2");
        mongoTemplate.save(internalUser2, mongoCollectionsProperties.getInternalUserCollection()).block();

        var result2 = this.internalUserRepository.findOneByIdUsuario("testUser");
        StepVerifier.create(result2)
                .expectNextMatches(user -> user.getUsuarioInterno().equals("internalUserTest"))
                .expectComplete()
                .verify();
    }

    /**
     * Test que valida que el repositorio no está cacheando valores empty
     * 1-> Intenta recuperar el usuario que no existe.
     * 2-> Almacena el dato en la base de datos
     * 3-> Intenta recuperar nuevamente el dato de base de datos comprobando que ha devuelto el insertado y NO el
     * vacío cacheado.
     */
    @Test
    void findOneByIdUsuario_noCachedEmpty() {
        //Get emtpy result
        var result = this.internalUserRepository.findOneByIdUsuario("testUser");
        StepVerifier.create(result)
                .expectComplete()
                .verify();

        //insert new internalUser
        InternalUser internalUser = InternalUser.builder()
                .idUsuario("testUser")
                .usuarioInterno("internalUserTest")
                .fechaBajaLogica(Date.from(LocalDate.of(9999,12,31).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()))
                .build();
        // Save an internal user
        mongoTemplate.save(internalUser, mongoCollectionsProperties.getInternalUserCollection())
                .block();

        var result2 = this.internalUserRepository.findOneByIdUsuario("testUser");
        StepVerifier.create(result2)
                .expectNextMatches(user -> user.getUsuarioInterno().equals("internalUserTest"))
                .expectComplete()
                .verify();
    }

}
