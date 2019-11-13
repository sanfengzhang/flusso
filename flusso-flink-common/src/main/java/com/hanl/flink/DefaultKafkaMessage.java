package com.hanl.flink;

public class DefaultKafkaMessage extends Message {

	private static final long serialVersionUID = 1L;

	private String key;

	private String value;

	private String topic;

	private int partition;

	private int offset;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public int getPartition() {
		return partition;
	}

	public void setPartition(int partition) {
		this.partition = partition;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	@Override
	public String toString() {
		return "EscafKafkaMessage [key=" + key + ", value=" + value + ", topic=" + topic + ", partition=" + partition
				+ ", offset=" + offset + "]";
	}

}
