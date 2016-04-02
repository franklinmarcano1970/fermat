package com.bitdubai.fermat_bch_plugin.layer.crypto_router.incoming_crypto.developer.bitdubai.version_1.structure;

import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseDataType;

/**
 * Created by Arturo Vallone 25/04/2015
 * Edited by Ezequiel Postan
 */
public class IncomingCryptoDataBaseConstants {

    // IncomingCrypto database name
    public static final String INCOMING_CRYPTO_DATABASE = "IncomingCryptoDatabase";

    // IncomingCrypto table name
    public static final String INCOMING_CRYPTO_REGISTRY_TABLE_NAME = "IncomingCryptoRegistry";

    /* This is the transaction Id
     * We use an Id different from the transaction hash because we distinguish between fermat transactions
     * and crypto transactions-
     */
    public static final ColumnDefinition INCOMING_CRYPTO_REGISTRY_TABLE_ID_COLUMN = new ColumnDefinition("Id", DatabaseDataType.STRING, 36, true);

    // Cryoto Transaction associated to the fermat transaction
    public static final ColumnDefinition INCOMING_CRYPTO_REGISTRY_TABLE_CRYPTO_TRANSACTION_COLUMN = new ColumnDefinition("cryptoTransaction", DatabaseDataType.STRING, 1000000000, false);
    // A field that stores who is responsible of this transaction
    public static final ColumnDefinition INCOMING_CRYPTO_REGISTRY_TABLE_SPECIALIST_COLUMN = new ColumnDefinition("specialist", DatabaseDataType.STRING, 10, false);
    // A crypto transaction has one status, the possible status are described in the CryptoStatus enum
    public static final ColumnDefinition INCOMING_CRYPTO_REGISTRY_TABLE_CRYPTO_STATUS_COLUMN = new ColumnDefinition("cryptoStatus", DatabaseDataType.STRING, 10, false);
    // A fermat transaction is defined as the application or reversion of a crypto transaction
    public static final ColumnDefinition INCOMING_CRYPTO_REGISTRY_TABLE_ACTION_COLUMN = new ColumnDefinition("action", DatabaseDataType.STRING, 10, false);
    // The protocol status can be found in the ProtocolStatus enum and list states in the TransactionTransferenceProtocol
    public static final ColumnDefinition INCOMING_CRYPTO_REGISTRY_TABLE_PROTOCOL_STATUS_COLUMN = new ColumnDefinition("protocolStatus", DatabaseDataType.STRING, 10, false);
    // The transaction status is defined by the TransactionStatus enum located in the TransactionTransferenceProtocol package (transaction_transference_protocol)
    public static final ColumnDefinition INCOMING_CRYPTO_REGISTRY_TABLE_TRANSACTION_STATUS_COLUMN = new ColumnDefinition("transactionStatus", DatabaseDataType.STRING, 10, false);
    public static final ColumnDefinition INCOMING_CRYPTO_REGISTRY_TABLE_TIMESTAMP_COLUMN = new ColumnDefinition("timestamp", DatabaseDataType.LONG_INTEGER, 100, false);

    public static final String INCOMING_CRYPTO_EVENTS_RECORDED_TABLE_NAME = "IncomingCryptoEventsRecorded";
    public static final ColumnDefinition INCOMING_CRYPTO_EVENTS_RECORDED_TABLE_ID_COLUMN = new ColumnDefinition("Id", DatabaseDataType.STRING, 36, true);
    public static final ColumnDefinition INCOMING_CRYPTO_EVENTS_RECORDED_TABLE_EVENT_COLUMN = new ColumnDefinition("Event", DatabaseDataType.STRING, 10, false);
    public static final ColumnDefinition INCOMING_CRYPTO_EVENTS_RECORDED_TABLE_SOURCE_COLUMN = new ColumnDefinition("Source", DatabaseDataType.STRING, 10, false);
    public static final ColumnDefinition INCOMING_CRYPTO_EVENTS_RECORDED_TABLE_STATUS_COLUMN = new ColumnDefinition("Status", DatabaseDataType.STRING, 10, false);
    public static final ColumnDefinition INCOMING_CRYPTO_EVENTS_RECORDED_TABLE_TIMESTAMP_COLUMN = new ColumnDefinition("Timestamp", DatabaseDataType.LONG_INTEGER, 100, false);

    public static class ColumnDefinition {
        public final String columnName;
        public final DatabaseDataType columnDataType;
        public final int columnDataTypeSize;
        public final boolean columnIsPrimaryKey;

        public ColumnDefinition(String columnName, DatabaseDataType columnDataType, int columnDataTypeSize, boolean columnIsPrimaryKey) {
            this.columnName = columnName;
            this.columnDataType = columnDataType;
            this.columnDataTypeSize = columnDataTypeSize;
            this.columnIsPrimaryKey = columnIsPrimaryKey;
        }
    }
}
