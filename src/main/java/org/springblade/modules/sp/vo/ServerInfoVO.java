package org.springblade.modules.sp.vo;

import lombok.Data;

@Data
public class ServerInfoVO {

    private  String name ;
    private  String company ;
    private  String url ;
    private  String version;
    private  String adminContact ;
    private  String description;

    public ServerInfoVO() {
        this.name = "My new Speckle Server";
        this.company = "Unknown Company";
        this.url = "http://10.5.57.107:8080";
        this.version = "dev";
        this.adminContact = "n/a";
        this.description = "This a community deployment of a Speckle Server";
    }
}
