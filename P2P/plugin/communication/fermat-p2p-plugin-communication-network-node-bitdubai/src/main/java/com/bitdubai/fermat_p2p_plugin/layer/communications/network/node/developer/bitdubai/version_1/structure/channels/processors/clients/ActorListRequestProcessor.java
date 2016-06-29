package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.clients;

import com.bitdubai.fermat_api.layer.all_definition.exceptions.InvalidParameterException;
import com.bitdubai.fermat_api.layer.all_definition.location_system.NetworkNodeCommunicationDeviceLocation;
import com.bitdubai.fermat_api.layer.osa_android.location_system.LocationSource;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.DiscoveryQueryParameters;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.Package;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.client.request.ActorListMsgRequest;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.client.respond.ActorCallMsgRespond;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.client.respond.ActorListMsgRespond;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.profiles.ActorProfile;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.HeadersAttName;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.MessageContentType;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.PackageType;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.endpoinsts.FermatWebSocketChannelEndpoint;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.PackageProcessor;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.entities.ActorsCatalog;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.entities.CheckedInActor;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.entities.NodesCatalog;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.exceptions.CantReadRecordDataBaseException;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.exceptions.RecordNotFoundException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.apache.commons.lang.ClassUtils;
import org.jboss.logging.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.websocket.Session;

/**
 * The Class <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.clients.ActorListRequestProcessor</code>
 * process all packages received the type <code>MessageType.ACTOR_LIST_REQUEST</code><p/>
 *
 * Created by Leon Acosta - (laion.cj91@gmail.com) on 24/06/2016.
 *
 * @author  lnacosta
 * @version 1.0
 * @since   Java JDK 1.7
 */
public class ActorListRequestProcessor extends PackageProcessor {

    /**
     * Represent the LOG
     */
    private final Logger LOG = Logger.getLogger(ClassUtils.getShortClassName(ActorListRequestProcessor.class));

    /**
     * Constructor whit parameter
     *
     * @param fermatWebSocketChannelEndpoint register
     */
    public ActorListRequestProcessor(FermatWebSocketChannelEndpoint fermatWebSocketChannelEndpoint) {
        super(fermatWebSocketChannelEndpoint, PackageType.ACTOR_LIST_REQUEST);
    }

    /**
     * (non-javadoc)
     * @see PackageProcessor#processingPackage(Session, Package)
     */
    @Override
    public void processingPackage(Session session, Package packageReceived) {

        LOG.info("Processing new package received " + packageReceived.getPackageType());

        String channelIdentityPrivateKey = getChannel().getChannelIdentity().getPrivateKey();
        String destinationIdentityPublicKey = (String) session.getUserProperties().get(HeadersAttName.CPKI_ATT_HEADER_NAME);

        ActorListMsgRequest messageContent = null;
        try {

            messageContent = ActorListMsgRequest.parseContent(packageReceived.getContent());

            /*
             * Create the method call history
             */
            methodCallsHistory(getGson().toJson(messageContent.getParameters())+getGson().toJson(messageContent.getNetworkServicePublicKey())+getGson().toJson(messageContent.getClientPublicKey()), destinationIdentityPublicKey);

            /*
             * Validate if content type is the correct
             */
            if (messageContent.getMessageContentType() == MessageContentType.JSON) {

                List<ActorProfile> actorsList = filterActorsFromCheckedInActor(messageContent.getParameters(), messageContent.getClientPublicKey());

                /*
                 * If all ok, respond whit success message
                 */
                ActorListMsgRespond actorListMsgRespond = new ActorListMsgRespond(ActorCallMsgRespond.STATUS.SUCCESS, ActorCallMsgRespond.STATUS.SUCCESS.toString(), actorsList, messageContent.getNetworkServicePublicKey(), messageContent.getQueryId());
                Package packageRespond = Package.createInstance(actorListMsgRespond.toJson(), packageReceived.getNetworkServiceTypeSource(), PackageType.ACTOR_LIST_RESPONSE, channelIdentityPrivateKey, destinationIdentityPublicKey);

                /*
                 * Send the respond
                 */
                session.getAsyncRemote().sendObject(packageRespond);

            }

        }catch (Exception exception){

            try {

                LOG.error(exception.getMessage());
                exception.printStackTrace();

                /*
                 * Respond whit fail message
                 */
                ActorListMsgRespond actorCallMsgRespond = new ActorListMsgRespond(
                        ActorListMsgRespond.STATUS.FAIL,
                        exception.getLocalizedMessage(),
                        null,
                        null,
                        messageContent == null ? null : messageContent.getQueryId()
                );
                Package packageRespond = Package.createInstance(actorCallMsgRespond.toJson(), packageReceived.getNetworkServiceTypeSource(), PackageType.ACTOR_LIST_RESPONSE, channelIdentityPrivateKey, destinationIdentityPublicKey);

                /*
                 * Send the respond
                 */
                session.getAsyncRemote().sendObject(packageRespond);

            } catch (Exception e) {
                LOG.error(e.getMessage());
            }
        }

    }

