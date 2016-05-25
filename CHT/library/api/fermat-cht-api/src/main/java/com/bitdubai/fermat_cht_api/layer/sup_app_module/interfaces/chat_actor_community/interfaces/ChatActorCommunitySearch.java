package com.bitdubai.fermat_cht_api.layer.sup_app_module.interfaces.chat_actor_community.interfaces;

import com.bitdubai.fermat_cht_api.layer.sup_app_module.interfaces.chat_actor_community.exceptions.CantGetChtActorSearchResult;

import java.io.Serializable;
import java.util.List;

/**
 * The interface <code>com.bitdubai.fermat_cht_api.layer.sup_app_module.interfaces.chat_actor_community.interfaces.ChatActorCommunitySearch</code>
 * provides the methods to search for a particular Chat actor
 * Created by Eleazar Oroño (eorono@protonmail.com) on 1/04/16.
 */
public interface ChatActorCommunitySearch extends Serializable {

    void addActorAlias(String alias);

    List<ChatActorCommunityInformation> getResult() throws CantGetChtActorSearchResult;

    String getAlias();

    String getPublicKey();
}
