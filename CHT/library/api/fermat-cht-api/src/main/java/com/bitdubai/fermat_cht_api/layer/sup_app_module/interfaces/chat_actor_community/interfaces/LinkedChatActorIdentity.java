package com.bitdubai.fermat_cht_api.layer.sup_app_module.interfaces.chat_actor_community.interfaces;

import java.util.UUID;

/**
 * Created by Eleazar (eorono@protonmail.com) on 3/04/16.
 */
public interface LinkedChatActorIdentity {

    /**
     * The method <code>getPublicKey</code> returns the UUID of the connected actor to this identity
     */
    UUID getConnectionId();

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

   
}
