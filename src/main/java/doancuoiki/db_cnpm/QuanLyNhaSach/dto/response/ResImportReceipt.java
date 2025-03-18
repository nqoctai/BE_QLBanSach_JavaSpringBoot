package doancuoiki.db_cnpm.QuanLyNhaSach.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
public class ResImportReceipt {
        private Long id;
        private double totalPrice;
        private Instant createdAt;
        private String createdBy;

        private ResImportReceiptDetail resImportReceiptDetail;

        private ResEmployee resEmployee;

        @Getter
        @Setter
        @AllArgsConstructor
        @NoArgsConstructor
        public static class ResImportReceiptDetail {
            private List<ResSupply> resSupplies;
            private double totalPrice;
            private int quantity;
        }


        @Getter
        @Setter
        @AllArgsConstructor
        @NoArgsConstructor
        public static class ResEmployee {
            private Long id;
            private String email;
        }


}
