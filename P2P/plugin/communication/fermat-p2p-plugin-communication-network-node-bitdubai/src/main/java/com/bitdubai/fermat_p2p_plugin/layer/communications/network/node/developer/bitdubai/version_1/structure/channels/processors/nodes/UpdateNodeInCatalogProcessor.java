package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.nodes;

import com.bitdubai.fermat_api.layer.all_definition.exceptions.InvalidParameterException;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.exceptions.CantInsertRecordDataBaseException;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.exceptions.CantUpdateRecordDataBaseException;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.exceptions.RecordNotFoundException;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.Package;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.profiles.NodeProfile;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.HeadersAttName;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.MessageContentType;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.PackageType;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.endpoinsts.FermatWebSocketChannelEndpoint;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.PackageProcessor;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.data.node.request.AddNodeToCatalogMsgRequest;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.data.node.respond.AddNodeToCatalogMsjRespond;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.data.node.respond.UpdateNodeInCatalogMsjRespond;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.entities.NodesCatalog;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.entities.NodesCatalogTransaction;

import org.apache.commons.lang.ClassUtils;
import org.jboss.logging.Logger;

import java.sql.Timestamp;

import javax.websocket.Session;

/**
 * The Class <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.nodes.UpdateNodeInCatalogProcessor</code>
 * <p/>
 * Created by Roberto Requena - (rart3001@gmail.com) on 04/04/16.
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public class UpdateNodeInCatalogProcessor extends PackageProcessor {

    /**
     * Represent the LOG
     */
    private final Logger LOG = Logger.getLogger(ClassUtils.getShortClassName(UpdateNodeInCatalogProcessor.class.getName()));

    /**
     * Constructor with parameter
     *
     * @param channel
     * */
    public UpdateNodeInCatalogProcessor(FermatWebSocketChannelEndpoint channel) {
        super(channel, PackageType.UPDATE_NODE_IN_CATALOG_REQUEST);
    }

    /**
     * (non-javadoc)
     * @see PackageProcessor#processingPackage(Session, Package)
     */
    @Override
    public void processingPackage(Session session, Package packageReceived) {

        LOG.info("Processing new package received");

        String channelIdentityPrivateKey = getChannel().getChannelIdentity().getPrivateKey();
        String destinationIdentityPublicKey = (String) session.getUserProperties().get(HeadersAttName.CPKI_ATT_HEADER_NAME);
        NodeProfile nodeProfile = null;
        UpdateNodeInCatalogMsjRespond updateNodeInCatalogMsjRespond;

        try {

            AddNodeToCatalogMsgRequest messageContent = AddNodeToCatalogMsgRequest.parseContent(packageReceived.getContent());

            /*
             * Create the method call history
             */
            methodCallsHistory(getGson().toJson(messageContent.getNodeProfile()), destinationIdentityPublicKey);

            /*
             * Validate if content type is the correct
             */
            if (messageContent.getMessageContentType() == MessageContentType.OBJECT){

                /*
                 * Obtain the profile of the node
                 */
                nodeProfile = messageContent.getNodeProfile();


                if (!getDaoFactory().getNodesCatalogDao().exists(nodeProfile.getIdentityPublicKey())){

                    LOG.info("The node profile to update no exist");

                    /*
                     * Notify the node already exist
                     */
                    updateNodeInCatalogMsjRespond = new UpdateNodeInCatalogMsjRespond(UpdateNodeInCatalogMsjRespond.STATUS.FAIL, "The node profile to update no exist", nodeProfile, Boolean.FALSE);

                }else {

                    LOG.info("Updating ...");

                    /*
                     * Insert NodesCatalog into data base
                     */
                    updateNodesCatalog(nodeProfile);

                    /*
                     * Insert NodesCatalogTransaction into data base
                     */
                    NodesCatalogTransaction transaction = insertNodesCatalogTransaction(nodeProfile);

                    /*
                     * Insert NodesCatalogTransactionsPendingForPropagation into data base
                     */
                    insertNodesCatalogTransactionsPendingForPropagation(transaction);

                    /*
                     * If all ok, respond whit success message
                     */
                    updateNodeInCatalogMsjRespond = new UpdateNodeInCatalogMsjRespond(UpdateNodeInCatalogMsjRespond.STATUS.SUCCESS, UpdateNodeInCatalogMsjRespond.STATUS.SUCCESS.toString(), nodeProfile, Boolean.TRUE);

                }

                Package packageRespond = Package.createInstance(updateNodeInCatalogMsjRespond.toJson(), packageReceived.getNetworkServiceTypeSource(), PackageType.UPDATE_NODE_IN_CATALOG_RESPOND, channelIdentityPrivateKey, destinationIdentityPublicKey);

                /*
                 * Send the respond
                 */
                session.getAsyncRemote().sendObject(packageRespond);

            }else {

                updateNodeInCatalogMsjRespond = new UpdateNodeInCatalogMsjRespond(UpdateNodeInCatalogMsjRespond.STATUS.FAIL, "Invalid content type: "+messageContent.getMessageContentType(), nodeProfile, Boolean.FALSE);
                Package packageRespond = Package.createInstance(updateNodeInCatalogMsjRespond.toJson(), packageReceived.getNetworkServiceTypeSource(), PackageType.ADD_NODE_TO_CATALOG_RESPOND, channelIdentityPrivateKey, destinationIdentityPublicKey);

                /*
                 * Send the respond
                 */
                session.getAsyncRemote().sendObject(packageRespond);

            }

            LOG.info("Processing finish");

        } catch (Exception exception){

            try {

                exception.printStackTrace();
                LOG.error(exception.getMessage());

                /*
                 * Respond whit fail message
                 */
                updateNodeInCatalogMsjRespond = new UpdateNodeInCatalogMsjRespond(AddNodeToCatalogMsjRespond.STATUS.EXCEPTION, exception.getLocalizedMessage(), nodeProfile, Boolean.FALSE);
                Package packageRespond = Package.createInstance(updateNodeInCatalogMsjRespond.toJson(), packageReceived.getNetworkServiceTypeSource(), PackageType.UPDATE_NODE_IN_CATALOG_RESPOND, channelIdentityPrivateKey, destinationIdentityPublicKey);

                /*
                 * Send the respond
                 */
                session.getAsyncRemote().sendObject(packageRespond);

            } catch (Exception e) {
                LOG.error(e.getMessage());
            }

        }

    }


    /**
     * Update a row into the data base
     *
     * @param nodeProfile
     * @throws CantInsertRecordDataBaseException
     */
    private void updateNodesCatalog(NodeProfile nodeProfile) throws CantUpdateRecordDataBaseException, RecordNotFoundException, InvalidParameterException {

        /*
         * Create the NodesCatalog
         */
        NodesCatalog nodeCatalog = new NodesCatalog();
        nodeCatalog.setIp(nodeProfile.getIp());
        nodeCatalog.setDefaultPort(nodeProfile.getDefaultPort());
        nodeCatalog.setIdentityPublicKey(nodeProfile.getIdentityPublicKey());
        nodeCatalog.setName(nodeProfile.getName());
        nodeCatalog.setOfflineCounter(0);
        nodeCatalog.setLastConnectionTimestamp(new Timestamp(System.currentTimeMillis()));


        //Validate if location are available
        if (nodeProfile.getLocation() != null){
            nodeCatalog.setLastLatitude(nodeProfile.getLocation().getLatitude());
            nodeCatalog.setLastLongitude(nodeProfile.getLocation().getLongitude());
        }

        /*
         * Save into the data base
         */
        getDaoFactory().getNodesCatalogDao().update(nodeCatalog);
    }

    /**
     * Create a new row into the data base
     *
     * @param nodeProfile
     * @throws CantInsertRecordDataBaseException
     */
    private NodesCatalogTransaction insertNodesCatalogTransaction(NodeProfile nodeProfile) throws CantInsertRecordDataBaseException {

        /*
         * Create the NodesCatalog
         */
        NodesCatalogTransaction transaction = new NodesCatalogTransaction();
        transaction.setIp(nodeProfile.getIp());
        transaction.setDefaultPort(nodeProfile.getDefaultPort());
        transaction.setIdentityPublicKey(nodeProfile.getIdentityPublicKey());
        transaction.setName(nodeProfile.getName());
        transaction.setTransactionType(NodesCatalogTransaction.UPDATE_TRANSACTION_TYPE);
        transaction.setHashId(transaction.getHashId());
        transaction.setLastConnectionTimestamp(new Timestamp(System.currentTimeMillis()));


        //Validate if location are available
        if (nodeProfile.getLocation() != null){
            transaction.setLastLatitude(nodeProfile.getLocation().getLatitude());
            transaction.setLastLongitude(nodeProfile.getLocation().getLongitude());
        }

        /*
         * Save into the data base
         */
        getDaoFactory().getNodesCatalogTransactionDao().create(transaction);

        return transaction;
    }

    /**
     * Create a new row into the data base
     *
     * @param transaction
     * @throws CantInsertRecordDataBaseException
     */
    private void insertNodesCatalogTransactionsPendingForPropagation(NodesCatalogTransaction transaction) throws CantInsertRecordDataBaseException {

        /*
         * Save into the data base
         */
        getDaoFactory().getNodesCatalogTransactionsPendingForPropagationDao().create(transaction);
    }
}
