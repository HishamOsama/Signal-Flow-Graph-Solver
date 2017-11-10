package controller.utils;

import javafx.beans.property.SimpleStringProperty;

public class ForwardPath {
	private final SimpleStringProperty path;
	private final SimpleStringProperty gain;

	ForwardPath(String path, String gain) {
		this.path = new SimpleStringProperty(path);
		this.gain = new SimpleStringProperty(gain);
	}

	public String getPath() {
		return path.get();
	}

	public void setPath(String fName) {
		path.set(fName);
	}

	public String getGain() {
		return gain.get();
	}

	public void setGain(String fName) {
		gain.set(fName);
	}

}
