package es.santander.adn360.core.model.document.repository;

import es.santander.adn360.core.model.document.InternalUser;
import reactor.core.publisher.Mono;

/**
 * Repository for internal users collection.
 */
public interface InternalUserReactiveRepository {

    /**
     * Gets an internal user by user id.
     *
     * @param idUsuario User id.
     * @return InternalUserDocument
     */
    Mono<InternalUser> findOneByIdUsuario(String idUsuario);
}
