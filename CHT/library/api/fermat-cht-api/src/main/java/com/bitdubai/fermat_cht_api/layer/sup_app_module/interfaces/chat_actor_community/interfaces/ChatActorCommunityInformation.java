package com.bitdubai.fermat_cht_api.layer.sup_app_module.interfaces.chat_actor_community.interfaces;

import com.bitdubai.fermat_api.layer.actor_connection.common.enums.ConnectionState;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 * The interface <code>com.bitdubai.fermat_cht_api.layer.sup_app_module.interfaces.chat_actor_community.interfaces.ChatActorCommunityInformation</code>
 * provides the method to extract information about an chat actor.
 *
 * Created by Eleazar Oroño (eorono@protonmail.com) on 31/03/16.
 */
public interface ChatActorCommunityInformation extends Serializable {

    /**
     * The method <code>getPublicKey</code> returns the public key of the represented chat actor
     * @return the public key of the chat actor
     */
    String getPublicKey();

    /**
     * The method <code>getAlias</code> returns the name of the represented chat actor
     *
     * @return the name of the chat actor
     */
    String getAlias();

    /**
     * The method <code>getProfileImage</code> returns the profile image of the represented chat actor
     *
     * @return the profile image
     */
    byte[] getImage();

    /**
     * The method <code>listChatActorAlias</code> returns the list of the public chat actor alias
     * @return
     */
    List listAlias();

    /**
     * The method <code>getConnectionState</code> returns the Connection State Status
     * @return ConnectionState object
     */
    ConnectionState getConnectionState();

    /**
     * The method <code>getConnectionId</code> returns the Connection UUID this actor has with the selected actor
     * @return UUID object
     */
    UUID getConnectionId();

    String getStatus();


}
