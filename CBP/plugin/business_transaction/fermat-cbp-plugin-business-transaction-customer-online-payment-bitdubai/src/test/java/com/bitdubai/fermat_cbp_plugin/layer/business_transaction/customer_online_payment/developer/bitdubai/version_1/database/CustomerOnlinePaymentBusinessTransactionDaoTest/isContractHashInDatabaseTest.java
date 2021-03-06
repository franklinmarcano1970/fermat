package com.bitdubai.fermat_cbp_plugin.layer.business_transaction.customer_online_payment.developer.bitdubai.version_1.database.CustomerOnlinePaymentBusinessTransactionDaoTest;

import com.bitdubai.fermat_api.layer.osa_android.database_system.Database;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTable;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTableRecord;
import com.bitdubai.fermat_api.layer.osa_android.database_system.PluginDatabaseSystem;
import com.bitdubai.fermat_cbp_api.all_definition.exceptions.UnexpectedResultReturnedFromDatabaseException;
import com.bitdubai.fermat_cbp_plugin.layer.business_transaction.customer_online_payment.developer.bitdubai.version_1.database.CustomerOnlinePaymentBusinessTransactionDao;
import com.bitdubai.fermat_cbp_plugin.layer.business_transaction.customer_online_payment.developer.bitdubai.version_1.database.CustomerOnlinePaymentBusinessTransactionDatabaseConstants;
import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.ErrorManager;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

/**
 * Created by alexander jimenez (alex_jimenez76@hotmail.com) on 03/02/16.
 */
public class isContractHashInDatabaseTest {
    @Mock
    private PluginDatabaseSystem mockPluginDatabaseSystem;
    @Mock
    private Database mockDatabase;
    @Mock
    DatabaseTable databaseTable;
    @Mock
    ErrorManager errorManager;
    @Mock
    DatabaseTableRecord databaseTableRecord;
    List<DatabaseTableRecord> databaseTableRecordsList  = new ArrayList<>();
    private UUID testId;
    private CustomerOnlinePaymentBusinessTransactionDao customerOnlinePaymentBusinessTransactionDao;


    @Before
    public void setup()throws Exception{
        testId = UUID.randomUUID();
        MockitoAnnotations.initMocks(this);
        customerOnlinePaymentBusinessTransactionDao = new CustomerOnlinePaymentBusinessTransactionDao(
                mockPluginDatabaseSystem,testId, mockDatabase,errorManager);
        databaseTableRecordsList.add(databaseTableRecord);
        setupMockitoGeneraRules();
    }

    public void setupMockitoGeneraRules()throws Exception{
        doNothing().when(databaseTable).loadToMemory();
        when(databaseTableRecord.getStringValue(
                        CustomerOnlinePaymentBusinessTransactionDatabaseConstants.ONLINE_PAYMENT_CONTRACT_HASH_COLUMN_NAME)
        ).thenReturn("Test");
    }
    @Test
    public void isContractHashInDatabaseTest_Should_Return_False()throws Exception{
        when(mockDatabase.getTable(
                CustomerOnlinePaymentBusinessTransactionDatabaseConstants.ONLINE_PAYMENT_TABLE_NAME)
        ).thenReturn(databaseTable);
        assertEquals(false, customerOnlinePaymentBusinessTransactionDao.isContractHashInDatabase("Test"));
    }
    @Test
    public void isContractHashInDatabaseTest_Should_Return_True()throws Exception{
        when(mockDatabase.getTable(
                CustomerOnlinePaymentBusinessTransactionDatabaseConstants.ONLINE_PAYMENT_TABLE_NAME)
        ).thenReturn(databaseTable);
        when(databaseTable.getRecords()).thenReturn(databaseTableRecordsList);
        assertEquals(true,customerOnlinePaymentBusinessTransactionDao.isContractHashInDatabase("Test"));
    }
    //Generic exception
    @Test(expected = UnexpectedResultReturnedFromDatabaseException.class)
    public void isContractHashInDatabaseTest_Should_Throw_Exception()throws Exception{
        customerOnlinePaymentBusinessTransactionDao = new CustomerOnlinePaymentBusinessTransactionDao(
                null,null,null,errorManager);
        customerOnlinePaymentBusinessTransactionDao.isContractHashInDatabase(null);
    }
}
