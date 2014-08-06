package com.curator.jobcurator.job;



import com.curator.jobcurator.State;

public class Job {
	
	private Integer id;
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public long getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(long createdTime) {
		this.createdTime = createdTime;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public State getStatus() {
		return status;
	}

	public void setStatus(State status) {
		this.status = status;
	}

	private long createdTime;
	private long startTime;
	private long endTime;
	private String name;
	private String hostName;
	private State status;
	
	
	public Job(Integer id, long createdTime, long startTime, long endTime, String name, String hostName, State status) {
		this.id = id;
		this.createdTime = createdTime;
		this.startTime = startTime;
		this.endTime = endTime;
		this.name = name;
		this.hostName = hostName;
		this.status = status;
	}

	public static class Builder {
		private Integer id;
		private long createdTime;
		private long startTime;
		private long endTime;
		private String name;
		private String hostName;
		private State status;
		
		public Job builder() {
			if (this.name == null) {
				throw new IllegalArgumentException("job name cannot empty");
			}
			if (this.createdTime == 0L) {
				throw new IllegalArgumentException("job createdTime cannot empty");
			}
			
			return new Job(id, createdTime, startTime, endTime, name, hostName, status);
		}
		
		public Builder Id(Integer id) {
			this.id = id;
			return this;
		}
		
		public Builder CreatedTime(long createdTime) {
			this.createdTime = createdTime;
			return this;
		}
		
		public Builder StartTime(long startTime) {
			this.startTime = startTime;
			return this;
		}
		
		public Builder EndTime(long endTime) {
			this.endTime = endTime;
			return this;
		}
		
		public Builder Name(String name) {
			this.name = name;
			return this;
		}
		
		public Builder HostName(String hostName) {
			this.hostName = hostName;
			return this;
		}
		
		public Builder Status(State status) {
			this.status = status;
			return this;
		}
		
	
	}

	@Override
	public String toString() {
		return "Job [id=" + id + ", createdTime=" + createdTime
				+ ", startTime=" + startTime + ", endTime=" + endTime
				+ ", name=" + name + ", hostName=" + hostName + ", status="
				+ status + "]";
	}
	


}
