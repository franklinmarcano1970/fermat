package com.bitdubai.sub_app.customers.fragmentFactory;

import com.bitdubai.fermat_android_api.engine.FermatFragmentFactory;
import com.bitdubai.fermat_android_api.layer.definition.wallet.AbstractFermatFragment;
import com.bitdubai.fermat_android_api.layer.definition.wallet.enums.FermatFragmentsEnumType;
import com.bitdubai.fermat_android_api.layer.definition.wallet.exceptions.FragmentNotFoundException;
import com.bitdubai.fermat_pip_api.layer.network_service.subapp_resources.SubAppResourcesProviderManager;
import com.bitdubai.sub_app.customers.fragments.MainFragment;
import com.bitdubai.sub_app.customers.session.CustomersSubAppSession;

import static com.bitdubai.sub_app.customers.fragmentFactory.CustomersFragmentsEnumType.MAIN_FRAGMET;

public class CustomersFragmentFactory extends FermatFragmentFactory<CustomersSubAppSession,SubAppResourcesProviderManager,CustomersFragmentsEnumType> {


    @Override
    public AbstractFermatFragment getFermatFragment(CustomersFragmentsEnumType fragments) throws FragmentNotFoundException {
        if (fragments == MAIN_FRAGMET) {
            return MainFragment.newInstance();
        }

        throw createFragmentNotFoundException(fragments);
    }

    @Override
    public CustomersFragmentsEnumType getFermatFragmentEnumType(String key) {
        return CustomersFragmentsEnumType.getValue(key);
    }

    private FragmentNotFoundException createFragmentNotFoundException(FermatFragmentsEnumType fragments) {
        String possibleReason, context;

        if (fragments == null) {
            possibleReason = "The parameter 'fragments' is NULL";
            context = "Null Value";
        } else {
            possibleReason = "Not found in switch block";
            context = fragments.toString();
        }

        return new FragmentNotFoundException("Fragment not found", new Exception(), context, possibleReason);
    }
}
