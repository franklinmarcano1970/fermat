package com.bitdubai.fermat_cbp_plugin.layer.business_transaction.broker_ack_offline_payment.developer.bitdubai.version_1.structure;

import com.bitdubai.fermat_api.CantStartAgentException;
import com.bitdubai.fermat_api.DealsWithPluginIdentity;
import com.bitdubai.fermat_api.layer.all_definition.enums.Plugins;
import com.bitdubai.fermat_api.layer.all_definition.events.EventSource;
import com.bitdubai.fermat_api.layer.all_definition.events.interfaces.FermatEvent;
import com.bitdubai.fermat_api.layer.all_definition.exceptions.InvalidParameterException;
import com.bitdubai.fermat_api.layer.all_definition.transaction_transference_protocol.Specialist;
import com.bitdubai.fermat_api.layer.all_definition.transaction_transference_protocol.Transaction;
import com.bitdubai.fermat_api.layer.all_definition.transaction_transference_protocol.exceptions.CantConfirmTransactionException;
import com.bitdubai.fermat_api.layer.all_definition.transaction_transference_protocol.exceptions.CantDeliverPendingTransactionsException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.Database;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DealsWithPluginDatabaseSystem;
import com.bitdubai.fermat_api.layer.osa_android.database_system.PluginDatabaseSystem;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantCreateDatabaseException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantInsertRecordException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantOpenDatabaseException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantUpdateRecordException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.DatabaseNotFoundException;
import com.bitdubai.fermat_api.layer.osa_android.logger_system.DealsWithLogger;
import com.bitdubai.fermat_api.layer.osa_android.logger_system.LogManager;
import com.bitdubai.fermat_bnk_api.layer.bnk_bank_money_transaction.deposit.interfaces.DepositManager;
import com.bitdubai.fermat_cbp_api.all_definition.agent.CBPTransactionAgent;
import com.bitdubai.fermat_cbp_api.all_definition.enums.ClauseType;
import com.bitdubai.fermat_cbp_api.all_definition.enums.ContractDetailType;
import com.bitdubai.fermat_cbp_api.all_definition.enums.ContractStatus;
import com.bitdubai.fermat_cbp_api.all_definition.enums.ContractTransactionStatus;
import com.bitdubai.fermat_cbp_api.all_definition.enums.MoneyType;
import com.bitdubai.fermat_cbp_api.all_definition.enums.PaymentType;
import com.bitdubai.fermat_cbp_api.all_definition.events.enums.EventStatus;
import com.bitdubai.fermat_cbp_api.all_definition.events.enums.EventType;
import com.bitdubai.fermat_cbp_api.all_definition.exceptions.CantInitializeCBPAgent;
import com.bitdubai.fermat_cbp_api.all_definition.exceptions.ObjectNotSetException;
import com.bitdubai.fermat_cbp_api.all_definition.exceptions.UnexpectedResultReturnedFromDatabaseException;
import com.bitdubai.fermat_cbp_api.all_definition.negotiation.Clause;
import com.bitdubai.fermat_cbp_api.layer.business_transaction.common.events.BrokerAckPaymentConfirmed;
import com.bitdubai.fermat_cbp_api.layer.business_transaction.common.exceptions.CannotSendContractHashException;
import com.bitdubai.fermat_cbp_api.layer.business_transaction.common.exceptions.CantGetContractListException;
import com.bitdubai.fermat_cbp_api.layer.business_transaction.common.interfaces.BankTransactionParametersRecord;
import com.bitdubai.fermat_cbp_api.layer.business_transaction.common.interfaces.BusinessTransactionRecord;
import com.bitdubai.fermat_cbp_api.layer.business_transaction.common.interfaces.ObjectChecker;
import com.bitdubai.fermat_cbp_api.layer.contract.customer_broker_purchase.exceptions.CantGetListCustomerBrokerContractPurchaseException;
import com.bitdubai.fermat_cbp_api.layer.contract.customer_broker_purchase.exceptions.CantUpdateCustomerBrokerContractPurchaseException;
import com.bitdubai.fermat_cbp_api.layer.contract.customer_broker_purchase.interfaces.CustomerBrokerContractPurchase;
import com.bitdubai.fermat_cbp_api.layer.contract.customer_broker_purchase.interfaces.CustomerBrokerContractPurchaseManager;
import com.bitdubai.fermat_cbp_api.layer.contract.customer_broker_sale.exceptions.CantGetListCustomerBrokerContractSaleException;
import com.bitdubai.fermat_cbp_api.layer.contract.customer_broker_sale.exceptions.CantUpdateCustomerBrokerContractSaleException;
import com.bitdubai.fermat_cbp_api.layer.contract.customer_broker_sale.interfaces.CustomerBrokerContractSale;
import com.bitdubai.fermat_cbp_api.layer.contract.customer_broker_sale.interfaces.CustomerBrokerContractSaleManager;
import com.bitdubai.fermat_cbp_api.layer.negotiation.customer_broker_sale.exceptions.CantGetListSaleNegotiationsException;
import com.bitdubai.fermat_cbp_api.layer.negotiation.customer_broker_sale.interfaces.CustomerBrokerSaleNegotiation;
import com.bitdubai.fermat_cbp_api.layer.negotiation.customer_broker_sale.interfaces.CustomerBrokerSaleNegotiationManager;
import com.bitdubai.fermat_cbp_api.layer.negotiation.exceptions.CantGetListClauseException;
import com.bitdubai.fermat_cbp_api.layer.network_service.transaction_transmission.exceptions.CantGetBankTransactionParametersRecordException;
import com.bitdubai.fermat_cbp_api.layer.network_service.transaction_transmission.exceptions.CantSendContractNewStatusNotificationException;
import com.bitdubai.fermat_cbp_api.layer.network_service.transaction_transmission.interfaces.BusinessTransactionMetadata;
import com.bitdubai.fermat_cbp_api.layer.network_service.transaction_transmission.interfaces.TransactionTransmissionManager;
import com.bitdubai.fermat_cbp_api.layer.wallet.crypto_broker.interfaces.CryptoBrokerWalletManager;
import com.bitdubai.fermat_cbp_plugin.layer.business_transaction.broker_ack_offline_payment.developer.bitdubai.version_1.BrokerAckOfflinePaymentPluginRoot;
import com.bitdubai.fermat_cbp_plugin.layer.business_transaction.broker_ack_offline_payment.developer.bitdubai.version_1.database.BrokerAckOfflinePaymentBusinessTransactionDao;
import com.bitdubai.fermat_cbp_plugin.layer.business_transaction.broker_ack_offline_payment.developer.bitdubai.version_1.database.BrokerAckOfflinePaymentBusinessTransactionDatabaseConstants;
import com.bitdubai.fermat_cbp_plugin.layer.business_transaction.broker_ack_offline_payment.developer.bitdubai.version_1.database.BrokerAckOfflinePaymentBusinessTransactionDatabaseFactory;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.DealsWithErrors;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.enums.UnexpectedPluginExceptionSeverity;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.interfaces.ErrorManager;
import com.bitdubai.fermat_pip_api.layer.platform_service.event_manager.interfaces.DealsWithEvents;
import com.bitdubai.fermat_pip_api.layer.platform_service.event_manager.interfaces.EventManager;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * Created by Manuel Perez (darkpriestrelative@gmail.com) on 18/12/15.
 */
