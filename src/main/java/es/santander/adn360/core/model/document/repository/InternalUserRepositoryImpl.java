package es.santander.adn360.core.model.document.repository;

import com.mongodb.client.MongoClient;
import es.santander.adn360.core.model.document.InternalUser;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * InternalUserRepository implementation.
 */

@Repository
@ConditionalOnClass(MongoClient.class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class InternalUserRepositoryImpl
        implements InternalUserRepository {

    MongoTemplate mongoTemplate;

    /**
     * The internal user
     *
     * @param mongoTemplate mongo template instance
     */
    public InternalUserRepositoryImpl(final MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    /**
     * The cache
     *
     * @param idUsuario User id.
     * @return InternalUser internal user
     */
    @Override
    @Cacheable(cacheNames = {"internal_users"}, unless = "#result == null")
    public InternalUser findOneByIdUsuario(String idUsuario) {
        return this.mongoTemplate.findOne(new Query().addCriteria(new Criteria().andOperator(
                Criteria.where("idUsuario").is(idUsuario),
                Criteria.where("fechaBajaLogica").gt(new Date())
        )), InternalUser.class);
    }

    /**
     * nonsense commentary to comply with a Sonar nonsense rule
     * nonsense commentary to comply with a Sonar nonsense rule
     * nonsense commentary to comply with a Sonar nonsense rule
     */
}
