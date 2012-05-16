package br.com.ecc.client.ui.component.upload;

import java.io.Serializable;
import java.util.Date;

public final class UploadedFile implements Serializable {
	private static final long serialVersionUID = 9202390725897799834L;
	
	private String filename;
	private String path;
	private Date date;
	private Integer progress;
	private Integer idArquivoDigital;

	public UploadedFile() {
	}

	public String getFilename() {
		return filename;
	}
	public void setFilename(final String filename) {
		this.filename = filename;
	}

	@Override
	public String toString() {
		return filename + " - " + getDate();
	}
	@Override
	public boolean equals(final Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final UploadedFile other = (UploadedFile) obj;
		if ((this.filename == null) ? (other.filename != null) : !this.filename.equals(other.filename)) {
			return false;
		}
		if (this.getDate() != other.getDate() && (this.getDate() == null || !this.getDate().equals(other.getDate()))) {
			return false;
		}
		return true;
	}
	@Override
	public int hashCode() {
		int hash = 5;
		hash = 67 * hash + (this.filename != null ? this.filename.hashCode() : 0);
		hash = 67 * hash + (this.getDate() != null ? this.getDate().hashCode() : 0);
		return hash;
	}

	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Integer getProgress() {
		return progress;
	}
	public void setProgress(Integer progress) {
		this.progress = progress;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Integer getIdArquivoDigital() {
		return idArquivoDigital;
	}

	public void setIdArquivoDigital(Integer idArquivoDigital) {
		this.idArquivoDigital = idArquivoDigital;
	}
}