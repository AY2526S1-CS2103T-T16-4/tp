package bloodnet;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import bloodnet.commons.core.Config;
import bloodnet.commons.core.LogsCenter;
import bloodnet.commons.core.Version;
import bloodnet.commons.exceptions.DataLoadingException;
import bloodnet.commons.util.ConfigUtil;
import bloodnet.commons.util.StringUtil;
import bloodnet.logic.Logic;
import bloodnet.logic.LogicManager;
import bloodnet.model.DonationRecordList;
import bloodnet.model.Model;
import bloodnet.model.ModelManager;
import bloodnet.model.PersonList;
import bloodnet.model.ReadOnlyDonationRecordList;
import bloodnet.model.ReadOnlyPersonList;
import bloodnet.model.ReadOnlyUserPrefs;
import bloodnet.model.UserPrefs;
import bloodnet.model.util.SampleDataUtil;
import bloodnet.storage.DonationRecordStorage;
import bloodnet.storage.JsonDonationRecordStorage;
import bloodnet.storage.JsonPersonStorage;
import bloodnet.storage.JsonUserPrefsStorage;
import bloodnet.storage.PersonStorage;
import bloodnet.storage.Storage;
import bloodnet.storage.StorageManager;
import bloodnet.storage.UserPrefsStorage;
import bloodnet.ui.Ui;
import bloodnet.ui.UiManager;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Runs the application.
 */
public class MainApp extends Application {

    public static final Version VERSION = new Version(0, 2, 2, true);

    private static final Logger logger = LogsCenter.getLogger(MainApp.class);

    protected Ui ui;
    protected Logic logic;
    protected Storage storage;
    protected Model model;
    protected Config config;

    @Override
    public void init() throws Exception {
        logger.info("=============================[ Initializing BloodNet ]===========================");
        super.init();

        AppParameters appParameters = AppParameters.parse(getParameters());
        config = initConfig(appParameters.getConfigPath());
        initLogging(config);

        UserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(config.getUserPrefsFilePath());
        UserPrefs userPrefs = initPrefs(userPrefsStorage);
        PersonStorage personStorage = new JsonPersonStorage(userPrefs.getPersonListFilePath());
        DonationRecordStorage donationRecordtorage = new JsonDonationRecordStorage(
            userPrefs.getDonationRecordListFilePath());
        storage = new StorageManager(personStorage, donationRecordtorage, userPrefsStorage);

        model = initModelManager(storage, userPrefs);

        logic = new LogicManager(model, storage);

        ui = new UiManager(logic);
    }

    /**
     * Returns a {@code ModelManager} with the data from {@code storage}'s bloodnet and {@code userPrefs}. <br>
     * The data from the sample bloodnet will be used instead if {@code storage}'s bloodnet is not found,
     * or an empty bloodnet will be used instead if errors occur when reading {@code storage}'s bloodnet.
     */
    private Model initModelManager(Storage storage, ReadOnlyUserPrefs userPrefs) {
        logger.info("Using person data file : " + storage.getPersonListFilePath());

        Optional<ReadOnlyPersonList> personListOptional;
        ReadOnlyPersonList initialPersonData;
        try {
            personListOptional = storage.readPersonList();
            if (!personListOptional.isPresent()) {
                logger.info("Creating a new data file " + storage.getPersonListFilePath()
                    + " populated with a sample PersonList.");
            }
            initialPersonData = personListOptional.orElseGet(SampleDataUtil::getSamplePersonList);
        } catch (DataLoadingException e) {
            logger.warning("Data file at " + storage.getPersonListFilePath() + " could not be loaded."
                + " Will be starting with an empty PersonList.");
            initialPersonData = new PersonList();
        }

        logger.info("Using donation record data file : " + storage.getDonationRecordListFilePath());

        Optional<ReadOnlyDonationRecordList> donationRecordListOptional;
        ReadOnlyDonationRecordList initialDonationRecordData;
        try {
            donationRecordListOptional = storage.readDonationRecordList();
            if (!donationRecordListOptional.isPresent()) {
                logger.info("Creating a new data file " + storage.getDonationRecordListFilePath()
                    + " populated with a sample DonationRecordList.");
            }
            initialDonationRecordData = donationRecordListOptional.orElseGet(
                SampleDataUtil::getSampleDonationRecordList);
        } catch (DataLoadingException e) {
            logger.warning("Data file at " + storage.getDonationRecordListFilePath() + " could not be loaded."
                + " Will be starting with an empty DonationRecordList.");
            initialDonationRecordData = new DonationRecordList();
        }

        return new ModelManager(initialPersonData, initialDonationRecordData, userPrefs);
    }

