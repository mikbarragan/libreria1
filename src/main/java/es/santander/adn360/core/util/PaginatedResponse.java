package es.santander.adn360.core.util;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

/**
 * Interface for indicating that a response has a paginated field.
 * @param <T> Paginated field
 * @param <K> Paginated field id
 */
public interface PaginatedResponse<T, K> {

    /**
     * Gets paginated field.
     *
     * @return List of elements that will be paginated
     */
    @JsonIgnore
    List<T> getPaginatedField();

    /**
     * Sets paginated field
     *
     * @param paginatedField Paginated field.
     */
    void setPaginatedField(List<T> paginatedField);

    /**
     * Find the position an object in the list by id.
     *
     * @param id input
     * @return offset
     */
    @JsonIgnore
    Long getOffsetById(K id);

    /**
     * Find the id an object in the list by index.
     *
     * @param offset input
     * @return idField
     */
    @JsonIgnore
    String getIdByOffset(Long offset);

    /**
     * Check if the field id is valid.
     *
     * @param id input
     * @return boolean valid field
     */
    @JsonIgnore
    boolean isValidOffset(K id);
}
