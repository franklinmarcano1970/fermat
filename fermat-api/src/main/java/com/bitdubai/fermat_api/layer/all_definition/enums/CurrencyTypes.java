package com.bitdubai.fermat_api.layer.all_definition.enums;

import com.bitdubai.fermat_api.layer.all_definition.enums.interfaces.FermatEnum;
import com.bitdubai.fermat_api.layer.all_definition.exceptions.InvalidParameterException;

/**
 * The enum class <code>com.bitdubai.fermat_api.layer.all_definition.enums.CurrencyTypes</code>
 * enumerates all the currency types that you will find in fermat.
 * <p/>
 * Created by Leon Acosta - (laion.cj91@gmail.com) on 07/03/2016.
 */
public enum CurrencyTypes implements FermatEnum {

    /**
     * To do make code more readable, please keep the elements in the Enum sorted alphabetically.
     */

    CRYPTO  ("CRY"),
    FIAT    ("FIA"),

    ;

    private final String code;

    CurrencyTypes(final String code) {
        this.code = code;
    }

    public static CurrencyTypes getByCode(final String code) throws InvalidParameterException {

        switch (code) {

            case "CRY":  return CRYPTO;
            case "FIA":  return FIAT  ;

            default:
                throw new InvalidParameterException(
                        "Code Received: " + code,
                        "The received code is not valid for the CurrencyTypes enum"
                );
        }
    }

    @Override
    public String getCode() {
        return this.code;
    }

}