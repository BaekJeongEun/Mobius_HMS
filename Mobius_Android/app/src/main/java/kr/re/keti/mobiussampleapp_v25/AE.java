package kr.re.keti.mobiussampleapp_v25;

/**
 * Created by araha on 2016-09-20.
 */
public class AE {
    public String appos = "";
    public String appId = "";
    public String aeId = "Smite";
    public String appName = "mite";
    public String pointOfAccess = "";
    public String appPort = "";
    public String appProtocol = "";
    public String tasPort = "";
    public String cilimit = "";

    public void setAEid(String aeid){
        this.aeId = aeid;
    }
    public void setAppName(String appname){
        this.appName = appname;
    }
    public String getAEid() {
        return this.aeId;
    }
    public String getappName(){
        return this.appName;
    }
}