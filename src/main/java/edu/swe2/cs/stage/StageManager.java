package edu.swe2.cs.stage;

import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class StageManager {

    private final static Logger LOG = LogManager.getLogger(StageManager.class);
    private static StageManager instance = null;
    private Map<EStage, Stage> stages = null;

    private StageManager() {
        stages = new HashMap<>();
    }

    /**
     * Get an instance of StageManager
     *
     * @return Singleton class instance
     */
    public static synchronized StageManager getInstance() {
        if (instance == null) {
            instance = new StageManager();
        }
        return instance;
    }

    /**
     * Add a stage to be managed by this StageManager
     *
     * @param eStage Enum identifying the stage
     * @param stage Stage to be mapped to given eStage
     */
    public void addStage(EStage eStage, Stage stage) {
        stages.put(eStage, stage);
    }

    /**
     * Return a stage that is mapped to given enum eStage and managed by this StageManager
     *
     * @param eStage Enum identifying the stage
     * @return Stage that is mapped to given enum eStage
     */
    public Stage getStage(EStage eStage) {
        Stage stage = stages.get(eStage);
        if (stage != null) {
            return stage;
        }
        LOG.error("Requesting a stage that does not exist");
        return null;
    }

    /**
     * Close a given stage managed by this StageManager
     *
     * @param eStage Enum identifying the stage
     */
    public void closeStage(EStage eStage) {
        getStage(eStage).close();
        LOG.info("Closing stage: " + eStage.toString());
    }

}
