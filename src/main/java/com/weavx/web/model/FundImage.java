package com.weavx.web.model;

public class FundImage {

	private int fundId;
	private int imageId;
	private String imageUrl;
	
	public FundImage(int fundId, int imageId, String imageUrl) {
		super();
		this.fundId = fundId;
		this.imageId = imageId;
		this.imageUrl = imageUrl;
	}

	public int getFundId() {
		return fundId;
	}

	public void setFundId(int fundId) {
		this.fundId = fundId;
	}

	public int getImageId() {
		return imageId;
	}

	public void setImageId(int imageId) {
		this.imageId = imageId;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	
	
	
}
