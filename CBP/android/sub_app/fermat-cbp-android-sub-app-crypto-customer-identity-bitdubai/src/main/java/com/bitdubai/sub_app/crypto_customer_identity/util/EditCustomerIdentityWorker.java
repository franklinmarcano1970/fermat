package com.bitdubai.sub_app.crypto_customer_identity.util;

import android.app.Activity;

import com.bitdubai.fermat_android_api.layer.definition.wallet.interfaces.FermatSession;
import com.bitdubai.fermat_android_api.ui.interfaces.FermatWorkerCallBack;
import com.bitdubai.fermat_android_api.ui.util.FermatWorker;
import com.bitdubai.fermat_cbp_api.layer.sub_app_module.crypto_customer_identity.interfaces.CryptoCustomerIdentityInformation;
import com.bitdubai.fermat_cbp_api.layer.sub_app_module.crypto_customer_identity.interfaces.CryptoCustomerIdentityModuleManager;
import com.bitdubai.sub_app.crypto_customer_identity.session.CryptoCustomerIdentitySubAppSession;

import static com.bitdubai.sub_app.crypto_customer_identity.session.CryptoCustomerIdentitySubAppSession.IDENTITY_INFO;

/**
 * Created by angel on 20/1/16.
 */

public class EditCustomerIdentityWorker extends FermatWorker {
    public static final int SUCCESS = 1;
    public static final int INVALID_ENTRY_DATA = 4;

    private CryptoCustomerIdentityModuleManager moduleManager;
    private CryptoCustomerIdentityInformation identityInfo;
    private CryptoCustomerIdentityInformation identity;

    public EditCustomerIdentityWorker(Activity context, FermatSession session, CryptoCustomerIdentityInformation identity, FermatWorkerCallBack callBack) {
        super(context, callBack);

        this.identity = identity;

        if (session != null) {
            CryptoCustomerIdentitySubAppSession subAppSession = (CryptoCustomerIdentitySubAppSession) session;
            identityInfo = (CryptoCustomerIdentityInformation) subAppSession.getData(IDENTITY_INFO);
            this.moduleManager = subAppSession.getModuleManager();
        }
    }

    @Override
    protected Object doInBackground() throws Exception {

        boolean valueChanged = (identity.isPublished() != identityInfo.isPublished());

        if ( identity == null ) {
            return INVALID_ENTRY_DATA;
        } else {
            moduleManager.updateCryptoCustomerIdentity(identity);
            /*
            if (valueChanged) {
                if (identity.isPublished())
                    moduleManager.publishIdentity(identity.getPublicKey());
                else
                    moduleManager.hideIdentity(identity.getPublicKey());
            }
            */
            return SUCCESS;
        }
    }
}