package es.santander.adn360.core.model.document.repository;

import com.santander.darwin.core.cache.DarwinCacheMono;
import es.santander.adn360.core.config.MongoProperties;
import es.santander.adn360.core.model.document.InternalUser;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.Date;

/**
 * InternalUserReactiveRepository implementation.
 */
@Repository
@EnableReactiveMongoRepositories
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class InternalUserReactiveRepositoryImpl implements InternalUserReactiveRepository {

    /** mongo template */
    private final ReactiveMongoTemplate reactiveMongoTemplate;

    /** mongo collection properties */
    private final MongoProperties mongoCollectionsProperties;

    /**
     * DarwinCacheMono
     */
    private final DarwinCacheMono darwinCacheMono;
    /**
     * The internal user
     *
     * @param reactiveMongoTemplate mongo template instance
     * @param mongoCollectionsProperties mongoCollectionsProperties
     * @param darwinCacheMono          darwin cache mono
     */
    public InternalUserReactiveRepositoryImpl(ReactiveMongoTemplate reactiveMongoTemplate,
                                              MongoProperties mongoCollectionsProperties,
                                              DarwinCacheMono darwinCacheMono) {
        this.reactiveMongoTemplate = reactiveMongoTemplate;
        this.mongoCollectionsProperties = mongoCollectionsProperties;
        this.darwinCacheMono = darwinCacheMono;
    }

    /**
     * The cache
     *
     * @param idUsuario User id.
     * @return InternalUser internal user
     */
    @Override
    public Mono<InternalUser> findOneByIdUsuario(String idUsuario) {

        String collectionName = mongoCollectionsProperties.getInternalUserCollection();
        final Query mongoQuery = new Query();
        mongoQuery.addCriteria(new Criteria().andOperator(
                Criteria.where("idUsuario").is(idUsuario),
                Criteria.where("fechaBajaLogica").gt(new Date())
        ));

        var result =  this.reactiveMongoTemplate.findOne(mongoQuery, InternalUser.class, collectionName);
        return darwinCacheMono.cache("reactive_internal_users",idUsuario,result);
    }
}