    private void initLogging(Config config) {
        LogsCenter.init(config);
    }

    /**
     * Returns a {@code Config} using the file at {@code configFilePath}. <br>
     * The default file path {@code Config#DEFAULT_CONFIG_FILE} will be used instead
     * if {@code configFilePath} is null.
     */
    protected Config initConfig(Path configFilePath) {
        Config initializedConfig;
        Path configFilePathUsed;

        configFilePathUsed = Config.DEFAULT_CONFIG_FILE;

        if (configFilePath != null) {
            logger.info("Custom Config file specified " + configFilePath);
            configFilePathUsed = configFilePath;
        }

        logger.info("Using config file : " + configFilePathUsed);

        try {
            Optional<Config> configOptional = ConfigUtil.readConfig(configFilePathUsed);
            if (!configOptional.isPresent()) {
                logger.info("Creating new config file " + configFilePathUsed);
            }
            initializedConfig = configOptional.orElse(new Config());
        } catch (DataLoadingException e) {
            logger.warning("Config file at " + configFilePathUsed + " could not be loaded."
                + " Using default config properties.");
            initializedConfig = new Config();
        }

        //Update config file in case it was missing to begin with or there are new/unused fields
        try {
            ConfigUtil.saveConfig(initializedConfig, configFilePathUsed);
        } catch (IOException e) {
            logger.warning("Failed to save config file : " + StringUtil.getDetails(e));
        }
        return initializedConfig;
    }

    /**
     * Returns a {@code UserPrefs} using the file at {@code storage}'s user prefs file path,
     * or a new {@code UserPrefs} with default configuration if errors occur when
     * reading from the file.
     */
    protected UserPrefs initPrefs(UserPrefsStorage storage) {
        Path prefsFilePath = storage.getUserPrefsFilePath();
        logger.info("Using preference file : " + prefsFilePath);

        UserPrefs initializedPrefs;
        try {
            Optional<UserPrefs> prefsOptional = storage.readUserPrefs();
            if (!prefsOptional.isPresent()) {
                logger.info("Creating new preference file " + prefsFilePath);
            }
            initializedPrefs = prefsOptional.orElse(new UserPrefs());
        } catch (DataLoadingException e) {
            logger.warning("Preference file at " + prefsFilePath + " could not be loaded."
                + " Using default preferences.");
            initializedPrefs = new UserPrefs();
        }

        //Update prefs file in case it was missing to begin with or there are new/unused fields
        try {
            storage.saveUserPrefs(initializedPrefs);
        } catch (IOException e) {
            logger.warning("Failed to save config file : " + StringUtil.getDetails(e));
        }

        return initializedPrefs;
    }

    @Override
    public void start(Stage primaryStage) {
        logger.info("Starting PersonList " + MainApp.VERSION);
        ui.start(primaryStage);
    }

    @Override
    public void stop() {
        logger.info("============================ [ Stopping PersonList ] =============================");
        try {
            storage.saveUserPrefs(model.getUserPrefs());
        } catch (IOException e) {
            logger.severe("Failed to save preferences " + StringUtil.getDetails(e));
        }
    }
}
