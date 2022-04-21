package com.vertx.shopping.models;

import com.vertx.shopping.services.BillDetail;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BillResponse {

  private UUID billId;

  private UUID userId;

  private double amount;

  private List<BillDetail> billDetails;
}
