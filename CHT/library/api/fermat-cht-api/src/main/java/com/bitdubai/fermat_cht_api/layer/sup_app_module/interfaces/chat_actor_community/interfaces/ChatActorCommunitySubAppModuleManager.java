package com.bitdubai.fermat_cht_api.layer.sup_app_module.interfaces.chat_actor_community.interfaces;

import com.bitdubai.fermat_api.layer.actor_connection.common.enums.ConnectionState;
import com.bitdubai.fermat_api.layer.actor_connection.common.exceptions.ActorConnectionNotFoundException;
import com.bitdubai.fermat_api.layer.actor_connection.common.exceptions.CantDisconnectFromActorException;
import com.bitdubai.fermat_api.layer.actor_connection.common.exceptions.CantListActorConnectionsException;
import com.bitdubai.fermat_api.layer.actor_connection.common.exceptions.ConnectionAlreadyRequestedException;
import com.bitdubai.fermat_api.layer.actor_connection.common.exceptions.UnexpectedConnectionStateException;
import com.bitdubai.fermat_api.layer.all_definition.settings.exceptions.CantGetSettingsException;
import com.bitdubai.fermat_api.layer.all_definition.settings.exceptions.CantPersistSettingsException;
import com.bitdubai.fermat_api.layer.all_definition.settings.exceptions.SettingsNotFoundException;
import com.bitdubai.fermat_api.layer.modules.ModuleSettingsImpl;
import com.bitdubai.fermat_api.layer.modules.exceptions.ActorIdentityNotSelectedException;
import com.bitdubai.fermat_api.layer.modules.exceptions.CantGetSelectedActorIdentityException;
import com.bitdubai.fermat_api.layer.modules.interfaces.ModuleManager;


import com.bitdubai.fermat_cht_api.layer.actor_network_service.exceptions.ConnectionRequestNotFoundException;

import com.bitdubai.fermat_cht_api.layer.identity.exceptions.CantGetChatActorWaitingException;
import com.bitdubai.fermat_cht_api.layer.identity.exceptions.CantListChatIdentityException;
import com.bitdubai.fermat_cht_api.layer.sup_app_module.interfaces.chat_actor_community.exceptions.ActorChatConnectionAlreadyRequestesException;
import com.bitdubai.fermat_cht_api.layer.sup_app_module.interfaces.chat_actor_community.exceptions.ActorChatTypeNotSupportedException;
import com.bitdubai.fermat_cht_api.layer.sup_app_module.interfaces.chat_actor_community.exceptions.ActorConnectionRequestNotFoundException;
import com.bitdubai.fermat_cht_api.layer.sup_app_module.interfaces.chat_actor_community.exceptions.CantAcceptChatRequestException;
import com.bitdubai.fermat_cht_api.layer.sup_app_module.interfaces.chat_actor_community.exceptions.CantGetChtActorSearchResult;
import com.bitdubai.fermat_cht_api.layer.sup_app_module.interfaces.chat_actor_community.exceptions.CantListChatActorException;
import com.bitdubai.fermat_cht_api.layer.sup_app_module.interfaces.chat_actor_community.exceptions.CantListChatIdentitiesToSelectException;
import com.bitdubai.fermat_cht_api.layer.sup_app_module.interfaces.chat_actor_community.exceptions.CantRequestActorConnectionException;
import com.bitdubai.fermat_cht_api.layer.sup_app_module.interfaces.chat_actor_community.exceptions.CantValidateActorConnectionStateException;
import com.bitdubai.fermat_cht_api.layer.sup_app_module.interfaces.chat_actor_community.exceptions.ChatActorConnectionDenialFailedException;
import com.bitdubai.fermat_cht_api.layer.sup_app_module.interfaces.chat_actor_community.exceptions.ChatActorDisconnectingFailedException;
import com.bitdubai.fermat_cht_api.layer.sup_app_module.interfaces.chat_actor_community.exceptions.ChatActorCancellingFailedException;
import com.bitdubai.fermat_cht_api.layer.sup_app_module.interfaces.chat_actor_community.settings.ChatActorCommunitySettings;


