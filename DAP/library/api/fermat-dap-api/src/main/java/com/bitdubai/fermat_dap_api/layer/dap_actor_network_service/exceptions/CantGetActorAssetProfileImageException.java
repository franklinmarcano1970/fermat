package com.bitdubai.fermat_dap_api.layer.dap_actor_network_service.exceptions;

import com.bitdubai.fermat_api.FermatException;

/**
 * Created by natalia on 04/09/15.
 */
public class CantGetActorAssetProfileImageException extends FermatException {


    public CantGetActorAssetProfileImageException(final String message, final Exception cause, final String context, final String possibleReason) {
        super(message, cause, context, possibleReason);
    }


}