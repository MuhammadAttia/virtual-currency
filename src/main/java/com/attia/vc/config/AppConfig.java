package com.attia.vc.config;

import com.attia.vc.model.Wallet;
import com.attia.vc.repository.WalletRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Configuration
@EnableScheduling
public class AppConfig {

    private final WalletRepository walletRepository;

    public AppConfig(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    @Scheduled(cron = "2 * * * * *") // for testing purposes every 1 minute
    // "0 0/30 * * * ?" for every 30 minutes
    public void updateWalletFund() {
        System.out.println("test: " + LocalDateTime.now());
        Iterable<Wallet> allWallets = walletRepository.findAll();
        allWallets.forEach(wallet -> {
            LocalDateTime now = LocalDateTime.now();
            long minutes = ChronoUnit.MINUTES.between(wallet.getLastAccrueDate(), now);
            if (minutes >= 30) {
                int multiply = (int) minutes / 30;
                wallet.setFund(wallet.getFund().add(new BigDecimal("0.25").multiply(new BigDecimal(multiply))));
                wallet.setLastAccrueDate(now);
                walletRepository.save(wallet);
            }
        });
    }

}
