package es.santander.adn360.core.model.document.repository;

import es.santander.adn360.core.model.document.InternalUser;

/**
 * Repository for internal users collection.
 */
public interface InternalUserRepository {

    /**
     * Gets an internal user by user id.
     *
     * @param idUsuario User id.
     * @return InternalUserDocument
     */
    InternalUser findOneByIdUsuario(String idUsuario);
}
