package br.com.ecc.server.service.upload;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import br.com.ecc.client.service.UploadProgressService;
import br.com.ecc.client.ui.component.upload.UploadedFile;
import br.com.ecc.server.SessionHelper;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.inject.Singleton;

@Singleton
public class UploadProgressServiceImpl extends RemoteServiceServlet implements UploadProgressService {
	private static final long serialVersionUID = -2369978776140825255L;

	@Override
	public List<UploadedFile> readFiles(String uploadDirectory) {
		//String uploadDirectory = SessionHelper.getUploadDir(getThreadLocalRequest().getSession());

		File[] listFiles = readFilesFromDir(uploadDirectory);
		sortFiles(listFiles);

		List<UploadedFile> files = new ArrayList<UploadedFile>();

		for (File file : listFiles) { 
			UploadedFile fileDto = new UploadedFile();
			fileDto.setPath(uploadDirectory);
			fileDto.setFilename(file.getName());
			fileDto.setDate(new Date(file.lastModified()));
			files.add(fileDto);
		}
		return files;
	}

	@Override
	public int countFiles(String uploadDirectory) {
		//String uploadDirectory = SessionHelper.getUploadDir(getThreadLocalRequest().getSession());
		return readFilesFromDir(uploadDirectory).length;
	}

	private File[] readFilesFromDir(final String directory) {
		File uploadDirectory = new File(directory);
		return uploadDirectory.listFiles(new FileFilter() {
			@Override
			public boolean accept(final File file) {
				return null == file ? false : file.isFile();
			}
		});
	}

	private void sortFiles(final File[] listFiles) {
		Arrays.sort(listFiles, new Comparator<File>() {
			@Override
			public int compare(final File f1, final File f2) {
				return Long.valueOf(f2.lastModified()).compareTo(f1.lastModified());
			}
		});
	}

	@Override
	public UploadedFile getProgress() {
		return SessionHelper.getUploadFile(getThreadLocalRequest().getSession());
	}

	@Override
	public void initialize(UploadedFile uploadedFile) {
		SessionHelper.setUploadFile(getThreadLocalRequest().getSession(), uploadedFile);
	}  
}