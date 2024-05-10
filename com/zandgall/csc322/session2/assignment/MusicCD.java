/*CSC322 SESSION 2: ASSIGNMENT - PROF. SUSAN FURTNEY
 > ZANDER GALL - GALLA@CSP.EDU

 ## MusicCD
 # Simple class that stores Music CD attribute data for an inventory. 

 : MADE IN NEOVIM */

package com.zandgall.csc322.session2.assignment;

import java.util.Date;

public class MusicCD extends Item {
	private final String artists, label, recordCompany, genres;
	private final int totalLength;
	private final Date releaseDate;

	public MusicCD(String title, String artists, Date releaseDate, String label, String recordCompany, int totalLength, String genres, float price) {
		super(title, price);
		this.artists = artists;
		this.releaseDate = releaseDate;
		this.label = label;
		this.recordCompany = recordCompany;
		this.totalLength = totalLength;
		this.genres = genres;
	}

	public String getArtists() { return artists; }
	public String getLabel() { return label; }
	public String getRecordCompany() { return recordCompany; }
	public String getGenres() { return genres; }
	public int getTotalLength() { return totalLength; }
	public Date getReleaseDate() { return releaseDate; }

	@Override
	public String getType() { return "MusicCD"; }
}
