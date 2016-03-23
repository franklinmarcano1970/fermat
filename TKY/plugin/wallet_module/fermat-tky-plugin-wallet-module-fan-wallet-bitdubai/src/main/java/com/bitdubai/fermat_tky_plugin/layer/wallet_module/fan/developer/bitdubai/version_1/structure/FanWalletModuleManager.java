package com.bitdubai.fermat_tky_plugin.layer.wallet_module.fan.developer.bitdubai.version_1.structure;

import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.interfaces.ErrorManager;
import com.bitdubai.fermat_tky_api.all_definitions.enums.SongStatus;
import com.bitdubai.fermat_tky_api.layer.external_api.exceptions.CantGetBotException;
import com.bitdubai.fermat_tky_api.layer.external_api.interfaces.TokenlyApiManager;
import com.bitdubai.fermat_tky_api.layer.external_api.interfaces.swapbot.Bot;
import com.bitdubai.fermat_tky_api.layer.identity.fan.exceptions.CantListFanIdentitiesException;
import com.bitdubai.fermat_tky_api.layer.identity.fan.interfaces.Fan;
import com.bitdubai.fermat_tky_api.layer.identity.fan.interfaces.TokenlyFanIdentityManager;
import com.bitdubai.fermat_tky_api.layer.song_wallet.exceptions.CantDeleteSongException;
import com.bitdubai.fermat_tky_api.layer.song_wallet.exceptions.CantDownloadSongException;
import com.bitdubai.fermat_tky_api.layer.song_wallet.exceptions.CantGetSongListException;
import com.bitdubai.fermat_tky_api.layer.song_wallet.exceptions.CantGetSongStatusException;
import com.bitdubai.fermat_tky_api.layer.song_wallet.exceptions.CantSynchronizeWithExternalAPIException;
import com.bitdubai.fermat_tky_api.layer.song_wallet.exceptions.CantUpdateSongDevicePathException;
import com.bitdubai.fermat_tky_api.layer.song_wallet.exceptions.CantUpdateSongStatusException;
import com.bitdubai.fermat_tky_api.layer.song_wallet.interfaces.SongWalletTokenlyManager;
import com.bitdubai.fermat_tky_api.layer.song_wallet.interfaces.WalletSong;
import com.bitdubai.fermat_tky_api.layer.wallet_module.FanWalletModule;

import java.util.List;
import java.util.UUID;

/**
 * Created by Alexander Jimenez (alex_jimenez76@hotmail.com) on 3/16/16.
 */
public class FanWalletModuleManager implements FanWalletModule {
    private final ErrorManager errorManager;
    private final SongWalletTokenlyManager songWalletTokenlyManager;
    private final TokenlyFanIdentityManager tokenlyFanIdentityManager;
    private final TokenlyApiManager tokenlyApiManager;
    public FanWalletModuleManager(ErrorManager errorManager,
                                  SongWalletTokenlyManager songWalletTokenlyManager,
                                  TokenlyFanIdentityManager tokenlyFanIdentityManager,
                                  TokenlyApiManager tokenlyApiManager) {
        this.errorManager = errorManager;
        this.songWalletTokenlyManager = songWalletTokenlyManager;
        this.tokenlyFanIdentityManager = tokenlyFanIdentityManager;
        this.tokenlyApiManager = tokenlyApiManager;
    }
    @Override
    public List<WalletSong> getSongsBySongStatus(SongStatus songStatus) throws CantGetSongListException {
        return songWalletTokenlyManager.getSongsBySongStatus(songStatus);
    }

    @Override
    public List<WalletSong> getAvailableSongs() throws CantGetSongListException {
        return songWalletTokenlyManager.getAvailableSongs();
    }

    @Override
    public List<WalletSong> getDeletedSongs() throws CantGetSongListException {
        return songWalletTokenlyManager.getDeletedSongs();
    }

    @Override
    public SongStatus getSongStatus(UUID songId) throws CantGetSongStatusException {
        return songWalletTokenlyManager.getSongStatus(songId);
    }

    @Override
    public void synchronizeSongs(String tokenlyUsername) throws CantSynchronizeWithExternalAPIException {
        songWalletTokenlyManager.synchronizeSongs(tokenlyUsername);
    }

    @Override
    public void synchronizeSongsByUser(String username) throws CantSynchronizeWithExternalAPIException {
        songWalletTokenlyManager.synchronizeSongsByUser(username);
    }

    @Override
    public void deleteSong(UUID songId) throws CantDeleteSongException, CantUpdateSongStatusException {
        songWalletTokenlyManager.deleteSong(songId);
    }

    @Override
    public void downloadSong(UUID songId) throws CantDownloadSongException, CantUpdateSongDevicePathException, CantUpdateSongStatusException {
        songWalletTokenlyManager.downloadSong(songId);
    }

    @Override
    public List<Fan> listIdentitiesFromCurrentDeviceUser() throws CantListFanIdentitiesException {
        return tokenlyFanIdentityManager.listIdentitiesFromCurrentDeviceUser();
    }

    @Override
    public Bot getBotByBotId(String botId) throws CantGetBotException {
        return tokenlyApiManager.getBotByBotId(botId);
    }

    @Override
    public Bot getBotBySwapbotUsername(String username) throws CantGetBotException {
        return tokenlyApiManager.getBotBySwapbotUsername(username);
    }
}