import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 * The interface <code>com.bitdubai.fermat_cht_api.layer.sup_app_module.interfaces.chat_actor_community.interfaces.ChatActorCommunitySubAppModuleManager</code>
 * provides all the necessary methods for the Chat Actor Community sub app.
 *
 * Created by Eleazar (eorono@protonmail.com) on 3/04/16.
 * @author eorono
 * @version 1.0.0
 */

public interface ChatActorCommunitySubAppModuleManager extends ModuleManager, Serializable, ModuleSettingsImpl<ChatActorCommunitySettings> {

    /**
     * The method <code>listWorldChatActor</code> returns the list of all chat actor in the world,
     * setting their status (CONNECTED, for example) with respect to the selectedIdentity parameter
     * logged in chat actor
     *
     * @return a list of all chat actor in the world
     *
     * @throws CantListChatActorException if something goes wrong.
     */
    List<ChatActorCommunityInformation> listWorldChatActor(ChatActorCommunitySelectableIdentity selectableIdentity, int max, int offset) throws CantListChatActorException, CantGetChtActorSearchResult, CantListActorConnectionsException;

    /**
     * The method <code>listSelectableIdentities</code> lists all the Chat Actor identities
     * stored locally in the device.
     *
     * @return a list of broker and customer identities the current device the user can use to log in.
     *
     * @throws CantListChatIdentitiesToSelectException if something goes wrong.
     */
    List<ChatActorCommunitySelectableIdentity> listSelectableIdentities() throws CantListChatIdentitiesToSelectException, CantListChatIdentityException;

    /**
     * Through the method <code>setSelectedActorIdentity</code> we can set the selected actor identity.
     */
    void setSelectedActorIdentity(ChatActorCommunitySelectableIdentity identity) throws CantPersistSettingsException, CantGetSettingsException, SettingsNotFoundException;

    /**
     * The method <code>getChatActorSearch</code> returns an interface that allows searching for remote
     * Chat Actor that are not linked to the local selectedIdentity
     *
     * @return a searching interface
     */
    ChatActorCommunitySearch getChatActorSearch();

    /**
     * The method <code>searchConnectedChatActor</code> returns an interface that allows searching for remote
     * Crypto Brokers that are linked to the local selectedIdentity
     *
     * @return a searching interface
     */
    ChatActorCommunitySearch searchConnectedChatActor(ChatActorCommunitySelectableIdentity selectedIdentity);

    /**
     * The method <code>requestConnectionToChatActor</code> initialises a contact request between
     * two Chat Actor.
     *
     * @param selectedIdentity       The selected local actor identity.
     * @param chatActorLocalToContact  The information of the remote actor to connect to.
     *
     * @throws CantRequestActorConnectionException           if something goes wrong.
     * @throws ActorChatConnectionAlreadyRequestesException if the connection already exists.
     * @throws ActorChatTypeNotSupportedException           if the actor type is not supported.
     */
    void requestConnectionToChatActor(ChatActorCommunitySelectableIdentity selectedIdentity     ,
                                         ChatActorCommunityInformation chatActorLocalToContact) throws CantRequestActorConnectionException,
            ActorChatTypeNotSupportedException,
            ActorChatConnectionAlreadyRequestesException, ConnectionAlreadyRequestedException;

    /**
     * The method <code>acceptChatActor</code> takes the information of a connection request, accepts
     * the request and adds the chat actor to the list managed by this plugin with ContactState CONTACT.
     *
     * @param requestId      The request id of te connection to accept.
     *
     * @throws CantAcceptChatRequestException           if something goes wrong.
     * @throws ActorConnectionRequestNotFoundException   if we cant find the connection request..
     */
    void acceptChatActor(UUID requestId) throws CantAcceptChatRequestException, ActorConnectionRequestNotFoundException;

