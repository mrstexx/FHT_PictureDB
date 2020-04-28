package edu.swe2.cs.bl;

import edu.swe2.cs.dal.DALFactory;
import edu.swe2.cs.dal.DBManager;
import edu.swe2.cs.dal.DataAccessException;
import edu.swe2.cs.dal.IDAL;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

//TODO: Old Cache --> Match this cache to new caching
// represents all files stored in db

public class FileCache {

    private List<String> fileCache;
    private static FileCache instance = null;

    private FileCache() {
        update();
    }

    public synchronized static FileCache getInstance() {
        if (instance == null) {
            instance = new FileCache();
        }
        return instance;
    }

    public void update() {
        try {
            IDAL dal =  DALFactory.getDAL();
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
        } catch (DataAccessException | ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
        }
    }

    private void clearRemovedFiles(List<String> fileNames) {
        if (fileCache != null) {
            for (String fileName : fileCache) {
                if (!fileNames.contains(fileName)) {
                    fileCache.remove(fileName);
                }
            }
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

    public void addFile(String fileName) {
        if (fileCache != null) {
            fileCache.add(fileName);
        }
    }

    public boolean containsFile(String fileName) {
        if (fileCache != null) {
            return fileCache.contains(fileName);
        }
        return false;
    }

    public int getSize() {
        return this.fileCache.size();
    }

}
