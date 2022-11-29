package com.attia.vc.mapper;

import com.attia.vc.model.Wallet;
import org.openapitools.model.WalletDetails;
import org.springframework.stereotype.Component;

@Component
public class WalletMapper {
    public WalletDetails mapWalletDAOTOWalletDetails(Wallet wallet) {
        return new WalletDetails().walletId(wallet.getUUID())
                .fund(wallet.getFund());
    }
}
