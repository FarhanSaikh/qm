package com.a2zdaddy.quizmoney;

/**
 * Created by FARHAN SAIKH on 3/4/2018.
 */

public class Post {

    String posttitle,postdesc,postimage;
    public Post(){

    }

    public Post(String posttitle, String postdesc, String postimage){

        this.posttitle=posttitle;
        this.postdesc=postdesc;
        this.postimage=postimage;


    }

    public String getPosttitle() {
        return posttitle;
    }

    public void setPosttitle(String posttitle) {
        this.posttitle = posttitle;
    }

    public String getPostdesc() {
        return postdesc;
    }

    public void setPostdesc(String postdesc) {
        this.postdesc = postdesc;
    }

    public String getPostimage() {
        return postimage;



    }

    public void setPostimage(String postimage) {
        this.postimage = postimage;
    }
}
