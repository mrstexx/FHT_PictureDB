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

    public static synchronized StageManager getInstance() {
        if (instance == null) {
            instance = new StageManager();
        }
        return instance;
    }

    public void addStage(EStage eStage, Stage stage) {
        stages.put(eStage, stage);
    }

    public Stage getStage(EStage eStage) {
        Stage stage = stages.get(eStage);
        if (stage != null) {
            return stage;
        }
        LOG.error("Requesting a stage that does not exist");
        return null;
    }

    public void closeStage(EStage eStage) {
        getStage(eStage).close();
        LOG.info("Closing stage: " + eStage.toString());
    }

}
