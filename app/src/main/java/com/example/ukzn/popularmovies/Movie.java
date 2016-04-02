package com.example.ukzn.popularmovies;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by ukzn on 3/31/2016.
 */
public class Movie implements Serializable {
    private String poster_path;
    private String overview;
    private String release_date;
    private int id;
    private String original_title;
    private String title;
    private double popularity;
    private int vote_count;
    private double vote_average;
    private String fullposterpath;
    private String fullbackdroppath;

    public String getPoster_path(){
        return this.poster_path;
    }
    public void setPoster_path(String pos_path){
        this.poster_path = pos_path;
    }
    public String getOverview(){
        return  this.overview;
    }
    public void setOverview(String oview){
        this.overview = oview;
    }
    public String getRelease_date(){
        return  this.release_date;
    }
    public void setRelease_date(String rDate){
        this.release_date = rDate;
    }
    public int getId(){
        return  this.id;
    }
    public void setId(int id){
        this.id = id;
    }
    public String getOriginal_title(){
        return  this.original_title;
    }
    public void setOriginal_title(String orititle){
        this.original_title = orititle;
    }
    public String getTitle(){
        return  this.title;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public double getPopularity(){
        return this.popularity;
    }
    public void setPopularity(double sp){
        this.popularity = sp;
    }
    public int getVote_count(){
        return this.vote_count;
    }
    public void setVote_count(int vcount){
        this.vote_count = vcount;
    }
    public double getVote_average(){
        return  this.vote_average;
    }
    public void setVote_average(double vaverage){
        this.vote_average = vaverage;
    }
    public String getFullposterpath(){
        return this.fullposterpath;
    }
    public void setFullposterpath(String fposterpath){
        this.fullposterpath = fposterpath;
    }
    public String getFullbackdroppath(){
        return this.fullbackdroppath;
    }
    public void setFullbackdroppath(String fbackdroppath){
        this.fullbackdroppath = fbackdroppath;
    }











}
