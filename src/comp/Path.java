package comp;

public class Path implements IPath {
	private String content;
	private float gain;

	public Path(String content, float gain) {
		this.content = content;
		this.gain = gain;
	}

	public String getContent() {
		return this.content;
	}

	public float getGain() {
		return this.gain;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setGain(float gain) {
		this.gain = gain;
	}
}