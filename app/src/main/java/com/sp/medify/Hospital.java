package com.sp.medify;

public class Hospital {
    public String hospitalname ;
    public String address;
    public String generaltelephone;
    public String emergencytelephone;
    public String email;
    public String website;
    public String visitinghours;
    public String image_url;

    public Hospital() {}

    public Hospital(String hospitalname, String address, String generaltelephone, String emergencytelephone,
                    String email, String website, String visitinghours, String image_url) {
        this.hospitalname = hospitalname;
        this.address = address;
        this.generaltelephone = generaltelephone;
        this.emergencytelephone = emergencytelephone;
        this.email = email;
        this.website = website;
        this.visitinghours = visitinghours;
        this.image_url = image_url;
    }

    public String getHospitalname() {
        return hospitalname;
    }

    public String getAddress() {
        return address;
    }

    public String getGeneraltelephone() {
        return generaltelephone;
    }

    public String getEmergencytelephone() {
        return emergencytelephone;
    }

    public String getEmail() {
        return email;
    }

    public String getWebsite() {
        return website;
    }

    public String getVisitinghours() {
        return visitinghours;
    }

    public String getImage_url() { return  image_url; }


    public void setHospitalname(String hospitalname) {
        this.hospitalname = hospitalname;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setGeneraltelephone(String generaltelephone) {
        this.generaltelephone = generaltelephone;
    }

    public void setEmergencytelephone(String emergencytelephone) {
        this.emergencytelephone = emergencytelephone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public void setVisitinghours(String visitinghours) {
        this.visitinghours = visitinghours;
    }

    public void setImage_url(String image_url) { this.image_url = image_url; }
}
