package com.abn.food.recipe.model;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Model to handle error/unhandled scenarios
 */
@Schema(description = "Model to handle error/unhandled scenarios")
public class Error {

    @Schema(description = "error code")
    private String code;

    @Schema(description = "error message")
    private String message;

    /**
     * Get error code
     *
     * @return error code
     */
    public String getCode() {
        return code;
    }

    /**
     * Set error code
     *
     * @param code error code
     * @return current object
     */
    public Error setCode(String code) {
        this.code = code;
        return this;
    }

    /**
     * Get error message
     *
     * @return error message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Set error message
     *
     * @param message error message
     * @return current object
     */
    public Error setMessage(String message) {
        this.message = message;
        return this;
    }
}