public class BrokerAckOfflinePaymentMonitorAgent implements
        CBPTransactionAgent,
        DealsWithLogger,
        DealsWithEvents,
        DealsWithErrors,
        DealsWithPluginDatabaseSystem,
        DealsWithPluginIdentity {

    Database database;
    MonitorAgent monitorAgent;
    Thread agentThread;
    LogManager logManager;
    EventManager eventManager;
    ErrorManager errorManager;
    PluginDatabaseSystem pluginDatabaseSystem;
    UUID pluginId;
    TransactionTransmissionManager transactionTransmissionManager;
    CustomerBrokerContractPurchaseManager customerBrokerContractPurchaseManager;
    CustomerBrokerContractSaleManager customerBrokerContractSaleManager;
    CustomerBrokerSaleNegotiationManager customerBrokerSaleNegotiationManager;
    DepositManager depositManager;
    CryptoBrokerWalletManager cryptoBrokerWalletManager;

    public BrokerAckOfflinePaymentMonitorAgent(
            PluginDatabaseSystem pluginDatabaseSystem,
            LogManager logManager,
            ErrorManager errorManager,
            EventManager eventManager,
            UUID pluginId,
            TransactionTransmissionManager transactionTransmissionManager,
            CustomerBrokerContractPurchaseManager customerBrokerContractPurchaseManager,
            CustomerBrokerContractSaleManager customerBrokerContractSaleManager,
            CustomerBrokerSaleNegotiationManager customerBrokerSaleNegotiationManager,
            DepositManager depositManager,
            CryptoBrokerWalletManager cryptoBrokerWalletManager)  {
        this.eventManager = eventManager;
        this.pluginDatabaseSystem = pluginDatabaseSystem;
        this.errorManager = errorManager;
        this.pluginId = pluginId;
        this.logManager=logManager;
        this.transactionTransmissionManager=transactionTransmissionManager;
        this.customerBrokerContractPurchaseManager=customerBrokerContractPurchaseManager;
        this.customerBrokerContractSaleManager=customerBrokerContractSaleManager;
        this.customerBrokerSaleNegotiationManager=customerBrokerSaleNegotiationManager;
        this.depositManager=depositManager;
        this.cryptoBrokerWalletManager=cryptoBrokerWalletManager;
    }

    @Override
    public void start() throws CantStartAgentException {

        Logger LOG = Logger.getGlobal();
        LOG.info("Customer online payment monitor agent starting");
        monitorAgent = new MonitorAgent();

        ((DealsWithPluginDatabaseSystem) this.monitorAgent).setPluginDatabaseSystem(this.pluginDatabaseSystem);
        ((DealsWithErrors) this.monitorAgent).setErrorManager(this.errorManager);

        try {
            ((MonitorAgent) this.monitorAgent).Initialize();
        } catch (CantInitializeCBPAgent exception) {
            errorManager.reportUnexpectedPluginException(
                    Plugins.BROKER_ACK_OFFLINE_PAYMENT,
                    UnexpectedPluginExceptionSeverity.DISABLES_THIS_PLUGIN,
                    exception);
        }

        this.agentThread = new Thread(monitorAgent,this.getClass().getSimpleName());
        this.agentThread.start();

    }

    @Override
    public void stop() {
        this.agentThread.interrupt();
    }

    @Override
    public void setErrorManager(ErrorManager errorManager) {
        this.errorManager=errorManager;
    }

    @Override
    public void setEventManager(EventManager eventManager) {
        this.eventManager=eventManager;
    }

    @Override
    public void setLogManager(LogManager logManager) {
        this.logManager=logManager;
    }

    @Override
    public void setPluginDatabaseSystem(PluginDatabaseSystem pluginDatabaseSystem) {
        this.pluginDatabaseSystem=pluginDatabaseSystem;
    }

    @Override
    public void setPluginId(UUID pluginId) {
        this.pluginId=pluginId;
    }

    /**
     * Private class which implements runnable and is started by the Agent
     * Based on MonitorAgent created by Rodrigo Acosta
     */
    private class MonitorAgent implements DealsWithPluginDatabaseSystem, DealsWithErrors, Runnable{

        ErrorManager errorManager;
        PluginDatabaseSystem pluginDatabaseSystem;
        public final int SLEEP_TIME = 5000;
        int iterationNumber = 0;
        BrokerAckOfflinePaymentBusinessTransactionDao brokerAckOfflinePaymentBusinessTransactionDao;
        boolean threadWorking;

        @Override
        public void setErrorManager(ErrorManager errorManager) {
            this.errorManager = errorManager;
        }

        @Override
        public void setPluginDatabaseSystem(PluginDatabaseSystem pluginDatabaseSystem) {
            this.pluginDatabaseSystem = pluginDatabaseSystem;
        }
        @Override
        public void run() {

            threadWorking=true;
            logManager.log(BrokerAckOfflinePaymentPluginRoot.getLogLevelByClass(this.getClass().getName()),
                    "Broker Ack Offline Payment Monitor Agent: running...", null, null);
            while(threadWorking){
                /**
                 * Increase the iteration counter
                 */
                iterationNumber++;
                try {
                    Thread.sleep(SLEEP_TIME);
                } catch (InterruptedException interruptedException) {
                    return;
                }

                /**
                 * now I will check if there are pending transactions to raise the event
                 */
                try {

                    logManager.log(BrokerAckOfflinePaymentPluginRoot.getLogLevelByClass(this.getClass().getName()), "Iteration number " + iterationNumber, null, null);
                    doTheMainTask();
                } catch (CannotSendContractHashException | CantUpdateRecordException | CantSendContractNewStatusNotificationException e) {
                    errorManager.reportUnexpectedPluginException(
                            Plugins.BROKER_ACK_OFFLINE_PAYMENT,
                            UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN,
                            e);
                }

            }

        }
        public void Initialize() throws CantInitializeCBPAgent {
            try {

                database = this.pluginDatabaseSystem.openDatabase(pluginId,
                        BrokerAckOfflinePaymentBusinessTransactionDatabaseConstants.DATABASE_NAME);
            }
            catch (DatabaseNotFoundException databaseNotFoundException) {

                //Logger LOG = Logger.getGlobal();
                //LOG.info("Database in Open Contract monitor agent doesn't exists");
                BrokerAckOfflinePaymentBusinessTransactionDatabaseFactory brokerAckOfflinePaymentBusinessTransactionDatabaseFactory=
                        new BrokerAckOfflinePaymentBusinessTransactionDatabaseFactory(this.pluginDatabaseSystem);
                try {
                    database = brokerAckOfflinePaymentBusinessTransactionDatabaseFactory.createDatabase(pluginId,
                            BrokerAckOfflinePaymentBusinessTransactionDatabaseConstants.DATABASE_NAME);
                } catch (CantCreateDatabaseException cantCreateDatabaseException) {
                    errorManager.reportUnexpectedPluginException(
                            Plugins.BROKER_ACK_OFFLINE_PAYMENT,
                            UnexpectedPluginExceptionSeverity.DISABLES_THIS_PLUGIN,
                            cantCreateDatabaseException);
                    throw new CantInitializeCBPAgent(cantCreateDatabaseException,
                            "Initialize Monitor Agent - trying to create the plugin database",
                            "Please, check the cause");
                }
            } catch (CantOpenDatabaseException exception) {
                errorManager.reportUnexpectedPluginException(
                        Plugins.BROKER_ACK_OFFLINE_PAYMENT,
                        UnexpectedPluginExceptionSeverity.DISABLES_THIS_PLUGIN,
                        exception);
                throw new CantInitializeCBPAgent(exception,
                        "Initialize Monitor Agent - trying to open the plugin database",
                        "Please, check the cause");
            }
        }

        private void doTheMainTask() throws
                CannotSendContractHashException,
                CantUpdateRecordException,
                CantSendContractNewStatusNotificationException {

            try{
                brokerAckOfflinePaymentBusinessTransactionDao =new BrokerAckOfflinePaymentBusinessTransactionDao(
                        pluginDatabaseSystem,
                        pluginId,
                        database);

                String contractHash;

                /**
                 * Check pending bank transactions to credit - Broker Side
                 * The status to verify is PENDING_CREDIT_BANK_WALLET, it represents that the payment
                 * is "physically" acknowledge by the broker.
                 */
                List<BusinessTransactionRecord> pendingToBankCreditList=
                        brokerAckOfflinePaymentBusinessTransactionDao.getPendingToBankCreditList();
                for(BusinessTransactionRecord pendingToBakCreditRecord : pendingToBankCreditList){
                    contractHash=pendingToBakCreditRecord.getTransactionHash();
                    depositManager.makeDeposit()
                }

                /**
                 * Check contract status to send. - Broker Side
                 * The status to verify is PENDING_ACK_OFFLINE_PAYMENT_NOTIFICATION, it represents that the payment is fully
                 * acknowledge by the broker.
                 */
                List<BusinessTransactionRecord> pendingToSubmitNotificationList=
                        brokerAckOfflinePaymentBusinessTransactionDao.getPendingToSubmitNotificationList();
                for(BusinessTransactionRecord pendingToSubmitNotificationRecord : pendingToSubmitNotificationList){
                    contractHash=pendingToSubmitNotificationRecord.getTransactionHash();
                    transactionTransmissionManager.sendContractStatusNotification(
                            pendingToSubmitNotificationRecord.getBrokerPublicKey(),
                            pendingToSubmitNotificationRecord.getCustomerPublicKey(),
                            contractHash,
                            pendingToSubmitNotificationRecord.getTransactionId(),
                            ContractTransactionStatus.OFFLINE_PAYMENT_ACK,
                            Plugins.BROKER_ACK_OFFLINE_PAYMENT
                    );
                    brokerAckOfflinePaymentBusinessTransactionDao.updateContractTransactionStatus(
                            contractHash,
                            ContractTransactionStatus.ONLINE_PAYMENT_ACK
                    );
                }

                /**
                 * Check pending notifications - Customer side
                 */
                List<BusinessTransactionRecord> pendingToSubmitConfirmationList=
                        brokerAckOfflinePaymentBusinessTransactionDao.getPendingToSubmitNotificationList();
                for(BusinessTransactionRecord pendingToSubmitConfirmationRecord : pendingToSubmitConfirmationList){
                    contractHash=pendingToSubmitConfirmationRecord.getTransactionHash();
                    transactionTransmissionManager.sendContractStatusNotification(
                            pendingToSubmitConfirmationRecord.getCustomerPublicKey(),
                            pendingToSubmitConfirmationRecord.getBrokerPublicKey(),
                            contractHash,
                            pendingToSubmitConfirmationRecord.getTransactionId(),
                            ContractTransactionStatus.CONFIRM_OFFLINE_ACK_PAYMENT,
                            Plugins.BROKER_ACK_OFFLINE_PAYMENT
                    );
                    brokerAckOfflinePaymentBusinessTransactionDao.updateContractTransactionStatus(
                            contractHash,
                            ContractTransactionStatus.CONFIRM_OFFLINE_ACK_PAYMENT
                    );
                }

                /**
                 * Check if pending events
                 */
                List<String> pendingEventsIdList= brokerAckOfflinePaymentBusinessTransactionDao.getPendingEvents();
                for(String eventId : pendingEventsIdList){
                    checkPendingEvent(eventId);
                }


            } catch (CantGetContractListException e) {
                throw new CannotSendContractHashException(
                        e,
                        "Sending contract hash",
                        "Cannot get the contract list from database");
            } catch (UnexpectedResultReturnedFromDatabaseException e) {
                throw new CannotSendContractHashException(
                        e,
                        "Sending contract hash",
                        "Unexpected result in database");
            }

        }

        private void raiseAckConfirmationEvent(String contractHash){
            FermatEvent fermatEvent = eventManager.getNewEvent(EventType.BROKER_ACK_PAYMENT_CONFIRMED);
            BrokerAckPaymentConfirmed brokerAckPaymentConfirmed = (BrokerAckPaymentConfirmed) fermatEvent;
            brokerAckPaymentConfirmed.setSource(EventSource.BROKER_ACK_OFFLINE_PAYMENT);
            brokerAckPaymentConfirmed.setContractHash(contractHash);
            brokerAckPaymentConfirmed.setPaymentType(PaymentType.FIAT_MONEY);
            eventManager.raiseEvent(brokerAckPaymentConfirmed);
        }

        private void checkPendingEvent(String eventId) throws
                UnexpectedResultReturnedFromDatabaseException {

            try{
                String eventTypeCode= brokerAckOfflinePaymentBusinessTransactionDao.getEventType(eventId);
                String contractHash;
                BusinessTransactionMetadata businessTransactionMetadata;
                ContractTransactionStatus contractTransactionStatus;
                BusinessTransactionRecord businessTransactionRecord;
                if(eventTypeCode.equals(EventType.INCOMING_NEW_CONTRACT_STATUS_UPDATE.getCode())){
                    //This will happen in customer side
                    List<Transaction<BusinessTransactionMetadata>> pendingTransactionList=
                            transactionTransmissionManager.getPendingTransactions(
                                    Specialist.UNKNOWN_SPECIALIST);
                    for(Transaction<BusinessTransactionMetadata> record : pendingTransactionList){
                        businessTransactionMetadata=record.getInformation();
                        contractHash=businessTransactionMetadata.getContractHash();
                        if(brokerAckOfflinePaymentBusinessTransactionDao.isContractHashInDatabase(contractHash)){
                            contractTransactionStatus= brokerAckOfflinePaymentBusinessTransactionDao.
                                    getContractTransactionStatus(contractHash);
                            //TODO: analyze what we need to do here.
                        }else{
                            CustomerBrokerContractPurchase customerBrokerContractPurchase=
                                    customerBrokerContractPurchaseManager.getCustomerBrokerContractPurchaseForContractId(
                                            contractHash);
                            //If the contract is null, I cannot handle with this situation
                            ObjectChecker.checkArgument(customerBrokerContractPurchase);
                            brokerAckOfflinePaymentBusinessTransactionDao.persistContractInDatabase(
                                    customerBrokerContractPurchase);
                            customerBrokerContractPurchaseManager.updateStatusCustomerBrokerPurchaseContractStatus(
                                    contractHash,
                                    ContractStatus.PENDING_MERCHANDISE);
                            raiseAckConfirmationEvent(contractHash);
                        }
                        transactionTransmissionManager.confirmReception(record.getTransactionID());
                    }
                    brokerAckOfflinePaymentBusinessTransactionDao.updateEventStatus(eventId, EventStatus.NOTIFIED);
                }
                if(eventTypeCode.equals(EventType.INCOMING_CONFIRM_BUSINESS_TRANSACTION_RESPONSE.getCode())){
                    //This will happen in broker side
                    List<Transaction<BusinessTransactionMetadata>> pendingTransactionList=
                            transactionTransmissionManager.getPendingTransactions(
                                    Specialist.UNKNOWN_SPECIALIST);
                    for(Transaction<BusinessTransactionMetadata> record : pendingTransactionList){
                        businessTransactionMetadata=record.getInformation();
                        contractHash=businessTransactionMetadata.getContractHash();
                        if(brokerAckOfflinePaymentBusinessTransactionDao.isContractHashInDatabase(contractHash)){
                            businessTransactionRecord =
                                    brokerAckOfflinePaymentBusinessTransactionDao.
                                            getBusinessTransactionRecordByContractHash(contractHash);
                            contractTransactionStatus= businessTransactionRecord.getContractTransactionStatus();
                            if(contractTransactionStatus.getCode().equals(ContractTransactionStatus.ONLINE_PAYMENT_ACK.getCode())){
                                businessTransactionRecord.setContractTransactionStatus(ContractTransactionStatus.CONFIRM_OFFLINE_ACK_PAYMENT);
                                customerBrokerContractSaleManager.updateStatusCustomerBrokerSaleContractStatus(
                                        contractHash,
                                        ContractStatus.PENDING_MERCHANDISE);
                                raiseAckConfirmationEvent(contractHash);
                            }
                        }
                        transactionTransmissionManager.confirmReception(record.getTransactionID());
                    }
                    brokerAckOfflinePaymentBusinessTransactionDao.updateEventStatus(eventId, EventStatus.NOTIFIED);
                }
                if(eventTypeCode.equals(EventType.NEW_CONTRACT_OPENED.getCode())){
                    //the eventId from this event is the contractId - Broker side
                    CustomerBrokerContractSale customerBrokerContractSale=
                            customerBrokerContractSaleManager.getCustomerBrokerContractSaleForContractId(
                                    eventId);
                    ObjectChecker.checkArgument(
                            customerBrokerContractSale,
                            "The customerBrokerContractSale is null");
                    MoneyType paymentType=getMoneyTypeFromContract(customerBrokerContractSale);
                    brokerAckOfflinePaymentBusinessTransactionDao.persistContractInDatabase(
                            customerBrokerContractSale,
                            paymentType);

                }

            } catch (CantUpdateRecordException exception) {
                throw new UnexpectedResultReturnedFromDatabaseException(
                        exception,
                        "Checking pending events",
                        "Cannot update the database");
            } catch (CantConfirmTransactionException exception) {
                throw new UnexpectedResultReturnedFromDatabaseException(
                        exception,
                        "Checking pending events",
                        "Cannot confirm the transaction");
            } catch (CantUpdateCustomerBrokerContractSaleException exception) {
                throw new UnexpectedResultReturnedFromDatabaseException(
                        exception,
                        "Checking pending events",
                        "Cannot update the contract sale status");
            } catch (CantDeliverPendingTransactionsException exception) {
                throw new UnexpectedResultReturnedFromDatabaseException(
                        exception,
                        "Checking pending events",
                        "Cannot get the pending transactions from transaction transmission plugin");
            } catch (CantInsertRecordException exception) {
                throw new UnexpectedResultReturnedFromDatabaseException(
                        exception,
                        "Checking pending events",
                        "Cannot insert a record in database");
            } catch (CantGetListCustomerBrokerContractPurchaseException exception) {
                throw new UnexpectedResultReturnedFromDatabaseException(
                        exception,
                        "Checking pending events",
                        "Cannot get the purchase contract");
            } catch (CantGetListCustomerBrokerContractSaleException exception) {
                throw new UnexpectedResultReturnedFromDatabaseException(
                        exception,
                        "Checking pending events",
                        "Cannot get the sale contract");
            } catch (CantUpdateCustomerBrokerContractPurchaseException exception) {
                throw new UnexpectedResultReturnedFromDatabaseException(
                        exception,
                        "Checking pending events",
                        "Cannot update the contract purchase status");
            } catch (ObjectNotSetException exception) {
                throw new UnexpectedResultReturnedFromDatabaseException(
                        exception,
                        "Checking pending events",
                        "The customerBrokerContractSale is null");
            } catch (CantGetListSaleNegotiationsException exception) {
                throw new UnexpectedResultReturnedFromDatabaseException(
                        exception,
                        "Checking pending events",
                        "The customerBrokerContractSale is null");
            }

        }

        /**
         * This method returns a BankTransactionParametersRecord from a given ContractHash/Id
         * @param contractHash
         * @return
         */
        private BankTransactionParametersRecord getBankTransactionParametersRecordFromContractId(
                String contractHash) throws CantGetBankTransactionParametersRecordException {
            try{
                CustomerBrokerContractSale customerBrokerContractSale=
                        customerBrokerContractSaleManager.getCustomerBrokerContractSaleForContractId(
                                contractHash);
                ObjectChecker.checkArgument(
                        customerBrokerContractSale,"The customerBrokerContractSale is null");
                String negotiationId=customerBrokerContractSale.getNegotiatiotId();
                ObjectChecker.checkArgument(
                        negotiationId,"The negotiationId for contractHash "+contractHash+" is null");
                CustomerBrokerSaleNegotiation customerBrokerSaleNegotiation=
                        customerBrokerSaleNegotiationManager.getNegotiationsByNegotiationId(
                                UUID.fromString(negotiationId));
                ObjectChecker.checkArgument(
                        customerBrokerSaleNegotiation,
                        "The customerBrokerSaleNegotiation by Id"+negotiationId+" is null");
                //Get the Bank wallet public key
                cryptoBrokerWalletManager.loadCryptoBrokerWallet("")
                //Create the BankTransactionParametersRecord
                BankTransactionParametersRecord bankTransactionParametersRecord=
                        new BankTransactionParametersRecord(
                                pluginId.toString())
            } catch (CantGetListCustomerBrokerContractSaleException e) {
                throw new CantGetBankTransactionParametersRecordException(
                        e,
                        "Getting the BankTransactionParametersRecord",
                        "Cannot get the CustomerBrokerContractSale by contractHash/Id:\n"+
                                contractHash);
            } catch (ObjectNotSetException e) {
                e.printStackTrace();
            } catch (CantGetListSaleNegotiationsException e) {
                e.printStackTrace();
            }

        }

        /**
         * This method returns the currency type from a contract
         *
         * @param customerBrokerContractSale
         * @return
         * @throws CantGetListSaleNegotiationsException
         */
        private MoneyType getMoneyTypeFromContract(
                CustomerBrokerContractSale customerBrokerContractSale) throws
                CantGetListSaleNegotiationsException {
            try {
                String negotiationId = customerBrokerContractSale.getNegotiatiotId();
                CustomerBrokerSaleNegotiation customerBrokerSaleNegotiation =
                        customerBrokerSaleNegotiationManager.getNegotiationsByNegotiationId(
                                UUID.fromString(negotiationId));
                ObjectChecker.checkArgument(customerBrokerSaleNegotiation,"The customerBrokerSaleNegotiation is null");
                Collection<Clause> clauses = customerBrokerSaleNegotiation.getClauses();
                ClauseType clauseType;
                for (Clause clause : clauses) {
                    clauseType = clause.getType();
                    if (clauseType.equals(ClauseType.BROKER_PAYMENT_METHOD)) {
                        return MoneyType.getByCode(clause.getValue());
                    }
                }
                throw new CantGetListSaleNegotiationsException(
                        "Cannot find the proper clause");
            } catch (InvalidParameterException e) {
                throw new CantGetListSaleNegotiationsException(
                        "Cannot get the negotiation list",
                        e);
            } catch (CantGetListClauseException e) {
                throw new CantGetListSaleNegotiationsException(
                        "Cannot find clauses list");
            } catch (ObjectNotSetException e) {
                throw new CantGetListSaleNegotiationsException(
                        "The customerBrokerSaleNegotiation is null",
                        e);
            }

        }

    }

}


