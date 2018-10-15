package java2.org.litespring.core.io.support;

import java2.org.litespring.core.io.FileSystemResource;
import java2.org.litespring.core.io.Resource;
import java2.org.litespring.util.Assert;
import java2.org.litespring.util.ClassUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class PackageResourceLoader {
    private static final Log logger = LogFactory.getLog(PackageResourceLoader.class);
    private ClassLoader classLoader;

    public PackageResourceLoader() {
        classLoader = ClassUtils.getDefaultClassLoader();
    }

    public PackageResourceLoader(ClassLoader classLoader) {
        Assert.notNull(classLoader, "Resource ClassLoader cannot be null");
        this.classLoader = classLoader;
    }

    /**
     * 根据basePackage遍历其下所有文件，将每一个文件封装成FileSystemResource
     *
     * @param basePackage
     * @return
     * @throws IOException
     */
    public Resource[] getResources(String basePackage) throws IOException {
        String rootDir = ClassUtils.convertClassNameToResourcePath(basePackage);
        //基于项目的ClassLoader得到路径，并根据该路径进行文件的获取，而并不是得到文件的绝对路径
        URL url = this.classLoader.getResource(rootDir);
        File rootDirFile = new File(url.getFile());

        Set<File> matchingFiles = retrieveMatchingFiles(rootDirFile);
        Resource[] resources = new Resource[matchingFiles.size()];

        int index = 0;
        for (File matchingFile : matchingFiles) {
            resources[index++] = new FileSystemResource(matchingFile);
        }
        return resources;

    }

    private Set<File> retrieveMatchingFiles(File rootDir) throws IOException {
        if (!rootDir.exists()) {
            if (logger.isDebugEnabled()) {
                logger.debug("Skipping [" + rootDir.getAbsolutePath() + "] because it does not exist");
            }
            return Collections.emptySet();
        }

        if (!rootDir.isDirectory()) {
            if (logger.isWarnEnabled()) {
                logger.warn("Skipping [" + rootDir.getAbsolutePath() + "] because it does not denote a directory");
            }
            return Collections.emptySet();
        }

        if (!rootDir.canRead()) {
            if (logger.isWarnEnabled()) {
                logger.warn("Cannot search for matching files underneath directory [" + rootDir.getAbsolutePath() +
                        "] because the application is not allowed to read the directory");
            }
            return Collections.emptySet();
        }

        HashSet<File> results = new LinkedHashSet<>();
        doRetrieveMatchingFiles(rootDir, results);

        return results;
    }

    private void doRetrieveMatchingFiles(File rootDir, Set<File> results) throws IOException {
        File[] dirContents = rootDir.listFiles();
        if (dirContents == null) {
            if (logger.isWarnEnabled()) {
                logger.warn("Could not retrieve contents of directory [" + rootDir.getAbsolutePath() + "]");
            }
            return;
        }

        for (File dirContent : dirContents) {
            if (dirContent.isDirectory()) {
                if (!dirContent.canRead()) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Skipping subdirectory [" + dirContent.getAbsolutePath() +
                                "] because the application is not allowed to read the directory");
                    }
                } else {
                    doRetrieveMatchingFiles(dirContent, results);
                }
            } else {
                results.add(dirContent);
            }
        }
    }
}
