package edu.swe2.cs.bl;

import edu.swe2.cs.dal.DALFactory;
import edu.swe2.cs.dal.DBManager;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

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
            Connection connection = DBManager.getInstance().getConnection();
            List<String> fileNames = DALFactory.getDAL().getFileNames(connection);
            if (fileCache == null) {
                fileCache = fileNames;
            } else {
                // delete files in cache that are not in db
                clearRemovedFiles(fileNames);
                // add files to cache that are new in db
                addNewFiles(fileNames);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
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
            if (fileCache.contains(fileName)) {
                return true;
            }
        }
        return false;
    }

}
