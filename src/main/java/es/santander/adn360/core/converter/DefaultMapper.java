package es.santander.adn360.core.converter;

/**
 * Interface to mapper objects
 * @param <B> El bean
 * @param <E> La entidad
 */
public interface DefaultMapper<B, E> {

    /**
     * Maps Bean class to Entity class
     *
     * @param bean input
     * @return Entity
     */
    E beanToEntity(B bean);

    /**
     * Maps Entity class to Bean class
     *
     * @param entity input
     * @return Bean
     */
    B entityToBean(E entity);

}
