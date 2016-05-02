package model;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FileStub {

	private String absolutePath;
	private long freeSpace;
	private boolean isDirectory;
	private boolean isFile;
	private long lastModified;
	private String name;
	private long totalSpace;
	private long usableSpace;

	public FileStub(File parent) {
		List<String> getersName = Arrays.asList(this.getClass().getMethods()).stream().filter((method) -> {
			return method.getName().indexOf("get") != -1;
		}).map(method -> method.getName()).map(methodName -> methodName.substring(3)).collect(Collectors.toList());
		for (String name : getersName) {
			String seterName = "set" + name;
			String geterName = "get" + name;

			Method geter;
			try {
				geter = parent.getClass().getMethod(geterName, new Class<?>[0]);
			} catch (NoSuchMethodException e) {
				try {
					geter = parent.getClass().getMethod(name, new Class<?>[0]);
				} catch (NoSuchMethodException e1) {
					try {
						geter = parent.getClass().getMethod("is" + name, new Class<?>[0]);
					} catch (NoSuchMethodException | SecurityException e2) {
						continue;
					}
				} catch (SecurityException e1) {
					continue;
				}
			} catch (SecurityException e) {
				continue;
			}

			try {
				Method seter = this.getClass().getMethod(seterName, geter.getReturnType());
				seter.invoke(this, geter.invoke(parent, new Object[0]));
			} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				continue;
			}
		}
	}

	public String getAbsolutePath() {
		return absolutePath;
	}

	public long getFreeSpace() {
		return freeSpace;
	}

	/**
	 * @return the lastModified
	 */
	public long getLastModified() {
		return lastModified;
	}

	public String getName() {
		return name;
	}

	public long getTotalSpace() {
		return totalSpace;
	}

	public long getUsableSpace() {
		return usableSpace;
	}

	public boolean isDirectory() {
		return isDirectory;
	}

	public boolean isFile() {
		return isFile;
	}

	public long lastModified() {
		return lastModified;
	}

	/**
	 * @param absolutePath
	 *            the absolutePath to set
	 */
	public void setAbsolutePath(String absolutePath) {
		this.absolutePath = absolutePath;
	}

	/**
	 * @param isDirectory
	 *            the isDirectory to set
	 */
	public void setDirectory(boolean isDirectory) {
		this.isDirectory = isDirectory;
	}

	/**
	 * @param isFile
	 *            the isFile to set
	 */
	public void setFile(boolean isFile) {
		this.isFile = isFile;
	}

	/**
	 * @param freeSpace
	 *            the freeSpace to set
	 */
	public void setFreeSpace(long freeSpace) {
		this.freeSpace = freeSpace;
	}

	/**
	 * @param lastModified
	 *            the lastModified to set
	 */
	public void setLastModified(long lastModified) {
		this.lastModified = lastModified;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param totalSpace
	 *            the totalSpace to set
	 */
	public void setTotalSpace(long totalSpace) {
		this.totalSpace = totalSpace;
	}

	/**
	 * @param usableSpace
	 *            the usableSpace to set
	 */
	public void setUsableSpace(long usableSpace) {
		this.usableSpace = usableSpace;
	}

}
