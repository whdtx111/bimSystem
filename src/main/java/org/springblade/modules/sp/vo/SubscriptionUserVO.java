package org.springblade.modules.sp.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SubscriptionUserVO {

    private String id;
    private String name;
    private String avatar;
    private String phone;
    private String email;
    private String isOrder;
    private LocalDateTime createdAt;

    public SubscriptionUserVO() {
    }

    public SubscriptionUserVO(String id, String name, String avatar, String phone, String email, String isOrder, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.avatar = avatar;
        this.phone = phone;
        this.email = email;
        this.isOrder = isOrder;
        this.createdAt = createdAt;
    }
}
