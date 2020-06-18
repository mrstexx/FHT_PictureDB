package edu.swe2.cs.bl;

import edu.swe2.cs.dal.DALFactory;
import edu.swe2.cs.dal.DBManager;
import edu.swe2.cs.dal.IDAL;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

//TODO: Old Cache --> Match this cache to new caching
// represents all files stored in db

public class FileCache {

    private static final Logger LOG = LogManager.getLogger(FileCache.class);

    private List<String> fileCache;
    private static FileCache instance = null;

    private FileCache() {
        update();
    }


    /**
     * Get an instance of file cache
     *
     * @return Singleton class instance
     */
    public synchronized static FileCache getInstance() {
        if (instance == null) {
            instance = new FileCache();
        }
        return instance;
    }


    /**
     * Synchronize file cache with database
     */
    public void update() {
        try {
            LOG.info("Updating file cache...Synchronize with the database data...");
            IDAL dal = DALFactory.getDAL();
            assert dal != null;
            dal.setConnection(DBManager.getInstance().getConnection());
            List<String> fileNames = dal.getFileNames();
            if (fileCache == null) {
                fileCache = fileNames;
            } else {
                // delete files in cache that are not in db
                clearRemovedFiles(fileNames);
                // add files to cache that are new in db
                addNewFiles(fileNames);
            }
        } catch (Exception e) {
            LOG.error("Error occurred while updating file cache.", e);
        }
    }

    private void clearRemovedFiles(List<String> fileNames) {
        if (fileCache != null) {
            fileCache.removeIf(fileName -> !fileNames.contains(fileName));
        }
    }

    private void addNewFiles(List<String> fileNames) {
        if (fileCache != null) {
            for (String fileName : fileNames) {
                if (!fileCache.contains(fileName)) {
                    fileCache.add(fileName);
                }
            }
        }
    }

    /**
     * Add file name to file to cache
     *
     * @param fileName Filename to be cached
     */
    public void addFile(String fileName) {
        if (fileCache != null) {
            LOG.info("Adding new file to cache data");
            fileCache.add(fileName);
        }
    }

    /**
     * Check if file cache contains specified file name
     *
     * @param fileName Filename to be checked
     * @return True if this cache contains specified file name
     */
    public boolean containsFile(String fileName) {
        if (fileCache != null) {
            return fileCache.contains(fileName);
        }
        return false;
    }

    /**
     * Get size of this file cache
     *
     * @return Number of files contained by this cache
     */
    public int getSize() {
        return this.fileCache.size();
    }

}
