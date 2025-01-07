package es.santander.adn360.core.model.document.repository;


import es.santander.adn360.core.config.MongoProperties;
import es.santander.adn360.core.model.document.InternalUser;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.actuate.observability.AutoConfigureObservability;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureObservability
class InternalUserRepositoryTest {

    // Inject implementation directly because of save method
    @Autowired
    private InternalUserRepositoryImpl internalUserRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private MongoProperties mongoProperties;

    @BeforeEach
    void setup() throws Exception {

        InternalUser internalUser = InternalUser.builder()
                .idUsuario("testUser")
                .usuarioInterno("internalUserTest")
                .fechaBajaLogica(Date.from(LocalDate.of(9999,12,31).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()))
                .build();
        // Save an internal user
        mongoTemplate.save(internalUser, mongoProperties.getInternalUserCollection());
    }

    @Test
    void cacheTest() {

        // Get the internal user
        InternalUser internalUser = this.internalUserRepository.findOneByIdUsuario("testUser");
        Assertions.assertThat(internalUser).isNotNull();
        Assertions.assertThat(internalUser.getIdUsuario()).isEqualTo("testUser");
        Assertions.assertThat(internalUser.getUsuarioInterno()).isEqualTo("internalUserTest");

        // Even if we change the internal user in the database, because the response is cached, we get same response as before
        InternalUser internalUser2 = InternalUser.builder().build();
        BeanUtils.copyProperties(internalUser, internalUser2);
        internalUser2.setUsuarioInterno("internalUserTest2");
        mongoTemplate.save(internalUser2, mongoProperties.getInternalUserCollection());
        internalUser = this.internalUserRepository.findOneByIdUsuario("testUser");
        Assertions.assertThat(internalUser).isNotNull();
        Assertions.assertThat(internalUser.getIdUsuario()).isEqualTo("testUser");
        Assertions.assertThat(internalUser.getUsuarioInterno()).isEqualTo("internalUserTest");

    }

    @Test
    void testDao() {

        // Get the internal user
        InternalUser internalUser = this.internalUserRepository.findOneByIdUsuario("testUser");
        Assertions.assertThat(internalUser).isNotNull();
        Assertions.assertThat(internalUser.getIdUsuario()).isEqualTo("testUser");
        Assertions.assertThat(internalUser.getUsuarioInterno()).isEqualTo("internalUserTest");

        //user not valid
        InternalUser internalUser2 = InternalUser.builder()
                .idUsuario("testUser2")
                .usuarioInterno("internalUserTest")
                .fechaBajaLogica(Date.from(LocalDate.now().minus(Period.ofDays(1)).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()))
                .build();
        // Save an internal user
        mongoTemplate.save(internalUser2, mongoProperties.getInternalUserCollection());
        internalUser = this.internalUserRepository.findOneByIdUsuario("testUser2");
        Assertions.assertThat(internalUser).isNull();

    }
}
