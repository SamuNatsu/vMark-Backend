package com.vmark.backend.entity;

import lombok.Data;

@Data
public class ItemPic {
    // Item picture ID
    private int ipid;

    // Item ID
    private int iid;

    // Picture blob data
    private byte[] data;
}
