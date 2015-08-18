package com.bitdubai.fermat_api.layer.dmp_network_service.wallet_store.interfaces;

import com.bitdubai.fermat_api.layer.all_definition.enums.WalletCategory;
import com.bitdubai.fermat_api.layer.all_definition.resources_structure.enums.ScreenSize;
import com.bitdubai.fermat_api.layer.all_definition.util.Version;
import com.bitdubai.fermat_api.layer.dmp_middleware.wallet_language.exceptions.CantGetWalletLanguageException;
import com.bitdubai.fermat_api.layer.dmp_network_service.wallet_store.exceptions.CantGetCatalogItemException;
import com.bitdubai.fermat_api.layer.dmp_network_service.wallet_store.exceptions.CantGetDesignerException;
import com.bitdubai.fermat_api.layer.dmp_network_service.wallet_store.exceptions.CantGetDeveloperException;
import com.bitdubai.fermat_api.layer.dmp_network_service.wallet_store.exceptions.CantGetSkinException;
import com.bitdubai.fermat_api.layer.dmp_network_service.wallet_store.exceptions.CantGetTranslatorException;
import com.bitdubai.fermat_api.layer.dmp_network_service.wallet_store.exceptions.CantGetWalletIconException;
import com.bitdubai.fermat_api.layer.dmp_network_service.wallet_store.exceptions.CantGetWalletsCatalogException;
import com.bitdubai.fermat_api.layer.dmp_network_service.wallet_store.exceptions.CantPublishDesignerInCatalogException;
import com.bitdubai.fermat_api.layer.dmp_network_service.wallet_store.exceptions.CantPublishDeveloperInCatalogException;
import com.bitdubai.fermat_api.layer.dmp_network_service.wallet_store.exceptions.CantPublishLanguageInCatalogException;
import com.bitdubai.fermat_api.layer.dmp_network_service.wallet_store.exceptions.CantPublishSkinInCatalogException;
import com.bitdubai.fermat_api.layer.dmp_network_service.wallet_store.exceptions.CantPublishTranslatorInCatalogException;
import com.bitdubai.fermat_api.layer.dmp_network_service.wallet_store.exceptions.CantPublishWalletInCatalogException;

import java.net.URL;
import java.util.List;
import java.util.UUID;

/**
 * Created by loui on 18/02/15.
 */
public interface WalletStoreManager {
    public void publishWallet(CatalogItem catalogItem) throws CantPublishWalletInCatalogException;

    public void publishSkin(Skin skin) throws CantPublishSkinInCatalogException;

    public void publishLanguage(Language language) throws CantPublishLanguageInCatalogException;

    public void publishDesigner(Designer designer) throws CantPublishDesignerInCatalogException;

    public void publishDeveloper(Developer developer) throws CantPublishDeveloperInCatalogException;

    public void publishTranslator(Translator translator) throws CantPublishTranslatorInCatalogException, CantPublishLanguageInCatalogException;


    public WalletCatalog getWalletCatalogue() throws CantGetWalletsCatalogException;

    public CatalogItem getCatalogItem(UUID walletId) throws CantGetCatalogItemException;

    public DetailedCatalogItem getDetailedCatalogItem(UUID walletId) throws CantGetCatalogItemException;

    public Language getLanguage(UUID walletId) throws CantGetWalletLanguageException;

    public Skin getSkin(UUID walletId) throws CantGetSkinException;

    public Developer getDeveloper(UUID developerId) throws CantGetDeveloperException;

    public Designer getDesigner(UUID designerId) throws CantGetDesignerException;

    public Translator getTranslator(UUID translatorId) throws CantGetTranslatorException;


    /**
     * Method returns an empty new instance of a CatalogItem
     *
     * @return CatalogItem
     */
    public CatalogItem constructEmptyCatalogItem();

    public Language constructLanguage(UUID skinId, String nameLanguage, String languageLabel, UUID walletId, Version version, Version initalWalletVersion, Version finalWalletVersion,  List<URL> videoPreviews, URL LanguageURL, long languageSizeInBytes, Translator translator, boolean isDefault );

    public Skin constructSkin(UUID skinId, String nameSkin, UUID walletId, ScreenSize screenSize, Version version, Version initalWalletVersion, Version finalWalletVersion, byte[] presentationImage, List<byte[]> previewImageList, boolean hasVideoPreview, List<URL> videoPreviews, URL skinURL, long skinSizeInBytes, Designer designer, boolean isDefault );

    public CatalogItem constructCatalogItem(UUID walletId, int defaultSizeInBytes,
                             String name, String description,
                             WalletCategory walletCategory,
                             byte[] icon,
                             Version version,
                             Version platformInitialVersion,
                             Version platformFinalVersion,
                             List<Skin> skins,
                             Skin skin,
                             Language language,
                             List<Language> languages,
                             Designer designer,
                             Developer developer,
                             Translator translator) throws CantGetWalletIconException;

    public Developer constructDeveloper(UUID developerId, String name, String PublicKey);

    public Designer constructDesigner(UUID designerrId, String name, String PublicKey);

}


