package com.huya.lighthouse.dataswitch.util;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.permission.FsAction;

/**
 * 
 */
public class HdfsFile extends File {

	private static final long serialVersionUID = 8733771166184236741L;

	private FileSystem fs;
	private Path path;

	public HdfsFile(FileSystem fs, String path) {
		this(fs, new Path(toRootDirIfEmpty(path)));
	}

	public HdfsFile(FileSystem fs, Path path) {
		super(path.getName());
		if (fs == null)
			throw new IllegalArgumentException("fs must be not null");
		this.fs = fs;
		this.path = path;
	}

	public HdfsFile(FileSystem fs, HdfsFile parent, String child) {
		this(fs, new Path(parent.getHdfsPath(), toRootDirIfEmpty(child)));
	}

	public HdfsFile(FileSystem fs, String parent, String child) {
		this(fs, new Path(toRootDirIfEmpty(parent), toRootDirIfEmpty(child)));
	}

	public HdfsFile(String path) throws Exception {
		this(getFileSystem(path), new Path(path));
	}

	public HdfsFile(Path path) throws Exception {
		this(getFileSystem(path.getName()), path);
	}
	
	public HdfsFile(String path, String hdfsSitePath) throws Exception {
		this(getFileSystem(path, hdfsSitePath), new Path(path));
	}
	
	public static FileSystem getFileSystem(String dir) throws Exception {
		return getFileSystem(dir, "");
	}
	
	public static FileSystem getFileSystem(String dir, String hdfsSitePath) throws Exception {
		if (StringUtils.isBlank(hdfsSitePath)) {
			String hadoopHome = System.getenv("HADOOP_HOME");
			if (StringUtils.isBlank(hadoopHome)) {
				throw new Exception("HADOOP_HOME cannot be null");
			}
			hdfsSitePath = hadoopHome + "/etc/hadoop/hdfs-site.xml";
		}
		InputStream hdfsInputStream = new FileInputStream(new File(hdfsSitePath));
		return getFileSystem(dir, hdfsInputStream);
	}
	
	public static FileSystem getFileSystem(String dir, InputStream hdfsInputStream) throws Exception {
		FileSystem fs = null;
		Configuration conf = new Configuration();
		conf.addResource(hdfsInputStream);
		conf.set("fs.hdfs.impl", "org.apache.hadoop.hdfs.DistributedFileSystem");
		fs = FileSystem.get(new URI(dir), conf);
		return fs;
	}

	public HdfsFile(FileSystem fs, Path parent, String child) {
		this(fs, new Path(parent, child));
	}

	public Path getHdfsPath() {
		return this.path;
	}

	private static String toRootDirIfEmpty(String child) {
		return "".equals(child) ? "/" : child;
	}

	@Override
	public boolean canExecute() {
		try {
			return FsAction.EXECUTE.implies(fs.getFileStatus(path).getPermission().getUserAction());
		} catch (IOException e) {
			handleIOException(e);
			return false;
		}
	}

	@Override
	public boolean canRead() {
		try {
			return fs.exists(this.path);
		} catch (IOException e) {
			handleIOException(e);
			return false;
		}
	}

	@Override
	public boolean canWrite() {
		try {
			return fs.exists(this.path);
		} catch (IOException e) {
			handleIOException(e);
			return false;
		}
	}

	private void handleIOException(IOException e) {
		throw new RuntimeException(e);
	}

	@Override
	public int compareTo(File pathname) {
		return path.compareTo(new Path(pathname.getName()));
	}

	@Override
	public boolean createNewFile() throws IOException {
		return fs.createNewFile(path);
	}

	@Override
	public boolean delete() {
		try {
			return fs.delete(path, true);
		} catch (IOException e) {
			handleIOException(e);
			return false;
		}
	}

	@Override
	public void deleteOnExit() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean equals(Object obj) {
		return this == obj;
	}

	@Override
	public boolean exists() {
		try {
			return fs.exists(path);
		} catch (IOException e) {
			handleIOException(e);
			return false;
		}
	}

	@Override
	public File getAbsoluteFile() {
		return this;
	}

	@Override
	public String getAbsolutePath() {
		return path.getName();
	}

	@Override
	public File getCanonicalFile() throws IOException {
		return this;
	}

	@Override
	public String getCanonicalPath() throws IOException {
		return this.path.getName();
	}

	@Override
	public long getFreeSpace() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getName() {
		return path.getName();
	}

	@Override
	public String getParent() {
		return path.getParent().getName();
	}

	@Override
	public File getParentFile() {
		return new HdfsFile(this.fs, path.getParent().getName());
	}

	@Override
	public String getPath() {
		return path.toString();
	}

	@Override
	public long getTotalSpace() {
		throw new UnsupportedOperationException();
	}

