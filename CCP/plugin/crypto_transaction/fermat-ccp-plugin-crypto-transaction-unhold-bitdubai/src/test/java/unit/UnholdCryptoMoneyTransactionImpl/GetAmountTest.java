package unit.UnholdCryptoMoneyTransactionImpl;

import com.bitdubai.fermat_ccp_plugin.layer.crypto_transaction.unhold.developer.bitdubai.version_1.utils.UnHoldCryptoMoneyTransactionImpl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by José Vilchez on 21/01/16.
 */
@RunWith(MockitoJUnitRunner.class)
public class GetAmountTest {

    @Test
    public void getAmount(){
        UnHoldCryptoMoneyTransactionImpl unHoldCryptoMoneyTransaction = mock(UnHoldCryptoMoneyTransactionImpl.class);
        when(unHoldCryptoMoneyTransaction.getAmount()).thenReturn(1f);
        assertThat(unHoldCryptoMoneyTransaction.getAmount()).isNotNull();
    }

}