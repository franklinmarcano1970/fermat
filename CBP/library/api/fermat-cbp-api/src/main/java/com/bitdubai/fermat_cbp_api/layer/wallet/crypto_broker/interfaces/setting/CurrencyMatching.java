package com.bitdubai.fermat_cbp_api.layer.wallet.crypto_broker.interfaces.setting;

import com.bitdubai.fermat_api.layer.world.interfaces.Currency;

import java.util.UUID;

/**
 * TODO add description
 *
 * Created by franklin on 14/02/16.
 */
public interface CurrencyMatching {

    String getOriginTransactionId();

    Currency getCurrencyGiving();

    float getAmountGiving();

    Currency getCurrencyReceiving();

    float getAmountReceiving();

}