	@Override
	public long getUsableSpace() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int hashCode() {
		return path.hashCode();
	}

	@Override
	public boolean isAbsolute() {
		return path.isAbsolute();
	}

	@Override
	public boolean isDirectory() {
		try {
			return fs.getFileStatus(path).isDirectory();
		} catch (IOException e) {
			handleIOException(e);
			return false;
		}
	}

	@Override
	public boolean isFile() {
		try {
			return fs.isFile(path);
		} catch (IOException e) {
			handleIOException(e);
			return false;
		}
	}

	@Override
	public boolean isHidden() {
		return path.getName().startsWith(".") || path.getName().startsWith("_");
	}

	@Override
	public long lastModified() {
		try {
			return fs.getFileStatus(path).getModificationTime();
		} catch (IOException e) {
			handleIOException(e);
			return 0;
		}
	}

	@Override
	public long length() {
		try {
			return fs.getFileStatus(path).getLen();
		} catch (IOException e) {
			handleIOException(e);
			return 0;
		}
	}

	@Override
	public String[] list() {
		FileStatus[] status;
		try {
			status = fs.listStatus(path);
			if (status == null)
				return new String[0];
			String[] results = new String[status.length];
			for (int i = 0; i < results.length; i++) {
				results[i] = status[i].getPath().getName();
			}
			return results;
		} catch (IOException e) {
			handleIOException(e);
			return null;
		}
	}

	@Override
	public String[] list(FilenameFilter filter) {
		FileStatus[] status = null;
		try {
			status = fs.listStatus(path);
			List<File> result = new ArrayList<File>(status.length);
			for (int i = 0; i < status.length; i++) {
				String name = status[i].getPath().getName();
				if (!filter.accept(this, name)) {
					continue;
				}
				result.add(new HdfsFile(fs, path, name));
			}
			return result.toArray(new String[0]);
		} catch (IOException e) {
			handleIOException(e);
			return null;
		}
	}

	@Override
	public File[] listFiles() {
		FileStatus[] status;
		try {
			status = fs.listStatus(path);
			HdfsFile[] results = new HdfsFile[status.length];
			for (int i = 0; i < results.length; i++) {
				results[i] = new HdfsFile(fs, status[i].getPath());
			}
			return results;
		} catch (IOException e) {
			handleIOException(e);
			return null;
		}
	}

	@Override
	public File[] listFiles(FileFilter filter) {
		FileStatus[] status = null;
		try {
			status = fs.listStatus(path);
			List<File> result = new ArrayList<File>(status.length);
			for (int i = 0; i < status.length; i++) {
				File file = new HdfsFile(fs, status[i].getPath());
				if (!filter.accept(file)) {
					continue;
				}
				result.add(file);
			}
			return result.toArray(new File[0]);
		} catch (IOException e) {
			handleIOException(e);
			return null;
		}
	}

	@Override
	public File[] listFiles(FilenameFilter filter) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean mkdir() {
		try {
			return fs.mkdirs(path);
		} catch (IOException e) {
			handleIOException(e);
			return false;
		}
	}

	@Override
	public boolean mkdirs() {
		try {
			return fs.mkdirs(path);
		} catch (IOException e) {
			handleIOException(e);
			return false;
		}
	}

	@Override
	public boolean renameTo(File dest) {
		try {
			return fs.rename(path, new Path(dest.getName()));
		} catch (IOException e) {
			handleIOException(e);
			return false;
		}
	}

	@Override
	public boolean setExecutable(boolean executable, boolean ownerOnly) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean setExecutable(boolean executable) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean setLastModified(long time) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean setReadable(boolean readable, boolean ownerOnly) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean setReadable(boolean readable) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean setReadOnly() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean setWritable(boolean writable, boolean ownerOnly) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean setWritable(boolean writable) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String toString() {
		return path.toString();
	}

	@Override
	public URI toURI() {
		return path.toUri();
	}

	@Override
	public URL toURL() throws MalformedURLException {
		throw new UnsupportedOperationException();
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}

	@Override
	protected void finalize() throws Throwable {
	}

	public InputStream open() {
		try {
			return fs.open(path);
		} catch (IOException e) {
			handleIOException(e);
			return null;
		}
	}

	public OutputStream create() {
		try {
			return fs.create(path, true);
		} catch (IOException e) {
			handleIOException(e);
			return null;
		}
	}

	public OutputStream append() {
		try {
			return fs.append(path);
		} catch (IOException e) {
			handleIOException(e);
			return null;
		}
	}
	
	public OutputStream append(int bufferSize) {
		try {
			return fs.append(path, bufferSize);
		} catch (IOException e) {
			handleIOException(e);
			return null;
		}
	}

}
