
-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema vc_db
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `vc_db` ;

-- -----------------------------------------------------
-- Schema vc_db
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `vc_db` DEFAULT CHARACTER SET utf8 ;
USE `vc_db` ;

-- -----------------------------------------------------
-- Table `vc_db`.`VC_TRANSACTION`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `vc_db`.`VC_TRANSACTION` ;

CREATE TABLE IF NOT EXISTS `vc_db`.`VC_TRANSACTION` (
                                                        `REFERENCE_NUMBER` VARCHAR(45) NOT NULL,
                                                        `AMOUNT` DOUBLE NOT NULL,
                                                        `SENDER_UUID` VARCHAR(45) NOT NULL,
                                                        `RECEIVER_UUID` VARCHAR(45) NOT NULL,
                                                        `CREATED_AT` DATETIME NULL DEFAULT NULL,
                                                        `ID` INT NOT NULL AUTO_INCREMENT,
                                                        UNIQUE INDEX `transaction_ref_UNIQUE` (`REFERENCE_NUMBER` ASC) VISIBLE,
                                                        PRIMARY KEY (`ID`),
                                                        UNIQUE INDEX `ID_UNIQUE` (`ID` ASC) VISIBLE)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `vc_db`.`VC_WALLET`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `vc_db`.`VC_WALLET` ;

CREATE TABLE IF NOT EXISTS `vc_db`.`VC_WALLET` (
                                                   `ID` INT NOT NULL AUTO_INCREMENT,
                                                   `FUND` DOUBLE NOT NULL DEFAULT 0.0,
                                                   `LAST_ACCRUE_DATE` DATETIME NULL DEFAULT NULL,
                                                   `UUID` VARCHAR(45) NOT NULL,
                                                   PRIMARY KEY (`ID`),
                                                   UNIQUE INDEX `UUID_UNIQUE` (`UUID` ASC) VISIBLE,
                                                   UNIQUE INDEX `ID_UNIQUE` (`ID` ASC) VISIBLE)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `vc_db`.`VC_USER`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `vc_db`.`VC_USER` ;

CREATE TABLE IF NOT EXISTS `vc_db`.`VC_USER` (
                                                 `ID` INT NOT NULL AUTO_INCREMENT,
                                                 `USERNAME` VARCHAR(45) NOT NULL,
                                                 `EMAIL` VARCHAR(45) NOT NULL,
                                                 `PASSWORD` VARCHAR(100) NOT NULL,
                                                 `CREATED_AT` DATETIME NULL DEFAULT NULL,
                                                 `UUID` VARCHAR(45) NOT NULL,
                                                 `WALLET_ID` INT NULL,
                                                 PRIMARY KEY (`ID`),
                                                 UNIQUE INDEX `email_UNIQUE` (`EMAIL` ASC) VISIBLE,
                                                 UNIQUE INDEX `username_UNIQUE` (`USERNAME` ASC) VISIBLE,
                                                 UNIQUE INDEX `UUID_UNIQUE` (`UUID` ASC) VISIBLE,
                                                 INDEX `fk_VC_USER_VC_WALLET_idx` (`WALLET_ID` ASC) VISIBLE,
                                                 UNIQUE INDEX `ID_UNIQUE` (`ID` ASC) VISIBLE,
                                                 CONSTRAINT `fk_VC_USER_VC_WALLET`
                                                     FOREIGN KEY (`WALLET_ID`)
                                                         REFERENCES `vc_db`.`VC_WALLET` (`ID`)
                                                         ON DELETE NO ACTION
                                                         ON UPDATE NO ACTION)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `vc_db`.`VC_USER_HAS_TRANSACTION`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `vc_db`.`VC_USER_HAS_TRANSACTION` ;

CREATE TABLE IF NOT EXISTS `vc_db`.`VC_USER_HAS_TRANSACTION` (
                                                                 `USER_ID` INT NOT NULL,
                                                                 `TRANSACTION_ID` INT NOT NULL,
                                                                 `TRANSACTION_TYPE` ENUM('SEND', 'RECEIVE') NOT NULL,
                                                                 PRIMARY KEY (`USER_ID`, `TRANSACTION_ID`),
                                                                 INDEX `fk_VC_USER_has_VC_TRANSACTION_VC_TRANSACTION1_idx` (`TRANSACTION_ID` ASC) VISIBLE,
                                                                 INDEX `fk_VC_USER_has_VC_TRANSACTION_VC_USER1_idx` (`USER_ID` ASC) VISIBLE,
                                                                 CONSTRAINT `fk_VC_USER_has_VC_TRANSACTION_VC_USER1`
                                                                     FOREIGN KEY (`USER_ID`)
                                                                         REFERENCES `vc_db`.`VC_USER` (`ID`)
                                                                         ON DELETE NO ACTION
                                                                         ON UPDATE NO ACTION,
                                                                 CONSTRAINT `fk_VC_USER_has_VC_TRANSACTION_VC_TRANSACTION1`
                                                                     FOREIGN KEY (`TRANSACTION_ID`)
                                                                         REFERENCES `vc_db`.`VC_TRANSACTION` (`ID`)
                                                                         ON DELETE NO ACTION
                                                                         ON UPDATE NO ACTION)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8;
