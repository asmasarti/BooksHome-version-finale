package com.example.BooksHome.Model;

public class users
{
    private String prenom,nom, mail,adresse,motdepasse,confirmerpass,tel,ville,phoProf;
    public users(){

    }
    public users(String prenom, String nom, String mail, String adresse,String tel,String ville){
        this.prenom = prenom;
        this.nom = nom;
        this.mail = mail;
        this.adresse = adresse;
        this.tel=tel;
        this.ville=ville;
    }

    public users(String prenom, String nom, String mail, String adresse,String motdepasse, String confirmerpass,String tel,String ville) {
        this.prenom = prenom;
        this.nom = nom;
        this.mail = mail;
        this.adresse = adresse;
        this.motdepasse = motdepasse;
        this.confirmerpass = confirmerpass;
        this.tel=tel;
        this.ville=ville;
    }

    public String getPhoProf() {
        return phoProf;
    }

    public void setPhoProf(String phoProf) {
        this.phoProf = phoProf;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getNom() {
        return nom;
    }

    public String getMail() {
        return mail;
    }

    public String getAdresse() {
        return adresse;
    }

    public String getMotdepasse() {
        return motdepasse;
    }

    public String getConfirmerpass() {
        return confirmerpass;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public void setMotdepasse(String motdepasse) {
        this.motdepasse = motdepasse;
    }

    public void setConfirerpass(String confirmerpass) {
        this.confirmerpass = confirmerpass;
    }

}
