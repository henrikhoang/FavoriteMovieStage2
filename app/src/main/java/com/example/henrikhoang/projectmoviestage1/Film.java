package com.example.henrikhoang.projectmoviestage1;

import org.parceler.Parcel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by henrikhoang on 6/12/17.
 */

@Parcel
public class Film {
    String title;
     String posterPath;
    double vote;
     String date;
   String overview;
    int id;
    String[] mAuthor;
    String[] mComment;
    String[] mTrailerId;

    public Film() {}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() { return id;}

    public void setId(int id) { this.id = id;}

    public String getPosterPath()
    {
        return posterPath;
    }

    public void setPosterPath(String posterPath){
         this.posterPath  = posterPath;
    }

    public String getDate () {
        SimpleDateFormat spf=new SimpleDateFormat("yyyy-mm-dd");
        try {
            Date newDate=spf.parse(date);
            spf= new SimpleDateFormat("mm-dd-yyyy");
            date = spf.format(newDate);
            return date;
        }
        catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public double getVote() {
        return vote;
    }

    public void setVote(double vote) { this.vote = vote; }

    public void setAuthor(String[] author) {
        this.mAuthor = author;
    }

    public String[] getAuthor() {
        return mAuthor;
    }

    public void setComment(String[] comment) {
        this.mComment = comment;
    }

    public String[] getComment() {
        return mComment;
    }

    public String[] getTrailerId() {
        return mTrailerId;
    }

    public void setTrailerId(String[] id) {
        this.mTrailerId = id;
    }

}