    private List<ActorsCatalog> filterActorsOnline(List<ActorsCatalog>  actorsCatalogs){

        List<ActorsCatalog> actors = new ArrayList<>();

        for(ActorsCatalog actorsCatalog : actorsCatalogs){

            try {

                if(actorsCatalog.getNodeIdentityPublicKey().equals(getNetworkNodePluginRoot().getIdentity().getPublicKey())) {

                    if (getDaoFactory().getCheckedInActorDao().exists(actorsCatalog.getIdentityPublicKey()))
                        actors.add(actorsCatalog);

                } else if(isActorOnline(actorsCatalog))
                    actors.add(actorsCatalog);

            } catch (CantReadRecordDataBaseException e) {
                e.printStackTrace();
            }

        }

        return actors;
    }

    /**
     * Filter all network service from data base that mach
     * with the parameters
     *
     * @param discoveryQueryParameters
     * @return List<ActorProfile>
     */
    private ArrayList<ActorProfile> filterActors(DiscoveryQueryParameters discoveryQueryParameters, String clientIdentityPublicKey) throws CantReadRecordDataBaseException, InvalidParameterException {

        Map<String, ActorProfile> profileList = new HashMap<>();

        Map<String, Object> filters = constructFiltersActorTable(discoveryQueryParameters);
        List<ActorsCatalog> actorsList;

        int max    = 10;
        int offset =  0;

        if (discoveryQueryParameters.getMax() != null && discoveryQueryParameters.getMax() > 0)
            max = (discoveryQueryParameters.getMax() > 100) ? 100 : discoveryQueryParameters.getMax();

        if (discoveryQueryParameters.getOffset() != null && discoveryQueryParameters.getOffset() >= 0)
            offset = discoveryQueryParameters.getOffset();

        while (profileList.size() < max && getDaoFactory().getActorsCatalogDao().getAllCount(filters) > offset) {

            if (discoveryQueryParameters.getLocation() != null)
                actorsList = getDaoFactory().getActorsCatalogDao().findAllNearestTo(filters, max, offset, discoveryQueryParameters.getLocation());
            else
                actorsList = getDaoFactory().getActorsCatalogDao().findAll(filters, max, offset);

            List<ActorsCatalog> actors = filterActorsOnline(actorsList);

            for (ActorsCatalog actorsCatalog : actors) {

                if (clientIdentityPublicKey == null ||
                        actorsCatalog.getClientIdentityPublicKey() == null ||
                        !actorsCatalog.getClientIdentityPublicKey().equals(clientIdentityPublicKey)) {

                    profileList.put(actorsCatalog.getIdentityPublicKey(), buildActorProfileFromActorCatalogRecord(actorsCatalog));
                }
            }

            offset += max;
        }

        return new ArrayList<>(profileList.values());

    }

    /*
     * get ActorProfile From ActorsCatalog
     */
    private ActorProfile buildActorProfileFromActorCatalogRecord(ActorsCatalog actor){

        ActorProfile actorProfile = new ActorProfile();
        actorProfile.setIdentityPublicKey(actor.getIdentityPublicKey());
        actorProfile.setAlias(actor.getAlias());
        actorProfile.setName(actor.getName());
        actorProfile.setActorType(actor.getActorType());
        actorProfile.setPhoto(actor.getPhoto());
        actorProfile.setExtraData(actor.getExtraData());
        actorProfile.setLocation(actor.getLastLocation());

        return actorProfile;
    }