    /**
     * The method <code>denyChatConnection</code> denies a conection request from other chat actor
     *
     * @param requestId      The request id of te connection to deny.
     *
     * @throws ChatActorConnectionDenialFailedException if something goes wrong.
     * @throws ActorConnectionRequestNotFoundException   if we cant find the connection request.
     */
    void denyChatConnection(UUID requestId) throws ChatActorConnectionDenialFailedException, ActorConnectionRequestNotFoundException;

    /**
     * The method <code>disconnectChatActor</code> disconnect an chat actor from the list managed by this
     * plugin
     *
     * @param requestId      The request id of te connection to disconnect.
     *
     * @throws ChatActorDisconnectingFailedException if something goes wrong.
     * @throws ActorConnectionRequestNotFoundException   if we cant find the connection request.
     */
    void disconnectChatActor(UUID requestId) throws ChatActorDisconnectingFailedException, ActorConnectionRequestNotFoundException, ConnectionRequestNotFoundException, CantDisconnectFromActorException, UnexpectedConnectionStateException, ActorConnectionNotFoundException;

    /**
     * The method <code>cancelChatActor</code> cancels an chat actor from the list managed by this
     *
     * @param requestId      The request id of te connection to cancel.
     *
     * @throws ChatActorCancellingFailedException if something goes wrong.
     * @throws ConnectionRequestNotFoundException   if we cant find the connection request.
     */
    void cancelChatActor(UUID requestId) throws ChatActorCancellingFailedException, ActorConnectionRequestNotFoundException, ConnectionRequestNotFoundException;

    public void exposeIdentityInWat();

    /**
     * The method <code>listAllConnectedChatActor</code> returns the list of all chat actor registered by the
     * logged in chat actor
     *
     * @return the list of chat actor connected to the logged in chat
     *
     * @throws CantListChatActorException if something goes wrong.
     */
    List<ChatActorCommunityInformation> listAllConnectedChatActor(final ChatActorCommunitySelectableIdentity selectedIdentity,
                                                                         final int                                     max             ,
                                                                         final int                                     offset          ) throws CantListChatActorException;
    /**
     * The method <code>listChatActorPendingLocalAction</code> returns the list of chat actor waiting to be accepted
     * or rejected by the logged in chat actor
     *
     * @return the list of chat actor waiting to be accepted or rejected by the  logged in chat
     *
     * @throws CantListChatActorException if something goes wrong.
     */
    List<ChatActorCommunityInformation> listChatActorPendingLocalAction    (final ChatActorCommunitySelectableIdentity selectedIdentity,
                                                                               final int max,
                                                                               final int offset) throws CantListChatActorException;




    List<ChatActorCommunityInformation> listChatActorPendingRemoteAction(final ChatActorCommunitySelectableIdentity selectedIdentity,
                                                                                final int max,
                                                                                final int offset) throws CantListChatActorException;
    /**
     * Count chat actor waiting
     * @return
     */
    List<ChatActorCommunityInformation> getChatActorWaitingYourAcceptanceCount(String publicKey, int max, int offset) throws CantGetChatActorWaitingException;

    /**
     * The method <code>getActorConnectionState</code> returns the ConnectionState of a given actor
     * with respect to the selected actor
     * @param publicKey
     *
     * @return
     *
     * @throws CantValidateActorConnectionStateException if something goes wrong.
     */
    ConnectionState getActorConnectionState(String publicKey) throws CantValidateActorConnectionStateException;


    @Override
    ChatActorCommunitySelectableIdentity getSelectedActorIdentity() throws CantGetSelectedActorIdentityException, ActorIdentityNotSelectedException;

    @Override
    void createIdentity(String name, String phrase, byte[] profile_img) throws Exception;

    @Override
    void setAppPublicKey(String publicKey);

    @Override
    int[] getMenuNotifications();


}