    /**
     * Construct data base filter from discovery query parameters
     *
     * @param discoveryQueryParameters
     * @return Map<String, Object> filters
     */
    private Map<String, Object> constructFiltersActorTable(DiscoveryQueryParameters discoveryQueryParameters){

        Map<String, Object> filters = new HashMap<>();

        if (discoveryQueryParameters.getIdentityPublicKey() != null){
            filters.put(CommunicationsNetworkNodeP2PDatabaseConstants.CHECKED_IN_ACTOR_IDENTITY_PUBLIC_KEY_COLUMN_NAME, discoveryQueryParameters.getIdentityPublicKey());
        }

        if (discoveryQueryParameters.getName() != null){
            filters.put(CommunicationsNetworkNodeP2PDatabaseConstants.CHECKED_IN_ACTOR_NAME_COLUMN_NAME, discoveryQueryParameters.getName());
        }

        if (discoveryQueryParameters.getAlias() != null){
            filters.put(CommunicationsNetworkNodeP2PDatabaseConstants.CHECKED_IN_ACTOR_ALIAS_COLUMN_NAME, discoveryQueryParameters.getAlias());
        }

        if (discoveryQueryParameters.getActorType() != null){
            filters.put(CommunicationsNetworkNodeP2PDatabaseConstants.CHECKED_IN_ACTOR_ACTOR_TYPE_COLUMN_NAME, discoveryQueryParameters.getActorType());
        }

        if (discoveryQueryParameters.getExtraData() != null){
            filters.put(CommunicationsNetworkNodeP2PDatabaseConstants.CHECKED_IN_ACTOR_EXTRA_DATA_COLUMN_NAME, discoveryQueryParameters.getExtraData());
        }

        return filters;
    }

    private Boolean isActorOnline(ActorsCatalog actorsCatalog){

        try {

            String nodeUrl = getNodeUrl(actorsCatalog.getNodeIdentityPublicKey());

            URL url = new URL("http://" + nodeUrl + "/fermat/rest/api/v1/online/component/actor/" + actorsCatalog.getIdentityPublicKey());

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String respond = reader.readLine();

            if (conn.getResponseCode() == 200 && respond != null && respond.contains("success")) {
                JsonParser parser = new JsonParser();
                JsonObject respondJsonObject = (JsonObject) parser.parse(respond.trim());

                return respondJsonObject.get("isOnline").getAsBoolean();

            } else {
                return false;
            }

        } catch (Exception e) {
            return false;
        }

    }

    private String getNodeUrl(final String publicKey) {

        try {

            NodesCatalog nodesCatalog = getDaoFactory().getNodesCatalogDao().findById(publicKey);
            return nodesCatalog.getIp()+":"+nodesCatalog.getDefaultPort();

        } catch (RecordNotFoundException exception) {
            throw new RuntimeException("Node not found in catalog: "+exception.getMessage());
        } catch (Exception exception) {
            throw new RuntimeException("Problem trying to find the node in the catalog: "+exception.getMessage());
        }
    }

    /*
     * TOD: uso temporal para que los muchachos puedan probar su plugin
     * mientras resolvemos lo de Actor_catalogs
     */
    private List<ActorProfile> filterActorsFromCheckedInActor(DiscoveryQueryParameters discoveryQueryParameters, String clientIdentityPublicKey) throws CantReadRecordDataBaseException, InvalidParameterException {

        List<ActorProfile> profileList = new ArrayList<>();
        List<CheckedInActor> listActorsLetf;
        Map<String, Object> filters = constructFiltersActorTable(discoveryQueryParameters);

        int max    = 10;
        int offset =  0;

        if( discoveryQueryParameters.getMax() != null &&
                discoveryQueryParameters.getOffset() != null &&
                discoveryQueryParameters.getMax() > 0 &&
                discoveryQueryParameters.getOffset() >= 0) {
            max = (discoveryQueryParameters.getMax() > 100) ? 100 : discoveryQueryParameters.getMax();
            offset = discoveryQueryParameters.getOffset();
        }

        if (discoveryQueryParameters.getLocation() != null)
            listActorsLetf = getDaoFactory().getCheckedInActorDao().findAllNearestTo(filters, max, offset, discoveryQueryParameters.getLocation());
        else
            listActorsLetf = getDaoFactory().getCheckedInActorDao().findAll(filters, max, offset);

        if(listActorsLetf != null) {
            for (CheckedInActor actor : listActorsLetf) {

                if (!actor.getClientIdentityPublicKey().equals(clientIdentityPublicKey))
                    profileList.add(getActorProfileFromCheckedInActor(actor));

            }
        }

        return profileList;

    }

    /*
    * get ActorProfile From CheckedInActor
    */
    private ActorProfile getActorProfileFromCheckedInActor(CheckedInActor actor){

        ActorProfile actorProfile = new ActorProfile();
        actorProfile.setIdentityPublicKey(actor.getIdentityPublicKey());
        actorProfile.setAlias(actor.getAlias());
        actorProfile.setName(actor.getName());
        actorProfile.setActorType(actor.getActorType());
        actorProfile.setPhoto(actor.getPhoto());
        actorProfile.setExtraData(actor.getExtraData());
        actorProfile.setLocation(new NetworkNodeCommunicationDeviceLocation(
                actor.getLatitude(),
                actor.getLongitude(),
                null     ,
                0        ,
                null     ,
                System.currentTimeMillis(),
                LocationSource.UNKNOWN));

        return actorProfile;
    }

}
